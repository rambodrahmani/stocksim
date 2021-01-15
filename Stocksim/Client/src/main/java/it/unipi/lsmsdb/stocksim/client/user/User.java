package it.unipi.lsmsdb.stocksim.client.user;

import it.unipi.lsmsdb.stocksim.client.app.ClientUtil;
import it.unipi.lsmsdb.stocksim.client.charting.HistoricalData;
import it.unipi.lsmsdb.stocksim.client.database.DBManager;
import it.unipi.lsmsdb.stocksim.client.database.Portfolio;
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

    // user portfolios
    private ArrayList<Portfolio> portfolios;

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
    public boolean login() throws CQLSessionException {
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
     * Retrieves the {@link HistoricalData} for the given stock symbol, using {@link DBManager}.
     *
     * @param symbol the ticker symbol to be searched;
     * @param startDate start date for the period;
     * @param endDate end date for the period;
     * @param granularity days granularity.
     *
     * @return the retrieved {@link HistoricalData}, might be empty.
     */
    public HistoricalData getHistoricalData(final String symbol, final LocalDate startDate, final LocalDate endDate, final int granularity) throws CQLSessionException {
        return dbManager.getHistoricalData(symbol, startDate, endDate, granularity);
    }

    /**
     * Creates a user {@link Portfolio} with the given name and ticker symbols.
     *
     * @param name {@link Portfolio} name;
     * @param symbols {@link Portfolio} ticker symbols.
     */
    public boolean createPortfolio(final String name, final String[] symbols) throws CQLSessionException {
        final Portfolio newPortfolio = dbManager.createPortfolio(name, symbols);

        // check if the new portfolio was correctly created
        if (newPortfolio != null) {
            portfolios.add(newPortfolio);
        }

        return newPortfolio != null;
    }

    /**
     * Prints user portfolio.
     */
    public void printPortfolios() {
        if (portfolios != null) {
            for (final Portfolio portfolio : portfolios) {
                ClientUtil.print(portfolio.getName() + ": [");
                for (final Stock stock : portfolio.getStocks()) {
                    ClientUtil.print(" " + stock.getSymbol() + ",");
                }
                ClientUtil.println(" ]");
            }
        } else {
            ClientUtil.println("Fetching user portfolios. Please try again later.");
        }
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
    public void setSurname(final String surname) {
        this.surname = surname;
    }

    /**
     * @param email the email address of the user.
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * @param portfolios the stock portfolios of the user.
     */
    public void setPortfolios(final ArrayList<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }

    /**
     * @return user login username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return user login password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return user real name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return user real surname.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @return user email address.
     */
    public String getEmail() {
        return email;
    }
}
