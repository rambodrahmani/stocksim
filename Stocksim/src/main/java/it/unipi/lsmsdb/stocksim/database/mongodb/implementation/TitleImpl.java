package it.unipi.lsmsdb.stocksim.database.mongodb.implementation;

import it.unipi.lsmsdb.stocksim.database.mongodb.entities.Stock;
import it.unipi.lsmsdb.stocksim.database.mongodb.entities.Title;

class TitleImpl extends Title {


    public TitleImpl(Stock stock, Double share) {
        super(stock, share);
    }
}
