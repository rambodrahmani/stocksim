package it.unipi.lsmsdb.stocksim.client.database;

import it.unipi.lsmsdb.stocksim.client.app.ClientUtil;
import org.bson.Document;

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
    private final Long marketCap;
    private final double trailingPE;
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

            ClientUtil.println(this.city);
            ClientUtil.println(this.state);
            ClientUtil.println(this.country);
            ClientUtil.println(this.phone);
            ClientUtil.println(this.address);

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
        this.shortName = stockDocument.getString("shortName");
        this.longName = stockDocument.getString("longName");
        this.exchangeTimezoneName = stockDocument.getString("exchangeTimezoneName");
        this.exchangeTimezoneShortName = stockDocument.getString("exchangeTimezoneShortName");
        this.quoteType = stockDocument.getString("quoteType");
        this.symbol = stockDocument.getString("symbol");
        this.market = stockDocument.getString("market");
        this.logoURL = stockDocument.getString("logoURL");
        this.marketCap = stockDocument.getLong("marketCap");
        this.trailingPE = stockDocument.getDouble("trailingPE");
        this.sector = stockDocument.getString("sector");
        this.website = stockDocument.getString("website");
        this.industry = stockDocument.getString("industry");
        this.longBusinessSummary = stockDocument.getString("longBusinessSummary");
        this.location = new Location((Document) stockDocument.get("location"));
    }

    /**
     * Converts the Stock into printable {@link String}.
     *
     * @return the {@link String} to be printed to STD Out.
     */
    public String toString() {
        String ret = "";

        ClientUtil.println(this.currency);
        ClientUtil.println(this.shortName);
        ClientUtil.println(this.longName);
        ClientUtil.println(this.exchangeTimezoneName);
        ClientUtil.println(this.exchangeTimezoneShortName);
        ClientUtil.println(this.quoteType);
        ClientUtil.println(this.symbol);
        ClientUtil.println(this.market);
        ClientUtil.println(this.logoURL);
        ClientUtil.println(String.valueOf(this.marketCap));
        ClientUtil.println(String.valueOf(this.trailingPE));
        ClientUtil.println(this.sector);
        ClientUtil.println(this.website);
        ClientUtil.println(this.industry);
        ClientUtil.println(this.longBusinessSummary);
        ClientUtil.println(this.location.toString());

        return ret;
    }
}
