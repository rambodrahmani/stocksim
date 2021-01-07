package it.unipi.lsmsdb.stocksim.client.database;

import it.unipi.lsmsdb.stocksim.client.charting.CandlestickChart;
import it.unipi.lsmsdb.stocksim.client.charting.Chart;
import it.unipi.lsmsdb.stocksim.client.charting.LineChart;
import it.unipi.lsmsdb.stocksim.client.charting.OHLCRow;
import org.apache.commons.lang.WordUtils;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;

import static it.unipi.lsmsdb.stocksim.lib.util.Util.*;

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
            if (isValidString(this.address) ){
                ret += ' '+this.address;
            }
            if (isValidString(this.city)){
                ret += ' '+this.city;
            }
            if (isValidString(this.state)){
                ret += ' '+this.state;
            }
            if (isValidString(this.country)){
                ret += ' '+this.country;
            }
            if (isValidString(this.phone)){
                ret += '\n'+this.phone;
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
        if (isValidString(this.longName)){
            ret += "longName:\t"+this.longName+ '\n';
        }
        ret += "Symbol:\t" + this.symbol + '\n';
        ret += "Quote type:\t" + this.quoteType + '\n';
        if (this.marketCap != null && this.marketCap!=0){
            ret += "Market capitalization:\t"+this.marketCap+ '\n';
        }
        if (this.trailingPE != null && this.trailingPE!=0){
            ret += "PE ratio:\t"+this.trailingPE+ '\n';
        }
        ret += "Market:\t" + this.market + '\n';
        ret += "Exchange timezone short name:\t" + this.exchangeTimezoneShortName + '\n';
        ret += "Exchange timezone name:\t" + this.exchangeTimezoneName + '\n';
        if (isValidString(this.sector)){
            ret += "Sector:\t"+this.sector+ '\n';
        }
        if (isValidString(this.industry)){
            ret += "Industry:\t"+this.industry+ '\n';
        }
        ret += "Currency:\t" + this.currency + '\n';
        final String locString= this.location.toString();
        if (isValidString(locString)){
            ret += "Location:\t"+locString+ '\n';
        }
        if (isValidString(this.logoURL)){
            ret += "Logo URL:\t"+this.logoURL+ '\n';
        }
        if (isValidString(this.website)){
            ret += "Website:\t"+this.website+ '\n';
        }
        if (isValidString(this.longBusinessSummary)){
            ret += "Long business summary:\n" +
                    WordUtils.wrap(this.longBusinessSummary, 80) + '\n';
        }
        return ret;
    }
}
