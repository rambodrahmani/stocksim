package it.unipi.lsmsdb.stocksim.client.database;

import java.time.LocalDate;

/**
 * StockSim Client Cassandra DB Query Builder.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class CassandraQueryBuilder {
    // Cassandra CQL insert query used to add a new ticker
    private final static String INSERT_QUERY = "INSERT INTO stocksim.tickers (symbol, date, adj_close, close, high, low, open, volume) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

    /**
     * @param symbol ticker symbol to be searched for.
     *
     * @return CQL query to be used to retrieve all historical data for
     *         the given symbol.
     */
    public final static String getTickerQuery(final String symbol) {
        return "SELECT * FROM stocksim.tickers WHERE symbol = '" + symbol + "' ORDER BY date DESC;";
    }

    /**
     * @param symbol ticker symbol to be searched for.
     *
     * @return CQL query to check if historical data is available for
     *         the given symbol.
     */
    public final static String getTickerExistenceQuery(final String symbol) {
        return "SELECT symbol FROM stocksim.tickers WHERE symbol = '" + symbol + "' LIMIT 1;";
    }

    /**
     * @return CQL query to be used to add historical data for a new ticker.
     */
    public final static String getUpdateInsertQuery() {
        return INSERT_QUERY;
    }

    /**
     * @param symbol the ticker symbol to be searched;
     * @param startDate start date for the period;
     * @param endDate end date for the period;
     * @param daysInterval time interval days.
     *
     * @return CQL query to be used to execute the AggregateOHLC with the given
     *         parameters.
     */
    public final static String getAggregateOHLCQuery(final String symbol, final LocalDate startDate, final LocalDate endDate, final int daysInterval) {
        return "SELECT AggregateOHLC(" + daysInterval + ", date, " +
                "open, close, high, low, volume, adj_close) " +
                "AS OHLCAggregation from stocksim.tickers " +
                "WHERE date < '" + endDate + "' " +
                "AND date > '" + startDate + "' " +
                "AND symbol = '" + symbol + "';";
    }
}
