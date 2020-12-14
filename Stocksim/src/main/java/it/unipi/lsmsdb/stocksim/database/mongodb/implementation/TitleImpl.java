package it.unipi.lsmsdb.stocksim.database.mongodb.implementation;

import it.unipi.lsmsdb.stocksim.database.mongodb.entities.Title;

class TitleImpl extends Title {
    protected TitleImpl(Stockimpl stock, Double share) {
        this.stock = stock;
        this.share = share;
    }


 }
