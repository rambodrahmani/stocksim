package it.unipi.lsmsdb.stocksim.lib.util;

import org.apache.commons.cli.*;

/**
 * Arguments parser implementation using Apache Commons CLI.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class ArgsParser {
    /**
     * Options list.
     */
    final private Options options;

    /**
     * Default command line parser instance.
     */
    final private CommandLineParser commandLineParser = new DefaultParser();

    /**
     * A formatter of help messages for command line options.
     */
    final private HelpFormatter helpFormatter = new HelpFormatter();

    /**
     * Default constructor with single initial option.
     */
    public ArgsParser(final Option option) {
        this.options = new Options();
        options.addOption(option);
    }

    /**
     * Default constructor with multiple initial options.
     */
    public ArgsParser(final Options options) {
        this.options = options;
    }

    /**
     * @param args list of arguments to be parsed against the Options descriptor;
     *
     * @return the {@link CommandLine} obtained parsing the list of arguments
     * against the Options descriptor;
     *
     * @throws ParseException
     */
    public CommandLine getCommandLine(final String[] args) throws ParseException {
        return commandLineParser.parse(this.options, args);
    }

    /**
     * @return the formatter of help messages for command line option.
     */
    public HelpFormatter getHelpFormatter() {
        return this.helpFormatter;
    }

    /**
     * Print the help for the Options descriptor with the specified command
     * line syntax.
     *
     * @param cmdLineSyntax the specified command line syntax.
     */
    public void printHelp(final String cmdLineSyntax) {
        getHelpFormatter().printHelp(cmdLineSyntax, options);
    }

    /**
     * Developer harness test.
     *
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        Option noUpdate = Option.builder("n")
                .longOpt("no-update")
                .argName("noupdate")
                .desc("Do not run database historical data update at startup.")
                .required(true)
                .hasArg(false)
                .build();

        // build and args parser with single option
        final ArgsParser argsParser = new ArgsParser(noUpdate);

        try {
            final CommandLine commandLine = argsParser.getCommandLine(args);
            if (commandLine.hasOption("no-update")) {
                System.out.println(commandLine.getOptionValue("no-update"));
            } else {
                System.out.println("Argument not specified.");
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            argsParser.printHelp("./stocksim --no-update");
            System.exit(1);
        }
    }
}
