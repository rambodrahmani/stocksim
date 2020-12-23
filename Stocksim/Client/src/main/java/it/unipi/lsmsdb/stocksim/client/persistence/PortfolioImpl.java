package it.unipi.lsmsdb.stocksim.client.persistence;

import it.unipi.lsmsdb.stocksim.client.entities.Portfolio;
import it.unipi.lsmsdb.stocksim.client.entities.Stock;
import it.unipi.lsmsdb.stocksim.client.entities.Title;
import it.unipi.lsmsdb.stocksim.client.entities.User;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Date;

/**
 * portfolio implemented from the document db
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
class PortfolioImpl extends Portfolio {
     protected PortfolioImpl(final User owner, final Document doc, final DBManager dbManager) {
         super();
         this.totalInvestment = 0.0;
         this.owner = owner;
         this.name = doc.getString("name");
         this.type = doc.getString("type");
         this.composition = new ArrayList<>();
         ArrayList<Document> titles = (ArrayList<Document>) doc.get("composition");
         for (Document title : titles) {
             this.composition.add(
                     new TitleImpl(
                             title.getString("ticker"),
                             title.getDouble("share"),
                             dbManager)
             );
             this.totalInvestment += title.getDouble("share");
         }

     }

     public void setName(final String name) {
       // todo

    }

     public void setTotalInvestment(final Double totalInvestment) {
        //TODO RESCALE
        this.totalInvestment = totalInvestment;
    }
    public boolean add(final Title title){
        return add(title.getStock(), title.getShare());

    }
     @Override
     public boolean add(final Stock stock, final Double share) {
         DBManager dbm=new DBManager();
         return dbm.addStockToPortfolio(stock, share, this);
     }

     protected void setComposition(final ArrayList<Title> composition) {
            // todo
     }

    @Override
    public void Simulate(final  Date start, final Date end) {
        //todo
    }
}
