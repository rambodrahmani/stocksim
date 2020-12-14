package it.unipi.lsmsdb.workgroup4.stocksim.database.cassandra;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

/**
 * Developer harness test for the Datastax Java Driver for Apache Cassandra.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class Main {

    /**
     * Entry point.
     *
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        final CassandraDBFactory cassandraDBFactory = new CassandraDBFactory();

        // default constructor: empty Cassandra DB
        CassandraDB cassandraDB = cassandraDBFactory.getCassandraDB();

        // Cassandra DB with single explicit contact point
        cassandraDB = cassandraDBFactory.getCassandraDB("172.16.3.94", 9042, "workgroup-04 Datacenter");

        // Cassandra DB with multiple explicit contact points
        // final ArrayList<String> hostnames = new ArrayList<String>(Arrays.asList("172.16.3.94", "172.16.3.95", "172.16.3.96"));
        // final ArrayList<Integer> ports = new ArrayList<Integer>(Arrays.asList(9042, 9042, 9042));
        // cassandraDB = cassandraDBFactory.getCassandraDB(hostnames, ports, "workgroup-04 Datacenter");

        // connect to Cassandra DB Server
        if (cassandraDB.connect()) {
            try {
                final ResultSet resultSet = cassandraDB.query("select * from test.stocks_3");

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
            } catch (CQLSessionException e) {
                e.printStackTrace();
            }
        } else {

        }

        // close Cassandra DB connection
        cassandraDB.disconnect();
    }
}