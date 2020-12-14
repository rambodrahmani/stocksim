package it.unipi.lsmsdb.stocksim.database.mongodb.persistence;

import it.unipi.lsmsdb.stocksim.database.mongodb.implementation.MongoDBManagerImpl;

/**
 *
 */
public class DBManagerFactory {

    /**
     *
     */
    DBManagerFactory() {}

    /**
     *
     * @return
     */
    public static DBManagerFactory create() {
        return new DBManagerFactory();
    }

    /**
     *
     * @return
     */
    public DocumentDBManager getService() {
        return (DocumentDBManager) new MongoDBManagerImpl();
    }
}
