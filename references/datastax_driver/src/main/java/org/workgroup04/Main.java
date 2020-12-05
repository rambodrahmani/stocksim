package org.workgroup04;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

import java.net.InetSocketAddress;

/**
 * Developer harness test for the Datastax Java Driver for Apache Cassandra.
 */
public class Main {

    /**
     * Entry point.
     *
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        try (CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("172.16.3.94", 9042))
                //.addContactPoint(new InetSocketAddress("172.16.3.95", 9042))
                //.addContactPoint(new InetSocketAddress("172.16.3.96", 9042))
                .withLocalDatacenter("datacenter1")
                .build();) {
            final ResultSet rs = session.execute("select * from test.stocks");
            for (Row row : rs) {
                System.out.print(row.getInt("id") + "|");
                System.out.print(row.getBigDecimal("adj_close") + "|");
                System.out.print(row.getBigDecimal("close") + "|");
                System.out.print(row.getString("date") + "|");
                System.out.print(row.getBigDecimal("high") + "|");
                System.out.print(row.getBigDecimal("low") + "|");
                System.out.print(row.getBigDecimal("open") + "|");
                System.out.println(row.getBigDecimal("volume"));
            }
        }
    }
}
