package it.unipi.lsmsdb.stocksim.client;

import it.unipi.lsmsdb.stocksim.client.entities.Portfolio;
import it.unipi.lsmsdb.stocksim.client.entities.Title;
import it.unipi.lsmsdb.stocksim.client.entities.User;
import it.unipi.lsmsdb.stocksim.util.Util;

/**
 * General purpose utility methods.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class ClientUtil extends Util{
    /**
     * Prints the main menu for the Server usage.
     */
    public final static void printWelcomeMessage() {
        print("Welcome to the StockSim Client.\n\n");
    }

    /**
     * Prints the main menu for the Server usage.
     */
    public final static void printMainMenu() {
        print("Available Commands:\n");
        print(String.format("%-6s %5s %-40s%n", "login", " ", "login to your account."));
        print(String.format("%-6s %5s %-40s%n", "register", " ", "register a new account."));
        print(String.format("%-6s %5s %-40s%n", "quit", " ", "quit Stocksim client."));
        print("> ");
    }

    /**
     * Prints a portfolio details and composition
     */
    public final static void printPortfolio(final Portfolio p){
        if(p==null)
            return;
        print(String.format("Portfolio %5s %-40s%n"," ",p.getName()));
        for (Title title : p.getComposition()) {
            print(String.format(
                    "%-6a %5s %-6s %5s %-40s%n",
                    title.getShare(), " ",
                    title.getStock().getTicker()," ",
                    title.getStock().getShort_name()));
        }
        print("write a ticker in capital letters to see stock details\n");
        print("> ");
    }


    public final static void printDashboard(final User u){
        if(u==null)
            return;
        print(String.format("Welcome %5s %-40s!%n"," ",u.getUsername()));
        for (Portfolio portfolio : u.getPortfolios()) {
            print(String.format(
                    "%-6a %5s %-20s %5s %-20s%n",
                    portfolio.getTotalInvestment(), " ",
                    portfolio.getName()," ",
                    portfolio.getName()));
        }
        print("write the name of a portfolio to see it's details\n");
        print("q to logout\n");
        print("p to see profile details\n");
        print("n to create a new portfolio\n");
        print("> ");
    }

    public static void printProfile(final User u) {
        print(String.format("Profile of %5s %-40s%n"," ",u.getUsername()));
    }
}
