package it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.entities;

import java.util.ArrayList;

public abstract  class User {

    protected  String username;
    protected String email;
    protected  String name;
    protected  String surname;
    protected ArrayList<Portfolio> portfolios;
    public abstract ArrayList<Portfolio> getPortfolios();
    public abstract Portfolio addPortfolio(String name, String Type);
}
