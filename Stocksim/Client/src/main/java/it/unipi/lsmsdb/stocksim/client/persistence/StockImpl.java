package it.unipi.lsmsdb.stocksim.client.persistence;

import it.unipi.lsmsdb.stocksim.client.entities.Location;
import it.unipi.lsmsdb.stocksim.client.entities.Stock;
import org.bson.Document;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TimeZone;

/**
 * Stock implemented from the document db
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
 class StockImpl extends Stock {

    /**
     * The constructor will build the object from the document,
     * taking care of the optional fields
     *
     * @param doc document object containing the stock information
     */
     protected StockImpl(final Document doc){
         super();
         URL website;
         TimeZone timeZone;
         URL logo;

         try {
             website=new URL(doc.getString("sector"));
         } catch (MalformedURLException e) {
             website=null;
         }

         try {
             timeZone=TimeZone.getTimeZone(doc.getString("exchangeTimeZoneShortName")) ;
         } catch (Exception e) {
             timeZone=null;
         }

         try {
             logo=new URL(doc.getString("logoURL"));
         } catch (MalformedURLException e) {
             logo=null;
         }

        this.ticker = doc.getString("ticker");
        this.sector = doc.getString("sector");
        this.industry = doc.getString("industry");
        this.summary = doc.getString("longBusinessSummary");
        this.location = new LocationImpl((Document) doc.get("location"));
        this.website = website;
        this.market = doc.getString("market");
        this.currency = doc.getString("currency");
        this.exchange = doc.getString("exchange");
        this.short_name = doc.getString("shortName");
        this.long_name = doc.getString("longName");
        this.exchange_time_zone = timeZone;
        this.exchange_time_zone_desc = doc.getString("exchangeTimeZoneName");
        this.quoteType = doc.getString("quoteType");
        this.logo_url= logo;
    }

    /**
     * Setters to be performed also in the database
     *
     * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
     */

    // todo
     @Override
     public void setSector(String sector) {

     }

     @Override
     public void setIndustry(String industry) {

     }

     @Override
     public void setSummary(String summary) {

     }

     @Override
     public void setLocation(Location location) {

     }

     @Override
     public void setWebsite(URL website) {

     }

     @Override
     public void setMarket(String market) {

     }

     @Override
     public void setCurrency(String currency) {

     }

     @Override
     public void setExchange(String exchange) {

     }

     @Override
     public void setShort_name(String short_name) {

     }

     @Override
     public void setLong_name(String long_name) {

     }

     @Override
     public void setExchange_time_zone(TimeZone exchange_time_zone) {

     }

     @Override
     public void setExchange_time_zone_desc(String exchange_time_zone_desc) {

     }

     @Override
     public void setQuoteType(String quoteType) {

     }

     @Override
     public void setLogo_url(URL logo_url) {

     }

     @Override
     public void setTicker(String ticker) {

     }
 }
