package it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.entities;

import java.net.URL;
import java.util.Currency;
import java.util.TimeZone;


public abstract class Stock {
    protected String ticker;
    protected String sector;
    protected String industry;
    protected String summary;
    protected Location location;
    protected URL website;
   protected String market;
   protected Currency currency;
    protected String exchange;
    protected  String short_name;
    protected  String long_name;
    protected  TimeZone exchange_time_zone;
    protected String exchange_time_zone_desc;
    protected String quoteType;
    protected URL logo_url;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public URL getWebsite() {
        return website;
    }

    public void setWebsite(URL website) {
        this.website = website;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public TimeZone getExchange_time_zone() {
        return exchange_time_zone;
    }

    public void setExchange_time_zone(TimeZone exchange_time_zone) {
        this.exchange_time_zone = exchange_time_zone;
    }

    public String getExchange_time_zone_desc() {
        return exchange_time_zone_desc;
    }

    public void setExchange_time_zone_desc(String exchange_time_zone_desc) {
        this.exchange_time_zone_desc = exchange_time_zone_desc;
    }

    public String getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(String quoteType) {
        this.quoteType = quoteType;
    }

    public URL getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(URL logo_url) {
        this.logo_url = logo_url;
    }
}


