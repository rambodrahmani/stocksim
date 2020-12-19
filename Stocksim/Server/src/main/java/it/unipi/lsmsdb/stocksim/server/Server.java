package it.unipi.lsmsdb.stocksim.server;

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
        Util.printWelcomeMessage();

        // infinite main loop
        while (true) {
            Util.printMainMenu();
            final String command = scanner.nextLine();
            parseCommand(command);
        }
    }

    public static void parseCommand(final String command) {
        switch (command) {
            case "status":
                final boolean consistent = dbManager.consistencyCheck();
                if (!consistent) {
                    Util.print("DATA CONSISTENCY CHECK FAILED.\n");
                } else {
                    Util.print("DATA CONSISTENCY CHECK SUCCESS.\n");
                }
                break;
            case "update":
                break;
            case "quit":
                System.exit(0);
                break;
            default:
                Util.print("Invalid command.\n\n");
                break;
        }
    }
}
