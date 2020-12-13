package it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.implementation;

import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.entities.Location;
import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.entities.Stock;

import java.net.URL;
import java.util.Currency;
import java.util.TimeZone;

 class Stockimpl extends Stock {

    protected Stockimpl(String ticker, String sector, String industry, String summary, Location location, URL website, String market, Currency currency, String exchange, String short_name, String long_name, TimeZone exchange_time_zone, String exchange_time_zone_desc, String quoteType, URL logo_url) {
        this.setTicker(ticker);
        this.setSector(sector);
        this.setIndustry(industry);
        this.setSummary(summary);
        this.setLocation(location);
        this.setWebsite(website);
        this.setMarket(market);
        this.setCurrency(currency);
        this.setExchange(exchange);
        this.setShort_name(short_name);
        this.setLong_name(long_name);
        this.setExchange_time_zone(exchange_time_zone);
        this.setExchange_time_zone_desc(exchange_time_zone_desc);
        this.setQuoteType(quoteType);
        this.setLogo_url(logo_url);
    }
}


