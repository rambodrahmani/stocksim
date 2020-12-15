package it.unipi.lsmsdb.stocksim.database.mongodb;

import it.unipi.lsmsdb.stocksim.database.DBFactory;
import it.unipi.lsmsdb.stocksim.database.mongodb.entities.*;
import it.unipi.lsmsdb.stocksim.database.mongodb.implementation.MongoDBFactory;
import it.unipi.lsmsdb.stocksim.database.DBFactory;
import it.unipi.lsmsdb.stocksim.database.mongodb.persistence.DocumentDBManager;
import org.apache.log4j.*;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    private static  final MongoDBFactory factory = MongoDBFactory.create();
    public static void main(String[] args) {

        DocumentDBManager dbManager =factory.getMongoDB();

        dbManager =factory.getMongoDB();
       Stocks mystocks=dbManager.getStocks("ticker", "TSLA");
    /*
        Stocks myOtherStocks=dbManager.getStocks("ticker", "SPX");
        for (Stock mystock : mystocks) {
            System.out.println(mystock.getTicker());
        }*/
        User myUser= dbManager.createUser(
                "TWOWS", "twows@google.com", "Yuri", "Mazzuoli","abcd1234");
     /*   User myUser2= dbManager.createUser(
                "TWOWS1", "twows@google.com", "Yuri", "Mazzuoli","abcd1234");*/
        User myUser3= dbManager.getUser("TWOWS");
        System.out.println(myUser3.getUsername());
        myUser3.addPortfolio("testPortfolio", "test");
        Portfolio p1=myUser3.addPortfolio("testPortfolio2", "test");
        if(p1==null)
            p1=myUser3.getPortfolios().get(0);
        Title tesla=new Title(mystocks.get(0),100.0);
        p1.add(tesla);
        dbManager.removePortfolio(myUser3.getPortfolios().get(1));

    }

}
