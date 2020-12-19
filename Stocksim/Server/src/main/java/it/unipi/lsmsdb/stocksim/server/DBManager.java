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
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

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
            final MongoCollection mongoTickersCollection = getMongoDB().getCollection(StocksimCollection.STOCKS.getCollectionName());
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
     *
     */
    public void updateDB() {
        // data consistency check
        try {
            final ResultSet resultSet = getCassandraDB().query("SELECT DISTINCT symbol FROM stocksim.tickers;");
            final int cassandraTickersCount = resultSet.all().size();
            final MongoCollection mongoTickersCollection = getMongoDB().getCollection(StocksimCollection.STOCKS.getCollectionName());
            final int mongoTickersCount = (int) mongoTickersCollection.countDocuments();
            if (cassandraTickersCount != mongoTickersCount) {
                System.out.println("DATA CONSISTENCY CHECK FAILED.");
                System.out.println("Cassandra Tickers count: " + cassandraTickersCount);
                System.out.println("Mongo Tickers count: " + mongoTickersCount);
            } else {
                // update tickers historical data
                for (final Row row : resultSet) {
                    final String table = row.getString("symbol");
                    System.out.println(table);

                    Calendar from = Calendar.getInstance();
                    Calendar to = Calendar.getInstance();
                    to.add(Calendar.DATE, 1);
                    Stock google = YahooFinance.get(table);
                    List<HistoricalQuote> histQuotes = google.getHistory(from, to, Interval.DAILY);
                    for (HistoricalQuote historicalQuote : histQuotes) {
                        System.out.println(historicalQuote.getDate().getTime());
                        System.out.println(historicalQuote.getOpen());
                        System.out.println(historicalQuote.getHigh());
                        System.out.println(historicalQuote.getLow());
                        System.out.println(historicalQuote.getClose());
                        System.out.println(historicalQuote.getAdjClose());
                        System.out.println(historicalQuote.getVolume());
                    }
                }
            }
        } catch (final CQLSessionException | IOException e) {
            e.printStackTrace();
        }

        // close Cassandra DB connection
        disconnectCassandraDB();

        // close Mongo DB connection
        disconnectMongoDB();
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
