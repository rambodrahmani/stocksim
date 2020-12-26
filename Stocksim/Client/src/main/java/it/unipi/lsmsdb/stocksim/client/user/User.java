package it.unipi.lsmsdb.stocksim.client.user;

import it.unipi.lsmsdb.stocksim.client.database.DBManager;

/**
 * This class represents a StockSim Client User.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class User {
    // user username
    private final String username;

    // user password
    private final String password;

    // user DB Manager
    private final DBManager dbManager = new DBManager();

    /**
     * Default constructor.
     *
     * @param username user login username;
     * @param password user login password.
     */
    public User(final String username, final String password) {
        this.username = username;
        this.password = password;
    }
}
