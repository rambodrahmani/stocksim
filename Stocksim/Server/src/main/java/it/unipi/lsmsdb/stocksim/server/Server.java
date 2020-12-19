package it.unipi.lsmsdb.stocksim.server;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

import it.unipi.lsmsdb.stocksim.database.cassandra.CQLSessionException;
import it.unipi.lsmsdb.stocksim.database.cassandra.CassandraDB;
import it.unipi.lsmsdb.stocksim.database.cassandra.CassandraDBFactory;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Sotcksim Server implementation.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class Server {
    /**
     * Server entry point.
     *
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        try {
            // connect to cassandra db
            final CassandraDBFactory cassandraDBFactory = new CassandraDBFactory();
            CassandraDB cassandraDB = cassandraDBFactory.getCassandraDB("192.168.2.133", 9042, "datacenter1");

            // connect to Cassandra DB Server
            if (cassandraDB.connect()) {
                try {
                    int i = 0;
                    final ResultSet resultSet = cassandraDB.query("SELECT DISTINCT symbol FROM stocksim.tickers;");
                    for (final Row row : resultSet) {
                        i = i + 1;
                        final String table = row.getString("symbol");
                        System.out.println(table);

                        /*Calendar from = Calendar.getInstance();
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
                        }*/
                    }
                    System.out.println(i);
                } catch (final CQLSessionException e) {
                    e.printStackTrace();
                }
            } else {

            }

            // close Cassandra DB connection
            cassandraDB.disconnect();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
