package it.unipi.lsmsdb.stocksim.database.mongoDB;

import it.unipi.lsmsdb.stocksim.database.DBFactory;
import java.util.List;

/**
 * Mongo DB Factory.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
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
    public MongoDB getMongoDB(final String databaseName) {
        return  new MongoDB(databaseName);
    }

    public MongoDB getMongoDB(final String host, final int port, final String databaseName) {
        return new MongoDB(host, port, databaseName);
    }

    public MongoDB getMongoDB(final List<MongoServer> servers, final String databaseName) {
        return new MongoDB(servers, databaseName);
    }

    public MongoDB getMongoDB(final List<MongoServer> servers, final String preferences, final String databaseName) {
        return  new MongoDB(servers, preferences, databaseName);
    }
}
