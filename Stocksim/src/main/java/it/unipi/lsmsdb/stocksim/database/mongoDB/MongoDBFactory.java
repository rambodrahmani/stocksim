package it.unipi.lsmsdb.stocksim.database.mongoDB;

import it.unipi.lsmsdb.stocksim.database.DBFactory;

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
    public MongoDBManager getMongoDBManager() {
        return  new MongoDB();
    }
    public MongoDBManager getMongoDBManager(String host, String port) {
        return new MongoDB(host, port);
    }

    public MongoDBManager getMongoDBManager(List<MongoServer> servers) {
        return  new MongoDB(servers);
    }
    public MongoDBManager getMongoDBManager(List<MongoServer>servers , String preferences){
        return  new MongoDB(servers, preferences);
    }
}
