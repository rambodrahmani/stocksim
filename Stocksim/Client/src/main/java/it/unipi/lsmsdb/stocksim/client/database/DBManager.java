package it.unipi.lsmsdb.stocksim.client.database;

import it.unipi.lsmsdb.stocksim.database.cassandra.CassandraDB;
import it.unipi.lsmsdb.stocksim.database.cassandra.CassandraDBFactory;
import it.unipi.lsmsdb.stocksim.database.mongoDB.MongoDB;
import it.unipi.lsmsdb.stocksim.database.mongoDB.MongoDBFactory;
import it.unipi.lsmsdb.stocksim.database.mongoDB.MongoServer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * SotckSim Client DB Manager.
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
}
