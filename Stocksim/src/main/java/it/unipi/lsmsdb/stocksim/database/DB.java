package it.unipi.lsmsdb.stocksim.database;

/**
 * DB Manager interface.
 * All DB Managers should implement this interface.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public interface DB {
    public boolean connect();
    public DB disconnect();
}
