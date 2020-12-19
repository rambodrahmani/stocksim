package it.unipi.lsmsdb.workgroup4.stocksim.database.mongoDB;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.*;


public class Main {
    private static  final MongoDBFactory factory = MongoDBFactory.create();

    public static void main(String[] args) {
        //connect and get the collection
        final String databaseName="StockSim";
        MongoDBManager dbManager = factory.getMongoDBManager();
        System.out.println(dbManager.connect());
        dbManager.open(databaseName);
        MongoCollection<Document> collection = dbManager.getCollection(MongoCollectionName.users.toString());

        //get a user
        Bson filter=and(eq("username","TWOWS"));
        Document user1=dbManager.findOne(filter, collection);
        System.out.println(user1.toJson());

        //insert a new user
        Document user2=user1;
        user2.replace("username", "TWOWS44");
        user2.remove("_id");
        dbManager.insertOne(user2,collection );
        Bson filter2=and(eq("username","TWOWS44"));
        user2=dbManager.findOne(filter2, collection);
        System.out.println(user2.toJson());

        //insert a new portfolio
        Document port1= ((ArrayList<Document>) user1.get("portfolios")).get(0);
        port1.replace("name", "test3");
        dbManager.insertInArray(filter2,"portfolios", port1, collection);

        // update a user filed
        Bson up1=Updates.set("email","test@test.it");
        dbManager.updateOne(filter2, up1,collection);
        user2=dbManager.findOne(filter2, collection);
        System.out.println(user2.toJson());

        //update one in nested array
        List<Bson> arrayFilters=Arrays.asList(eq("port.name","test3"));
        Bson updates= Updates.set("portfolios.$[port].type", "prov333");
        dbManager.updateOneInNestedArray(filter2,  arrayFilters, updates, collection);
        user2=dbManager.findOne(filter2, collection);
        System.out.println(user2.toJson());

        //delete from array
        Bson filter3=eq("name", "test3");
        dbManager.deleteFromArray(filter2, "portfolios", filter3, collection);
        user2=dbManager.findOne(filter2, collection);
        System.out.println(user2.toJson());

        //delete a user
        System.out.println(dbManager.deleteOne(filter2, collection));

        //find stocks
        MongoCollection<Document> collection1 = dbManager.getCollection(MongoCollectionName.stocks.toString());
        Bson filter4=eq("quoteType", "ETF");
        for (Document document : dbManager.findMany(filter4, collection1)) {
            System.out.println(document.toJson());
        }

    }

}
