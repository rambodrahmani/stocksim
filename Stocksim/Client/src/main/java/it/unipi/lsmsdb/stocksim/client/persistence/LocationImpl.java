package it.unipi.lsmsdb.stocksim.client.persistence;

import it.unipi.lsmsdb.stocksim.client.entities.Location;
import org.bson.Document;

/**
 * Location implemented from the document db
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
class LocationImpl extends Location {
    protected LocationImpl(Document doc){
        //todo
        super(
                doc.getString("city"),
                doc.getString("state"),
                doc.getString("country"),
                doc.getString("address"),
                doc.getString("phone")
        );
    }
}
