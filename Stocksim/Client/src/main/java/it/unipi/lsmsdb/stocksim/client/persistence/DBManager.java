package it.unipi.lsmsdb.stocksim.client.persistence;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import it.unipi.lsmsdb.stocksim.client.entities.Portfolio;
import it.unipi.lsmsdb.stocksim.client.entities.Stock;
import it.unipi.lsmsdb.stocksim.client.entities.Title;
import it.unipi.lsmsdb.stocksim.client.entities.User;
import it.unipi.lsmsdb.stocksim.database.cassandra.CassandraDB;
import it.unipi.lsmsdb.stocksim.database.cassandra.CassandraDBFactory;
import it.unipi.lsmsdb.stocksim.database.mongoDB.MongoDB;
import it.unipi.lsmsdb.stocksim.database.mongoDB.MongoDBFactory;
import it.unipi.lsmsdb.stocksim.database.mongoDB.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.security.AlgorithmConstraints;
import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

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
            disconnectMongoDB();
        }
        if(res!=null)
            return new StockImpl(res); //return stock instance
        return null;
    }


    /**
     * @param attribute the ticker name in capital letters
     * @return a list of stocks, can be empty
     */
    public List<Stock> getStockByAttribute(final String attribute, final String value) {
        MongoDB db=getMongoDB(); //get the database and the collection
        MongoCollection<Document> stocksColl=db.getCollection(StocksimCollection.STOCKS.getCollectionName());
        Bson filter=eq(attribute,value);    //prepare query
        ArrayList<Document> resultSet=null;
        ArrayList<Stock> res=new ArrayList<>();
        try {
            resultSet=db.findMany(filter, stocksColl); //get resultSet
        }
        catch (final Exception e){

        }finally {
            disconnectMongoDB();
        }
        if(resultSet==null)
            return res;
        for (Document doc : resultSet) {
            res.add(new StockImpl(doc));
        }
        return  res; //return stock instance
    }

    /**
     * @param attribute the ticker name in capital letters
     * @return a list of stocks, can be empty
     */
    public ArrayList<Stock> getStockByAttribute(String attribute, double min, double max) {
        MongoDB db=getMongoDB(); //get the database and the collection
        MongoCollection<Document> stocksColl=db.getCollection(StocksimCollection.STOCKS.getCollectionName());
        Bson filter=and(gt(attribute,min), lt

                (attribute, max));    //prepare query
        ArrayList<Document> resultSet=null;
        ArrayList<Stock> res=new ArrayList<>();
        try {
            resultSet=db.findMany(filter, stocksColl); //get resultSet
        }
        catch (final Exception e){

        }finally {
            disconnectMongoDB();
        }
        if(resultSet==null)
            return res;
        for (Document doc : resultSet) {
            res.add(new StockImpl(doc));
        }
        return  res; //return stock instance
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
        disconnectMongoDB();
        if(ret){
            port.getComposition().add(new TitleImpl(stock, share)); //update entity
        }
        return  ret;
    }

    /** create a new portfolio if possible
     * @param owner the owner of the new portfolio
     * @param name the name of the new portfolio
     * @param type the type of the new portfolio
     * @return the instance of the new portfolio, null if something(s) went wrong
     */
    public Portfolio createPortfolio(User owner, String name, String type) {
        if(owner.getPortfolioByName("name")!=null)
            return null;
        MongoDB db = getMongoDB(); //get the database and the collection
        MongoCollection<Document> userColl = db.getCollection(StocksimCollection.USERS.getCollectionName());
        Bson filter1=eq("username", owner.getUsername());
        Document newdoc=new Document();
        newdoc.append("name", name).append("type", type).append("composition", new ArrayList<Document>());
        Boolean res=false;
        try {
            // insert the new portfolio in the database
            res=db.insertInArray(filter1, "portfolios",newdoc, userColl );
        }
        catch (final Exception e){
            disconnectMongoDB();
            return null;
        }
        disconnectMongoDB();
        if(res) // create the new instance
            return new PortfolioImpl(owner,newdoc,this);
        return null;
    }

    /** delete a portfolio if exists
     * @param portfolio the portfolio to be deleted
     * @return true if everything went good, false otherwise
     */
    public boolean deletePortfolio(final Portfolio portfolio) {
        MongoDB db = getMongoDB(); //get the database and the collection
        MongoCollection<Document> userColl = db.getCollection(StocksimCollection.USERS.getCollectionName());
        Bson filter1=eq("username", portfolio.getOwner().getUsername());
        Bson filter2=eq("name", portfolio.getName());
        try {
            if(db.deleteFromArray(filter1, "portfolios",filter2, userColl)){
                disconnectMongoDB();
                return true;
            }
        }catch (final Exception e){

        }
        finally {
            disconnectMongoDB();
            return false;
        }
    }

    /** delete a title from a portfolio, if both exists
     * @param port the portfolio where to perform the operation
     * @param stock the of the title to be deleted
     * @return true if everything went good, false otherwise
     */
    public boolean removeStockFromPortfolio(final Stock stock,final Portfolio  port) {
        MongoDB db = getMongoDB();  //get the database and the collection
        MongoCollection<Document> userColl = db.getCollection(StocksimCollection.USERS.getCollectionName());
        //prepare query
        Bson filter = eq("username", port.getOwner().getUsername());
        Bson filter2 = eq("ticker", stock.getTicker());
        List<Bson> arrayFilters = Arrays.asList(eq("port.name",port.getName()));
        Bson updates= Updates.pull("portfolios.$[port].composition", filter2);
        Boolean ret = false;
        try {   //get resultSet
            ret=db.updateOneInNestedArray(filter,  arrayFilters, updates, userColl);
        }
        catch (final Exception e){
        }
        disconnectMongoDB();
        return  ret;
    }

    /** update a text field of a portfolio
     * @param field the field to be updated
     * @param value the new value
     * @param  port the portfolio to be updated
     * @return true if everything went good, false otherwise
     */
    public boolean updatePortfolio(final String field, final String value, final Portfolio port){
        if(field!="name" && field!="type")
            return false;
        MongoDB db = getMongoDB();  //get the database and the collection
        MongoCollection<Document> userColl = db.getCollection(StocksimCollection.USERS.getCollectionName());
        //prepare query
        Bson filter = eq("username", port.getOwner().getUsername());
        List<Bson> arrayFilters = Arrays.asList(eq("port.name",port.getName()));
        Bson updates= Updates.set("portfolios.$[port]."+field, value);
        Boolean ret = false;
        try {   //get resultSet
            ret=db.updateOneInNestedArray(filter,  arrayFilters, updates, userColl);
        }
        catch (final Exception e){
        }
        disconnectMongoDB();
        return  ret;
    }

    /** replace a title in a portfolio
     * @param newTitle the new title to be added
     * @param oldTitle the old title to be deleted
     * @param  port the portfolio to be updated
     * @return true if everything went good, false otherwise
     */
    public boolean replaceTitle(final Title newTitle, final Title oldTitle,final Portfolio port){
        MongoDB db = getMongoDB();  //get the database and the collection
        MongoCollection<Document> userColl = db.getCollection(StocksimCollection.USERS.getCollectionName());
        //prepare query
        Bson filter = eq("username", port.getOwner().getUsername());
        List<Bson> arrayFilters = Arrays.asList(eq("port.name",port.getName()));
        Document newTitleDoc = new Document();
        Bson remFilter=eq("ticker",oldTitle.getStock().getTicker());
        newTitleDoc.append("ticker",newTitle.getStock().getTicker()).append("share", newTitle.getShare());
        Bson remove= Updates.pull("portfolios.$[port].composition", oldTitle);
        Bson add= Updates.push("portfolios.$[port].composition", newTitleDoc);
        Bson updates=Updates.combine(remove, add);
        Boolean ret = false;
        try {   //get resultSet
            ret=db.updateOneInNestedArray(filter,  arrayFilters, updates, userColl);
        }
        catch (final Exception e){
        }
        disconnectMongoDB();
        return  ret;
    }

    /** verify if a username is available
     * @param username the owner of the new portfolio
     * @return true if it's available, false otherwise
     */
    public  boolean checkUsername(String username){
        MongoDB db = getMongoDB(); //get the database and the collection
        MongoCollection<Document> userColl = db.getCollection(StocksimCollection.USERS.getCollectionName());
        Bson filter1=eq("username", username);
        Document existent=null;
        try {
            // verify if exists another portfolio with the same name
            existent = db.findOne(filter1, userColl);
        }
        catch (final Exception e){
            disconnectMongoDB();
            return false;
        }
        disconnectMongoDB();
        if(existent!= null){
            return false;
        }
        return true;
    }

    /** verify if an email is available
     * @param email the owner of the new portfolio
     * @return true if it's available, false otherwise
     */
    public  boolean checkEmail(final String email){
        MongoDB db = getMongoDB(); //get the database and the collection
        MongoCollection<Document> userColl = db.getCollection(StocksimCollection.USERS.getCollectionName());
        Bson filter1=eq("email", email);
        Document existent=null;
        try {
            // verify if exists another portfolio with the same name
            existent = db.findOne(filter1, userColl);
        }
        catch (final Exception e){
            disconnectMongoDB();
            return false;
        }
        disconnectMongoDB();
        if(existent!= null){
            return false;
        }
        return true;
    }

    /**
     */
    public User registerNewUser(String name, String surname, String username, String email, String password){
        MongoDB db = getMongoDB(); //get the database and the collection
        MongoCollection<Document> userColl = db.getCollection(StocksimCollection.USERS.getCollectionName());
        Document newDoc=new Document();
        newDoc.append("name", name).append("surname", surname).append("email",email).append("username", username)
                .append("password",  DigestUtils.sha256Hex(password))
        .append("portfolios", new ArrayList<Document>());
        User ret=null;
        try {
            if(!db.insertOne(newDoc,userColl )){
                disconnectMongoDB();
                return null;
            }
        }
        catch (Exception e){
            disconnectMongoDB();
            return null;
        }
        disconnectMongoDB();
            return new UserImpl(newDoc, this);

    }

    /**
     * @param username
     * @param password the quantity of the stock
     * @return the instance of the logged user if exists, null otherwise
     */
    public User login(final String username, final String password) {
        MongoDB db = getMongoDB(); //get the database and the collection
        MongoCollection<Document> userColl = db.getCollection(StocksimCollection.USERS.getCollectionName());
        MessageDigest messageDigest=null;
        try {
            messageDigest=MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        Bson filter = and(eq("username", username),
                eq("password", DigestUtils.sha256Hex(password)));
        Document user=null;
        try {
            user = db.findOne(filter, userColl);
        }
        catch (final Exception e){
            disconnectMongoDB();
            return null;
        }
        disconnectMongoDB();;
        if(user!=null)
            return new UserImpl(user, this);
        else
            return null;
    }

    /** starts admin only functionalities */

    public boolean deleteUser(User user){
        MongoDB db = getMongoDB(); //get the database and the collection
        MongoCollection<Document> userColl = db.getCollection(StocksimCollection.USERS.getCollectionName());
        Bson filters=eq("username", user.getUsername());
        try {
            if(db.deleteOne(filters, userColl )){
                disconnectMongoDB();
                return true;
            }
        }
        catch (Exception e){

        }
        disconnectMongoDB();
        return false;
    }

}
