package it.unipi.lsmsdb.stocksim.client.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import it.unipi.lsmsdb.stocksim.lib.database.mongoDB.MongoDB;
import it.unipi.lsmsdb.stocksim.lib.database.mongoDB.MongoDBFactory;
import it.unipi.lsmsdb.stocksim.lib.database.mongoDB.MongoServer;
import it.unipi.lsmsdb.stocksim.lib.database.mongoDB.StocksimCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;

public class testMain {
    public static void main(String[] args) {
        final MongoDBFactory mongoDBFactory = MongoDBFactory.create();

        // connect and get the collection
        MongoDB dbManager = mongoDBFactory.getMongoDB(
                "172.16.3.94", 27017,"stocksim");
        System.out.println(dbManager.connect());
        final MongoCollection<Document> collection =
                dbManager.getCollection(StocksimCollection.STOCKS.getCollectionName());
        Bson name= Filters.eq("symbol", "FTSEMIB.MI");
        Document stockDoc= dbManager.findOne(name, collection);
        Stock testStock=new Stock(stockDoc);
        System.out.println(testStock.toString());
    }
}
