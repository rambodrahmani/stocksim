package it.unipi.lsmsdb.stocksim.server;

import it.unipi.lsmsdb.stocksim.util.Util;

/**
 * General purpose utility methods.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class ServerUtil extends Util{
    /**
     * Prints the main menu for the Server usage.
     */
    public final static void printWelcomeMessage() {
        println("Welcome to the StockSim Server.\n\n");
    }

    /**
     * Prints the main menu for the Server usage.
     */
    public final static void printMainMenu() {
        println("Available Commands:");
        print(String.format("%-6s %5s %-40s%n", "status", " ", "check databases status."));
        print(String.format("%-6s %5s %-40s%n", "update", " ", "update databases historical data."));
        print("Available Commands:\n");
        print(String.format("%-6s %5s %-40s%n", "login", " ", "check databases status."));
        print(String.format("%-6s %5s %-40s%n", "register", " ", "update databases historical data."));
        print(String.format("%-6s %5s %-40s%n", "quit", " ", "quit Stocksim server."));
        print("> ");
    }
}
