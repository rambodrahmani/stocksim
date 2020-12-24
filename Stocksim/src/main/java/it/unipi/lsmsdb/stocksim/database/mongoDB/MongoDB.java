package it.unipi.lsmsdb.stocksim.database.mongoDB;

import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.lsmsdb.stocksim.database.DB;
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
public class MongoDB implements DB {
    // mongo db connection uri string
    private final ConnectionString uri;

    // mongo database name
    private final String databaseName;

    // connection to mongo db
    private MongoClient mongoClient;

    // database retrieved using the mongo client
    private MongoDatabase mongoDatabase;

    /**
     * Default constructor: prepares the connection to localhost.
     *
     * @param databaseName Mongo database name.
     */
    protected MongoDB(final String databaseName) {
        this.uri = new ConnectionString("mongodb://127.0.0.1:27017");
        this.databaseName = databaseName;
    }

    /**
     * Prepares the connection to a single server.
     *
     * @param host server dotted ip address.
     * @param port server port.
     */
    protected MongoDB(final String host, final int port, final String databaseName) {
        this.uri = new ConnectionString("mongodb://" + host + ":" + String.valueOf(port));
        this.databaseName = databaseName;
    }

    /**
     * Prepares the connection to a cluster.
     *
     * @param servers list of Mongo Servers in the cluster.
     */
    protected MongoDB(final List<MongoServer> servers, final String databaseName) {
        this.uri = new ConnectionString("mongodb://" + buildConnectionString(servers));
        this.databaseName = databaseName;
    }

    /**
     * Prepares the connection to a cluster with explicit preferences.
     *
     * @param servers list of Mongo Servers in the cluster.
     * @param preferences preferences string for the cluster.
     */
    protected MongoDB(final List<MongoServer>servers , final String preferences, final String databaseName) {
        uri = new ConnectionString("mongodb://" + buildConnectionString(servers) + "?" + preferences);
        this.databaseName = databaseName;
    }

    /**
     * Builds the string for the cluster connection.
     *
     * @param servers list of Mongo Servers in the cluster.
     *
     * @return the prepared connection string.
     */
    private String buildConnectionString(final List<MongoServer> servers) {
        String s = "";
        for (final MongoServer server : servers) {
            s += server.hostname + ":" + server.port + ",";
        }
        return  s.substring(0, s.length()-1);
    }

    /**
     * Setup the connection to the server, if possible.
     * No communication to the server is actually performed.
     *
     * @return true if the connection is successful, false otherwise.
     */
    @Override
    public boolean connect() {
        try {
            mongoClient = MongoClients.create(uri);
            mongoDatabase = mongoClient.getDatabase(databaseName);
        } catch (final Exception  e){
            mongoClient.close();
            mongoClient = null;
        }

        return mongoClient != null;
    }

    /**
     * Closes the connection to the database.
     */
    @Override
    public MongoDB disconnect() {
        mongoClient.close();
        return null;
    }

    /**
     * Retrieves the mongo collection with the given name.
     *
     * @return the collection object if retrieved, null otherwise.
     */
    public MongoCollection<Document> getCollection(final String collectionName) {
        if (mongoDatabase != null) {
            return mongoDatabase.getCollection(collectionName);
        }
        return  null;
    }

    /**
     * Finds one document in the given collection.
     *
     * @param filter the filter in bson format to be used.
     * @param collection the collection where to perform the search.
     *
     * @return the first document that satisfies the given filter.
     */
    public Document findOne(final Bson filter, final MongoCollection<Document> collection) throws MongoException{
        return collection.find(filter).first();
    }

    /**
     * Finds the documents in the given collection.
     *
     * @param filter the filter in bson format to be used.
     * @param collection the collection where to perform the search.
     *
     * @return the documents that satisfy the given filter,
     *         might be an empty list, but never null.
     */
    public ArrayList<Document> findMany(final Bson filter, final MongoCollection<Document> collection) throws MongoException {
        ArrayList<Document> result = new ArrayList<>();
        collection.find(filter).forEach(result::add);
        return  result;
    }

