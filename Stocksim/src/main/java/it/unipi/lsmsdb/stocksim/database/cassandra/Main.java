package it.unipi.lsmsdb.stocksim.database.cassandra;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

import java.time.LocalDate;
import java.util.*;

/**
 * Developer harness test for the Datastax Java Driver for Apache Cassandra.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class Main {

    /**
     * Developer harness test entry point.
     *
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        // cassandra db factory
        final CassandraDBFactory cassandraDBFactory = CassandraDBFactory.create();

        // default constructor: empty Cassandra DB
        CassandraDB cassandraDB = cassandraDBFactory.getCassandraDB();

        // Cassandra DB with single explicit contact point
        cassandraDB = cassandraDBFactory.getCassandraDB("172.16.3.94", 9042, "workgroup-04 Datacenter");

        // Cassandra DB with multiple explicit contact points
        final ArrayList<String> hostnames = new ArrayList<String>(Arrays.asList("172.16.3.94", "172.16.3.95", "172.16.3.96"));
        final ArrayList<Integer> ports = new ArrayList<Integer>(Arrays.asList(9042, 9042, 9042));
        cassandraDB = cassandraDBFactory.getCassandraDB(hostnames, ports, "datacenter1");

        testAggregation(cassandraDB);


        // connect to Cassandra DB Server
        if (cassandraDB.connect()) {
            try {
                final ResultSet resultSet = cassandraDB.query("select * from stocksim.tickers where symbol='AAPL';");

                for (final Row row : resultSet) {
                    System.out.print(row.getInt("id") + "|");
                    System.out.print(row.getBigDecimal("adj_close") + "|");
                    System.out.print(row.getBigDecimal("close") + "|");
                    System.out.print(row.getLocalDate("date") + "|");
                    System.out.print(row.getBigDecimal("high") + "|");
                    System.out.print(row.getBigDecimal("low") + "|");
                    System.out.print(row.getBigDecimal("open") + "|");
                    System.out.println(row.getBigDecimal("volume"));
                }
            } catch (final CQLSessionException e) {
                e.printStackTrace();
            }
        } else {

        }

        // close Cassandra DB connection and set reference to null
        cassandraDB = cassandraDB.disconnect();
    }

    /**
     * Developer harness test for the aggregation function of Cassandra.
     *
     * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
     */
    public static void testAggregation(CassandraDB db)  {
        Map<LocalDate, Map> data=null;
        Map<String, Float> candle=null;
        Set<LocalDate> keySet=null;
        if(db.connect()){
            final ResultSet resultSet;
            try {
                // aggregation query using a user defined aggregation functon
                resultSet = db.query(
                        "select PeriodParam(20,date, \n" +
                                "   open, close, high, low, volume,adj_close)\n" +
                                "    as Period from stocksim.tickers where date<'2020-12-1' " +
                                "and date>'2020-6-10' and symbol='TSLA';"
                );
                // for each row we have a map
                for (final Row row : resultSet) {
                    data=row.getMap("Period", LocalDate.class, Map.class );
                    System.out.println(data);
                    if(data==null)
                        continue;
                    // taking the keySet of the map, witch is the final date of the period;
                    // this is not known a priori because it's influenced by market closure
                    // during different period of years and weeks; so we let the cassandra
                    // aggregator compute it autonomously
                    keySet= data.keySet();
                    for (LocalDate finalDate : keySet) {
                        // every date identify OHLC data (and more) for one candle
                        candle = data.get(finalDate);
                        if(candle==null)
                            continue;
                        System.out.print(finalDate+ " |");
                        System.out.print(candle.get("open") + " |");
                        System.out.print(candle.get("adj_close") + " |");
                        System.out.print(candle.get("close") + " |");
                        System.out.print(candle.get("date") + " |");
                        System.out.print(candle.get("high") + " |");
                        System.out.print(candle.get("low") + " |");
                        System.out.println(candle.get("volume"));
                    }

                }
            } catch (CQLSessionException e) {
                e.printStackTrace();
            }
            db.disconnect();
        }
    }
}