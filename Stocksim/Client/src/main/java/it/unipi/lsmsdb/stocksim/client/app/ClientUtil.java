package it.unipi.lsmsdb.stocksim.client.app;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import it.unipi.lsmsdb.stocksim.client.admin.AdminMenuAction;
import it.unipi.lsmsdb.stocksim.client.database.Stock;
import it.unipi.lsmsdb.stocksim.client.user.User;
import it.unipi.lsmsdb.stocksim.client.user.UserMenuAction;
import it.unipi.lsmsdb.stocksim.lib.util.Util;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.LoggerFactory;

import static it.unipi.lsmsdb.stocksim.client.app.Client.LOGGER_CONTEXT;

/**
 * General purpose utility methods.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class ClientUtil extends Util{
    /**
     * Prints the main menu for the Server usage.
     */
    public final static void printWelcomeMessage() {
        println("Welcome to the StockSim Client.\n");
    }

    /**
     * Prints the login menu for the Client usage in admin mode.
     */
    public final static void printAdminLoginMenu() {
        print("Available Commands:\n");
        print(String.format("%-10s %5s %-40s%n", AdminMenuAction.LOGIN.getActionName(), " ", AdminMenuAction.LOGIN.getActionDescription()));
        print(String.format("%-10s %5s %-40s%n", AdminMenuAction.QUIT.getActionName(), " ", AdminMenuAction.QUIT.getActionDescription()));
        print("> ");
    }

    /**
     * Prints the main menu for the Client usage in admin mode.
     */
    public final static void printAdminMainMenu() {
        print("Available Commands:\n");
        print(String.format("%-12s %5s %-40s%n", AdminMenuAction.ADD_TICKER.getActionName(), " ", AdminMenuAction.ADD_TICKER.getActionDescription()));
        print(String.format("%-12s %5s %-40s%n", AdminMenuAction.ADD_ADMIN.getActionName(), " ", AdminMenuAction.ADD_ADMIN.getActionDescription()));
        print(String.format("%-12s %5s %-40s%n", AdminMenuAction.REMOVE_ADMIN.getActionName(), " ", AdminMenuAction.REMOVE_ADMIN.getActionDescription()));
        print(String.format("%-12s %5s %-40s%n", AdminMenuAction.REMOVE_USER.getActionName(), " ", AdminMenuAction.REMOVE_USER.getActionDescription()));
        print(String.format("%-12s %5s %-40s%n", AdminMenuAction.LOGOUT.getActionName(), " ", AdminMenuAction.LOGOUT.getActionDescription()));
        print(String.format("%-12s %5s %-40s%n", AdminMenuAction.QUIT.getActionName(), " ", AdminMenuAction.QUIT.getActionDescription()));
        print("> ");
    }

    /**
     * Prints the login menu for the Client usage in admin mode.
     */
    public final static void printUserLoginMenu() {
        print("Available Commands:\n");
        print(String.format("%-10s %5s %-40s%n", UserMenuAction.REGISTER.getActionName(), " ", UserMenuAction.REGISTER.getActionDescription()));
        print(String.format("%-10s %5s %-40s%n", UserMenuAction.LOGIN.getActionName(), " ", UserMenuAction.LOGIN.getActionDescription()));
        print(String.format("%-10s %5s %-40s%n", UserMenuAction.QUIT.getActionName(), " ", UserMenuAction.QUIT.getActionDescription()));
        print("> ");
    }

    /**
     * Prints the main menu for the Client usage in admin mode.
     */
    public final static void printUserMainMenu(final User user) {
        print("[" + user.getUsername() + "] Available Commands:\n");
        print(String.format("%-18s %5s %-40s%n", UserMenuAction.SEARCH_STOCK.getActionName(), " ", UserMenuAction.SEARCH_STOCK.getActionDescription()));
        print(String.format("%-18s %5s %-40s%n", UserMenuAction.VIEW_STOCK.getActionName(), " ", UserMenuAction.VIEW_STOCK.getActionDescription()));
        print(String.format("%-18s %5s %-40s%n", UserMenuAction.CREATE_PORTFOLIO.getActionName(), " ", UserMenuAction.CREATE_PORTFOLIO.getActionDescription()));
        print(String.format("%-18s %5s %-40s%n", UserMenuAction.LIST_PORTFOLIOS.getActionName(), " ", UserMenuAction.CREATE_PORTFOLIO.getActionDescription()));
        print(String.format("%-18s %5s %-40s%n", UserMenuAction.VIEW_PORTFOLIO.getActionName(), " ", UserMenuAction.CREATE_PORTFOLIO.getActionDescription()));
        print(String.format("%-18s %5s %-40s%n", UserMenuAction.SIMULATE_PORTFOLIO.getActionName(), " ", UserMenuAction.CREATE_PORTFOLIO.getActionDescription()));
        print(String.format("%-18s %5s %-40s%n", UserMenuAction.DELETE_PORTFOLIO.getActionName(), " ", UserMenuAction.CREATE_PORTFOLIO.getActionDescription()));
        print(String.format("%-18s %5s %-40s%n", UserMenuAction.LOGOUT.getActionName(), " ", UserMenuAction.LOGOUT.getActionDescription()));
        print(String.format("%-18s %5s %-40s%n", UserMenuAction.QUIT.getActionName(), " ", UserMenuAction.QUIT.getActionDescription()));
        print("> ");
    }

    /**
     * @param string the plain text to be hashed.
     *
     * @return the SHA-256 hash for the given plain text string.
     */
    public final static String SHA256Hash(final String string) {
        return DigestUtils.sha256Hex(string);
    }

    /**
     * @return StockSim Client logger.
     */
    public final static Logger getLogger() {
        final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        final Logger rootLogger = loggerContext.getLogger(LOGGER_CONTEXT);
        return rootLogger;
    }

    /**
     * Sets the logging level for SLF4J, MongoDB and Cassandra.
     *
     * @param logLevel the log {@link Level} to be used.
     */
    public final static void setLogLevel(final Level logLevel) {
        // set log level for SLF4J
        setNettyLogLevel(logLevel);

        // set log level for the mongodb driver
        setMongoLogLevel(logLevel);

        // set log level for the cassandra driver
        setCassandraLogLevel(logLevel);

        // finally, set StockSim Client loggin level
        getLogger().setLevel(logLevel);
    }
}
