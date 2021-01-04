package it.unipi.lsmsdb.stocksim.server.app;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import it.unipi.lsmsdb.stocksim.lib.util.Util;
import org.slf4j.LoggerFactory;

import static it.unipi.lsmsdb.stocksim.server.app.Server.LOGGER_CONTEXT;

/**
 * General purpose utility methods.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class ServerUtil extends Util {
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
     * @return StockSim Server logger.
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

        // finally, set StockSim Server loggin level
        getLogger().setLevel(logLevel);
    }
}
