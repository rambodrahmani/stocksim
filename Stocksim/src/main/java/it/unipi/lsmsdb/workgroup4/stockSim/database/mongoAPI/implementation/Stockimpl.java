package it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.implementation;

import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.entities.Location;
import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.entities.Stock;

import java.net.URL;
import java.util.Currency;
import java.util.TimeZone;

 class Stockimpl extends Stock {

    protected Stockimpl(String ticker, String sector, String industry, String summary, Location location, URL website, String market, Currency currency, String exchange, String short_name, String long_name, TimeZone exchange_time_zone, String exchange_time_zone_desc, String quoteType, URL logo_url) {
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
    }
}


