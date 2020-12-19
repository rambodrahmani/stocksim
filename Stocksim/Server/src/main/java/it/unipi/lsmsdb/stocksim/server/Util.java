package it.unipi.lsmsdb.stocksim.server;

/**
 * General purpose utility methods.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class Util {
    /**
     * Prints the given message to the console.
     *
     * @param message the String message to be printed.
     */
    public final static void print(final String message) {
        System.out.print(message);
    }

    /**
     * Prints the main menu for the Server usage.
     */
    public final static void printWelcomeMessage() {
        print("Welcome to the StockSim Server.\n\n");
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
