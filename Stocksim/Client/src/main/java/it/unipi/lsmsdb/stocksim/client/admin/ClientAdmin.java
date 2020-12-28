package it.unipi.lsmsdb.stocksim.client.admin;

import it.unipi.lsmsdb.stocksim.client.app.ClientUtil;

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
        try {
            final AdminMenuAction action = AdminMenuAction.valueOf(command.toUpperCase().replace("-", "_"));
            switch (action) {
                case LOGIN:
                    if (isLoggedIn()) {
                        ClientUtil.println("Admin login already executed.\n");
                    } else {
                        if (login()) {
                            ClientUtil.println("Admin login executed correctly.");
                            ClientUtil.println("Welcome " + admin.getName() + " " + admin.getSurname() + ".\n");
                        } else {
                            ClientUtil.println("Admin login failed.\n");
                        }
                    }
                    break;
                case ADD_TICKER:
                    if (isLoggedIn()) {
                        if (addTicker()) {
                            ClientUtil.println("New ticker added.\n");
                        } else {
                            ClientUtil.println("Could not add new ticker.\n");
                        }
                    } else {
                        ClientUtil.println("You need to login first.\n");
                    }
                    break;
                case ADD_ADMIN:
                    if (isLoggedIn()) {
                        if (addAdmin()) {
                            ClientUtil.println("New admin account created.\n");
                        } else {
                            ClientUtil.println("Could not create new admin account.\n");
                        }
                    } else {
                        ClientUtil.println("You need to login first.\n");
                    }
                    break;
                case REMOVE_ADMIN:
                    if (isLoggedIn()) {
                        if (removeAdmin()) {
                            ClientUtil.println("Admin account deleted.\n");
                        } else {
                            ClientUtil.println("Could not delete admin account.\n");
                        }
                    } else {
                        ClientUtil.println("You need to login first.\n");
                    }
                    break;
                case REMOVE_USER:
                    if (isLoggedIn()) {
                        if (removeUser()) {
                            ClientUtil.println("User account deleted.\n");
                        } else {
                            ClientUtil.println("Could not delete user account.\n");
                        }
                    } else {
                        ClientUtil.println("You need to login first.\n");
                    }
                    break;
                case LOGOUT:
                    if (isLoggedIn()) {
                        logout();
                        ClientUtil.println("Admin logged out.\n");
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
        } catch (final IllegalArgumentException e) {
            ClientUtil.println("Invalid command.\n");
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

        // do the login
        admin = new Admin(username, password);
        ret = admin.login();

        return ret;
    }

    /**
     * Add ticker.
     */
    private static boolean addTicker() {
        boolean ret = true;

        // ask for admin username
        ClientUtil.print("Ticker symbol: ");
        final String symbol = scanner.nextLine();

        // ask for password username
        ClientUtil.print("CSV path: ");
        final String csvPath = scanner.nextLine();

        // add new ticker
        admin.addTicker(symbol, csvPath);

        return ret;
    }

    /**
     * Add admin account.
     */
    private static boolean addAdmin() {
        boolean ret = true;

        // ask for new admin account name
        ClientUtil.print("Admin account name: ");
        final String name = scanner.nextLine();

        // ask for new admin account surname
        ClientUtil.print("Admin account surname: ");
        final String surname = scanner.nextLine();

        // ask for new admin account username
        ClientUtil.print("Admin account username: ");
        final String username = scanner.nextLine();

        // ask for new admin account password
        ClientUtil.print("Admin account password: ");
        final String password = scanner.nextLine();

        // do create the admin account
        ret = admin.createAdminAccount(name, surname, username, password);

        return ret;
    }

    /**
     * Remove admin account.
     */
    private static boolean removeAdmin() {
        boolean ret = true;

        // ask for new admin account username
        ClientUtil.print("Admin account username: ");
        final String username = scanner.nextLine();

        // ask for new admin account password
        ClientUtil.print("Admin account password: ");
        final String password = scanner.nextLine();

        // check if we are trying to delete the currently logged in admin account
        if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
            ClientUtil.println("Could not delete currently logged in admin account.");
            ret = false;
        } else {
            // do create the admin account
            ret = admin.removeAdminAccount(username, password);
        }

        return ret;
    }

    /**
     * Remove user account.
     */
    private static boolean removeUser() {
        boolean ret = true;

        // ask for new user account email
        ClientUtil.print("User account email: ");
        final String email = scanner.nextLine();

        // remove the user with the given email
        ret = admin.removeUserAccount(email);

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
