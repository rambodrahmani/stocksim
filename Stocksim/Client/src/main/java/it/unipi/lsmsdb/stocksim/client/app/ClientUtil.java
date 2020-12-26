package it.unipi.lsmsdb.stocksim.client.app;

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
     * Prints the main menu for the Client usage in admin mode.
     */
    public final static void printAdminMainMenu() {
        print("Available Commands:\n");
        print(String.format("%-6s %5s %-40s%n", "login", " ", "login to your admin account."));
        print(String.format("%-6s %5s %-40s%n", "quit", " ", "quit StockSim client."));
        print("> ");
    }

    /**
     * Prints the main menu for the Client usage in user mode.
     */
    public final static void printUserMainMenu() {
        print("Available Commands:\n");
        print(String.format("%-6s %5s %-40s%n", "login", " ", "login to your user account."));
        print(String.format("%-6s %5s %-40s%n", "register", " ", "register a new user account."));
        print(String.format("%-6s %5s %-40s%n", "quit", " ", "quit StockSim client."));
        print("> ");
    }
}
