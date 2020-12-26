package it.unipi.lsmsdb.stocksim.client.app;

import it.unipi.lsmsdb.stocksim.client.admin.Admin;

import java.util.Scanner;

/**
 * StockSim Admin Client implementation.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class ClientAdmin {
    // Default input scanner.
    private static final Scanner scanner = new Scanner(System.in);

    // StockSim admin
    private static Admin admin = null;

    /**
     * StockSim Client admin mode run main loop.
     */
    public static void run() {
        ClientUtil.println("*** [RUNNING IN ADMIN MODE] ***\n");

        // infinite main loop
        while (true) {
            if (isLoggedIn()) {
                ClientUtil.printAdminMainMenu();
            } else {
                ClientUtil.printAdminLoginMenu();
            }
            final String command = scanner.nextLine();
            parseCommand(command);
        }
    }

    /**
     * Parses and executes the given command.
     *
     * @param command the command to be executed, if valid.
     */
    private static void parseCommand(final String command) {
        switch (command) {
            case "login":
                if (login()) {
                    ClientUtil.println("Admin login executed correctly.");
                    ClientUtil.println("Welcome " + admin.getName() + " " + admin.getSurname() + ".\n");
                } else {
                    ClientUtil.println("Admin login failed.\n");
                }
                break;
            case "logout":
                logout();
                ClientUtil.println("Admin logged out.\n");
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
    private static boolean login() {
        boolean ret = true;

        // ask for admin username
        ClientUtil.print("Username: ");
        final String username = scanner.nextLine();

        // ask for password username
        ClientUtil.print("Password: ");
        final String password = scanner.nextLine();

        // do the login if not already logged in
        if (isLoggedIn()) {
            ClientUtil.println("Admin login already executed.");
        } else {
            admin = new Admin(username, password);
            ret = admin.login();
        }

        return ret;
    }

    /**
     * Admin logout.
     */
    private static void logout() {
        admin = admin.logout();
    }

    /**
     * @return if the admin has been allocated and logged in.
     */
    private static boolean isLoggedIn() {
        return admin != null && admin.isLoggedIn();
    }
}
