package it.unipi.lsmsdb.stocksim.client.admin;

/**
 * This class represents a StockSim Client Admin.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class Admin {
    // admin username
    private final String username;

    // admin password
    private final String password;

    /**
     * Default constructor.
     *
     * @param username admin login username;
     * @param password admin login password.
     */
    public Admin(final String username, final String password) {
        this.username = username;
        this.password = password;
    }
}
