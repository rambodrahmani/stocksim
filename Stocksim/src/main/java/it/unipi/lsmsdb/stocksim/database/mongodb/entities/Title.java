package it.unipi.lsmsdb.stocksim.database.mongodb.entities;

import it.unipi.lsmsdb.stocksim.database.mongodb.entities.Stock;

public  class Title {
    protected Stock stock;
    protected Double share;
    public Stock getStock() {
        return stock;
    }

    public Double getShare() {
        return share;
    }



    public Title(Stock stock, Double share) {
        this.stock = stock;
        this.share = share;
    }
}
