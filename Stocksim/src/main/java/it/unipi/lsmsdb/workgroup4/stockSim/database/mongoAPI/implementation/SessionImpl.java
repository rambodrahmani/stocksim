package it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.implementation;

import it.unipi.lsmdb.workgroup4.mongoAPI.entities.*;
import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.entities.Session;
import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.entities.User;

class SessionImpl extends Session {

     protected SessionImpl(User logged_user) {
         this.logged_user = logged_user;
     }
 }
