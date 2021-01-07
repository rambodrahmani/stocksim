package it.unipi.lsmsdb.stocksim.client.user;



import it.unipi.lsmsdb.stocksim.client.charting.OHLCRow;
import it.unipi.lsmsdb.stocksim.client.database.Stock;

import java.util.ArrayList;
import java.util.Date;

/**
 * Historical values data structure, wrapping a list of OHLCRows
 *
 *  @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class HistoricalDataset {
    ArrayList<OHLCRow> rows; // actual dataset

    protected ArrayList<OHLCRow> getRows() {
        return rows;
    }

    public HistoricalDataset() {
        this.rows = new ArrayList<OHLCRow>();
    }

    public void append(final Date date, final Float open, final Float high,
                  final Float low, final Float close, final Float volume){
        rows.add(new OHLCRow(date, open, high, low, close, volume));
    }
}
