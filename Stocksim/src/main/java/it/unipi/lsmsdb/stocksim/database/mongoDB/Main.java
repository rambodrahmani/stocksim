package it.unipi.lsmsdb.stocksim.database.mongoDB;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Developer harness test for the Mongo Driver Sync for MongoDB.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class Main {
    /**
     * Developer harness test entry point.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        // mongo db factory
        final MongoDBFactory mongoDBFactory = MongoDBFactory.create();

        // connect and get the collection
        MongoDB dbManager = mongoDBFactory.getMongoDB("stocksim");
        System.out.println(dbManager.connect());
        final MongoCollection<Document> collection = dbManager.getCollection(StocksimCollection.USERS.getCollectionName());

        // get info for a given user
        Bson filter = and(eq("username","TWOWS"));
        final Document user1 = dbManager.findOne(filter, collection);
        System.out.println(user1.toJson());

        // insert a new user
        Document user2 = user1;
        user2.replace("username", "TWOWS44");
        user2.remove("_id");
        dbManager.insertOne(user2, collection );
        Bson filter2 = and(eq("username","TWOWS44"));
        user2 = dbManager.findOne(filter2, collection);
        System.out.println(user2.toJson());

        // insert a new portfolio
        Document port1 = ((ArrayList<Document>) user1.get("portfolios")).get(0);
        port1.replace("name", "test3");
        dbManager.insertInArray(filter2,"portfolios", port1, collection);

        // update a user info field
        Bson up1 = Updates.set("email", "test@test.it");
        dbManager.updateOne(filter2, up1, collection);
        user2 = dbManager.findOne(filter2, collection);
        System.out.println(user2.toJson());

        // update user info in nested array
        List<Bson> arrayFilters = Arrays.asList(eq("port.name","test3"));
        Bson updates = Updates.set("portfolios.$[port].type", "prov333");
        dbManager.updateOneInNestedArray(filter2,  arrayFilters, updates, collection);
        user2 = dbManager.findOne(filter2, collection);
        System.out.println(user2.toJson());

        // delete user portfolio from array
        Bson filter3=eq("name", "test3");
        dbManager.deleteFromArray(filter2, "portfolios", filter3, collection);
        user2 = dbManager.findOne(filter2, collection);
        System.out.println(user2.toJson());

        // delete a user
        System.out.println(dbManager.deleteOne(filter2, collection));

        // find user stocks
        MongoCollection<Document> collection1 = dbManager.getCollection(StocksimCollection.STOCKS.getCollectionName());
        final Bson filter4 = eq("quoteType", "ETF");
        for (final Document document : dbManager.findMany(filter4, collection1)) {
            System.out.println(document.toJson());
        }
    }
}
