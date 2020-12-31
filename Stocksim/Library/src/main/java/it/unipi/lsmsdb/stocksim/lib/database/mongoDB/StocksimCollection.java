package it.unipi.lsmsdb.stocksim.lib.database.mongoDB;

/**
 * MongoDB Collections names for the StockSim application.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public enum StocksimCollection {
    USERS("users"),     // users info collection
    STOCKS("stocks"),   // stocks info collection
    ADMINS("admins");   // stocksim admin users info collection

    private final String collectionName;

    /**
     * Default constructor.
     *
     * @param collectionName the string for the collection name.
     */
    private StocksimCollection(final String collectionName) {
        this.collectionName = collectionName;
    }

    /**
     * @return the collection name.
     */
    public String getCollectionName() {
        return this.collectionName;
    }
}
