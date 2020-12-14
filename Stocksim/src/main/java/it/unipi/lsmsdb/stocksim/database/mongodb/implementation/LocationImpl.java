package it.unipi.lsmsdb.stocksim.database.mongodb.implementation;

import it.unipi.lsmsdb.stocksim.database.mongodb.entities.Location;
import org.bson.Document;

class LocationImpl extends Location {

    protected LocationImpl(String city, String state, String country, String address, String phone) {
        this.city=city;
        this.state=state;
        this.country=country;
        this.address=address;
        this.phone=phone;
    }

    public LocationImpl(Document doc){
        this.city=doc.getString("city");
        this.state=doc.getString("state");
        this.country=doc.getString("country");
        this.address=doc.getString("address");
        this.phone=doc.getString("phone");
    }
}

