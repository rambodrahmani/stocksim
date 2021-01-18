package it.unipi.lsmsdb.stocksim.client.user;

/**
 * StockSim Client User menu actions enum.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public enum SearchMenuAction {
    SYMBOL_SEARCH("symbol-search", "search for a stock ticker using its ticker."),
    INDUSTRY_SEARCH("industry-search", "search for a stock ticker using the industry sector."),
    COUNTRY_SEARCH("country-search", "search for a stock ticker using the country.");

    // action name for the main menu
    private final String actionName;

    // action description for the main menu
    private final String actionDescription;

    /**
     * Default constructor.
     *
     * @param actionName the string for the action name.
     * @param actionDescription the string for the action description.
     */
    private SearchMenuAction(final String actionName, final String actionDescription) {
        this.actionName = actionName;
        this.actionDescription = actionDescription;
    }

    /**
     * @return the menu action name.
     */
    public String getActionName() {
        return this.actionName;
    }

    /**
     * @return the menu action description.
     */
    public String getActionDescription() {
        return actionDescription;
    }
}
