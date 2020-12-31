package it.unipi.lsmsdb.stocksim.lib.yfinance;

import java.time.LocalDate;

/**
 * This class represents the historical data fetched using
 * {@link YahooFinance} API.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class YFHistoricalData {
    private final LocalDate date;
    private final double adj_close;
    private final double close;
    private final double high;
    private final double low;
    private final double open;
    private final double volume;

    /**
     * Default constructor.
     *
     * @param date stock market day reference date;
     * @param adj_close stock adjusted closing price;
     * @param close stock closing price;
     * @param high stock highest price;
     * @param low stock lowest price;
     * @param open stock opening price;
     * @param volume stock trade volume;
     */
    public YFHistoricalData(final LocalDate date, final double adj_close, final double close,
                            final double high, final double low, final double open,
                            final double volume) {
        this.date = date;
        this.adj_close = adj_close;
        this.close = close;
        this.high = high;
        this.low = low;
        this.open = open;
        this.volume = volume;
    }

    /**
     * @return this.date.
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * @return this.adj_close.
     */
    public double getAdjClose() {
        return this.adj_close;
    }

    /**
     * @return this.close.
     */
    public double getClose() {
        return this.close;
    }

    /**
     * @return this.high.
     */
    public double getHigh() {
        return this.high;
    }

    /**
     * @return this.low.
     */
    public double getLow() {
        return this.low;
    }

    /**
     * @return this.open.
     */
    public double getOpen() {
        return this.open;
    }

    /**
     * @return this.volume.
     */
    public double getVolume() {
        return this.volume;
    }
}
