package it.unipi.lsmsdb.stocksim.lib.yfinance;

import it.unipi.lsmsdb.stocksim.lib.util.Util;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * This class represents the asset profile fetched using
 * {@link YahooFinance} API.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class YFAssetProfile {
    private String currency;
    private String shortName;
    private String longName;
    private String exchangeTimezoneName;
    private String exchangeTimezoneShortName;
    private String quoteType;
    private String symbol;
    private String market;
    private double marketCap;
    private double trailingPE;
    private String website;
    private String logoURL;
    private String sector;
    private String city;
    private String industry;
    private String longBusinessSummary;

    // location object
    private String state;
    private String country;
    private String phone;
    private String address;

    /**
     * Default constructor.
     */
    public YFAssetProfile() {
    }

    /**
     * @return MongoDB {@link Document}.
     */
    public Document getAssetDocument() {
        // create new location document: only add available fields
        final Document locationDocument = new Document();
        if (Util.isValidString(getState())) {
            locationDocument.append("state", getState());
        }
        if (Util.isValidString(getCity())) {
            locationDocument.append("city", getCity());
        }
        if (Util.isValidString(getCountry())) {
            locationDocument.append("country", getCountry());
        }
        if (Util.isValidString(getPhone())) {
            locationDocument.append("phone", getPhone());
        }
        if (Util.isValidString(getAddress())) {
            locationDocument.append("address", getAddress());
        }

        // create new asset document: only add available fields
        final Document assetDocument = new Document("_id", new ObjectId());
        if (Util.isValidString(getCurrency())) {
            assetDocument.append("currency", getCurrency());
        }
        if (Util.isValidString(getShortName())) {
            assetDocument.append("shortName", getShortName());
        }
        if (Util.isValidString(getLongName())) {
            assetDocument.append("longName", getLongName());
        }
        if (Util.isValidString(getExchangeTimezoneName())) {
            assetDocument.append("exchangeTimezoneName", getExchangeTimezoneName());
        }
        if (Util.isValidString(getExchangeTimezoneShortName())) {
            assetDocument.append("exchangeTimezoneShortName", getExchangeTimezoneShortName());
        }
        if (Util.isValidString(getQuoteType())) {
            assetDocument.append("quoteType", getQuoteType());
        }
        if (Util.isValidString(getSymbol())) {
            assetDocument.append("symbol", getSymbol());
        }
        if (Util.isValidString(getMarket())) {
            assetDocument.append("market", getMarket());
        }
        if (Util.isValidString(getLogoURL())) {
            assetDocument.append("logoURL", getLogoURL());
        }
        if (getMarketCap() > 0) {
            assetDocument.append("marketCap", getMarketCap());
        }
        if (getTrailingPE() > 0) {
            assetDocument.append("trailingPE", getTrailingPE());
        }
        if (Util.isValidString(getSector())) {
            assetDocument.append("sector", getSector());
        }
        if (Util.isValidString(getWebsite())) {
            assetDocument.append("website", getWebsite());
        }
        if (Util.isValidString(getIndustry())) {
            assetDocument.append("industry", getIndustry());
        }
        if (Util.isValidString(getLongBusinessSummary())) {
            assetDocument.append("longBusinessSummary", getLongBusinessSummary());
        }
        assetDocument.append("location", locationDocument);

        return assetDocument;
    }

    /**
     * SETTER AND GETTERS.
     */

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setShortName(final String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getLongName() {
        return longName;
    }

    public void setExchangeTimezoneName(String exchangeTimezoneName) {
        this.exchangeTimezoneName = exchangeTimezoneName;
    }

    public String getExchangeTimezoneName() {
        return exchangeTimezoneName;
    }

    public void setExchangeTimezoneShortName(String exchangeTimezoneShortName) {
        this.exchangeTimezoneShortName = exchangeTimezoneShortName;
    }

    public String getExchangeTimezoneShortName() {
        return exchangeTimezoneShortName;
    }

    public void setQuoteType(String quoteType) {
        this.quoteType = quoteType;
    }

    public String getQuoteType() {
        return quoteType;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getMarket() {
        return market;
    }

    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public void setTrailingPE(double trailingPE) {
        this.trailingPE = trailingPE;
    }

    public double getTrailingPE() {
        return trailingPE;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWebsite() {
        return website;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getSector() {
        return sector;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getIndustry() {
        return industry;
    }

    public void setLongBusinessSummary(String longBusinessSummary) {
        this.longBusinessSummary = longBusinessSummary;
    }

    public String getLongBusinessSummary() {
        return longBusinessSummary;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
