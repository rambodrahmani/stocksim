package it.unipi.lsmsdb.stocksim.database.mongodb.entities;

import java.util.ArrayList;

public abstract  class User {

    protected  String username;
    protected String email;
    protected  String name;
    protected  String surname;
    protected ArrayList<Portfolio> portfolios;
    public abstract ArrayList<Portfolio> getPortfolios();
    public abstract Portfolio addPortfolio(String name, String Type);

    protected void setPortfolios(ArrayList<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
