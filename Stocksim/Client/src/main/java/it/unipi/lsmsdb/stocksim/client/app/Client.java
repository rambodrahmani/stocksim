package it.unipi.lsmsdb.stocksim.client.app;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import it.unipi.lsmsdb.stocksim.client.admin.ClientAdmin;
import it.unipi.lsmsdb.stocksim.client.user.ClientUser;
import it.unipi.lsmsdb.stocksim.lib.util.ArgsParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StockSim Client implementation.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class Client {
    // StockSim Client logger context
    public final static String LOGGER_CONTEXT = "it.unipi.lsmsdb.stocksim.client";

    /**
     * StockSim Client entry point.
     *
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        // set log level to be used globally by StockSim Client
        ClientUtil.setLogLevel(Level.ERROR);

        // print welcome message
        ClientUtil.printWelcomeMessage();

        // parse command line arguments to check if the client must be run in
        // user mode or admin mode
        parseArguments(args);
    }

    /**
     * Parses the given command line arguments.
     *
     * @param args the command line arguments to be parsed.
     */
    private static void parseArguments(final String[] args) {
        // prepare args parser option
        final Option admin = Option.builder("a")
                .longOpt("admin")
                .argName("admin")
                .desc("Run StockSim client in admin mode.")
                .required(false)
                .hasArg(false)
                .build();

        // build an args parser with single option
        final ArgsParser argsParser = new ArgsParser(admin);

        try {
            final CommandLine commandLine = argsParser.getCommandLine(args);

            if (commandLine.hasOption("admin")) {
                ClientAdmin.run();
            } else {
                ClientUser.run();
            }
        } catch (final ParseException e) {
            argsParser.printHelp("./client --admin");
            System.exit(1);
        }
    }
}
