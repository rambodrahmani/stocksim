package it.unipi.lsmsdb.stocksim.server.database;

/**
 * StockSim Server Cassandra DB Query Builder.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class CassandraQueryBuilder {
    // Cassandra CQL select query used to retrieve ticker symbols
    private final static String TICKER_SYMBOLS = "SELECT DISTINCT symbol FROM stocksim.tickers;";

    // Cassandra CQL insert query used during historical data update.
    private final static String UPDATE_INSERT_QUERY = "INSERT INTO stocksim.tickers (symbol, date, adj_close, close, high, low, open, volume) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

    public final static String getTickerSymbolsQuery() {
        return TICKER_SYMBOLS;
    }

    public final static String getLastUpdateDateQuery(final String symbol) {
        return "SELECT date FROM stocksim.tickers WHERE symbol='" + symbol + "' ORDER BY date DESC;";
    }

    public final static String getUpdateInsertQuery() {
        return UPDATE_INSERT_QUERY;
    }
}
