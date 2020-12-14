package it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.implementation;

import it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.entities.*;
import it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.entities.Title;

class TitleImpl extends Title {
    protected TitleImpl(Stockimpl stock, Double share) {
        this.stock = stock;
        this.share = share;
    }


 }
