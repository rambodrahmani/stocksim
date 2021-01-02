package it.unipi.lsmsdb.stocksim.lib.database.mongoDB;

/**
 * This class represents a MongoDB Server.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class MongoServer {
    // mongo server ip address
    private String hostname;

    // mongo server port number
    private int port;

    /**
     * Default constructor.
     *
     * @param hostname the address of the MongoDB Server.
     * @param port the port of the MongoDB Server.
     */
    public MongoServer(final String hostname, final int port) {
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * @return mongo server hostname address.
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @return mongo server port.
     */
    public int getPort() {
        return port;
    }
}
