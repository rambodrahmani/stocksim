package it.unipi.lsmsdb.stocksim.client.app;

import it.unipi.lsmsdb.stocksim.client.ClientUtil;
import it.unipi.lsmsdb.stocksim.client.entities.Portfolio;
import it.unipi.lsmsdb.stocksim.client.entities.Stock;
import it.unipi.lsmsdb.stocksim.client.entities.Title;
import it.unipi.lsmsdb.stocksim.client.persistence.DBManager;
import it.unipi.lsmsdb.stocksim.client.entities.User;
import it.unipi.lsmsdb.stocksim.util.Util;
import jnr.ffi.Struct;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Function;

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
        String command="";
        while(loggedUser!=null) {
            ClientUtil.println("write the name of a portfolio to see it's details\n");
            ClientUtil.println("s to browse stocks\n");
            ClientUtil.println("p to see profile details\n");
            ClientUtil.println("n to create a new portfolio\n");
            ClientUtil.println("q to logout\n");
            ClientUtil.println("> ");
            command=scanner.nextLine();
            switch (command) {
                case "s":
                    browseStocks();
                    break;
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
        }

    }

    /**
     * Browse the stocks' collection
     */
    private static void browseStocks() {
        ArrayList<Function> f=new ArrayList<>();
        String command;
        ClientUtil.println("available search methods:\n");
        ClientUtil.println("1. by ticker\n");
        ClientUtil.println("2. by currency\n");
        ClientUtil.println("3. by capitalization range\n");
        ClientUtil.println("4. by PE ratio range\n");
        ClientUtil.println("5. by Sector\n");
        ClientUtil.println("6. by Quote Type (Equity/ETF)\n");
        ClientUtil.println("7. by Industry\n");
        ClientUtil.println("> ");
        while(true){
            command = scanner.nextLine();
            switch (command) {
                case "1":
                    searchBy("ticker");
                    break;
                case "2":
                    searchBy("currency");
                    break;
                case "3":
                    searchIn("marketCap");
                    break;
                case "4":
                    searchIn("trailingPE");
                    break;
                case "5":
                    searchBy("sector");
                    break;
                case "6":
                    searchBy("quoteType");
                    break;
                case "7":
                    searchBy("industry");
                    break;
            }
        }

        
    }

    /**
     * Search by a parameter in a particular range
     * @param attribute the attribute which to perform the search
     */
    private static void searchIn(String attribute) {
        ClientUtil.println("insert min from research:");
        String min = scanner.nextLine();
        ClientUtil.println("insert max from research:");
        String max= scanner.nextLine();
        try {
            ClientUtil.printStocks(
                    factory.getStockByAttribute(attribute,
                            Double.parseDouble(min), Double.parseDouble(max))
            );
        }catch (Exception e)
        {        }
    }

    /**
     * Search by a parameter having an exact value
     * @param attribute the attribute which to perform the search
     */
    private static void searchBy(String attribute) {
        ClientUtil.println("insert value from research:\n");
        String command = scanner.nextLine();
        try {
            ClientUtil.printStocks(factory.getStockByAttribute(attribute, command));
        }
        catch (Exception e)
        {        }

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
        ClientUtil.println(
                String.format( "%-6f%% of the portfolio %5s %-6f",
                        t.getShare()*100/portfolioTotInv,
                        " ", t.getShare())
        );
        ClientUtil.printStock(t.getStock());
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
     * Delete a portfolio if exists
     */
    private static void deletePortfolio(Portfolio p) {
        factory.deletePortfolio(p);
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
            try {
                loggedUser=factory.login(
                        command.split(" ")[0],
                        command.split(" ")[1]);
            }
            catch (Exception e)
            {
                loggedUser=null;
            }
        } while(loggedUser==null);
        return true;
    }
    /**
     * Logout procedure
     */
    private static void logout() {
        loggedUser=null;
    }

}
