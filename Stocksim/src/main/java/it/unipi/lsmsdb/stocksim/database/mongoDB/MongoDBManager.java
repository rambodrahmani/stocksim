package it.unipi.lsmsdb.stocksim.database.mongoDB;

import com.mongodb.client.MongoCollection;

import it.unipi.lsmsdb.stocksim.database.DBManager;
import org.bson.Document;
import org.bson.conversions.Bson;


import java.util.ArrayList;
import java.util.List;

public interface MongoDBManager extends DBManager {

    boolean open(String databaseName);
    void close();
    MongoCollection<Document> getCollection(String collectionName);
    ArrayList<Document> findMany(Bson filter, MongoCollection<Document> collection);
    Document findOne(Bson filter,MongoCollection<Document> collection);
    Boolean updateOne(Bson filter,Bson updates, MongoCollection<Document> collection);
    Boolean insertInArray(Bson filter, String arrayName, Document newDoc, MongoCollection<Document> collection);
    Boolean updateOneInNestedArray(Bson filter, List<Bson> arrayFilter, Bson updates, MongoCollection<Document> collection);
    Boolean insertOne(Document newDoc, MongoCollection<Document> collection);
    Boolean deleteOne(Bson filter, MongoCollection<Document> collection);
    Boolean deleteFromArray(Bson filter, String arrayNAme, Bson arrayFilter, MongoCollection<Document> collection);
    int deleteMany(Bson filter, MongoCollection<Document> collection);

}
