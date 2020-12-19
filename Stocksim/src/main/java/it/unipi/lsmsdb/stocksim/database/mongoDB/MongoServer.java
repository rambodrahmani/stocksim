package it.unipi.lsmsdb.stocksim.database.mongoDB;
/**
 * This class represents a MongoDB Server.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class MongoServer {
    public String hostname;
    public String port;

    /**
     * Default constructor.
     *
     * @param hostname the address of the MongoDB Server.
     * @param port the port of the MongoDB Server.
     */
    public MongoServer(final String hostname, final String port) {
        this.hostname = hostname;
        this.port = port;
    }
}
