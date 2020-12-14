package it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.implementation;

import it.unipi.lsmdb.workgroup4.mongoAPI.entities.*;
import it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.entities.Session;
import it.unipi.lsmsdb.workgroup4.stocksim.database.mongodb.entities.User;

class SessionImpl extends Session {

     protected SessionImpl(User logged_user) {
         this.logged_user = logged_user;
     }
 }
