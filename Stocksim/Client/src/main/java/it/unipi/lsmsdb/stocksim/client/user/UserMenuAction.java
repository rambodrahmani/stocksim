package it.unipi.lsmsdb.stocksim.client.user;

/**
 * StockSim Client User menu actions enum.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public enum UserMenuAction {
    REGISTER("register", "create a new user account."),
    LOGIN("login", "login to your user account."),
    CREATE_PORTFOLIO("create-portfolio", "create a new stock portfolio."),
    SEARCH("search-stock", "search for a stock ticker."),
    LOGOUT("logout", "logout from current user account."),
    QUIT("quit", "quit StockSim client.");

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
    private UserMenuAction(final String actionName, final String actionDescription) {
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
