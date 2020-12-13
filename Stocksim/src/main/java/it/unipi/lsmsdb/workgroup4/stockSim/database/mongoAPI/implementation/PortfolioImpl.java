package it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.implementation;
import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.entities.Portfolio;
import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.entities.Title;
import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.entities.User;
import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.persistence.DocumentDBManager;
import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.entities.Stock;

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
         return add(new TitleImpl((Stockimpl) stock, share));
     }

     @Override
     public void setComposition(ArrayList<Title> composition) {

     }
 }
