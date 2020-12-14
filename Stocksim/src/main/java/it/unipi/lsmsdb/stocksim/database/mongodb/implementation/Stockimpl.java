package it.unipi.lsmsdb.stocksim.database.mongodb.implementation;

<<<<<<< Updated upstream:Stocksim/src/main/java/it/unipi/lsmsdb/stocksim/database/mongodb/implementation/Stockimpl.java
import it.unipi.lsmsdb.stocksim.database.mongodb.entities.Location;
import it.unipi.lsmsdb.stocksim.database.mongodb.entities.Stock;
=======
import it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.entities.Location;
import it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.entities.Stock;
import org.bson.Document;
>>>>>>> Stashed changes:Stocksim/src/main/java/it/unipi/lsmsdb/workgroup4/stocksim/database/mongodb/implementation/Stockimpl.java

import java.net.MalformedURLException;
import java.net.URL;
import java.util.TimeZone;

 class Stockimpl extends Stock {

    protected Stockimpl(String ticker, String sector, String industry, String summary, Location location, URL website, String market, String currency, String exchange, String short_name, String long_name, TimeZone exchange_time_zone, String exchange_time_zone_desc, String quoteType, URL logo_url) {
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
    protected  Stockimpl(Document doc){
        this.setTicker(doc.getString("ticker"));
        this.setSector(doc.getString("sector"));
        this.setIndustry(doc.getString("industry"));
        this.setSummary(doc.getString("longBusinessSummary"));
        this.setLocation(new LocationImpl((Document) doc.get("location"));
        try {
            this.setWebsite(new URL(doc.getString("sector"));
        } catch (MalformedURLException e) {
            this.setWebsite(null);
        }
        this.setMarket(doc.getString("maket"));
        this.setCurrency(doc.getString("currency"));
        this.setExchange(doc.getString("exchange"));
        this.setShort_name(doc.getString("shortName"));
        this.setLong_name(doc.getString("longName"));
        this.setExchange_time_zone(TimeZone.getTimeZone(doc.getString("exchanheTimeZoneShortName")) );
        this.setExchange_time_zone_desc(doc.getString("exchanheTimeZoneName"));
        this.setQuoteType(doc.getString("quoteTyope"));
        try {
            this.setLogo_url(new URL(doc.getString("logoURL")));
        } catch (MalformedURLException e) {
            this.setLogo_url(null);
        }

    }


 }


