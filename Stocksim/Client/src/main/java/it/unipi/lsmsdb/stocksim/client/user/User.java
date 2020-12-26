package it.unipi.lsmsdb.stocksim.client.user;

import it.unipi.lsmsdb.stocksim.client.admin.Admin;
import it.unipi.lsmsdb.stocksim.client.database.DBManager;

/**
 * This class represents a StockSim Client User.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class User {
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
    public User(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Executes admin login.
     *
     * @return true if the login is successful, false otherwise.
     */
    public boolean login() {
        this.loggedIn = dbManager.userLogin(this);
        return loggedIn;
    }

    /**
     * Executes admin logout.
     *
     * @return null.
     */
    public User logout() {
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
    public void setSurname(String surname) {
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
