package it.unipi.lsmsdb.stocksim.client.database;

import org.apache.commons.lang.WordUtils;
import org.bson.Document;

import java.util.Objects;

import static it.unipi.lsmsdb.stocksim.lib.util.Util.isValidString;

/**
 * This class represents a Stock.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class Stock {
    private final String currency;
    private final String shortName;
    private final String longName;
    private final String exchangeTimezoneName;
    private final String exchangeTimezoneShortName;
    private final String quoteType;
    private final String symbol;
    private final String market;
    private final String logoURL;
    private final Double marketCap;
    private Double trailingPE;
    private final String sector;
    private final String website;
    private final String industry;
    private final String longBusinessSummary;
    private final Location location;

    /**
     * This represents the Stock embedded document location.
     */
    private static class Location {
        private final String city;
        private final String state;
        private final String country;
        private final String phone;
        private final String address;

        /**
         * Default constructor. Creates a Location using the given JSON Document.
         *
         * @param locationDocument the JSON document to be parsed.
         */
        public Location(final Document locationDocument) {
            this.city = locationDocument.getString("city");
            this.state = locationDocument.getString("state");
            this.country = locationDocument.getString("country");
            this.phone = locationDocument.getString("phone");
            this.address = locationDocument.getString("address");
        }

        /**
         * Converts the Stock Location into printable {@link String}.
         *
         * @return the {@link String} to be printed to STD Out.
         */
        public String toString() {
            String ret = "";
            if (isValidString(this.address)) {
                ret += ' ' + this.address;
            }
            if (isValidString(this.city)) {
                ret += ' ' + this.city;
            }
            if (isValidString(this.state)) {
                ret += ' ' + this.state;
            }
            if (isValidString(this.country)) {
                ret += ' ' + this.country;
            }
            if (isValidString(this.phone)) {
                ret += '\n' + this.phone;
            }
            return ret;
        }
    }

    /**
     * Default constructor. Creates a Stock using the given JSON Document.
     *
     * @param stockDocument the JSON document to be parsed.
     */
    public Stock(final Document stockDocument) {
        this.currency = stockDocument.getString("currency");
        this.symbol = stockDocument.getString("symbol");
        this.shortName = stockDocument.getString("shortName");

        this.exchangeTimezoneName = stockDocument.getString("exchangeTimezoneName");
        this.exchangeTimezoneShortName = stockDocument.getString("exchangeTimezoneShortName");
        this.quoteType = stockDocument.getString("quoteType");
        this.location = new Location((Document) stockDocument.get("location"));
        this.market = stockDocument.getString("market");
        
        // the following fields could be null or undefined
        this.longName = stockDocument.getString("longName");
        this.logoURL = stockDocument.getString("logoURL");
        this.marketCap = stockDocument.getDouble("marketCap");

        // trailing PE might not be a double
        try {
            this.trailingPE = stockDocument.getDouble("trailingPE");
        } catch (final ClassCastException e) {
            this.trailingPE = 0.0;
        }

        this.sector = stockDocument.getString("sector");
        this.website = stockDocument.getString("website");
        this.industry = stockDocument.getString("industry");
        this.longBusinessSummary = stockDocument.getString("longBusinessSummary");
    }

    /**
     * @return stock ticker symbol.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Converts the Stock into printable {@link String}.
     * Fields that are null or absent will be left empty.
     *
     * @return the {@link String} to be printed to STD Out.
     */
    public String toString() {
        String ret = "";

        ret += "Short Name: " + this.shortName + '\n';

        if (isValidString(this.longName)){
            ret += "Long Name: "+this.longName+ '\n';
        }

        ret += "Symbol: " + this.symbol + '\n';

        ret += "Quote type: " + this.quoteType + '\n';

        if (this.marketCap != null && this.marketCap != 0) {
            ret += "Market capitalization: " + this.marketCap+ '\n';
        }

        if (this.trailingPE != null && this.trailingPE != 0) {
            ret += "PE ratio: " + this.trailingPE+ '\n';
        }

        ret += "Market: " + this.market + '\n';

        ret += "Exchange timezone short name: " + this.exchangeTimezoneShortName + '\n';

        ret += "Exchange timezone name: " + this.exchangeTimezoneName + '\n';

        if (isValidString(this.sector)) {
            ret += "Sector: " + this.sector + '\n';
        }

        if (isValidString(this.industry)) {
            ret += "Industry: " + this.industry + '\n';
        }

        ret += "Currency: " + this.currency + '\n';

        final String locString = this.location.toString();
        if (isValidString(locString)) {
            ret += "Location: " + locString + '\n';
        }

        if (isValidString(this.logoURL)) {
            ret += "Logo URL: " + this.logoURL + '\n';
        }

        if (isValidString(this.website)) {
            ret += "Website: " + this.website + '\n';
        }

        if (isValidString(this.longBusinessSummary)) {
            ret += "Long business summary:\n" +
                    WordUtils.wrap(this.longBusinessSummary, 80) + '\n';
        }

        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (this == o || this.symbol.equals(((Stock)o).getSymbol())) return true;
        Stock stock = (Stock) o;
        return Objects.equals(currency, stock.currency) && Objects.equals(shortName, stock.shortName) && Objects.equals(longName, stock.longName) && Objects.equals(exchangeTimezoneName, stock.exchangeTimezoneName) && Objects.equals(exchangeTimezoneShortName, stock.exchangeTimezoneShortName) && Objects.equals(quoteType, stock.quoteType) && Objects.equals(symbol, stock.symbol) && Objects.equals(market, stock.market) && Objects.equals(logoURL, stock.logoURL) && Objects.equals(marketCap, stock.marketCap) && Objects.equals(trailingPE, stock.trailingPE) && Objects.equals(sector, stock.sector) && Objects.equals(website, stock.website) && Objects.equals(industry, stock.industry) && Objects.equals(longBusinessSummary, stock.longBusinessSummary) && Objects.equals(location, stock.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, shortName, longName, exchangeTimezoneName, exchangeTimezoneShortName, quoteType, symbol, market, logoURL, marketCap, trailingPE, sector, website, industry, longBusinessSummary, location);
    }
}
