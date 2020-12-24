package it.unipi.lsmsdb.stocksim.server.yfinance;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * This class represents the historical data fetched using
 * {@link YahooFinance} API.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class YFHistoricalData {
    private final LocalDate date;
    private final BigDecimal adj_close;
    private final BigDecimal close;
    private final BigDecimal high;
    private final BigDecimal low;
    private final BigDecimal open;
    private final BigDecimal volume;

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
    public YFHistoricalData(final LocalDate date, final BigDecimal adj_close, final BigDecimal close,
                            final BigDecimal high, final BigDecimal low, final BigDecimal open,
                            final BigDecimal volume) {
        this.date = date;
        this.adj_close = adj_close;
        this.close = close;
        this.high = high;
        this.low = low;
        this.open = open;
        this.volume = volume;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getAdjClose() {
        return adj_close;
    }

    public BigDecimal getClose() {
        return close;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public BigDecimal getVolume() {
        return volume;
    }
}
