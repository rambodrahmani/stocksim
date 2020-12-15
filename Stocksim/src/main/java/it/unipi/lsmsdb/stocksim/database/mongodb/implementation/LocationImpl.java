package it.unipi.lsmsdb.stocksim.database.mongodb.implementation;

import it.unipi.lsmsdb.stocksim.database.mongodb.entities.Location;
import org.bson.Document;

class LocationImpl extends Location {

    protected LocationImpl(Document doc){
        super(
                doc.getString("city"),
                doc.getString("state"),
                doc.getString("country"),
                doc.getString("address"),
                doc.getString("phone")
        );
    }
}

