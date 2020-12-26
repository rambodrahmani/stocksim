package it.unipi.lsmsdb.stocksim.client.app;

import it.unipi.lsmsdb.stocksim.client.admin.Admin;
import it.unipi.lsmsdb.stocksim.client.database.DBManager;

import java.util.Scanner;

/**
 * StockSim Admin Client implementation.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class ClientAdmin {
    // Default input scanner.
    private final Scanner scanner = new Scanner(System.in);

    // StockSim Client admin mode DB Manager.
    private final DBManager dbManager = new DBManager();

    // StockSim admin
    private Admin admin = null;

    /**
     * StockSim Client admin mode run main loop.
     */
    public void run() {
        ClientUtil.println("*** [RUNNING IN ADMIN MODE] ***\n");

        // infinite main loop
        while (true) {
            ClientUtil.printAdminMainMenu();
            final String command = scanner.nextLine();
            parseCommand(command);
        }
    }

    /**
     * Parses and executes the given command.
     *
     * @param command the command to be executed, if valid.
     */
    private void parseCommand(final String command) {
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
     * Admin login.
     */
    private void login() {
        // ask for admin username
        final String username = scanner.nextLine();

        // ask for password username
        final String password = scanner.nextLine();

        // do the login
        if (admin == null) {
            admin = new Admin(username, password);
        } else {
            ClientUtil.println("Admin login already executed/");
        }
    }
}
