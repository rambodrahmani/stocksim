package it.unipi.lsmsdb.stocksim.database.mongodb.persistence;

public class MongoServer {
    public String host;
    public String port;

    public MongoServer(String host, String port) {
        this.host = host;
        this.port = port;
    }
}
