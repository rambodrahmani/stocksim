package it.unipi.lsmsdb.stocksim.client;

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
        Util.print("Welcome to the StockSim Client.\n\n");
    }

    /**
     * Prints the main menu for the Server usage.
     */
    public final static void printMainMenu() {
        Util.print("Available Commands:\n");
        Util.print(String.format("%-6s %5s %-40s%n", "login", " ", "login to your account."));
        Util.print(String.format("%-6s %5s %-40s%n", "register", " ", "register a new account."));
        Util.print(String.format("%-6s %5s %-40s%n", "quit", " ", "quit Stocksim client."));
        Util.print("> ");
    }
}
