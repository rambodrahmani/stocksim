package it.unipi.lsmsdb.workgroup4.stocksim.server;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

import it.unipi.lsmsdb.workgroup4.stocksim.database.cassandra.CQLSessionException;
import it.unipi.lsmsdb.workgroup4.stocksim.database.cassandra.CassandraDB;
import it.unipi.lsmsdb.workgroup4.stocksim.database.cassandra.CassandraDBFactory;
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
            final CassandraDBFactory cassandraDBFactory = new CassandraDBFactory();
            CassandraDB cassandraDB = cassandraDBFactory.getCassandraDB("172.16.3.94", 9042, "workgroup-04 Datacenter");

            // connect to Cassandra DB Server
            if (cassandraDB.connect()) {
                try {
                    final ResultSet resultSet = cassandraDB.query("SELECT * FROM system_schema.tables WHERE keyspace_name = 'stocksim';");
                    for (final Row row : resultSet) {
                        final String table = row.getString("table_name");
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
                } catch (final CQLSessionException e) {
                    e.printStackTrace();
                }
            } else {

            }

            // close Cassandra DB connection
            cassandraDB.disconnect();

            Stock stock = YahooFinance.get("ZYME");
            System.out.println(stock.getQuote().getOpen());
            System.out.println(stock.getQuote().getDayHigh());
            System.out.println(stock.getQuote().getDayLow());
            System.out.println(stock.getQuote().getPreviousClose());
            System.out.println(stock.getQuote().getVolume());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
