package it.unipi.lsmsdb.stocksim.client.app;

import it.unipi.lsmsdb.stocksim.client.database.DBManager;
import it.unipi.lsmsdb.stocksim.util.Util;
import java.util.Scanner;

/**
 * StockSim Client User mode implementation.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class ClientUser {
    // Default input scanner.
    private final Scanner scanner = new Scanner(System.in);

    // StockSim Client user mode DB Manager.
    private final DBManager dbManager = new DBManager();

    /**
     * StockSim Client user mode run main loop.
     */
    public void run() {
        ClientUtil.println("*** [RUNNING IN USER MODE] ***\n");

        // infinite main loop
        while (true) {
            ClientUtil.printUserMainMenu();
            final String command = scanner.nextLine();
            parseCommand(command);
        }
    }

    /**
     * Parses and executes the given command.
     *
     * @param command the command to be executed, if valid.
     */
    public void parseCommand(final String command) {
        switch (command) {
            case "login":
                login();
                break;
            case "quit":
                System.exit(0);
                break;
            default:
                ClientUtil.println("Invalid command.\n");
                break;
        }
    }

    /**
     * User login.
     */
    private void login() {
        // ask for admin username
        final String username = scanner.nextLine();

        // ask for password username
        final String password = scanner.nextLine();

        // do th login
    }
}
