package it.unipi.lsmsdb.stocksim.client.persistence;


import it.unipi.lsmsdb.stocksim.client.entities.Stock;
import it.unipi.lsmsdb.stocksim.client.entities.Title;
/**
 * Title implemented from the document db
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
class TitleImpl extends Title {


    public TitleImpl(final Stock stock, final Double share) {
        super(stock, share);
    }

    public TitleImpl(final String ticker, final Double share, final DBManager dbManager) {
        super();
        this.stock=dbManager.getStockByTicker(ticker); // retrieve stock information
        this.share= share;
    }
}
