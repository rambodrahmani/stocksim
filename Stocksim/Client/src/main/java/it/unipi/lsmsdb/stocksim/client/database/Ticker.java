package it.unipi.lsmsdb.stocksim.client.database;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a ticker.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class Ticker {
    private final String symbol;
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
    public Ticker(final String symbol, final LocalDate date, final BigDecimal adj_close,
                  final BigDecimal close, final BigDecimal high, final BigDecimal low,
                  final BigDecimal open, final BigDecimal volume) {
        this.symbol = symbol;
        this.date = date;
        this.adj_close = adj_close;
        this.close = close;
        this.high = high;
        this.low = low;
        this.open = open;
        this.volume = volume;
    }

    /**
     * @return this.symbol.
     */
    public String getSymbol() {
        return symbol;
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
    public BigDecimal getAdjClose() {
        return this.adj_close;
    }

    /**
     * @return this.close.
     */
    public BigDecimal getClose() {
        return this.close;
    }

    /**
     * @return this.high.
     */
    public BigDecimal getHigh() {
        return this.high;
    }

    /**
     * @return this.low.
     */
    public BigDecimal getLow() {
        return this.low;
    }

    /**
     * @return this.open.
     */
    public BigDecimal getOpen() {
        return this.open;
    }

    /**
     * @return this.volume.
     */
    public BigDecimal getVolume() {
        return this.volume;
    }
}
