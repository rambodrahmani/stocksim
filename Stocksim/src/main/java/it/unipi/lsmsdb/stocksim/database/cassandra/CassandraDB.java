package it.unipi.lsmsdb.stocksim.database.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import it.unipi.lsmsdb.stocksim.database.DB;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.*;

/**
 * Apache Cassandra DB Manager.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class CassandraDB implements DB {
    // server host address
    private ArrayList<String> hostnames;

    // server port
    private ArrayList<Integer> ports;

    // DB contact points vector
    private Vector<InetSocketAddress> contactPoints;

    // Cassandra DB datacenter parameter: only needed with explicit contact points
    private String datacenter;

    // session with convenience methods to execute CQL statements
    private CqlSession cqlSession;

    /**
     * Default constructor.
     */
    protected CassandraDB() {
    }

    /**
     * Constructor which sets server hostname and port for a single explicit contact point.
     *
     * @param hostname Cassandra DB Server hostname.
     * @param port Cassandra DB Server port.
     * @param datacenter Cassandra DB datacenter parameter.
     */
    protected CassandraDB(final String hostname, final Integer port, final String datacenter) {
        this.hostnames = new ArrayList<String>(Arrays.asList(hostname));
        this.ports = new ArrayList<Integer>(Arrays.asList(port));
        this.datacenter = datacenter;
    }

    /**
     * Constructor which sets server hostname and port for multiple explicit contact points.
     *
     * @param hostnames Cassandra DB Server hostname.
     * @param ports Cassandra DB Server port.
     */
    protected CassandraDB(final ArrayList<String> hostnames, final ArrayList<Integer> ports, final String datacenter) {
        this.hostnames = hostnames;
        this.ports = ports;
        this.datacenter = datacenter;
    }

    /**
     * Populates the DB contact points collection and builds the CqlSession.
     */
    public boolean connect() {
        boolean ret = true;

        // populate DB contact points collection
        contactPoints = new Vector<InetSocketAddress>();
        Iterator<String> hostname = hostnames.iterator();
        Iterator<Integer> port = ports.iterator();
        while (hostname.hasNext() && port.hasNext()) {
            contactPoints.add(new InetSocketAddress(hostname.next(), port.next()));
        }

        // build CqlSession
        cqlSession = CqlSession.builder()
                .addContactPoints(contactPoints)
                .withLocalDatacenter(datacenter)
                .build();

        return ret;
    }

    /**
     * Executes the given CQL Query using the current CQL session.
     *
     * @param query the CQL query to be executed.
     *
     * @return the result set for the given CQL query.
     */
    public ResultSet query(final String query) throws CQLSessionException {
        ResultSet ret = null;

        if (cqlSession != null) {
            final SimpleStatement simpleStatement = SimpleStatement.newInstance(query);
            ret = cqlSession.execute(simpleStatement);
        } else {
            throw new CQLSessionException("CQL Session not initialized.");
        }

        return ret;
    }

    /**
     * Executes the given CQL Query using the current CQL session.
     *
     * @param query the CQL query to be executed.
     * @param timeout the CQL statement timeout in seconds; this won't override server side settings.
     *
     * @return the result set for the given CQL query.
     */
    public ResultSet query(final String query, final int timeout) throws CQLSessionException {
        ResultSet ret = null;

        if (cqlSession != null) {
            final SimpleStatement simpleStatement = SimpleStatement.builder(query).setTimeout(Duration.ofSeconds(timeout)).build();
            ret = cqlSession.execute(simpleStatement);
        } else {
            throw new CQLSessionException("CQL Session not initialized.");
        }

        return ret;
    }

    /**
     * Closes the CQL Session, relinquishing any underlying resources.
     */
    public void disconnect() {
        cqlSession.close();
    }
}
