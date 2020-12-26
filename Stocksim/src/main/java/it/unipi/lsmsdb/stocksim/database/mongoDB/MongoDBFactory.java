package it.unipi.lsmsdb.stocksim.database.mongoDB;

import it.unipi.lsmsdb.stocksim.database.DBFactory;
import java.util.List;

/**
 * MongoDB Factory.
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
     * @param databaseName name of the database on the mongo db.
     *
     * @return a {@link MongoDB} with default connection to the localhost.
     */
    public MongoDB getMongoDB(final String databaseName) {
        return  new MongoDB(databaseName);
    }

    /**
     * @param hostname server address;
     * @param port server port;
     * @param databaseName name of the database on the mongo db.
     *
     * @return a {@link MongoDB} with connection to a single server.
     */
    public MongoDB getMongoDB(final String hostname, final int port, final String databaseName) {
        return new MongoDB(hostname, port, databaseName);
    }

    /**
     * @return a {@link MongoDB} with connection to a cluster of {@link MongoServer}.
     */
    public MongoDB getMongoDB(final List<MongoServer> servers, final String databaseName) {
        return new MongoDB(servers, databaseName);
    }

    /**
     * @return a {@link MongoDB} with connection to a cluster of {@link MongoServer}
     *         with the specified preferences.
     */
    public MongoDB getMongoDB(final List<MongoServer> servers, final String preferences, final String databaseName) {
        return  new MongoDB(servers, preferences, databaseName);
    }
}
