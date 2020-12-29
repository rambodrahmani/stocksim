package it.unipi.lsmsdb.stocksim.client.database;

/**
 * StockSim Client Cassandra DB Query Builder.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class CassandraQueryBuilder {
    /**
     * @param symbol ticker symbol to be searched for.
     *
     * @return CQL query to be used to retrieve all historical data for
     *         the given symbol.
     */
    public final static String getTickerQuery(final String symbol) {
        return "SELECT * FROM stocksim.tickers WHERE symbol='" + symbol + "' ORDER BY date DESC;";
    }
}
