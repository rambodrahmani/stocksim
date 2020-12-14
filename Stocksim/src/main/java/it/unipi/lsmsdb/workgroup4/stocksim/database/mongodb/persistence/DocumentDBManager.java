package it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.persistence;

import it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.entities.Portfolio;
import it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.entities.Session;
import it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.entities.Title;
import it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.entities.User;
import it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.entities.Stocks;

import java.util.ArrayList;

public interface DocumentDBManager {
    Stocks getStocks(String Attribute, String Value);
    Session login(String username, String password);
    boolean logout(Session loggedSession);
    ArrayList<Portfolio> loadPortfolios(User owner);
    boolean addTitleToPortfolio(Portfolio portfolio, Title title);
    boolean addTitlesToPortfolio(Portfolio portfolio, ArrayList<Title> titles);
    boolean removeTitleFromPortfolio(Portfolio portfolio, Title title);
    boolean removeTitlesFromPortfolio(Portfolio portfolio, ArrayList<Title> titles);
    boolean wipePortfolio(Portfolio portfolio);
    Portfolio createPortfolio(User owner, String name, String type);
    boolean removePortfolio(Portfolio portfolio);
    User getUser(String Username);
}
