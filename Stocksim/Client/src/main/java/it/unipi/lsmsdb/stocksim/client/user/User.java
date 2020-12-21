package it.unipi.lsmsdb.stocksim.client.user;

import java.util.ArrayList;
/**
 * User generic data structure
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public    class User {

    protected  String username;
    protected String email;
    protected  String name;
    protected  String surname;
    protected ArrayList<Portfolio> portfolios;

    protected User() {

    }

    public User(String username, String email, String name, String surname, ArrayList<Portfolio> portfolios) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.portfolios = portfolios;
    }

    public  ArrayList<Portfolio> getPortfolios(){
        return portfolios;
    }
    public  Portfolio getPortfolioByName(){
        return null;
    }
    public  Portfolio addPortfolio(String name, String Type){
        return  null;
    }


    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
