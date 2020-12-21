package it.unipi.lsmsdb.stocksim.client;

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
}
