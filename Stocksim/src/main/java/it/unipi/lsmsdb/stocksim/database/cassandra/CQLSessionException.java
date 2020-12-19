package it.unipi.lsmsdb.stocksim.database.cassandra;

/**
 * CQL Session custom exception.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class CQLSessionException extends Exception {

    /**
     * Default constructor.
     *
     * @param message exception message.
     */
    public CQLSessionException(final String message) {
        super(message);
    }
}
