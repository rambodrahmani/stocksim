package it.unipi.lsmsdb.stocksim.server;

import it.unipi.lsmsdb.stocksim.util.ArgsParser;
import org.apache.commons.cli.*;

import java.util.Scanner;

/**
 * Sotcksim Server implementation.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class Server {
    /**
     * Default input scanner.
     */
    final static Scanner scanner = new Scanner(System.in);

    /**
     * Stocksim Server DB Manager.
     */
    final static DBManager dbManager = new DBManager();

    /**
     * Server entry point.
     *
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        // print welcome message
        ServerUtil.printWelcomeMessage();

        // parse command line arguments and run historical data update
        // if needed
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
        final Option noUpdate = Option.builder("n")
                .longOpt("no-update")
                .argName("noupdate")
                .desc("Do not run database historical data update at startup.")
                .required(false)
                .hasArg(false)
                .build();

        // build and args parser with single option
        final ArgsParser argsParser = new ArgsParser(noUpdate);

        try {
            final CommandLine commandLine = argsParser.getCommandLine(args);

            if (!commandLine.hasOption("no-update")) {
                // automatically update the database on startup
                dbManager.updateDB();
            }
        } catch (final ParseException e) {
            argsParser.printHelp("./stocksim --no-update");
            System.exit(1);
        }
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
