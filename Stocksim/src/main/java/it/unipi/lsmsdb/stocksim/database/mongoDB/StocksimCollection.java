package it.unipi.lsmsdb.stocksim.database.mongoDB;

/**
 * MongoDB Collections names.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public enum StocksimCollection {
    USERS("users"),
    STOCKS("stocks"),
    ADMINS("admins");

    private final String collectionName;

    private StocksimCollection(final String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCollectionName() {
        return this.collectionName;
    }
}
