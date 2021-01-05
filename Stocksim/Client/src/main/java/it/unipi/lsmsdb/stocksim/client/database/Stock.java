package it.unipi.lsmsdb.stocksim.client.database;

import it.unipi.lsmsdb.stocksim.client.app.ClientUtil;
import org.bson.Document;

import java.util.ArrayList;

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
    private final Double trailingPE;
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
            
            ret += this.address + ", " +
                this.city + ", " +
                this.state + ", " +
                this.country + ";\n" +
                "Phone: " + this.phone;

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
        this.longName = stockDocument.getString("longName");
        this.exchangeTimezoneName = stockDocument.getString("exchangeTimezoneName");
        this.exchangeTimezoneShortName = stockDocument.getString("exchangeTimezoneShortName");
        this.quoteType = stockDocument.getString("quoteType");
        this.location = new Location((Document) stockDocument.get("location"));
        this.logoURL = stockDocument.getString("logoURL");
        this.market = stockDocument.getString("market");
        
        // the following fields could be null or undefined
        
        this.marketCap = stockDocument.getLong("marketCap");
        this.trailingPE = stockDocument.getDouble("trailingPE");
        this.sector = stockDocument.getString("sector");
        this.website = stockDocument.getString("website");
        this.industry = stockDocument.getString("industry");
        this.longBusinessSummary = stockDocument.getString("longBusinessSummary");
        
    }

    /**
     * Converts the Stock into printable {@link String}.
     * Fields that are null or absent will be left empty (no dash or anything
     * will be printed).
     * @return the {@link String} to be printed to STD Out.
     */
    public String toString() {
        
        String ret = "";
        
        ret += "Short name:\t" + this.shortName + '\n';
        ret += "Long name:\t" + this.longName + '\n';
        ret += "Symbol:\t" + this.symbol + '\n';
        
        ret += "Market capitalization:\t";
        if(this.marketCap != null){
            ret += this.marketCap;
        }
        ret += '\n';
        
        ret += "Trailing PE:\t";
        if(this.trailingPE != null){
            ret += this.trailingPE;
        }
        ret += '\n';
        
        ret += "Market:\t" + this.market + '\n';
        ret += "Exchange timezone short name:\t" + this.exchangeTimezoneShortName + '\n';
        ret += "Exchange timezone name:\t" + this.exchangeTimezoneName + '\n';
        ret += "Quote type:\t" + this.quoteType + '\n';
        
        ret += "Sector:\t";
        if(this.sector != null){
            ret += this.sector;
        }
        ret += '\n';
    
        ret += "Industry:\t";
        if(this.industry != null){
            ret += this.industry;
        }
        ret += '\n';
        
        ret += "Currency:\t" + this.currency + '\n';
        ret += "Location:\t" + this.location.toString();
        ret += "Logo URL:\t" + this.logoURL + '\n';
        ret += "Website:\t";
        if(this.website != null){
            ret += this.website;
        }
        ret += '\n';
        
        if(this.longBusinessSummary != null){
            ret += "Long business summary:\t" + this.longBusinessSummary + '\n';
        }
        
        return ret;
    }
}
