package it.unipi.lsmsdb.stocksim.client.entities;

import java.net.URL;
import java.util.TimeZone;

/**
 * Stock generic data structure
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public abstract class Stock {
    protected String ticker;
    protected String sector;
    protected String industry;
    protected String summary;
    protected Location location;
    protected URL website;
    protected String market;
    protected String currency;
    protected String exchange;
    protected  String short_name;
    protected  String long_name;
    protected  TimeZone exchange_time_zone;
    protected String exchange_time_zone_desc;
    protected String quoteType;
    protected URL logo_url;
    protected double PE_ratio;
    protected int market_cap;

    public Stock(String ticker, String sector, String industry, String summary, Location location, URL website, String market, String currency, String exchange, String short_name, String long_name, TimeZone exchange_time_zone, String exchange_time_zone_desc, String quoteType, URL logo_url, double PE_ratio, int market_cap) {
        this.ticker = ticker;
        this.sector = sector;
        this.industry = industry;
        this.summary = summary;
        this.location = location;
        this.website = website;
        this.market = market;
        this.currency = currency;
        this.exchange = exchange;
        this.short_name = short_name;
        this.long_name = long_name;
        this.exchange_time_zone = exchange_time_zone;
        this.exchange_time_zone_desc = exchange_time_zone_desc;
        this.quoteType = quoteType;
        this.logo_url = logo_url;
        this.PE_ratio = PE_ratio;
        this.market_cap = market_cap;
    }

    public double getPE_ratio() {
        return PE_ratio;
    }

    public abstract void setPE_ratio(double PE_ratio);

    public int getMarket_cap() {
        return market_cap;
    }

    public abstract void setMarket_cap(int market_cap);

    protected Stock() {
    }

    public abstract void setSector(String sector);

    public abstract void setIndustry(String industry);

    public abstract void setSummary(String summary);

    public abstract void setLocation(Location location);

    public abstract void setWebsite(URL website);

    public abstract void setMarket(String market);

    public abstract void setCurrency(String currency);

    public abstract void setExchange(String exchange);

    public abstract void setShort_name(String short_name);

    public abstract void setLong_name(String long_name);

    public abstract void setExchange_time_zone(TimeZone exchange_time_zone);

    public abstract void setExchange_time_zone_desc(String exchange_time_zone_desc);

    public abstract void setQuoteType(String quoteType);

    public abstract void setLogo_url(URL logo_url);

    public abstract void setTicker(String ticker);

    public String getTicker() {
        return ticker;
    }

    public String getSector() {
        return sector;
    }

    public String getIndustry() {
        return industry;
    }

    public String getSummary() {
        return summary;
    }

    public Location getLocation() {
        return location;
    }

    public URL getWebsite() {
        return website;
    }

    public String getMarket() {
        return market;
    }

    public String getCurrency() {
        return currency;
    }

    public String getExchange() {
        return exchange;
    }

    public String getShort_name() {
        return short_name;
    }

    public String getLong_name() {
        return long_name;
    }

    public TimeZone getExchange_time_zone() {
        return exchange_time_zone;
    }

    public String getExchange_time_zone_desc() {
        return exchange_time_zone_desc;
    }

    public String getQuoteType() {
        return quoteType;
    }

    public URL getLogo_url() {
        return logo_url;
    }
}
