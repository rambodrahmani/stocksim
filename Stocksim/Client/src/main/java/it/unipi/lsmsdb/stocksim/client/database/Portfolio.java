package it.unipi.lsmsdb.stocksim.client.database;

import java.util.ArrayList;

/**
 * User Portfolio.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class Portfolio {
    private final ArrayList<Stock> stocks;

    /**
     * Default constructor.
     * Constructs a portfolio for the given {@link ArrayList} of {@link Stock}.
     *
     * @param stocks the {@link ArrayList} of {@link Stock} to be used.
     */
    public Portfolio(final ArrayList<Stock> stocks) {
        this.stocks = stocks;
    }
}
