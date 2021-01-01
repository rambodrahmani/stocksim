package it.unipi.lsmsdb.stocksim.server.app;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import it.unipi.lsmsdb.stocksim.server.database.DBManager;
import it.unipi.lsmsdb.stocksim.lib.util.ArgsParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * SotckSim Server implementation.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class Server {
    // StockSim Server logger context
    public final static String LOGGER_CONTEXT = "it.unipi.lsmsdb.stocksim.server";

    // StockSim Server logger
    private final static LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    private final static Logger rootLogger = loggerContext.getLogger(LOGGER_CONTEXT);

    // Default input scanner used to read input commands
    private final static Scanner scanner = new Scanner(System.in);

    // StockSim Server DB Manager
    private final static DBManager dbManager = new DBManager(rootLogger);

    /**
     * StockSim Server entry point.
     *
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        // set log level to be used globally by StockSim Server
        ServerUtil.setLogLevel(Level.ERROR);

        // print welcome message
        ServerUtil.printWelcomeMessage();

        // parse command line arguments and run historical data update if needed
        parseArguments(args);

        // start parallel updater thread
        startUpdaterThread();

        // infinite main loop
        while (true) {
            ServerUtil.printMainMenu();
            final String command = scanner.nextLine();
            parseCommand(command);
        }
    }

    /**
     * Parses the given command line arguments.
     *
     * @param args the command line arguments to be parsed.
     */
    private static void parseArguments(final String[] args) {
        // prepare args parser option
        final Option noUpdate = Option.builder("n")
                .longOpt("no-update")
                .argName("noupdate")
                .desc("Do not run database historical data update at startup.")
                .required(false)
                .hasArg(false)
                .build();

        // build an args parser with single option
        final ArgsParser argsParser = new ArgsParser(noUpdate);

        try {
            final CommandLine commandLine = argsParser.getCommandLine(args);

            if (!commandLine.hasOption("no-update")) {
                // automatically update the database on startup
                dbManager.updateDB();
            }
        } catch (final ParseException e) {
            argsParser.printHelp("./server --no-update");
            System.exit(1);
        }
    }

    /**
     * Defines the {@link Runnable} to be executed by the thread in charge
     * of executing the daily update.
     */
    private static void startUpdaterThread() {
        final Runnable runnable = () -> {
        };

        final Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Parses and executes the given command.
     *
     * @param command the command to be executed, if valid.
     */
    private static void parseCommand(final String command) {
        switch (command) {
            case "status":
                final boolean consistent = dbManager.consistencyCheck();
                if (!consistent) {
                    ServerUtil.println("DATA CONSISTENCY CHECK FAILED.\n");
                } else {
                    ServerUtil.println("DATA CONSISTENCY CHECK SUCCESS.\n");
                }
                break;
            case "update":
                dbManager.updateDB();
                break;
            case "quit":
                System.exit(0);
                break;
            default:
                ServerUtil.println("Invalid command.\n");
                break;
        }
    }
}
