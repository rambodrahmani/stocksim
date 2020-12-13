package it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.entities;

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

}


