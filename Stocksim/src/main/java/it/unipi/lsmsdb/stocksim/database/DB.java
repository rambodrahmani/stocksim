package it.unipi.lsmsdb.stocksim.database;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import it.unipi.lsmsdb.stocksim.database.cassandra.CQLSessionException;

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
