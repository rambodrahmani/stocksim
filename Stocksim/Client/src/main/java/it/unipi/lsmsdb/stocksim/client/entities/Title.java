package it.unipi.lsmsdb.stocksim.client.entities;

/**
 * Title generic data structure
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public  class Title { //todo probably nothing here
    protected Stock stock;
    protected Double share;

    public Stock getStock() {
        return stock;
    }

    public Double getShare() {
        return share;
    }

    public Title(Stock stock, Double share) {
        this.stock = stock;
        this.share = share;
    }

    protected Title(){}
}
