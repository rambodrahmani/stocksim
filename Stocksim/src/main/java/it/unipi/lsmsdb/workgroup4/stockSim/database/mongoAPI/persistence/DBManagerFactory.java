package it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.persistence;

import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.implementation.MongoDBManagerImpl;

public class DBManagerFactory {
        DBManagerFactory(){ }
        public static DBManagerFactory create(){ return new DBManagerFactory();}
        public DocumentDBManager getService(){
            return (DocumentDBManager) new MongoDBManagerImpl();
        }

}
