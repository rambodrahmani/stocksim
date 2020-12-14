package it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb;

import it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.persistence.DBManagerFactory;
import it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.persistence.DocumentDBManager;

public class Main {
    private static  final DBManagerFactory factory = DBManagerFactory.create();
    public static void main(String[] args) {
        DocumentDBManager dbManager =factory.getService();
    }
}
