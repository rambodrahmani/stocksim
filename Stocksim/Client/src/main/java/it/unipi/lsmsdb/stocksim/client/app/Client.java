package it.unipi.lsmsdb.stocksim.client.app;

import it.unipi.lsmsdb.stocksim.client.ClientUtil;
import it.unipi.lsmsdb.stocksim.client.entities.Portfolio;
import it.unipi.lsmsdb.stocksim.client.persistence.DBManager;
import it.unipi.lsmsdb.stocksim.client.entities.User;
import it.unipi.lsmsdb.stocksim.util.Util;

import java.util.Scanner;

/**
 * Sotcksim Client implementation.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class

Client {
    /**
     * Default input scanner.
     */
    final static Scanner scanner = new Scanner(System.in);

    /**
     * Stocksim Server DB Manager.
     */
    final static DBManager factory = new DBManager();

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
                Util.print("Invalid command.\n\n");
                break;
        }
    }

    private static void register() {
        // todo register a new user
    }

    private static void dashboard() {
        // todo print user dashboard
        Util.println(loggedUser.getUsername());
        for (Portfolio portfolio : loggedUser.getPortfolios()) {
            Util.println(portfolio.getName()+" "+portfolio.getType()+" "+portfolio.getTotalInvestment());
        }

    }

    private static boolean login() {
        String command;
        do {
            Util.print("Insert username and password divided by a space, or q to go back to main menu:\n");
            command = scanner.nextLine();
            if (command.equals("q"))
                return false;
        } while((loggedUser=factory.login(
                command.split(" ")[0],
                command.split(" ")[1])
        )==null);
        return true;
    }
}
