package it.unipi.lsmsdb.stocksim.client.user;

import it.unipi.lsmsdb.stocksim.client.admin.AdminMenuAction;
import it.unipi.lsmsdb.stocksim.client.app.ClientUtil;

import java.util.Scanner;

/**
 * StockSim Client User mode implementation.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class ClientUser {
    // Default input scanner.
    private static final Scanner scanner = new Scanner(System.in);

    // StockSim user
    private static User user = null;

    /**
     * StockSim Client user mode run main loop.
     */
    public static void run() {
        ClientUtil.println("*** [RUNNING IN USER MODE] ***\n");

        // infinite main loop
        while (true) {
            if (isLoggedIn()) {
                ClientUtil.printUserMainMenu();
            } else {
                ClientUtil.printUserLoginMenu();
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
        final UserMenuAction action = UserMenuAction.valueOf(command.toUpperCase().replace("-", "_"));
        switch (action) {
            case LOGIN:
                if (isLoggedIn()) {
                    ClientUtil.println("User login already executed.\n");
                } else {
                    if (login()) {
                        ClientUtil.println("User login executed correctly.");
                        ClientUtil.println("Welcome " + user.getName() + " " + user.getSurname() + ".\n");
                    } else {
                        ClientUtil.println("User login failed.\n");
                    }
                }
                break;
            case LOGOUT:
                if (isLoggedIn()) {
                    logout();
                    ClientUtil.println("User logged out.\n");
                } else {
                    ClientUtil.println("You need to login first.\n");
                }
                break;
            case QUIT:
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
    private static boolean login() {
        boolean ret = true;

        // ask for user username
        ClientUtil.print("Username: ");
        final String username = scanner.nextLine();

        // ask for password username
        ClientUtil.print("Password: ");
        final String password = scanner.nextLine();

        // do the login if not already logged in
        if (isLoggedIn()) {
            ClientUtil.println("User login already executed.");
        } else {
            user = new User(username, password);
            ret = user.login();
        }

        return ret;
    }

    /**
     * User logout.
     */
    private static void logout() {
        user = user.logout();
    }

    /**
     * @return if the user has been allocated and logged in.
     */
    private static boolean isLoggedIn() {
        return user != null && user.isLoggedIn();
    }
}
