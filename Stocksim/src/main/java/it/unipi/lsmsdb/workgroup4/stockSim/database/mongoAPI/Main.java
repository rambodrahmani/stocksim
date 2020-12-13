package it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI;

import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.persistence.DBManagerFactory;
import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.persistence.DocumentDBManager;

public class Main {
    private static  final DBManagerFactory factory = DBManagerFactory.create();
    public static void main(String[] args) {
        DocumentDBManager dbManager =factory.getService();
    }
}
