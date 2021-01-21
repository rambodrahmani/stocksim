package it.unipi.lsmsdb.stocksim.client.database;

import it.unipi.lsmsdb.stocksim.client.app.ClientUtil;

import java.util.ArrayList;

/**
 * This class represents a Portfolio Aggregation.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class PortfolioAggregation {
    private final String sector;
    private final double total;
    private final ArrayList<Stock> stocks;

    /**
     * Default constructor.
     *
     * @param sector aggregation sector name;
     * @param total total number of stocks for this sector;
     * @param stocks stocks for this sector;
     */
    public PortfolioAggregation(final String sector, final double total, final ArrayList<Stock> stocks) {
        this.sector = sector;
        this.total = total;
        this.stocks = stocks;
    }

    /**
     * @return aggregation total number of stocks.
     */
    public double getTotal() {
        return total;
    }

    /**
     * @return Portfolio aggregation stocks;
     */
    public ArrayList<Stock> getStocks() {
        return stocks;
    }

    @Override
    public String toString() {
        // retrieve stocks list as string
        String stocksString = "[ ";
        for (final Stock stock : stocks) {
            stocksString += stock.getSymbol() + " ";
        }
        stocksString += "]";

        // build final string to be returned
        if (ClientUtil.isValidString(sector)) {
            return sector + "\n" + stocksString;
        } else {
            return "N.A.\n" + stocksString;
        }
    }
}
