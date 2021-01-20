package it.unipi.lsmsdb.stocksim.client.database;

/**
 * This class repsents a Sector Aggregation.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class SectorAggregation {
    private final String sector;
    private final Double marketCapitalization;
    private final Double avgTrailingPE;

    /**
     * Default constructor.
     *
     * @param sector sector name;
     * @param marketCapitalization sector total market capitalization;
     * @param avgTrailingPE sector average trailing P/E.
     */
    public SectorAggregation(final String sector, final Double marketCapitalization, final Double avgTrailingPE) {
        this.sector = sector;
        this.marketCapitalization = marketCapitalization;
        this.avgTrailingPE = avgTrailingPE;
    }

    /**
     * SETTERS & GETTERS
     */

    public String getSector() {
        return sector;
    }

    public Double getMarketCapitalization() {
        return marketCapitalization;
    }

    public Double getAvgTrailingPE() {
        return avgTrailingPE;
    }
}
