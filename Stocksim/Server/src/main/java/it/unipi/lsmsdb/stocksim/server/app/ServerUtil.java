package it.unipi.lsmsdb.stocksim.server.app;

import ch.qos.logback.classic.Level;
import it.unipi.lsmsdb.stocksim.util.Util;

/**
 * General purpose utility methods.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class ServerUtil extends Util{
    /**
     * Prints the main menu for the Server usage.
     */
    public final static void printWelcomeMessage() {
        println("Welcome to the StockSim Server.\n");
    }

    /**
     * Prints the main menu for the Server usage.
     */
    public final static void printMainMenu() {
        print("Available Commands:\n");
        print(String.format("%-6s %5s %-40s%n", "status", " ", "check databases status."));
        print(String.format("%-6s %5s %-40s%n", "update", " ", "update databases historical data."));
        print(String.format("%-6s %5s %-40s%n", "quit", " ", "quit Stocksim server."));
        print("> ");
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
    }
}
