package it.unipi.lsmsdb.stocksim.server;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
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
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.*;

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
        try {
            // data consistency check
            if (!consistencyCheck()) {
                Util.println("DATA CONSISTENCY CHECK FAILED. UNABLE TO PROCEED WITH UPDATE.\n");
            } else {
                // consistency check success, start historical data update
                Util.println("DATA CONSISTENCY CHECK SUCCESS. PROCEEDING WITH UPDATE.\n");

                // query tickers list from cassandra
                final ResultSet tickersResultSet = getCassandraDB().query("SELECT DISTINCT symbol FROM stocksim.tickers;");

                // for each ticker symbol
                for (final Row row : tickersResultSet) {
                    // retrieve ticker symbol
                    final String symbol = row.getString("symbol");
                    Util.println("Updating historical data for: " + symbol + ".");

                    // query symbol last update date
                    final ResultSet dateResultSet = getCassandraDB().query("SELECT date FROM stocksim.tickers WHERE symbol='" + symbol + "' ORDER BY date DESC;");

                    // needed for unix timestamp extraction
                    ZoneId zoneId = ZoneId.systemDefault();

                    // get last update date and timestamp
                    final LocalDate lastUpdateDate = dateResultSet.one().getLocalDate("date");
                    Util.println("Last update date: " + lastUpdateDate.toString() + ".");

                    // add one day before calculating timestamp
                    long lastUpdateTimestamp = lastUpdateDate.plusDays(1).atTime(LocalTime.NOON).atZone(ZoneId.systemDefault()).toEpochSecond();

                    // get current date and timestamp
                    final LocalDate now = LocalDate.now();
                    long nowTimestamp = now.atStartOfDay(zoneId).toEpochSecond();

                    // get days since last update
                    int daysBetween = (int) DAYS.between(lastUpdateDate, now);

                    // print number of days since last data update for this ticker
                    Util.println("Days since last update " + String.valueOf(daysBetween) + ".");

                    // historical data already up to date
                    if (daysBetween == 0) {
                        Util.println("Historical data for " + symbol + " already up to date. Moving on.");
                    } else {
                        final String YFinanceURL = "https://query1.finance.yahoo.com/v8/finance/chart/" + symbol + "?"
                                + "period1=" + lastUpdateTimestamp
                                + "&period2=" + nowTimestamp
                                + "&interval=1d"
                                + "&events=history";
                        Util.println("Request URL: " + YFinanceURL);
                        try {
                            final JSONObject historicalData = new JSONObject(IOUtils.toString(new URL(YFinanceURL), StandardCharsets.UTF_8));
                            final JSONArray timestamp = historicalData.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONArray("timestamp");
                            final JSONObject quote = historicalData.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONObject("indicators").getJSONArray("quote").getJSONObject(0);
                            final JSONArray adjclose = historicalData.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONObject("indicators").getJSONArray("adjclose").getJSONObject(0).getJSONArray("adjclose");

                            for (int i = 0 ; i < timestamp.length(); i++) {
                                final int epochSecs = timestamp.getInt(i);
                                final LocalDate updateDate = Instant.ofEpochSecond(epochSecs).atZone(ZoneId.systemDefault()).toLocalDate();
                                final String date = updateDate.toString();
                                final double adj_close = adjclose.getDouble(i);
                                final double close = quote.getJSONArray("close").getDouble(i);
                                final double high = quote.getJSONArray("high").getDouble(i);
                                final double low = quote.getJSONArray("low").getDouble(i);
                                final double open = quote.getJSONArray("open").getDouble(i);
                                final double volume = quote.getJSONArray("volume").getDouble(i);

                                Util.println(symbol + "|" + date + "|" + adj_close + "|" + close + "|" + high + "|" + low + "|" + open + "|" + volume);
                            }

                            Util.println("Historical data updated for " + symbol + ". Moving on.\n");
                        } catch (final IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (final CQLSessionException e) {
            e.printStackTrace();
        }

        // update terminated
        Util.println("Historical data update terminated without errors.");

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
