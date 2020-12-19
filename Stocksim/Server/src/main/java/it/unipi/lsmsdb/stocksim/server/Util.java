package it.unipi.lsmsdb.stocksim.server;

/**
 * General purpose utility methods.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class Util {
    /**
     * Prints the main menu for the Server usage.
     */
    public final static void printWelcomeMessage() {
        System.out.println("Welcome to the StockSim Server.\n");
    }

    /**
     * Prints the main menu for the Server usage.
     */
    public final static void printMainMenu() {
        System.out.println("Available Commands:");
        System.out.printf("%-6s %5s %-40s%n", "status", " ", "check databases status.");
        System.out.printf("%-6s %5s %-40s%n", "update", " ", "update databases historical data.");
        System.out.printf("%-6s %5s %-40s%n", "quit", " ", "quit Stocksim server.");
        System.out.println("");
    }
}
