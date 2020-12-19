package it.unipi.lsmsdb.stocksim.database.mongoDB;

import it.unipi.lsmsdb.stocksim.database.DBFactory;

import java.util.List;

/**
 *
 */
public class MongoDBFactory implements DBFactory {

    /**
     * Default constructor.
     */
    private MongoDBFactory() {
    }

    /**
     * @return a {@link MongoDBFactory} instance.
     */
    public static MongoDBFactory create() {
        return new MongoDBFactory();
    }

    /**
     *
     * @return
     */
    public MongoDB getMongoDBManager(final String databaseName) {
        return  new MongoDB(databaseName);
    }
    public MongoDB getMongoDBManager(final String host, final int port, final String databaseName) {
        return new MongoDB(host, port, databaseName);
    }

    public MongoDB getMongoDBManager(final List<MongoServer> servers, final String databaseName) {
        return new MongoDB(servers, databaseName);
    }
    public MongoDB getMongoDBManager(final List<MongoServer>servers, final String preferences, final String databaseName) {
        return  new MongoDB(servers, preferences, databaseName);
    }
}
