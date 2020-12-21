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
        Util.println("Welcome to the StockSim Server.\n\n");
    }

    /**
     * Prints the main menu for the Server usage.
     */
    public final static void printMainMenu() {
        Util.println("Available Commands:");
        Util.print(String.format("%-6s %5s %-40s%n", "status", " ", "check databases status."));
        Util.print(String.format("%-6s %5s %-40s%n", "update", " ", "update databases historical data."));
        Util.print("Available Commands:\n");
        Util.print(String.format("%-6s %5s %-40s%n", "login", " ", "check databases status."));
        Util.print(String.format("%-6s %5s %-40s%n", "register", " ", "update databases historical data."));
        Util.print(String.format("%-6s %5s %-40s%n", "quit", " ", "quit Stocksim server."));
        Util.print("> ");
    }
}
