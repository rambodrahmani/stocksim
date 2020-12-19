package it.unipi.lsmsdb.stocksim.database.mongoDB;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import com.mongodb.client.*;

import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.lsmsdb.stocksim.database.cassandra.CQLSessionException;


import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * DB Manager for MongoDB.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class MongoDB implements MongoDBManager {
    private final ConnectionString uri;
    protected MongoClient mongoClient;
    protected MongoDatabase db;

    /**
     * prepare the connection to a single server
     * @param host server dotted ip address
     * @param port server port
     *
     */
    protected MongoDB(final String host, final String port) {
        uri = new ConnectionString("mongodb://"+host+":"+port);
    }

    /**
     * prepare the connection to localhost
     *
     */
    protected MongoDB(){
        uri = new ConnectionString("mongodb://127.0.0.1:27017");
    }

    /**
     *  connect to a local cluster
     * @param servers  list of servers ip address ad port
     *
     */
    protected MongoDB(final List<MongoServer> servers){
            uri = new ConnectionString("mongodb://"+buildString(servers));
    }

    /**
     *  connect to a local cluster explicit preferences
     * @param servers  list of servers ip address ad port
     * @param preferences preferences string for the cluster
     */
    protected MongoDB(final List<MongoServer>servers , final String preferences){
        String s=buildString(servers)+"?"+preferences;
        uri = new ConnectionString("mongodb://"+s);
    }

    /**
     * setup the connection to the server, if possible
     * will not start to communicate to the server
     *
     * @return  true if everything's ok, false otherwise
     */
    @Override //
    public boolean connect() {
        try {
            mongoClient = MongoClients.create(uri);
        }
        catch (final Exception  e){
            mongoClient=null;
        }
        return mongoClient != null;
    }

    /**
     * open the connection to the database if possible
     * this function will start to communicate with the server
     *
     * @return  true if everything's ok, false otherwise
     */
    @Override
    public boolean open(final String databaseName)//
    {
        connect();
        try {
            db=mongoClient.getDatabase(databaseName);
        }
        catch (final Exception e){
            db=null;
        }
        return db != null;
    }


    @Override
    public ResultSet query(final String query) throws CQLSessionException {
        return null;
    }

    /**
     * close the connection to the database
     *
     */
    @Override
    public void close(){
        try {
            mongoClient.close();
        }catch (final Exception  e){

        }
    }

    /**
     * connect to a specific connection, if possible
     * function open must has been called with positive outcome
     *
     * @return  the collection object if everything's ok, null otherwise
     */
    @Override
    public MongoCollection<Document> getCollection(final String collectionName){
        if(db!=null)
            return db.getCollection(collectionName);
        return  null;
    }

    /**
     * find a document in a collection
     * @param filter filters in bson format
     * @param collection the collection where to perform the find
     * @return  the documents that satisfy the filters, an empty array if there aren't any
     */
    @Override
    public ArrayList<Document> findMany(Bson filter, MongoCollection<Document> collection)throws MongoException {
        ArrayList<Document> result = new ArrayList<>();
        collection.find(filter).forEach(result::add);
        return  result;
    }

    /**
     * find a document in a collection
     * @param filter filters in bson format
     * @param collection the collection where to perform the find
     * @return  the first document that satisfy the filters, null if there aren't any
     */
    @Override
    public Document findOne(final Bson filter, final MongoCollection<Document> collection) throws MongoException{
        return collection.find(filter).first();
    }

    /**
     * update a document, if possible
     * @param collection the collection where to perform the operation
     * @param updates updates to be performed
     * @return  true if everything's ok and document has been updated, false otherwise
     */
    @Override
    public Boolean updateOne(final Bson filter,final  Bson updates, final MongoCollection<Document> collection)
            throws MongoException{
        UpdateResult upRes=collection.updateOne(filter, updates);
        if(upRes.wasAcknowledged() &&upRes.getMatchedCount()==0)
            return true;
        return  false;
    }

    /**
     * insert a document in an array, if possible
     * @param collection the collection where to perform the operation
     * @param newDoc the document to be inserted
     * @return  true if everything's ok, false otherwise
     */
    @Override
    public Boolean insertOne(Document newDoc, MongoCollection<Document> collection) throws MongoException{
        return collection.insertOne(newDoc).wasAcknowledged();
    }

    /**
     * insert a document in an array, if possible. USE ONLY WITH SINGLE NESTED ARRAYS
     * @param filter filters in bson format for the parent document
     * @param collection the collection where to perform the operation
     * @param array the name of the array where to perform the insert
     * @param newDoc the document to be inserted
     * @return  true if everything's ok, false otherwise
     */
    @Override
    public Boolean insertInArray(final Bson filter, final String array, final Document newDoc,
                                 final MongoCollection<Document> collection) throws MongoException{
        UpdateResult upRes=collection.updateOne(filter, Updates.push(array, newDoc));
        if(upRes.wasAcknowledged() &&upRes.getMatchedCount()>0)
            return true;
        return  false;
    }

    /**
     * update a document in a nested array at any level, if possible
     * @param collection the collection where to perform the operation
     * @param updates updates to be performed
     * @param filter filters to find the parent document
     * @param arrayFilters filters to be applied at the array(s) field(s)
     * @return  true if everything's ok and one document has been updated, false otherwise
     */
    @Override
    public Boolean updateOneInNestedArray(final Bson filter,final List<Bson> arrayFilters,
                                       final Bson updates, final MongoCollection<Document> collection) {
        UpdateOptions uo=new UpdateOptions();
        uo.arrayFilters(arrayFilters);
        UpdateResult upRes=collection.updateOne(filter, updates, uo);
        if(upRes.wasAcknowledged() &&upRes.getMatchedCount()==1)
            return true;
        return  false;
    }


    /**
     * delete one document witch match filters, if possible
     * @param collection the collection where to perform the operation
     * @param filter filters to find the document
     * @return  true if everything's ok and one document has been deleted, false otherwise
     */
    @Override
    public Boolean deleteOne(Bson filter, MongoCollection<Document> collection) {
        DeleteResult deleteResult = collection.deleteOne(filter);
        if(deleteResult.wasAcknowledged() &&deleteResult.getDeletedCount()>0)
            return true;
        return  false;
    }

    /**
     * delete documents witch match filters in an array, if possible
     * @param collection the collection where to perform the operation
     * @param arrayName the array where to delete the element
     * @param filter filters to find the parent document
     * @param arrayFilter filters to be applied at the array field(s)
     * @return  true if everything's ok and at least one document has been deleted, false otherwise
     */
    @Override
    public Boolean deleteFromArray(final Bson filter,final String arrayName ,
                                      final Bson arrayFilter, final MongoCollection<Document> collection) {
        UpdateResult upRes=collection.updateOne( filter,
                Updates.pull(arrayName,arrayFilter)
        );
        if(upRes.wasAcknowledged() &&upRes.getMatchedCount()>0)
            return true;
        return  false;
    }

    /**
     * delete documents witch match filters, if possible
     * @param collection the collection where to perform the operation
     * @param filter filters to find the documents
     * @return  the number of documents deleted. In case of error(s) return 0
     */
    @Override
    public int deleteMany(Bson filter, MongoCollection<Document> collection) {
        DeleteResult deleteResult = collection.deleteMany(filter);
        if(deleteResult.wasAcknowledged())
            return (int) deleteResult.getDeletedCount();
        return  0;
    }






    // build the string for local cluster connection
    private String buildString(List<MongoServer> servers){
        String s="";
        for (MongoServer server : servers) {
            s+=server.host+":"+server.port+",";
        }
        return  s.substring(0, s.length()-1);
    }



}
