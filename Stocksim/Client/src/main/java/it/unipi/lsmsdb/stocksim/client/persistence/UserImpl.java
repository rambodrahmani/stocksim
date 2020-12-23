package it.unipi.lsmsdb.stocksim.client.persistence;

import it.unipi.lsmsdb.stocksim.client.entities.Portfolio;
import it.unipi.lsmsdb.stocksim.client.entities.User;
import org.bson.Document;
import java.util.ArrayList;

/**
 * User implemented from the document db
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class UserImpl extends User {

    protected UserImpl(final Document doc ,final DBManager dbManager) {
        super();
        this.portfolios=new ArrayList<>();
        this.username = doc.getString("username");
        this.email = doc.getString("email");
        this.name =  doc.getString("name");
        this.surname =  doc.getString("surname");
        ArrayList<Document> portList= ((ArrayList<Document>) doc.get("portfolios"));
        for (Document port : portList) {
            this.portfolios.add(new PortfolioImpl(this, port, dbManager));
        }
    }


    /**
     * create a new portfolio if possible
     * @param name the name of the new portfolio
     * @param type the type of the new portfolio
     * @return the instance of the new portfolio, null if something(s) went wrong
     */
    @Override
    public Portfolio addPortfolio(String name, String type) {
        DBManager dbManager=new DBManager();
        return  dbManager.createPortfolio(this, name, type);
    }
}
