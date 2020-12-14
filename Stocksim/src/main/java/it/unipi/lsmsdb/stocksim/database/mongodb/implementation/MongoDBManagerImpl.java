package it.unipi.lsmsdb.stocksim.database.mongodb.implementation;

import com.mongodb.client.MongoCollection;

import static com.mongodb.client.model.Filters.*;

import com.mongodb.client.model.Updates;
<<<<<<< Updated upstream:Stocksim/src/main/java/it/unipi/lsmsdb/stocksim/database/mongodb/implementation/MongoDBManagerImpl.java
import it.unipi.lsmsdb.stocksim.database.mongodb.entities.*;
import it.unipi.lsmsdb.stocksim.database.mongodb.persistence.DocumentDBManager;
import it.unipi.lsmsdb.stocksim.database.mongodb.persistence.MongoDBManager;
=======
import it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.entities.*;
import it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.entities.*;
import it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.persistence.DocumentDBManager;
import it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.persistence.MongoDBManager;
import org.bson.BsonArray;
>>>>>>> Stashed changes:Stocksim/src/main/java/it/unipi/lsmsdb/workgroup4/stocksim/database/mongodb/implementation/MongoDBManagerImpl.java
import org.bson.Document;

import javax.print.Doc;
import java.util.ArrayList;

public class MongoDBManagerImpl extends MongoDBManager {


    public MongoDBManagerImpl() {
    }

    @Override
    public Stocks getStocks(String attribute, String value) {
        connect();
        final MongoCollection<Document> stocksColl = db.getCollection("stocks");
        Stocks res= new StocksImpl();
        for (Document doc : stocksColl.find(eq(attribute, value))) {
            Stock item= new Stockimpl(doc);
            res.add(item);
        }
        return res;
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
