package it.unipi.lsmsdb.stocksim.client.user;

import it.unipi.lsmsdb.stocksim.client.app.ClientUtil;
import it.unipi.lsmsdb.stocksim.client.charting.*;
import it.unipi.lsmsdb.stocksim.client.database.Stock;
import it.unipi.lsmsdb.stocksim.lib.database.cassandra.CQLSessionException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * StockSim Client User mode implementation.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class ClientUser {
    // Default input scanner.
    private static final Scanner scanner = new Scanner(System.in);

    // StockSim user
    private static User user = null;

    /**
     * StockSim Client user mode run main loop.
     */
    public static void run() {
        ClientUtil.println("*** [RUNNING IN USER MODE] ***\n");

        // infinite main loop
        while (true) {
            if (isLoggedIn()) {
                ClientUtil.printUserMainMenu(user);
            } else {
                ClientUtil.printUserLoginMenu();
            }
            final String command = scanner.nextLine();
            parseCommand(command);
        }
    }

    /**
     * Parses and executes the given command.
     *
     * @param command the command to be executed, if valid.
     */
    private static void parseCommand(final String command) {
        try {
            final UserMenuAction action = UserMenuAction.valueOf(command.toUpperCase().replace("-", "_"));
            switch (action) {
                case REGISTER:
                    if (isLoggedIn()) {
                        ClientUtil.println("User login already executed. Logout first.\n");
                    } else {
                        if (register()) {
                            ClientUtil.println("User sign up executed correctly. You can now login.\n");
                        } else {
                            ClientUtil.println("User sign up failed.\n");
                        }
                    }
                    break;
                case LOGIN:
                    if (isLoggedIn()) {
                        ClientUtil.println("User login already executed.\n");
                    } else {
                        if (login()) {
                            ClientUtil.println("User login executed correctly.");
                            ClientUtil.println("Welcome " + user.getName() + " " + user.getSurname() + ".\n");
                        } else {
                            ClientUtil.println("User login failed.\n");
                        }
                    }
                    break;
                case SEARCH_STOCK:
                    if (isLoggedIn()) {
                        searchStock();
                    } else {
                        ClientUtil.println("You need to login first.\n");
                    }
                    break;
                case VIEW_STOCK:
                    if (isLoggedIn()) {
                        viewStock();
                    } else {
                        ClientUtil.println("You need to login first.\n");
                    }
                    break;
                case CREATE_PORTFOLIO:
                    if (isLoggedIn()) {
                        if (createPortfolio()) {
                            ClientUtil.println("Portfolio created correctly.\n");
                        } else {
                            ClientUtil.println("Could not create new user Portfolio.\n");
                        }
                    } else {
                        ClientUtil.println("You need to login first.\n");
                    }
                    break;
                case LIST_PORTFOLIOS:
                    if (isLoggedIn()) {
                        listPortfolios();
                        ClientUtil.println("");
                    } else {
                        ClientUtil.println("You need to login first.\n");
                    }
                    break;
                case VIEW_PORTFOLIO:
                    if (isLoggedIn()) {
                        viewPortfolio();
                    } else {
                        ClientUtil.println("You need to login first.\n");
                    }
                    break;
                case SIMULATE_PORTFOLIO:
                    if (isLoggedIn()) {
                        simulatePortfolio();
                    } else {
                        ClientUtil.println("You need to login first.\n");
                    }
                    break;
                case DELETE_PORTFOLIO:
                    if (isLoggedIn()) {
                        if (deletePortfolio()) {
                            ClientUtil.println("Portfolio deleted.\n");
                        } else {
                            ClientUtil.println("Could not delete portfolio.\n");
                        }
                    } else {
                        ClientUtil.println("You need to login first.\n");
                    }
                    break;
                case LOGOUT:
                    if (isLoggedIn()) {
                        logout();
                        ClientUtil.println("User logged out.\n");
                    } else {
                        ClientUtil.println("You need to login first.\n");
                    }
                    break;
                case QUIT:
                    quit();
                    break;
                default:
                    ClientUtil.println("Invalid command.\n");
                    break;
            }
        } catch (final IllegalArgumentException e) {
            ClientUtil.println("Invalid command.\n");
        }
    }

    /**
     * User Sign up.
     *
     * @return true in case of success, false otherwise.
     */
    private static boolean register() {
        boolean ret = true;

        // ask for new user real name
        ClientUtil.print("Name: ");
        final String name = scanner.nextLine();

        // ask for new user real surname
        ClientUtil.print("Surname: ");
        final String surname = scanner.nextLine();

        // ask for new user email address
        ClientUtil.print("E-Mail: ");
        final String email = scanner.nextLine();

        // ask for new user login username
        ClientUtil.print("Username [login]: ");
        final String username = scanner.nextLine();

        // ask for new user login password
        ClientUtil.print("Password [login]: ");
        final String password = scanner.nextLine();

        // check input strings are all valid
        if (ClientUtil.isValidString(name) && ClientUtil.isValidString(surname) &&
            ClientUtil.isValidString(email) && ClientUtil.isValidString(username) &&
            ClientUtil.isValidString(password)) {
            // create new user for sign up
            final User newUser = new User(name, surname, email, username, password);
            ret = newUser.register();
        } else {
            ret = false;
        }

        return ret;
    }

    /**
     * User login.
     *
     * @return true in case of success, false otherwise.
     */
    private static boolean login() {
        boolean ret = true;

        // ask for user username
        ClientUtil.print("Username: ");
        final String username = scanner.nextLine();

        // ask for password username
        ClientUtil.print("Password: ");
        final String password = scanner.nextLine();

        // check input strings are all valid
        if (ClientUtil.isValidString(username) && ClientUtil.isValidString(password)) {
            try {
                user = new User(username, password);
                ret = user.login();
            } catch (CQLSessionException e) {
                ret = false;
                e.printStackTrace();
            }
        } else {
            ret = false;
        }

        return ret;
    }

    /**
     * Searches for a stock using the ticker symbol.
     */
    private static void searchStock() {
        // ask for stock ticker symbol
        ClientUtil.print("Ticker Symbol: ");
        final String symbol = scanner.nextLine();

        // check input string is valid
        if (ClientUtil.isValidString(symbol)) {
            try {
                final Stock stock = user.searchStock(symbol);
                if (stock != null) {
                    ClientUtil.println(stock.toString());
                } else {
                    ClientUtil.println("No stock found for the given symbol.\n");
                }
            } catch (final CQLSessionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Searches for a stock using the ticker symbol, if available,
     * shows historical data for the given period.
     */
    private static void viewStock() {
        // ask for stock ticker symbol
        ClientUtil.print("Ticker Symbol: ");
        final String symbol = scanner.nextLine();

        // ask for start date
        ClientUtil.print("Start Date [YYYY-MM-DD]: ");
        final String startDate = scanner.nextLine();

        // ask for end date
        ClientUtil.print("End Date [YYYY-MM-DD]: ");
        final String endDate = scanner.nextLine();

        // ask for days granularity: how many days for every row of data
        ClientUtil.print("Days granularity: ");
        final String granularity = scanner.nextLine();

        // check all input strings are valid
        if (ClientUtil.isValidString(symbol) && ClientUtil.isValidString(startDate) &&
            ClientUtil.isValidString(endDate) && ClientUtil.isValidString(granularity)) {
            try {
                user.viewStock(symbol, startDate, endDate, granularity);
            } catch (final DateTimeParseException e) {
                ClientUtil.println("Incorrect date format.\n");
            } catch (final NumberFormatException e) {
                ClientUtil.println("Incorrect days granularity.\n");
            } catch (final CQLSessionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a portfolio for the user with the given Stock
     * tickers.
     */
    private static boolean createPortfolio() {
        boolean ret = true;

        // ask for stock portfolio name
        ClientUtil.print("Portfolio name: ");
        final String name = scanner.nextLine();

        // ask for stock tickers
        ClientUtil.print("Ticker Symbols [comma separated]: ");
        final String input = scanner.nextLine();

        if (ClientUtil.isValidString(name) && ClientUtil.isValidString(input)) {
            final String[] tickers = input.split(", ");

            // actually create the portfolio in the DB
            try {
                ret = user.createPortfolio(name, tickers);
            } catch (CQLSessionException e) {
                e.printStackTrace();
                ret = false;
            }
        }

        return ret;
    }

    /**
     * Lists user portfolios.
     */
    private static void listPortfolios() {
        user.printPortfolios();
    }

    /**
     * Displays portfolio information.
     */
    private static void viewPortfolio() {
        // ask for stock portfolio name
        ClientUtil.print("Portfolio name: ");
        final String name = scanner.nextLine();

        // check if the input string is valid
        if (ClientUtil.isValidString(name)) {

        }
    }

    /**
     * Runs simulation and displays statistics for the given portfolio.
     */
    private static void simulatePortfolio() {
        // ask for stock portfolio name
        ClientUtil.print("Portfolio name: ");
        final String name = scanner.nextLine();

        // check if the input string is valid
        if (ClientUtil.isValidString(name)) {

        }
    }

    /**
     * Deletes user portfolio using the name.
     *
     * @return true if the portfolio is deleted, false otherwise.
     */
    private static boolean deletePortfolio() {
        boolean ret = true;

        // ask for stock portfolio name
        ClientUtil.print("Portfolio name: ");
        final String name = scanner.nextLine();

        // check if the input string is valid
        if (ClientUtil.isValidString(name)) {
            ret = user.deletePortfolio(name);
        }

        return ret;
    }

    /**
     * User logout.
     */
    private static void logout() {
        user = user.logout();
    }

    /**
     * Quits application.
     */
    private static void quit() {
        user.quit();
        System.exit(0);
    }

    /**
     * @return if the user has been allocated and logged in.
     */
    private static boolean isLoggedIn() {
        return user != null && user.isLoggedIn();
    }
}
