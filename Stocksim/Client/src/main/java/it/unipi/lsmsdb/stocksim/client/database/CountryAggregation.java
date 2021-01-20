package it.unipi.lsmsdb.stocksim.client.database;

/**
 * This class represents a Sector Aggregation.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class CountryAggregation {
    private final String country;
    private final Double marketCapitalization;
    private final Double avgTrailingPE;

    /**
     * Default constructor.
     *
     * @param country country name;
     * @param marketCapitalization country total market capitalization;
     * @param avgTrailingPE country average trailing P/E.
     */
    public CountryAggregation(final String country, final Double marketCapitalization, final Double avgTrailingPE) {
        this.country = country;
        this.marketCapitalization = marketCapitalization;
        this.avgTrailingPE = avgTrailingPE;
    }

    /**
     * SETTERS & GETTERS
     */

    public String getCountry() {
        return country;
    }

    public Double getMarketCapitalization() {
        return marketCapitalization;
    }

    public Double getAvgTrailingPE() {
        return avgTrailingPE;
    }
}
