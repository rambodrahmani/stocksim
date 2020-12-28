package it.unipi.lsmsdb.stocksim.server.database;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import it.unipi.lsmsdb.stocksim.database.cassandra.CQLSessionException;
import it.unipi.lsmsdb.stocksim.database.cassandra.CassandraDB;
import it.unipi.lsmsdb.stocksim.database.cassandra.CassandraDBFactory;
import it.unipi.lsmsdb.stocksim.database.mongoDB.MongoDB;
import it.unipi.lsmsdb.stocksim.database.mongoDB.MongoDBFactory;
import it.unipi.lsmsdb.stocksim.database.mongoDB.MongoServer;
import it.unipi.lsmsdb.stocksim.database.mongoDB.StocksimCollection;
import it.unipi.lsmsdb.stocksim.server.app.ServerUtil;
import it.unipi.lsmsdb.stocksim.server.yfinance.YFHistoricalData;
import it.unipi.lsmsdb.stocksim.server.yfinance.YFSummaryData;
import it.unipi.lsmsdb.stocksim.server.yfinance.YahooFinance;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONException;

import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Filters.eq;
import static java.time.temporal.ChronoUnit.DAYS;

/**
 * SotckSim Server DB Manager.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class DBManager {
    // Cassandra DB Factory
    private final CassandraDBFactory cassandraDBFactory = CassandraDBFactory.create();

    // Cassandra DB instance
    private CassandraDB cassandraDB;

    // Mongo DB Factory
    private final MongoDBFactory mongoDBFactory = MongoDBFactory.create();

    // Mongo DB shared instance
    private MongoDB mongoDB;

    /**
     * @return Cassandra DB shared instance;
     */
    private CassandraDB getCassandraDB() {
        if (cassandraDB == null) {
            final ArrayList<String> hostnames = new ArrayList<String>(Arrays.asList("172.16.3.94", "172.16.3.95", "172.16.3.96"));
            final ArrayList<Integer> ports = new ArrayList<Integer>(Arrays.asList(9042, 9042, 9042));
            cassandraDB = cassandraDBFactory.getCassandraDB(hostnames, ports, "datacenter1");
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
            final MongoServer mongoServer1 = new MongoServer("172.16.3.94", 27017);
            final MongoServer mongoServer2 = new MongoServer("172.16.3.95", 27017);
            final MongoServer mongoServer3 = new MongoServer("172.16.3.96", 27017);
            final ArrayList<MongoServer> servers = new ArrayList<>(Arrays.asList(mongoServer1, mongoServer2, mongoServer3));

            mongoDB = mongoDBFactory.getMongoDB(servers, "stocksim");
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

    /**
     * Checks databases data consistency.
     *
     * @return true if Cassandra DB and Mongo DB data are consistent with each other.
     */
    public boolean consistencyCheck() {
        boolean ret = false;

        try {
            final ResultSet resultSet = getCassandraDB().query(CassandraQueryBuilder.getTickerSymbolsQuery());
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
        // save starting time to calculate elapsed time
        final long startTimeMillis = System.currentTimeMillis();

        // exceptions counter during the update process
        int exceptionsCounter = 0;

        // list of the failed symbol updates
        final ArrayList<String> exceptions = new ArrayList<>();

        try {
            // data consistency check
            if (!consistencyCheck()) {
                ServerUtil.println("DATA CONSISTENCY CHECK FAILED. UNABLE TO PROCEED WITH UPDATE.\n");
            } else {
                // consistency check success, start historical data update
                ServerUtil.println("DATA CONSISTENCY CHECK SUCCESS. PROCEEDING WITH UPDATE.\n");

                // query tickers list from cassandra
                final ResultSet tickersResultSet = getCassandraDB().query(CassandraQueryBuilder.getTickerSymbolsQuery());

                // connect to mongodb
                getMongoDB();

                // for each ticker symbol
                for (final Row row : tickersResultSet) {
                    // retrieve ticker symbol
                    final String symbol = row.getString("symbol");
                    ServerUtil.println("Updating historical data for: " + symbol + ".");

                    // query symbol last update date
                    final ResultSet dateResultSet = getCassandraDB().query(CassandraQueryBuilder.getLastUpdateDate(symbol));

                    // needed for unix timestamp extraction
                    final ZoneId nyZoneId = ZoneId.of("America/New_York");

                    // get last update date and timestamp
                    final LocalDate lastUpdateDate = dateResultSet.one().getLocalDate("date");
                    ServerUtil.println("Last update date: " + lastUpdateDate.toString() + ".");

                    // add one day before calculating timestamp
                    long lastUpdateTimestamp = lastUpdateDate.plusDays(1).atTime(LocalTime.MIDNIGHT).atZone(nyZoneId).toEpochSecond();

                    // get current date and timestamp
                    final LocalDate now = LocalDate.now();
                    long currentTimestamp = now.atStartOfDay(nyZoneId).toEpochSecond();

                    // get clock time
                    final Instant instant = Clock.systemDefaultZone().instant();
                    final int currentTime = instant.atZone(nyZoneId).getHour();

                    // get days since last update
                    final long daysBetween = DAYS.between(lastUpdateDate.plusDays(1), now);

                    // print number of days since last data update for this ticker
                    ServerUtil.println("Days since last update: " + String.valueOf(daysBetween) + ".");

                    // historical data already up to date
                    if (daysBetween == 0 ) {
                        ServerUtil.println("Historical data for " + symbol + " already up to date. Moving on.\n");
                    } else {
                        try {
                            // build yahoo finance object for current ticker symbol
                            final YahooFinance yahooFinance = new YahooFinance(symbol, lastUpdateTimestamp, currentTimestamp);

                            // get summary data from yahoo finance
                            ServerUtil.println("Request URL: " + yahooFinance.getV10URL());
                            YFSummaryData yfSummaryData = yahooFinance.getSummaryData();

                            // first update mongo db fields
                            final MongoCollection<Document> stocksCollection = getMongoDB().getCollection(StocksimCollection.STOCKS.getCollectionName());
                            final Bson stockFilter = eq("ticker", symbol);
                            final Bson updateTrailingPE = Updates.set("trailingPE", yfSummaryData.getTrailingPE());
                            final Bson updateMarketCap = Updates.set("marketCap", yfSummaryData.getMarketCap());
                            final Bson updateSet = Updates.combine(updateTrailingPE, updateMarketCap);
                            getMongoDB().updateOne(stockFilter, updateSet, stocksCollection);

                            // update historical data
                            ServerUtil.println("Request URL: " + yahooFinance.getV8URL());
                            final ArrayList<YFHistoricalData> yfHistoricalData = yahooFinance.getHistoricalData();
                            for (final YFHistoricalData historicalData : yfHistoricalData) {
                                final PreparedStatement preparedStatement = getCassandraDB().prepareStatement(CassandraQueryBuilder.getUpdateInsertQuery());
                                final BoundStatement bounded = preparedStatement.bind(symbol, historicalData.getDate(), historicalData.getAdjClose(),
                                        historicalData.getClose(), historicalData.getHigh(), historicalData.getLow(),
                                        historicalData.getOpen(), historicalData.getVolume());
                                getCassandraDB().execute(bounded);
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

        // close Mongo DB connection
        disconnectMongoDB();

        // close Cassandra DB connection
        disconnectCassandraDB();
    }
}