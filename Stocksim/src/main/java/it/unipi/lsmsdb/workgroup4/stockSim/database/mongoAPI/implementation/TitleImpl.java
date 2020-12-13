package it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.implementation;

import it.unipi.lsmdb.workgroup4.mongoAPI.entities.*;
import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.entities.Title;

class TitleImpl extends Title {
    protected TitleImpl(Stockimpl stock, Double share) {

        this.stock = stock;
        this.share = share;
    }


 }
