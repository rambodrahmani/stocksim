package it.unipi.lsmsdb.stocksim.database.mongodb.implementation;

import it.unipi.lsmsdb.stocksim.database.mongodb.entities.Session;
import it.unipi.lsmsdb.stocksim.database.mongodb.entities.User;

class SessionImpl extends Session {

     protected SessionImpl(User logged_user) {
         this.logged_user = logged_user;
     }
 }
