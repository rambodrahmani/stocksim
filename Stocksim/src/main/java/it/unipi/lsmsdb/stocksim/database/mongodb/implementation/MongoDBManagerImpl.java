package it.unipi.lsmsdb.stocksim.database.mongodb.implementation;

import com.mongodb.client.MongoCollection;

import static com.mongodb.client.model.Filters.*;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import it.unipi.lsmsdb.stocksim.database.mongodb.entities.*;
import it.unipi.lsmsdb.stocksim.database.mongodb.persistence.DocumentDBManager;
import it.unipi.lsmsdb.stocksim.database.mongodb.persistence.MongoDBManager;



import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MongoDBManagerImpl extends MongoDBManager implements  DocumentDBManager{


    public MongoDBManagerImpl() {
    }

    // get a list of stocks with the same attribute value
    @Override
    public Stocks getStocks(String attribute, String value) {
        connect();
        final MongoCollection<Document> stocksColl = db.getCollection("stocks");
        Stocks res= new StocksImpl();
        for (Document doc : stocksColl.find(eq(attribute, value))) {
            Stock item= new Stockimpl(doc);
            res.add(item);
        }
        close();
        return res;

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

    // load in memory the portfolios of a specific users and return their list
    @Override
    public ArrayList<Portfolio> loadPortfolios(User owner) {
        connect();
        final MongoCollection<Document> usersColl = db.getCollection("users");
        ArrayList<Portfolio> res= new ArrayList<Portfolio>();
        Document user =usersColl.find(eq("username", owner.getUsername())).first();
        List<Document> portfs= (List<Document>) user.get("portfolios");
        for (Document port : portfs) {
            res.add(new PortfolioImpl(port, (DocumentDBManager) this));
        }
        close();
        return res;

    }

    //add a title to a specific portfolio
    @Override
    public boolean addTitleToPortfolio(Portfolio portfolio, Title title) {
        return false;
    }

    @Override
    public boolean addTitlesToPortfolio(Portfolio portfolio, ArrayList<Title> titles) {
        return false;
    }

    @Override
    public boolean removeTitleFromPortfolio(Portfolio portfolio, Title title) {
        return false;
    }

    @Override
    public boolean removeTitlesFromPortfolio(Portfolio portfolio, ArrayList<Title> titles) {
        return false;
    }

    //remove every titles from a portfolio
    @Override
    public boolean wipePortfolio(Portfolio portfolio) {

        return removeTitlesFromPortfolio(portfolio, portfolio.getComposition());
    }

    //create an empty portfolio
    @Override
    public Portfolio createPortfolio(User owner, String name, String type) {
        Portfolio p =new PortfolioImpl(owner,name, type, (DocumentDBManager) this);
        connect();
        final MongoCollection<Document> usersColl = db.getCollection("users");
        Document po=new Document().append("name",name ).append("type", type);
        usersColl.updateOne(eq("username", owner.getUsername()), Updates.push("portfolios", po));
        close();
        return  p;
    }

    @Override
    public boolean removePortfolio(Portfolio portfolio) {
        connect();
        final MongoCollection<Document> usersColl = db.getCollection("users");
        Document user = usersColl.find(eq("username", portfolio.getOwner().getUsername())).first();
        Bson match = eq("name", portfolio.getName());
        usersColl.updateOne(
                eq("username", portfolio.getOwner().getUsername()),
                Updates.pull("portfolios", match)
        );
        close();
        return true;

    }

    @Override
    public User getUser(String username)  {
        connect();
        final MongoCollection<Document> usersColl = db.getCollection("users");
        close();
        return new UserImpl(usersColl.find(eq("username", username)).first(),  this);

    }





}
