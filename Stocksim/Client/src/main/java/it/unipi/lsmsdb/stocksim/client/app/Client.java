package it.unipi.lsmsdb.stocksim.client.app;

import it.unipi.lsmsdb.stocksim.client.ClientUtil;
import it.unipi.lsmsdb.stocksim.client.persistence.DBManager;
import it.unipi.lsmsdb.stocksim.client.user.*;
import org.bson.Document;

import java.util.Scanner;

/**
 * Sotcksim Client implementation.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class Client {
    /**
     * Default input scanner.
     */
    final static Scanner scanner = new Scanner(System.in);

    /**
     * Stocksim Server DB Manager.
     */
    final static DBManager dbManager = new DBManager();

    /**
     * Logged user information, no user is logged in
     */
     static User loggedUser = null;

    /**
     * Server entry point.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        // print welcome message
        ClientUtil.printWelcomeMessage();

        // infinite main loop
        while (true) {
            ClientUtil.printMainMenu();
            final String command = scanner.nextLine();
            parseCommand(command);
        }
    }
    public static void parseCommand(final String command) {
        switch (command) {
            case "login":
               if(login())
                   dashboard();
                break;
            case "register":
                register();
                break;
            case "quit":
                System.exit(0);
                break;
            default:
                ClientUtil.print("Invalid command.\n\n");
                break;
        }
    }

    private static void register() {
    }

    private static void dashboard() {
    }

    private static boolean login() {
        String command;
        do {
            ClientUtil.print("Insert username and password divided by a space, or q to go back to main menu:\n");
            command = scanner.nextLine();
            if (command.equals("q"))
                return false;
        } while((loggedUser=dbManager.login(
                command.split(" ")[0],
                command.split(" ")[1])
        )!=null);
        return true;
    }
}
