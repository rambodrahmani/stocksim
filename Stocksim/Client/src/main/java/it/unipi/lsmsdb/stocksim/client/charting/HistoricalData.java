package it.unipi.lsmsdb.stocksim.client.charting;

import java.util.ArrayList;
import java.util.Date;

/**
 * Historical values data structure, wrapping a list of OHLCRows.
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
     * @param date the end date of each time interval;
     * @param open price of the first day of the time interval;
     * @param high the highest price of the time interval;
     * @param low the lowest price of the time interval
     * @param close price of the last day of the time interval;
     * @param volume sum of the volumes of the time interval;
     * @param adjClose price of the last day of the time interval.
     */
    public void append(final Date date, final Float open, final Float high,
                  final Float low, final Float close, final Float volume, final Float adjClose) {
        rows.add(new OHLCRow(date, open, high, low, close, volume, adjClose));
    }

    /**
     * @return the internal data structure.
     */
    public ArrayList<OHLCRow> getRows() {
        return rows;
    }
}
