package it.unipi.lsmsdb.stocksim.client.database;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

/**
 * User Portfolio.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class Portfolio {
    // user portfolio name
    final String name;

    // user portfolio stocks list
    private final ArrayList<Stock> stocks;

    // user portfolio stocks shares
    private ArrayList<Integer> shares;

    /**
     * Default constructor.
     * Constructs a portfolio for the given {@link ArrayList} of {@link Stock}.
     *
     * @param name user portfolio name;
     * @param stocks the {@link ArrayList} of {@link Stock} to be used.
     * @param shares the amount of shares for each {@link Stock} in the portfolio.
     */
    public Portfolio(final String name, final ArrayList<Stock> stocks, final ArrayList<Integer> shares) {
        this.name = name;
        this.stocks = stocks;
        this.shares = shares;
    }

    /**
     * @return portfolio name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return user portfolio stocks.
     */
    public ArrayList<Stock> getStocks() {
        return stocks;
    }

    /**
     * @return user portfolio stocks shares.
     */
    public ArrayList<Integer> getShares() {
        return shares;
    }

    /**
     * @param symbol the stock symbol to retrieve the share for.
     *
     * @return the portfolio share for the given stock symbol.
     */
    public int getShare(final String symbol) {
        int ret = 0;

        for (int i = 0; i < getStocks().size(); i++) {
            if (getStocks().get(i).getSymbol().equals(symbol)) {
                ret = getShares().get(i).intValue();
                break;
            }
        }

        return ret;
    }

    /**
     * @return the {@link Document} to be inserted into MongoDB.
     */
    public Document getDocument() {
        // create new portfolio document for mongo db
        final Document portfolioDocument = new Document("_id", new ObjectId());
        portfolioDocument.append("name", getName());

        // add stocks to portfolio document
        final ArrayList<Document> stocksDocuments = new ArrayList<>();
        for (int i = 0; i < getStocks().size(); i++) {
            final Document stockDocument = new Document("symbol", stocks.get(i).getSymbol());
            stockDocument.append("share", shares.get(i).intValue());
            stocksDocuments.add(stockDocument);
        }
        portfolioDocument.append("stocks", stocksDocuments);

        return portfolioDocument;
    }
}
