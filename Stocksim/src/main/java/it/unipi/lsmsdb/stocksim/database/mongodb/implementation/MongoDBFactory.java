package it.unipi.lsmsdb.stocksim.database.mongodb.implementation;

import it.unipi.lsmsdb.stocksim.database.DBFactory;
import it.unipi.lsmsdb.stocksim.database.mongodb.persistence.DocumentDBManager;
import it.unipi.lsmsdb.stocksim.database.mongodb.persistence.MongoServer;

import java.net.Socket;
import java.util.List;

/**
 *
 */
public class MongoDBFactory implements DBFactory {

    /**
     *
     */
    public MongoDBFactory() {
    }

    /**
     *
     * @return
     */
    public static MongoDBFactory create() {
        return new MongoDBFactory();
    }

    /**
     *
     * @return
     */
    public DocumentDBManager getMongoDB() {
        return  new MongoDBManager();
    }
    public DocumentDBManager getMongoDB(String host, String port) {
        return new MongoDBManager(host, port);
    }

    public DocumentDBManager getMongoDB(List<MongoServer> servers) {
        return  new MongoDBManager(servers);
    }
    public DocumentDBManager getMongoDB(List<MongoServer>servers , String preferences){
        return  new MongoDBManager(servers, preferences);
    }
}
