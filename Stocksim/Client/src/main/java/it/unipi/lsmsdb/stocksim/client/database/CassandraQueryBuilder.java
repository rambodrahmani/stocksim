package it.unipi.lsmsdb.stocksim.client.database;

/**
 * StockSim Client Cassandra DB Query Builder.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class CassandraQueryBuilder {
    // Cassandra CQL insert query used during historical data update.
    private final static String UPDATE_INSERT_QUERY = "INSERT INTO stocksim.tickers (symbol, date, adj_close, close, high, low, open, volume) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

    /**
     * @param symbol ticker symbol to be searched for.
     *
     * @return CQL query to be used to retrieve all historical data for
     *         the given symbol.
     */
    public final static String getTickerQuery(final String symbol) {
        return "SELECT * FROM stocksim.tickers WHERE symbol='" + symbol + "' ORDER BY date DESC;";
    }

    public final static String getUpdateInsertQuery() {
        return UPDATE_INSERT_QUERY;
    }
}
