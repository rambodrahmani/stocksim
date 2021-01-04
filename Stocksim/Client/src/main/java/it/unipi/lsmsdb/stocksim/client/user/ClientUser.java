package it.unipi.lsmsdb.stocksim.client.user;

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
                ClientUtil.printUserMainMenu(user);
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
        try {
            final UserMenuAction action = UserMenuAction.valueOf(command.toUpperCase().replace("-", "_"));
            switch (action) {
                case REGISTER:
                    if (isLoggedIn()) {
                        ClientUtil.println("User login already executed. Logout first.\n");
                    } else {
                        if (register()) {
                            ClientUtil.println("User sign up executed correctly. You can now login.\n");
                        } else {
                            ClientUtil.println("User sign up failed.\n");
                        }
                    }
                    break;
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
                case SEARCH_STOCK:
                    if (isLoggedIn()) {
                        
                    } else {
                        ClientUtil.println("You need to login first.\n");
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
        } catch (final IllegalArgumentException e) {
            ClientUtil.println("Invalid command.\n");
        }
    }

    /**
     * User Sign up.
     *
     * @return true in case of success, false otherwise.
     */
    private static boolean register() {
        boolean ret = true;

        // ask for new user real name
        ClientUtil.print("Name: ");
        final String name = scanner.nextLine();

        // ask for new user real surname
        ClientUtil.print("Surname: ");
        final String surname = scanner.nextLine();

        // ask for new user email address
        ClientUtil.print("E-Mail: ");
        final String email = scanner.nextLine();

        // ask for new user login username
        ClientUtil.print("Username [login]: ");
        final String username = scanner.nextLine();

        // ask for new user login password
        ClientUtil.print("Password [login]: ");
        final String password = scanner.nextLine();

        // create new user for sign up
        final User newUser = new User(name, surname, email, username, password);
        ret = newUser.register();

        return ret;
    }

    /**
     * User login.
     *
     * @return true in case of success, false otherwise.
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
