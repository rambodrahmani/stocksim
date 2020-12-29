package it.unipi.lsmsdb.stocksim.client.admin;

import it.unipi.lsmsdb.stocksim.client.database.DBManager;

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

    // admin name
    private String name;

    // admin surname
    private String surname;

    // admin logged in flag
    private boolean loggedIn = false;

    // admin DB Manager
    private final DBManager dbManager = new DBManager();

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

    /**
     * Default constructor.
     *
     * @param name admin account name;
     * @param surname admin account surname;
     * @param username admin login username;
     * @param password admin login password.
     */
    public Admin(final String name, final String surname, final String username, final String password) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }

    /**
     * Executes admin login.
     *
     * @return true if the login is successful, false otherwise.
     */
    public boolean login() {
        this.loggedIn = dbManager.adminLogin(this);
        return loggedIn;
    }

    /**
     * Uses YahooFinance API to retrieve summary and historical data for the
     * given ticker symbol. If successful, the data is added to the database.
     *
     * @param symbol ticker symbol to be searched.
     *
     * @return
     */
    public boolean addTicker(final String symbol) {
        boolean ret  = true;

        // check if symbol is not already present in the db

        // retrieve summary data

        // retrieve historical data

        // all retrieved correctly, load in the database

        return ret;
    }

    /**
     * Creates a new admin account with the given name, surname, username and
     * password.
     *
     * @param name admin account name;
     * @param surname admin account surname;
     * @param username admin account username;
     * @param password admin account password.
     *
     * @return true if the admin account is created without errors, false otherwise.
     */
    public boolean createAdminAccount(final String name, final String surname,
                                      final String username, final String password) {
        boolean ret = true;

        // check if an admin account with the given credentials already exists
        if (dbManager.adminLogin(new Admin(username, password))) {
            ret = false;
        } else {
            final Admin newAdmin = new Admin(name, surname, username, password);
            ret = dbManager.createAdminAccount(newAdmin);
        }

        return ret;
    }

    /**
     * Deletes the admin account with the given username and password.
     *
     * @param username admin account username;
     * @param password admin account password.
     *
     * @return true if the admin account is deleted without errors, false otherwise.
     */
    public boolean removeAdminAccount(final String username, final String password) {
        boolean ret = true;

        // check if an admin account with the given credentials actually exists
        if (dbManager.adminLogin(new Admin(username, password))) {
            ret = dbManager.deleteAdminAccount(username, password);
        } else {
            ret = false;
        }

        return ret;
    }

    /**
     * Deletes the user account with the given email.
     *
     * @param email admin account email.
     *
     * @return true if the user account is deleted without errors, false otherwise.
     */
    public boolean removeUserAccount(final String email) {
        boolean ret = true;

        // delete user account using email address
        dbManager.deleteUserAccount(email);

        return ret;
    }

    /**
     * Executes admin logout.
     *
     * @return null.
     */
    public Admin logout() {
        this.loggedIn = false;
        return null;
    }

    /**
     * @return true if the user is logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * @param name the name retrieved from the db.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @param surname the surname retrieved from the db.
     */
    public void setSurname(final String surname) {
        this.surname = surname;
    }

    /**
     * @return admin username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return admin password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return admin name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return admin surname.
     */
    public String getSurname() {
        return surname;
    }
}
