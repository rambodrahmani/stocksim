package it.unipi.lsmsdb.stocksim.client.charting;

import it.unipi.lsmsdb.stocksim.client.charting.OHLCRow;

import java.util.ArrayList;
import java.util.Date;

/**
 * Historical values data structure, wrapping a list of OHLCRows
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class HistoricalData {
    // internal data structure
    private final ArrayList<OHLCRow> rows;

    /**
     * Default constructor. Initializes the internal data structure.
     */
    public HistoricalData() {
        this.rows = new ArrayList<OHLCRow>();
    }

    /**
     * Adds a new item to the internal data structure.
     *
     * @param date
     * @param open
     * @param high
     * @param low
     * @param close
     * @param volume
     */
    public void append(final Date date, final Float open, final Float high,
                  final Float low, final Float close, final Float volume) {
        rows.add(new OHLCRow(date, open, high, low, close, volume));
    }

    /**
     * @return the internal data structure.
     */
    public ArrayList<OHLCRow> getRows() {
        return rows;
    }
}
