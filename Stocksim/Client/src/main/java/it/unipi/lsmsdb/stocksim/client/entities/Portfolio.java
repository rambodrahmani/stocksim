package it.unipi.lsmsdb.stocksim.client.entities;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public  abstract class Portfolio {
    protected User owner;
    protected ArrayList<Title> composition;
    protected String name;
    protected String type;
    protected Double totalInvestment;

    protected  Portfolio(){
    }

    protected Portfolio(User owner, ArrayList<Title> composition, String name, String type) {
        this.owner = owner;
        this.composition = composition;
        this.name = name;
        this.type = type;
        this.totalInvestment=0.0;
        for (Title title : composition) {
            this.totalInvestment+=title.share;
        }
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Double getTotalInvestment() {
        return totalInvestment;
    }

    public abstract boolean add(Title title);

    public abstract boolean add(Stock stock, Double share);

    public User getOwner() {
        return owner;
    }

    public ArrayList<Title> getComposition() {
        return composition;
    }

    public abstract void Simulate(Date start, Date end);
}