    /**
     * Updates the document found using the given filter.
     *
     * @param filter the filter in bson format to be used.
     * @param collection the collection where to perform the search.
     * @param updates updates to be performed.
     *
     * @return true if a document has been updated, false otherwise.
     */
    public Boolean updateOne(final Bson filter, final Bson updates, final MongoCollection<Document> collection) throws MongoException {
        UpdateResult upRes = collection.updateOne(filter, updates);
        return (upRes.wasAcknowledged() && upRes.getMatchedCount() == 0);
    }

    /**
     * insert a document in an array, if possible
     *
     * @param collection the collection where to perform the operation
     * @param newDoc the document to be inserted
     *
     * @return  true if everything's ok, false otherwise
     */
    public Boolean insertOne(Document newDoc, MongoCollection<Document> collection) throws MongoException{
        return collection.insertOne(newDoc).wasAcknowledged();
    }

    /**
     * insert a document in an array, if possible. USE ONLY WITH SINGLE NESTED ARRAYS
     *
     * @param filter filters in bson format for the parent document
     * @param collection the collection where to perform the operation
     * @param array the name of the array where to perform the insert
     * @param newDoc the document to be inserted
     *
     * @return  true if everything's ok, false otherwise
     */
    public Boolean insertInArray(final Bson filter, final String array, final Document newDoc,
                                 final MongoCollection<Document> collection) throws MongoException {
        UpdateResult upRes=collection.updateOne(filter, Updates.push(array, newDoc));
        return (upRes.wasAcknowledged() &&upRes.getMatchedCount()>0);
    }

    /**
     * update a document in a nested array at any level, if possible
     * @param collection the collection where to perform the operation
     * @param updates updates to be performed
     * @param filter filters to find the parent document
     * @param arrayFilters filters to be applied at the array(s) field(s)
     * @return  true if everything's ok and one document has been updated, false otherwise
     */
    public Boolean updateOneInNestedArray(final Bson filter,final List<Bson> arrayFilters, final Bson updates, final MongoCollection<Document> collection) {
        UpdateOptions uo=new UpdateOptions();
        uo.arrayFilters(arrayFilters);
        UpdateResult upRes=collection.updateOne(filter, updates, uo);
        return (upRes.wasAcknowledged() && upRes.getMatchedCount() == 1);
    }


    /**
     * delete one document witch match filters, if possible
     *
     * @param collection the collection where to perform the operation
     * @param filter filters to find the document
     *
     * @return  true if everything's ok and one document has been deleted, false otherwise
     */
    public Boolean deleteOne(Bson filter, MongoCollection<Document> collection) {
        DeleteResult deleteResult = collection.deleteOne(filter);
        return (deleteResult.wasAcknowledged() && deleteResult.getDeletedCount() > 0);
    }

    /**
     * delete documents witch match filters in an array, if possible
     *
     * @param collection the collection where to perform the operation
     * @param arrayName the array where to delete the element
     * @param filter filters to find the parent document
     * @param arrayFilter filters to be applied at the array field(s)
     *
     * @return  true if everything's ok and at least one document has been deleted, false otherwise
     */
    public Boolean deleteFromArray(final Bson filter,final String arrayName, final Bson arrayFilter, final MongoCollection<Document> collection) {
        UpdateResult upRes=collection.updateOne(filter, Updates.pull(arrayName,arrayFilter));
        return (upRes.wasAcknowledged() && upRes.getMatchedCount() > 0);
    }

    /**
     * delete documents witch match filters, if possible
     *
     * @param collection the collection where to perform the operation
     * @param filter filters to find the documents
     *
     * @return  the number of documents deleted. In case of error(s) return 0
     */
    public int deleteMany(Bson filter, MongoCollection<Document> collection) {
        DeleteResult deleteResult = collection.deleteMany(filter);

        if (deleteResult.wasAcknowledged()) {
            return (int) deleteResult.getDeletedCount();
        }

        return  0;
    }
}
