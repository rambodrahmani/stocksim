package it.unipi.lsmsdb.stocksim.database.mongoDB;
/**
 * data structure for a mongoDB server
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class MongoServer {
    public String host;
    public String port;

    public MongoServer(String host, String port) {
        this.host = host;
        this.port = port;
    }
}
