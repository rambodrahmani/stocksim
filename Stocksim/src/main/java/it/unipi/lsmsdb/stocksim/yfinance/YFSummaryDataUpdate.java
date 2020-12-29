package it.unipi.lsmsdb.stocksim.yfinance;

/**
 * This class represents the summary data update fetched using
 * {@link YahooFinance} API.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class YFSummaryDataUpdate {
    private double trailingPE;
    private double marketCap;

    /**
     * Default constructor.
     */
    public YFSummaryDataUpdate() {
        this.trailingPE = 0;
        this.marketCap = 0;
    }

    /**
     * Default constructor with initial parameters.
     *
     * @param trailingPE trailing PE value.
     * @param marketCap market capitalization value.
     */
    public YFSummaryDataUpdate(final double trailingPE, final double marketCap) {
        this.trailingPE = trailingPE;
        this.marketCap = marketCap;
    }

    /**
     * @param trailingPE the trailing PE to be set.
     */
    public void setTrailingPE(final double trailingPE) {
        this.trailingPE = trailingPE;
    }

    public double getTrailingPE() {
        return trailingPE;
    }

    /**
     * @param marketCap the market capitalization to be set.
     */
    public void setMarketCap(final double marketCap) {
        this.marketCap = marketCap;
    }

    public double getMarketCap() {
        return marketCap;
    }
}
