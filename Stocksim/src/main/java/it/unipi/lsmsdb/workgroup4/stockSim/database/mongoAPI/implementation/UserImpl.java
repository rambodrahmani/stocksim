package it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.implementation;

import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.entities.Portfolio;
import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.entities.User;
import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.persistence.DocumentDBManager;

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
    public int loadPortfolios(){

        portfolios= dbManager.loadPortfolios(this);
        return portfolios.size();
    }

    @Override
    public ArrayList<Portfolio> getPortfolios() {
        return null;
    }

    @Override
    public Portfolio addPortfolio(String name, String type) {
        Portfolio p=dbManager.createPortfolio(this, name,type);
        this.portfolios.add(p);
        return p;
    }


}
