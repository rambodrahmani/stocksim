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

    /**
     * Default constructor.
     * Constructs a portfolio for the given {@link ArrayList} of {@link Stock}.
     *
     * @param name user portfolio name;
     * @param stocks the {@link ArrayList} of {@link Stock} to be used.
     */
    public Portfolio(final String name, final ArrayList<Stock> stocks) {
        this.name = name;
        this.stocks = stocks;
    }

    /**
     * @return portfolio name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return portfolio stocks.
     */
    public ArrayList<Stock> getStocks() {
        return stocks;
    }

    /**
     * @return the {@link Document} to be inserted into MongoDB.
     */
    public Document getDocument() {
        // create new portfolio document for mongo db
        final Document portfolioDocument = new Document("_id", new ObjectId());
        portfolioDocument.append("name", getName());

        // add stocks to portfolio document
        final ArrayList<String> symbols = new ArrayList<>();
        for (final Stock stock : stocks) {
            symbols.add(stock.getSymbol());
        }
        portfolioDocument.append("tickers", symbols);

        return portfolioDocument;
    }
}
