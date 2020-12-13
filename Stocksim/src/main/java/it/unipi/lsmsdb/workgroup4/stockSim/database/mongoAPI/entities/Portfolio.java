package it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.entities;

import java.util.ArrayList;

public abstract class Portfolio {
    protected User owner;
    protected ArrayList<Title> composition;
    protected String name;
    protected String type;
    protected Double totalInvestment;


    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Double getTotalInvestment() {
        return totalInvestment;
    }

    public abstract void setName(String name);

    public abstract void setType(String type);

    public abstract  void setTotalInvestment(Double totalInvestment);
    public abstract boolean add(Title title);
    public abstract boolean add(Stock stock, Double share);

    public User getOwner() {
        return owner;
    }

    public ArrayList<Title> getComposition() {
        return composition;
    }

    public abstract void setComposition(ArrayList<Title> composition);
}
