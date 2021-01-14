package it.unipi.lsmsdb.stocksim.server.app;

import ch.qos.logback.classic.Level;
import it.unipi.lsmsdb.stocksim.lib.util.ArgsParser;
import it.unipi.lsmsdb.stocksim.server.database.DBManager;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Scanner;

/**
 * SotckSim Server implementation.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class Server {
    // StockSim Server logger context
    public final static String LOGGER_CONTEXT = "it.unipi.lsmsdb.stocksim.server";

    // Default input scanner used to read input commands
    private final static Scanner scanner = new Scanner(System.in);

    // StockSim Server DB Manager
    private final static DBManager dbManager = new DBManager();

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

        // start parallel updater thread
        startUpdaterThread();

        // parse command line arguments and run historical data update if needed
        parseArguments(args);

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
     * of executing the daily historical data update.
     */
    private static void startUpdaterThread() {
        final Runnable updaterRunnable = () -> {
            try {
                // thread infinite main loop
                while (true) {
                    // get current New York clock time
                    final ZoneId nyZoneId = ZoneId.of("America/New_York");
                    final Instant instant = Clock.systemDefaultZone().instant();
                    final int currentHour = instant.atZone(nyZoneId).getHour();

                    ServerUtil.println("\n" + "[UPDATER THREAD] Current New York time: " + instant.atZone(nyZoneId).toString());

                    if (currentHour < 20) {
                        final int sleepHours = 20 - currentHour;
                        ServerUtil.print("[UPDATER THREAD] Going to sleep for " + sleepHours + " hours before next update.\n> ");
                        Thread.sleep(sleepHours * 60 * 60 *1000);
                    } else {
                        ServerUtil.println("[UPDATER THREAD] Starting historical data update.");
                        dbManager.updateDB();
                        final int sleepHours = (24 - Clock.systemDefaultZone().instant().atZone(nyZoneId).getHour()) + 20;
                        ServerUtil.print("[UPDATER THREAD] Going to sleep for " + sleepHours + " hours before next update.\n> ");
                        Thread.sleep(sleepHours * 60 * 60 *1000);
                    }
                }
            } catch (final InterruptedException e) {
                // nothing to do
            }
        };

        // start updater thread
        final Thread updaterThread = new Thread(updaterRunnable, "StockSim Server Updater");
        updaterThread.start();
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
