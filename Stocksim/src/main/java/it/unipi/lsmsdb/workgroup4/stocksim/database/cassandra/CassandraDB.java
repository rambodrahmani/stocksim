package it.unipi.lsmsdb.workgroup4.stocksim.database.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import it.unipi.lsmsdb.workgroup4.stocksim.database.DBManager;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * Apache Cassandra DB Manager.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class CassandraDB implements DBManager {
    // server host address
    private ArrayList<String> hostnames;

    // server port
    private ArrayList<Integer> ports;

    // Cassandra DB datacenter parameter: only needed with explicit contact points
    private String datacenter;

    // DB contact points vector
    private Vector<InetSocketAddress> contactPoints;

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
     * Populates the DB contactpoints collection and builds the CqlSession.
     */
    public boolean connect() {
        boolean ret = true;

        // populate DB contactpoints collection
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
            ret = cqlSession.execute(query);
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
