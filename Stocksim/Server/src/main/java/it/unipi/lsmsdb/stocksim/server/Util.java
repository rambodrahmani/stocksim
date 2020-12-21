package it.unipi.lsmsdb.stocksim.server;

/**
 * General purpose utility methods.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class Util extends it.unipi.lsmsdb.stocksim.util.Util {


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
        print(String.format("%-6s %5s %-40s%n", "login", " ", "check databases status."));
        print(String.format("%-6s %5s %-40s%n", "register", " ", "update databases historical data."));
        print(String.format("%-6s %5s %-40s%n", "quit", " ", "quit Stocksim server."));
        print("> ");
    }
}
