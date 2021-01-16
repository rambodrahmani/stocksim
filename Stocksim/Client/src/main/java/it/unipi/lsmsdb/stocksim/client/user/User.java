package it.unipi.lsmsdb.stocksim.client.user;

import it.unipi.lsmsdb.stocksim.client.app.ClientUtil;
import it.unipi.lsmsdb.stocksim.client.charting.*;
import it.unipi.lsmsdb.stocksim.client.database.DBManager;
import it.unipi.lsmsdb.stocksim.client.database.Portfolio;
import it.unipi.lsmsdb.stocksim.client.database.Stock;
import it.unipi.lsmsdb.stocksim.lib.database.cassandra.CQLSessionException;
import org.bson.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

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
     * Retrieves and shows historical data and infor for the given ticker symbol.
     *
     * @param symbol stock ticker symbol;
     * @param startDate historical data start date;
     * @param endDate historical data end date;
     * @param granularity historical data days granularity.
     *
     * @throws CQLSessionException
     */
    public void viewStock(final String symbol, final String startDate, final String endDate, final String granularity) throws CQLSessionException {
        // get historical data runnable
        final Runnable historicalDataRunnable = () -> {
            try {
                // parse string dates
                final LocalDate start = LocalDate.parse(startDate);
                final LocalDate end = LocalDate.parse(endDate);

                // check if the given date interval is valid
                if (start.isBefore(end)) {
                    // retrieve stock historical data
                    final HistoricalData historicalData = getHistoricalData(symbol, start, end, Integer.parseInt(granularity));
                    final ArrayList<OHLCRow> rows = historicalData.getRows();

                    // check if historical data was correctly retrieved
                    if (rows != null) {
                        // create candle stick chart
                        final CandlestickChart candlestickChart = ChartingFactory.getCandlestickChart(symbol + " Candlestick",
                                "Time", "Price", symbol, rows);

                        // retrieve FULL stock historical data
                        final HistoricalData fullHistoricalData = getHistoricalData(symbol, start, end, 1);
                        final ArrayList<OHLCRow> fullRows = fullHistoricalData.getRows();

                        // check if FULL historical data was correctly retrieved
                        if (fullRows != null) {
                            // create line chart
                            final ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
                            final ArrayList<Number> values = new ArrayList<Number>();
                            for (int i = fullRows.size() - 1; i > 0; i--) {
                                final LocalDate date = fullRows.get(i).getDate()
                                        .toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate();
                                dates.add(date);
                                values.add(fullRows.get(i).getAdjClose());
                            }
                            final TimeSeriesChart timeSeriesChart = ChartingFactory.getTimeSeriesChart(symbol + " Adjusted Close", "Time", "Adjusted Close Price", dates, values);

                            // populate charts to be displayed
                            final ArrayList<Chart> charts = new ArrayList<>();
                            charts.add(candlestickChart);
                            charts.add(timeSeriesChart);

                            // display charts
                            ChartUtil.showCharts(charts, symbol + " Historical Data");
                        }
                    }
                } else {
                    ClientUtil.println("Invalid date interval. The start date must be before the end date.\n");
                }
            } catch (final CQLSessionException e) {
                e.printStackTrace();
            }
        };

        // start historical data retrieval thread
        final Thread historicalDataThread = new Thread(historicalDataRunnable, "Historical Data Retrieval");
        historicalDataThread.start();

        // check if the stock ticker actually exists
        final Stock stock = searchStock(symbol);
        if (stock != null) {
            ClientUtil.println(stock.toString());
        } else {
            ClientUtil.println("No stock found for the given symbol.\n");
        }
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
            ClientUtil.println("Fetching user portfolios.");
        }
    }

    /**
     * Deletes the user {@link Portfolio} with the given name.
     *
     * @param name {@link Portfolio} name;
     */
    public boolean deletePortfolio(final String name) {
        return dbManager.deletePortfolio(name);
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

    /**
     * Quits user.
     * Disconnect from all databases.
     */
    public void quit() {
        dbManager.disconnect();
    }
}
