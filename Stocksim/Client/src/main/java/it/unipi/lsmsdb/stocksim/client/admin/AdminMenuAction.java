package it.unipi.lsmsdb.stocksim.client.admin;

/**
 * StockSim Client Admin menu actions enum.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public enum AdminMenuAction {
    LOGIN("login", "login to your admin account."),
    ADD_TICKER("add-ticker", "add a new ticker symbol to the database."),
    ADD_ADMIN("add-admin", "create new admin account."),
    REMOVE_ADMIN("remove-admin", "delete admin account."),
    REMOVE_USER("remove-user", "delete user account."),
    LOGOUT("logout", "logout from current admin account."),
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
    private AdminMenuAction(final String actionName, final String actionDescription) {
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
