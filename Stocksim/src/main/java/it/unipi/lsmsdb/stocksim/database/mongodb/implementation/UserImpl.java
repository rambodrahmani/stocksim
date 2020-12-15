package it.unipi.lsmsdb.stocksim.database.mongodb.implementation;

import it.unipi.lsmsdb.stocksim.database.mongodb.entities.Portfolio;
import it.unipi.lsmsdb.stocksim.database.mongodb.entities.User;
import it.unipi.lsmsdb.stocksim.database.mongodb.persistence.DocumentDBManager;
import org.bson.Document;

import java.util.ArrayList;

public class UserImpl extends User {

    DocumentDBManager dbManager;
    protected UserImpl(String username, String email, String name, String surname, DocumentDBManager dbManager) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.dbManager = dbManager;
        this.portfolios = null;
    }

    public UserImpl(Document doc, DocumentDBManager dbManager) {
        this(
                doc.getString("username"),
                doc.getString("email"),
                doc.getString("name"),
                doc.getString("surname"),
                dbManager
        );
    }



    public int loadPortfolios(){

        portfolios= dbManager.loadPortfolios(this);
        return portfolios.size();
    }

    @Override
    public ArrayList<Portfolio> getPortfolios() {
        if(portfolios==null)
            this.loadPortfolios();
        return  this.portfolios;
    }

    @Override
    public Portfolio addPortfolio(String name, String type) {
        Portfolio p=dbManager.createPortfolio(this, name,type);
        if(this.portfolios==null){
            this.loadPortfolios();
        }
        this.portfolios.add(p);
        return p;
    }


}
