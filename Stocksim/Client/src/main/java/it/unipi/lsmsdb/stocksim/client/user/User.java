package it.unipi.lsmsdb.stocksim.client.user;

import it.unipi.lsmsdb.stocksim.client.charting.OHLCRow;
import it.unipi.lsmsdb.stocksim.client.database.DBManager;
import it.unipi.lsmsdb.stocksim.client.database.Stock;
import it.unipi.lsmsdb.stocksim.lib.database.cassandra.CQLSessionException;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * This class represents a StockSim Client User.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class User {
    // user name
    private String name;

    // user surname
    private String surname;

    // user surname
    private String email;

    // user username: used for login
    private final String username;

    // user password: used for login
    private final String password;

    // user logged in flag
    private boolean loggedIn = false;

    // user DB Manager
    private final DBManager dbManager = new DBManager();


    /**
     * Default constructor with all fields.
     *
     * @param name     the user real name;
     * @param surname  the user real surname;
     * @param email    the user email address;
     * @param username the user login username;
     * @param password the user login password.
     */
    public User(final String name, final String surname, final String email, final String username, final String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.username = username;
        this.password = password;
    }

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

    /**
     * Executes user sign up.
     *
     * @return true if the sign up is successful, false otherwise.
     */
    public boolean register() {
        this.loggedIn = dbManager.userRegister(this);
        return loggedIn;
    }

    /**
     * Executes user login.
     *
     * @return true if the login is successful, false otherwise.
     */
    public boolean login() {
        this.loggedIn = dbManager.userLogin(this);
        return loggedIn;
    }

    /**
     * Search for the given ticker symbol using the {@link DBManager}.
     *
     * @param symbol the ticker symbol to be searched for.
     *
     * @return the retrieved {@link Stock} in case of success, null otherwise.
     */
    public Stock searchStock(final String symbol) throws CQLSessionException {
        return dbManager.searchStock(symbol);
    }

    /**
     * Executes user logout.
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
     * @param name the real name of the user.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @param surname the real surname of the user.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return user username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return user password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return user name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return user surname.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @return the user email address.
     */
    public String getEmail() {
        return email;
    }


    public HistoricalDataset getHistoricalData(
            String symbol, LocalDate startDate, LocalDate endDate, int ndays)
            throws CQLSessionException {
        return dbManager.getHistoricaldata(symbol, startDate, endDate, ndays);
    }
}
