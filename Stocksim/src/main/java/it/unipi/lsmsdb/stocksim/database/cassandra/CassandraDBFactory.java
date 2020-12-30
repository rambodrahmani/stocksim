package it.unipi.lsmsdb.stocksim.database.cassandra;

import it.unipi.lsmsdb.stocksim.database.DBFactory;
import it.unipi.lsmsdb.stocksim.database.mongoDB.MongoDBFactory;

import java.util.ArrayList;

/**
 * Apache Cassandra DB Factory.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class CassandraDBFactory implements DBFactory {
    /**
     * Default constructor.
     */
    private CassandraDBFactory() {
    }

    /**
     * @return a {@link MongoDBFactory} instance.
     */
    public static CassandraDBFactory create() {
        return new CassandraDBFactory();
    }

    /**
     * @return an empty {@link CassandraDB}.
     */
    public CassandraDB getCassandraDB() {
        return new CassandraDB();
    }

    /**
     * @return a {@link CassandraDB} with a single explicit contact point.
     */
    public CassandraDB getCassandraDB(final String hostname, final Integer port, final String datacenter) {
        return new CassandraDB(hostname, port, datacenter);
    }

    /**
     * @return a {@link CassandraDB} with multiple explicit contact points.
     */
    public CassandraDB getCassandraDB(final ArrayList<String> hostnames, final ArrayList<Integer> ports, final String datacenter) {
        return new CassandraDB(hostnames, ports, datacenter);
    }
}
