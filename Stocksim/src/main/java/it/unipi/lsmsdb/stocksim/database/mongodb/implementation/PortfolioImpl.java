package it.unipi.lsmsdb.stocksim.database.mongodb.implementation;
import it.unipi.lsmsdb.stocksim.database.mongodb.entities.Portfolio;
import it.unipi.lsmsdb.stocksim.database.mongodb.entities.Title;
import it.unipi.lsmsdb.stocksim.database.mongodb.entities.User;
import it.unipi.lsmsdb.stocksim.database.mongodb.persistence.DocumentDBManager;
import it.unipi.lsmsdb.stocksim.database.mongodb.entities.Stock;
import org.bson.Document;

import java.util.ArrayList;

class PortfolioImpl extends Portfolio {
    DocumentDBManager dbManager;

    protected PortfolioImpl(User owner, String name, String type, DocumentDBManager dbManager) {
        composition=new ArrayList<Title>();
        this.name = name;
        this.type = type;
        this.totalInvestment=0.0;
        this.dbManager=dbManager;
        this.owner=owner;
    }

     protected PortfolioImpl(User owner,Document doc, DocumentDBManager db) {
            this(owner,doc.getString("name"), doc.getString("type"),db);

     }
    protected void LoadComposition(){
        this.composition=dbManager.loadPortfolioComposizion(this);
    }
     public void setName(String name) {
        this.name = name;

    }

     @Override
     public void setType(String type) {

     }

     public void setTotalInvestment(Double totalInvestment) {
        //TODO RESCALE
        this.totalInvestment = totalInvestment;
    }
    public boolean add(Title title){

        return dbManager.addTitleToPortfolio(this, title);

    }
     @Override
     public boolean add(Stock stock, Double share) {
         return add(new Title((Stockimpl) stock, share));
     }

     @Override
     public void setComposition(ArrayList<Title> composition) {

     }
 }
