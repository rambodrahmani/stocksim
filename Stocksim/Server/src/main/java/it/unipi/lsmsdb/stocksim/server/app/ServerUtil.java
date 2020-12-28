package it.unipi.lsmsdb.stocksim.server.app;

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
        println("Welcome to the StockSim Server.\n");
    }

    /**
     * Prints the main menu for the Server usage.
     */
    public final static void printMainMenu() {
        print("Available Commands:\n");
        print(String.format("%-6s %5s %-40s%n", "status", " ", "check databases status."));
        print(String.format("%-6s %5s %-40s%n", "update", " ", "update databases historical data."));
        print(String.format("%-6s %5s %-40s%n", "quit", " ", "quit Stocksim server."));
        print("> ");
    }
}