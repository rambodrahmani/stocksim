package it.unipi.lsmsdb.stocksim.client.persistence;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import it.unipi.lsmsdb.stocksim.client.entities.Portfolio;
import it.unipi.lsmsdb.stocksim.client.entities.Stock;
import it.unipi.lsmsdb.stocksim.client.entities.User;
import it.unipi.lsmsdb.stocksim.database.cassandra.CassandraDB;
import it.unipi.lsmsdb.stocksim.database.cassandra.CassandraDBFactory;
import it.unipi.lsmsdb.stocksim.database.mongoDB.MongoDB;
import it.unipi.lsmsdb.stocksim.database.mongoDB.MongoDBFactory;
import it.unipi.lsmsdb.stocksim.database.mongoDB.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.Arrays;
import java.util.List;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Sotcksim Server DB Manager.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class DBManager {
    /**
     * Cassandra DB Factory.
     */
    final CassandraDBFactory cassandraDBFactory = CassandraDBFactory.create();

    /**
     * Cassandra DB instance.
     */
    private CassandraDB cassandraDB;

    /**
     * Mongo DB Factory.
     */
    final MongoDBFactory mongoDBFactory = MongoDBFactory.create();

    /**
     * Mongo DB shared instance.
     */
    private MongoDB mongoDB;

    /**
     * @return Cassandra DB shared instance;
     */
    private CassandraDB getCassandraDB() {
        if (cassandraDB == null) {
            cassandraDB = cassandraDBFactory.getCassandraDB("192.168.2.133", 9042, "datacenter1");

        }
        cassandraDB.connect();
        return cassandraDB;
    }

    /**
     * Disconnects from Cassandra DB and sets reference to null.
     */
    private void disconnectCassandraDB() {
        cassandraDB = getCassandraDB().disconnect();
    }

    /**
     * @return Mongo DB shared instance;
     */
    private MongoDB getMongoDB() {
        if (mongoDB == null) {
            mongoDB = mongoDBFactory.getMongoDBManager( "stocksim");
        }
        mongoDB.connect();

        return mongoDB;
    }

    /**
     * Disconnects from Mongo DB and sets reference to null.
     */
    private void disconnectMongoDB() {
        mongoDB = getMongoDB().disconnect();
    }

    /**
     * @param ticker the ticker name in capital letters
     * @return an instance of the stock object, null if not present or some error(s) occurred;
     */
    public Stock getStockByTicker(final String ticker){
        MongoDB db=getMongoDB(); //get the database and the collection
        MongoCollection<Document> stocksColl=db.getCollection(StocksimCollection.STOCKS.getCollectionName());
        Bson filter=eq("ticker",ticker);    //prepare query
        Document res=null;
        try {
            res=db.findOne(filter, stocksColl); //get resultSet
        }
        catch (final Exception e){

        }finally {
            db.disconnect();
        }
        if(res!=null)
            return new StockImpl(res); //return stock instance
        return null;
    }

    /**
     * @param stock the stock to be added to the portfolio
     * @param share the quantity of the stock
     * @param port the portfolio where to perform the operation
     * @return an instance of the stock object;
     */
    public boolean addStockToPortfolio(final Stock stock, final double share, final Portfolio port){
        MongoDB db = getMongoDB();  //get the database and the collection
        MongoCollection<Document> userColl = db.getCollection(StocksimCollection.USERS.getCollectionName());
        //prepare query
        Bson filter = eq("username", port.getOwner().getUsername());
        List<Bson> arrayFilters = Arrays.asList(eq("port.name",port.getName()));
        Document newTitle = new Document();
        newTitle.append("ticker",stock.getTicker()).append("share", share);
        Bson updates= Updates.push("portfolios.$[port].composition", newTitle);
        Boolean ret = false;
        try {   //get resultSet
            ret=db.updateOneInNestedArray(filter,  arrayFilters, updates, userColl);
        }
        catch (final Exception e){
        }
        finally {
            db.disconnect();
        }
        if(ret){
            port.getComposition().add(new TitleImpl(stock, share)); //update entity
        }
        return  ret;
    }

    /**
     * @param username
     * @param password the quantity of the stock
     * @return the instance of the logged user if exists, null otherwise
     */
    public User login(final String username, final String password) {
        MongoDB db = getMongoDB(); //get the database and the collection
        MongoCollection<Document> userColl = db.getCollection(StocksimCollection.USERS.getCollectionName());
        Bson filter = and(eq("username", username), eq("password", password));
        Document user=null;
        try {
           user = db.findOne(filter, userColl);
        }
        catch (final Exception e){
            return null;
        }
        finally {
            db.disconnect();
        }
        if(user!=null)
            return new UserImpl(user, this);
        else
            return null;
    }
}
