package it.unipi.lsmsdb.stocksim.database.mongodb.implementation;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;

import it.unipi.lsmsdb.stocksim.database.cassandra.CQLSessionException;
import it.unipi.lsmsdb.stocksim.database.mongodb.entities.*;
import it.unipi.lsmsdb.stocksim.database.mongodb.persistence.DocumentDBManager;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Projections.*;
import it.unipi.lsmsdb.stocksim.database.mongodb.persistence.MongoServer;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MongoDBManager implements  DocumentDBManager{
    private final ConnectionString uri;
    protected MongoClient mongoClient;
    protected MongoDatabase db;

    // connect to a single server
    protected MongoDBManager(String host, String port) {
        uri = new ConnectionString("mongodb://"+host+":"+port);
    }

    // connect to localhost
    protected  MongoDBManager(){
        uri = new ConnectionString("mongodb://127.0.0.1:27017");
    }

    // connect to a local cluster
    public MongoDBManager(List<MongoServer> servers) {
        String s=buildString(servers);
        uri = new ConnectionString("mongodb://"+s);
    }

    // connect to a local cluster explicit preferences
    public  MongoDBManager(List<MongoServer>servers , String preferences){
        String s=buildString(servers)+"?"+preferences;
        uri = new ConnectionString("mongodb://"+s);
    }

    public boolean open() //open the connection to the database
    {
        connect();
        try {
            db=mongoClient.getDatabase("StockSim");
        }
        catch (Exception e){
            db=null;
        }
        if(db==null)
            return false;
        return true;
    }

    @Override // connect to the server
    public boolean connect() {
        try {
            mongoClient = MongoClients.create(uri);
        }
        catch (Exception  e){
            mongoClient=null;
        }
        if(mongoClient==null)
            return  false;
        return true;
    }

    @Override
    public ResultSet query(String query) throws CQLSessionException {
        return null;
    }

    protected void close(){
        mongoClient.close();
    } //close the connection to the database

    // get a list of stocks with the same attribute value
    @Override
    public Stocks getStocks(String attribute, String value) {
        open();
        final MongoCollection<Document> stocksColl = db.getCollection("stocks");
        Stocks res= new Stocks(); //create a list and add each result
        for (Document doc : stocksColl.find(eq(attribute, value))) {
            Stock item= new Stockimpl(doc); // create the object
            res.add(item);
        }
        close();
        return res;
    }

    @Override //return a user from the given username
    public User getUser(String username)  {
        open();
        final MongoCollection<Document> usersColl = db.getCollection("users");
        //create the object to return
        User ret=new UserImpl(usersColl.find(eq("username", username)).first(),  this);
        close();
        return  ret;
    }

    @Override //load in memory all the stocks in a portfolio
    public ArrayList<Title> loadPortfolioComposizion(Portfolio portfolio) {
        return null; //todo
    }

    //create a new user
    public User createUser( String username, String email,String name, String surname, String password){
        open();
        final MongoCollection<Document> usersColl = db.getCollection("users");
        // check if the username is already used
        if(usersColl.countDocuments(eq("username", username))!=0){
            close();
            return null;
        }
        Document userDoc=new Document(); //create the document
        userDoc.append("username", username);
        userDoc.append("password", password);
        userDoc.append("email", email);
        userDoc.append("name", name);
        userDoc.append("surname", surname);

        userDoc.append("portfolios", new ArrayList<Document>());
        usersColl.insertOne(userDoc); //write into the database
        close();
        return new UserImpl(userDoc,this);// create amd return the inMemory object

    }

    //create an empty portfolio
    @Override
    public Portfolio createPortfolio(User owner, String name, String type) {
        open();
        final MongoCollection<Document> usersColl = db.getCollection("users");
        // check if the username is already used
        Bson filter=match(eq("username",owner.getUsername()));
        Bson unwind=unwind("$portfolios");
        Bson filter2=match(eq("portfolios.name",name));
        for (Document document : usersColl.aggregate(Arrays.asList(filter,unwind,filter2))) {
            close();
            return null;
        }
        Document po=new Document().append("name",name ).append("type", type).append("composition",new ArrayList<String>());
        usersColl.updateOne(eq("username", owner.getUsername()), Updates.push("portfolios", po));
        close();
        Portfolio p =new PortfolioImpl(owner,po, this);
        return  p;
    }

    // load in memory the portfolios of a specific users and return their list
    @Override
    public ArrayList<Portfolio> loadPortfolios(User owner) {
        open();
        final MongoCollection<Document> usersColl = db.getCollection("users");
        ArrayList<Portfolio> res= new ArrayList<>();
        // prepare query
        ArrayList<Document> portfs= (ArrayList<Document>)
                usersColl.find(eq("username",owner.getUsername())).first().get("portfolios");
        for (Document port : portfs) {
            res.add(new PortfolioImpl(owner,port,  this)); //create objects
        }
        close();
        return res;

    }

    //add a title to a specific portfolio
    @Override
    public boolean addTitleToPortfolio(Portfolio portfolio, Title title) {
        open();
        final MongoCollection<Document> usersColl = db.getCollection("users");
        Document titleDoc=new Document(); // create the document
        titleDoc.append("ticker", title.getStock().getTicker());
        titleDoc.append("share", title.getShare());
        //prepare query
        Bson update=Updates.push("portfolios.$.composition", titleDoc);
        Bson filter=and(
                eq("username",portfolio.getOwner().getUsername()),
                eq("portfolios.name", portfolio.getName())
                );
        usersColl.updateOne(filter, update); //write into the db
        close();
        return true;
    }



    @Override
    public boolean addTitlesToPortfolio(Portfolio portfolio, ArrayList<Title> titles) {
        //todo
        return false;
    }

    @Override
    public boolean removeTitleFromPortfolio(Portfolio portfolio, Title title) {
        //todo
        return false;
    }

    @Override
    public boolean removeTitlesFromPortfolio(Portfolio portfolio, ArrayList<Title> titles) {
        //todo
        return false;
    }

    //remove every titles from a portfolio
    @Override
    public boolean wipePortfolio(Portfolio portfolio) {

        return removeTitlesFromPortfolio(portfolio, portfolio.getComposition());
    }



    @Override //remove a portfolio
    public boolean removePortfolio(Portfolio portfolio) {
        open();
        final MongoCollection<Document> usersColl = db.getCollection("users");
        //prepare query
        Document user = usersColl.find(eq("username", portfolio.getOwner().getUsername())).first();
        Bson match = eq("name", portfolio.getName());
        usersColl.updateOne( // update user's portfolios
                eq("username", portfolio.getOwner().getUsername()),
                Updates.pull("portfolios", match)
        );
        close();
        return true;

    }



    // build the string for local cluster connection
    private String buildString(List<MongoServer> servers){
        String s="";
        for (MongoServer server : servers) {
            s+=server.host+":"+server.port+",";
        }
        return  s.substring(0, s.length()-1);
    }

/*@Override
    public Session login(String username, String password) {
        connect();
        final MongoCollection<Document> myColl = db.getCollection("users");
        return null;
    }

    @Override
    public boolean logout(Session loggedSession) {
        connect();
        final MongoCollection<Document> myColl = db.getCollection("users");
        return false;

    }*/


}
