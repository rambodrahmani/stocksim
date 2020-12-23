package it.unipi.lsmsdb.stocksim.client.app;

import it.unipi.lsmsdb.stocksim.client.ClientUtil;
import it.unipi.lsmsdb.stocksim.client.entities.Portfolio;
import it.unipi.lsmsdb.stocksim.client.entities.Stock;
import it.unipi.lsmsdb.stocksim.client.entities.Title;
import it.unipi.lsmsdb.stocksim.client.persistence.DBManager;
import it.unipi.lsmsdb.stocksim.client.entities.User;
import it.unipi.lsmsdb.stocksim.util.Util;
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
        loggedUser=factory.login("TWOWS", "abcd1234");
        dashboard();
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

    /**
     * Register a new user
     */
    private static void register() {
        // TODO: register a new user
    }

    /**
     * Show logged user portfolios summaries
     */
    private static void dashboard() {
        ClientUtil.printDashboard(loggedUser);
        String command=scanner.nextLine();
        while(loggedUser!=null) {
            switch (command) {
                case "q":
                    logout();
                    break;
                case "p":
                    ClientUtil.printProfile(loggedUser);
                    break;
                case "n":
                    createNewPortfolio();
                    break;
                default:
                    Portfolio p = loggedUser.getPortfolioByName(command);
                    if (p != null)
                        showPortfolio(p);
                    else
                        Util.print("Invalid command.\n\n");
                    break;
            }
            command = scanner.nextLine();
        }

    }

    /**
     * Show portfolio's details
     */
    private static void showPortfolio(Portfolio p) {
        ClientUtil.printPortfolio(p);
        String command=scanner.nextLine();
        while(command!="q") {
            switch (command) {
                case "p":
                    ClientUtil.printProfile(loggedUser);
                    break;
                case "n":
                    createNewPortfolio();
                    break;
                default:
                    Title t = p.getTitleByTicker(command);
                    if (t != null)
                        showTitleDetails(t, p.getTotalInvestment());
                    else
                        Util.print("Invalid command.\n\n");
                    break;
            }
            command=scanner.nextLine();
        }
    }

    /**
     * Show title's details
     */
    private static void showTitleDetails(Title t, Double portfolioTotInv) {
        //todo
    }

    /**
     * Allow a user to create a new portfolio inserting details from terminal
     */
    private static void createNewPortfolio() {
        // this is for test
        // todo
        Portfolio newp=loggedUser.addPortfolio("ClientPort", "client");
        if(newp!=null)
            newp.add(factory.getStockByTicker("AAPL"), 100.00);
    }

    /**
     * Login procedure
     */
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
    /**
     * Logout procedure
     */
    private static void logout() {
        loggedUser=null;
    }

}
