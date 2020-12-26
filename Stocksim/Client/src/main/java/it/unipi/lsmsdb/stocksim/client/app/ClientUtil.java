package it.unipi.lsmsdb.stocksim.client.app;

import it.unipi.lsmsdb.stocksim.util.Util;
import org.apache.commons.codec.digest.DigestUtils;

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
     * Prints the login menu for the Client usage in admin mode.
     */
    public final static void printAdminLoginMenu() {
        print("Available Commands:\n");
        print(String.format("%-6s %5s %-40s%n", "login", " ", "login to your admin account."));
        print(String.format("%-6s %5s %-40s%n", "quit", " ", "quit StockSim client."));
        print("> ");
    }

    /**
     * Prints the main menu for the Client usage in admin mode.
     */
    public final static void printAdminMainMenu() {
        print("Available Commands:\n");
        print(String.format("%-6s %5s %-40s%n", "logout", " ", "login to your admin account."));
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

    /**
     * @param string the plain text to be hashed.
     *
     * @return the SHA-256 hash for the given plain text string.
     */
    public final static String SHA256Hash(final String string) {
        return DigestUtils.sha256Hex(string);
    }
}
