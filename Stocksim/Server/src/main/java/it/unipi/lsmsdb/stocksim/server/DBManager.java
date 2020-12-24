package it.unipi.lsmsdb.stocksim.server;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.insert.InsertInto;
import com.mongodb.client.MongoCollection;
import it.unipi.lsmsdb.stocksim.database.cassandra.CQLSessionException;
import it.unipi.lsmsdb.stocksim.database.cassandra.CassandraDB;
import it.unipi.lsmsdb.stocksim.database.cassandra.CassandraDBFactory;
import it.unipi.lsmsdb.stocksim.database.mongoDB.MongoDB;
import it.unipi.lsmsdb.stocksim.database.mongoDB.MongoDBFactory;
import it.unipi.lsmsdb.stocksim.database.mongoDB.StocksimCollection;
import it.unipi.lsmsdb.stocksim.util.Util;
import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Sotcksim Server DB Manager.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class DBManager {
    /**
     * Cassandra DB Factory.
     */
    final CassandraDBFactory cassandraDBFactory = CassandraDBFactory.create();

    /**
     * Cassandra DB instance.
     */
    private CassandraDB cassandraDB;

    /**
     * Mongo DB Factory.
     */
    final MongoDBFactory mongoDBFactory = MongoDBFactory.create();

    /**
     * Mongo DB shared instance.
     */
    private MongoDB mongoDB;

    /**
     * Checks databases data consistency.
     *
     * @return true if Cassandra DB and Mongo DB data are consistent with each other.
     */
    public boolean consistencyCheck() {
        boolean ret = false;

        try {
            final ResultSet resultSet = getCassandraDB().query("SELECT DISTINCT symbol FROM stocksim.tickers;");
            final int cassandraTickersCount = resultSet.all().size();
            final MongoCollection<Document> mongoTickersCollection = getMongoDB().getCollection(StocksimCollection.STOCKS.getCollectionName());
            final int mongoTickersCount = (int) mongoTickersCollection.countDocuments();

            ret = (cassandraTickersCount == mongoTickersCount);

            // close db connections
            disconnectCassandraDB();
            disconnectMongoDB();
        } catch (final CQLSessionException e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * Executes a data consistency check and then updates Cassandra historical data.
     */
    public void updateDB() {
        // exceptions counter during the update process
        int exceptionsCounter = 0;

        // list of the failed symbol updates
        final ArrayList<String> exceptions = new ArrayList<>();

        // save starting time to calculate elapsed time
        final long startTimeMillis = System.currentTimeMillis();

        try {
            // data consistency check
            if (!consistencyCheck()) {
                ServerUtil.println("DATA CONSISTENCY CHECK FAILED. UNABLE TO PROCEED WITH UPDATE.\n");
            } else {
                // consistency check success, start historical data update
                ServerUtil.println("DATA CONSISTENCY CHECK SUCCESS. PROCEEDING WITH UPDATE.\n");

                // query tickers list from cassandra
                final ResultSet tickersResultSet = getCassandraDB().query("SELECT DISTINCT symbol FROM stocksim.tickers;");

                // for each ticker symbol
                for (final Row row : tickersResultSet) {
                    // retrieve ticker symbol
                    final String symbol = row.getString("symbol");
                    ServerUtil.println("Updating historical data for: " + symbol + ".");

                    // query symbol last update date
                    final ResultSet dateResultSet = getCassandraDB().query("SELECT date FROM stocksim.tickers WHERE symbol='" + symbol + "' ORDER BY date DESC;");

                    // needed for unix timestamp extraction
                    final ZoneId nyZoneId = ZoneId.of("America/New_York");

                    // get last update date and timestamp
                    final LocalDate lastUpdateDate = dateResultSet.one().getLocalDate("date");
                    ServerUtil.println("Last update date: " + lastUpdateDate.toString() + ".");

                    // add one day before calculating timestamp
                    long lastUpdateTimestamp = lastUpdateDate.plusDays(1).atTime(LocalTime.MIDNIGHT).atZone(nyZoneId).toEpochSecond();

                    // get current date and timestamp
                    final LocalDate now = LocalDate.now();
                    long nowTimestamp = now.atStartOfDay(nyZoneId).toEpochSecond();

                    // get clock time
                    final Instant instant = Clock.systemDefaultZone().instant();
                    final int currentTime = instant.atZone(nyZoneId).getHour();

                    // get days since last update
                    final long daysBetween = DAYS.between(lastUpdateDate.plusDays(1), now);

                    // print number of days since last data update for this ticker
                    ServerUtil.println("Days since last update: " + String.valueOf(daysBetween) + ".");

                    // historical data already up to date
                    if (daysBetween == 0 || (daysBetween == 1 && currentTime < 20)) {
                        ServerUtil.println("Historical data for " + symbol + " already up to date. Moving on.\n");
                    } else {
                        final String YFinanceURL = "https://query1.finance.yahoo.com/v8/finance/chart/" + symbol + "?"
                                + "period1=" + lastUpdateTimestamp
                                + "&period2=" + nowTimestamp
                                + "&interval=1d"
                                + "&events=history";
                        ServerUtil.println("Request URL: " + YFinanceURL);
                        try {
                            final JSONObject historicalData = new JSONObject(IOUtils.toString(new URL(YFinanceURL), StandardCharsets.UTF_8));
                            final JSONArray timestamp = historicalData.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONArray("timestamp");
                            final JSONObject quote = historicalData.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONObject("indicators").getJSONArray("quote").getJSONObject(0);
                            final JSONArray adjclose = historicalData.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONObject("indicators").getJSONArray("adjclose").getJSONObject(0).getJSONArray("adjclose");

                            for (int i = 0 ; i < timestamp.length(); i++) {
                                final int epochSecs = timestamp.getInt(i);
                                final LocalDate updateDate = Instant.ofEpochSecond(epochSecs).atZone(ZoneId.systemDefault()).toLocalDate();
                                final BigDecimal adj_close = new BigDecimal(adjclose.getDouble(i), MathContext.DECIMAL64);
                                final BigDecimal close = new BigDecimal(quote.getJSONArray("close").getDouble(i), MathContext.DECIMAL64);
                                final BigDecimal high = new BigDecimal(quote.getJSONArray("high").getDouble(i), MathContext.DECIMAL64);
                                final BigDecimal low = new BigDecimal(quote.getJSONArray("low").getDouble(i), MathContext.DECIMAL64);
                                final BigDecimal open = new BigDecimal(quote.getJSONArray("open").getDouble(i), MathContext.DECIMAL64);
                                final BigDecimal volume = new BigDecimal(quote.getJSONArray("volume").getDouble(i), MathContext.DECIMAL64);

                                final PreparedStatement preparedStatement = getCassandraDB().prepareStatement("INSERT INTO stocksim.tickers (symbol, date, adj_close, close, high, low, open, volume) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                                final BoundStatement bound = preparedStatement.bind(symbol, updateDate, adj_close, close, high, low, open, volume);
                                getCassandraDB().execute(bound);
                            }

                            ServerUtil.println("Historical data updated for " + symbol + ". Moving on.\n");
                        } catch (final IOException | JSONException e) {
                            e.printStackTrace();
                            exceptionsCounter++;
                            exceptions.add(symbol);
                        }
                    }
                }
            }
        } catch (final CQLSessionException e) {
            e.printStackTrace();
        }

        // save starting time to calculate elapsed time
        final long finishTimeMillis = System.currentTimeMillis();

        // calculate elapsed time in millis
        final long timeElapsedMillis = finishTimeMillis - startTimeMillis;
        final String elapsedTime = String.format("Elapsed time %d hrs, %d mins, %d secs",
                TimeUnit.MILLISECONDS.toHours(timeElapsedMillis),
                TimeUnit.MILLISECONDS.toMinutes(timeElapsedMillis),
                TimeUnit.MILLISECONDS.toSeconds(timeElapsedMillis));

        // update terminated: print statistics
        ServerUtil.println("Historical data update terminated.");
        ServerUtil.println(elapsedTime);
        ServerUtil.println("Exceptions during update process: " + exceptionsCounter + ".");
        ServerUtil.println("Failed updates: " + exceptions.toString() + ".\n");

        // close Cassandra DB connection
        disconnectCassandraDB();
    }

    /**
     * @return Cassandra DB shared instance;
     */
    private CassandraDB getCassandraDB() {
        if (cassandraDB == null) {
            cassandraDB = cassandraDBFactory.getCassandraDB("192.168.2.133", 9042, "datacenter1");
            cassandraDB.connect();
        }

        return cassandraDB;
    }

    /**
     * Disconnects from Cassandra DB and sets reference to null.
     */
    private void disconnectCassandraDB() {
        cassandraDB = getCassandraDB().disconnect();
    }

    /**
     * @return Mongo DB shared instance;
     */
    private MongoDB getMongoDB() {
        if (mongoDB == null) {
            mongoDB = mongoDBFactory.getMongoDBManager("192.168.2.133", 27017, "stocksim");
            mongoDB.connect();
        }

        return mongoDB;
    }

    /**
     * Disconnects from Mongo DB and sets reference to null.
     */
    private void disconnectMongoDB() {
        mongoDB = getMongoDB().disconnect();
    }
}
