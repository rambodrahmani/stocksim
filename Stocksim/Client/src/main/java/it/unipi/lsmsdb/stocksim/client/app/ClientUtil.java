package it.unipi.lsmsdb.stocksim.client.app;

import it.unipi.lsmsdb.stocksim.client.entities.Portfolio;
import it.unipi.lsmsdb.stocksim.client.entities.Stock;
import it.unipi.lsmsdb.stocksim.client.entities.Title;
import it.unipi.lsmsdb.stocksim.client.entities.User;
import it.unipi.lsmsdb.stocksim.util.Util;

import java.util.List;

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
        println("Welcome to the StockSim Client.\n");
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
                    "%-6f %5s %-6s %5s %-40s%n",
                    title.getShare(), " ",
                    title.getStock().getTicker()," ",
                    title.getStock().getShort_name()));
        }
        print("write a ticker in capital letters to see stock details\n");
        print("> ");
    }

    /**
     * Prints user dashboard
     */
    public final static void printDashboard(final User u){
        if(u==null)
            return;
        print(String.format("Welcome %5s %-40s!%n"," ",u.getUsername()));
        for (Portfolio portfolio : u.getPortfolios()) {
            print(String.format(
                    "%-6f %5s %-20s %5s %-20s%n",
                    portfolio.getTotalInvestment(), " ",
                    portfolio.getName()," ",
                    portfolio.getName()));
        }
    }

    public static void printProfile(final User u) {
        // todo to be completed
        print(String.format("Profile of %5s %-40s%n"," ",u.getUsername()));
    }

    public static void printStock(final Stock stock){
        print(String.format(
                "%-6s %5s %-20s %5s %-20s%n",
                stock.getTicker(), " ",
                stock.getShort_name()," ",
                stock.getQuoteType()));
    }
    public static void printStockDetails(final Stock stock) {
        // todo to be completed
        if(stock==null)
            return;
        printStock(stock);
        print(String.format(
                "%-6s %5s %-20s %5s %-20s%n",
                stock.getCurrency(), " ",
                stock.getMarket_cap()," ",
                stock.getPE_ratio()));
    }

    public static void printStocks(List<Stock> stocks) {
        for (Stock stock : stocks) {
            printStock(stock);
        }
    }
}
