package it.unipi.lsmsdb.workgroup4.stocksim.database;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import it.unipi.lsmsdb.workgroup4.stocksim.database.cassandra.CQLSessionException;

/**
 * DB Manager interface.
 *
 * All DB Managers should implement this interface.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public interface DBManager {
    public boolean connect();
    public ResultSet query(final String query) throws CQLSessionException;
}
