package it.unipi.lsmsdb.stocksim.client.entities;

import java.util.ArrayList;

/**
 * User generic data structure
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public  abstract   class User {
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

    /**
     * find a portfolio by it's name, null if it does not exists
     *
     * @param name the name of the requested portfolio
     * @return a portfolio object, can be null
     */
    public Portfolio getPortfolioByName(final String name) {
        for (Portfolio portfolio : this.portfolios) {
            if(portfolio.getName()==name)
                return portfolio;
        }
        return null;
    }

    public  abstract  Portfolio addPortfolio(String name, String Type);

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
