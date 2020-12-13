package it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.persistence;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.entities.*;

import java.util.ArrayList;

public abstract class MongoDBManager {
    private final ConnectionString uri;
    private MongoClient mongoClient;



    protected MongoDatabase db;
    protected MongoDBManager() {
        uri = new ConnectionString("mongodb://172.16.3.94:27017");
    }
    protected void connect(){
        mongoClient = MongoClients.create(uri);
        db=mongoClient.getDatabase("StockSim");
    }

    public abstract Stocks getStocks(String Attribute, String Value);

    public abstract Session login(String username, String password);

    public abstract boolean logout(Session loggedSession);

    public abstract ArrayList<Portfolio> loadPortfolios(User owner);

    public abstract boolean addTitleToPortfolio(Portfolio portfolio, Title title);

    public abstract boolean addTitlesToPortfolio(Portfolio portfolio, ArrayList<Title> titles);

    public abstract boolean removeTitleFromPortfolio(Portfolio portfolio, Title title);

    public abstract boolean removeTitlesFromPortfolio(Portfolio portfolio, ArrayList<Title> titles);

    public abstract boolean wipePortfolio(Portfolio portfolio);

    public abstract Portfolio createPortfolio(User owner, String name, String type);

    public abstract boolean removePortfolio(Portfolio portfolio);

    public abstract User getUser(String Username);
}
