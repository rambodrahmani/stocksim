package it.unipi.lsmsdb.stocksim.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

/**
 * General purpose utility methods.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class Util {
    /**
     * Prints the given message to the console.
     *
     * @param message the String message to be printed.
     */
    public final static void print(final String message) {
        System.out.print(message);
    }

    /**
     * Prints the given message to the console ending with a new line.
     *
     * @param message the String message to be printed.
     */
    public final static void println(final String message) {
        System.out.println(message);
    }

    /**
     * Sets the logging {@link Level} to be used by the SLF4J.
     *
     * @param logLevel the log {@link Level} for the SLF4J.
     */
    public final static void setNettyLogLevel(final Level logLevel) {
        final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        final Logger rootLogger = loggerContext.getLogger("io.netty");
        rootLogger.setLevel(logLevel);
    }

    /**
     * Sets the logging {@link Level} to be used by the MongoDB Sync Driver.
     *
     * @param logLevel the log {@link Level} for the MongoDB Sync Driver.
     */
    public final static void setMongoLogLevel(final Level logLevel) {
        final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        final Logger rootLogger = loggerContext.getLogger("org.mongodb");
        rootLogger.setLevel(logLevel);
    }

    /**
     * Sets the logging {@link Level} to be used by the Cassandra Datastax Driver.
     *
     * @param logLevel the log {@link Level} for the Cassandra Datastax Driver.
     */
    public final static void setCassandraLogLevel(final Level logLevel) {
        final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        final Logger rootLogger = loggerContext.getLogger("com.datastax");
        rootLogger.setLevel(logLevel);
    }

    /**
     * Checks if the given string is valid: not null and not empty.
     *
     * @param string the string to be checked.
     *
     * @return true if the given string is valid, false otherwise.
     */
    public final static boolean isNotNull(final String string) {
        return (string != null) && (!string.equals("")) && (string.length() > 0);
    }
}
