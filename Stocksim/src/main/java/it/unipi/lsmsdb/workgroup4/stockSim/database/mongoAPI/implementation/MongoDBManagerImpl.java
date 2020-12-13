package it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.implementation;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;

import com.mongodb.client.model.Updates;
import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.entities.*;
import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.implementation.PortfolioImpl;
import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.implementation.UserImpl;
import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.persistence.DocumentDBManager;
import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.persistence.MongoDBManager;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

public class MongoDBManagerImpl extends MongoDBManager {


    public MongoDBManagerImpl() {
    }

    @Override
    public Stocks getStocks(String Attribute, String Value) {
        connect();
        final MongoCollection<Document> myColl = db.getCollection("stocks");

        return null;
    }

    @Override
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
    }

    @Override
    public ArrayList<Portfolio> loadPortfolios(User owner) {

        return new ArrayList<Portfolio>();

    }

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

    @Override
    public boolean wipePortfolio(Portfolio portfolio) {
        return false;
    }

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
        return false;
    }

    @Override
    public User getUser(String Username)  {
        return new UserImpl(Username, "aa","  ","aa", (DocumentDBManager) this);
    }





}
