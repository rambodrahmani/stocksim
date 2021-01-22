package it.unipi.lsmsdb.stocksim.client.charting;

import java.util.ArrayList;
import java.util.Date;

/**
 * Weighted Average Historical values data structure, wrapping a list of OHLCRows.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class AvgHistoricalData {
    // historical datas to average
    private final ArrayList<HistoricalData> historicalDatas;

    // weights for the average
    private final ArrayList<Integer> shares;

    // hold weighted avg historical data
    private final ArrayList<Date> dates = new ArrayList<>();
    private final ArrayList<Number> opens = new ArrayList<>();
    private final ArrayList<Number> highs = new ArrayList<>();
    private final ArrayList<Number> lows = new ArrayList<>();
    private final ArrayList<Number> closes = new ArrayList<>();
    private final ArrayList<Number> volumes = new ArrayList<>();
    private final ArrayList<Number> adjCloses = new ArrayList<>();

    // internal data structure
    private final HistoricalData historicalData;

    /**
     * Default constructor. Initializes the internal data structure.
     *
     * @param historicalDatas the array of {@link HistoricalData} to average.
     * @param shares weights for the average.
     */
    public AvgHistoricalData(final ArrayList<HistoricalData> historicalDatas, final ArrayList<Integer> shares) {
        this.historicalDatas = historicalDatas;
        this.shares = shares;
        historicalData = new HistoricalData();

        // generate average historical data
        generateAverageHistoricalData();
    }

    /**
     * Calculates the weighted average historical data.
     */
    private void generateAverageHistoricalData() {
        // different stock have different historical data availability
        int availableData = Integer.MAX_VALUE;
        for (int j = 0; j < historicalDatas.size(); j++) {
            final int size = historicalDatas.get(j).getRows().size();
            if (availableData > size) {
                availableData = size;
            }
        }

        // calculate total shares sum
        float totalShares = 0;
        for (final Integer share : shares) {
            totalShares += share;
        }

        // average historical data using shares as weights
        for (int j = 0; j < historicalDatas.size(); j++) {
            final ArrayList<OHLCRow> ohlcRows = historicalDatas.get(j).getRows();
            if (j == 0) {
                for (int i = 0; i < availableData; i++) {
                    dates.add(ohlcRows.get(i).getDate());
                    opens.add(shares.get(j) * ohlcRows.get(i).getOpen().floatValue());
                    highs.add(shares.get(j) * ohlcRows.get(i).getHigh().floatValue());
                    lows.add(shares.get(j) * ohlcRows.get(i).getLow().floatValue());
                    closes.add(shares.get(j) * ohlcRows.get(i).getClose().floatValue());
                    volumes.add(shares.get(j) * ohlcRows.get(i).getVolume().floatValue());
                    adjCloses.add(shares.get(j) * ohlcRows.get(i).getAdjClose().floatValue());
                }
            } else {
                for (int i = 0; i < availableData; i++) {
                    opens.set(i, opens.get(i).floatValue() + (shares.get(j) * ohlcRows.get(i).getOpen().floatValue()));
                    highs.set(i, highs.get(i).floatValue() + (shares.get(j) * ohlcRows.get(i).getHigh().floatValue()));
                    lows.set(i, lows.get(i).floatValue() + (shares.get(j) * ohlcRows.get(i).getLow().floatValue()));
                    closes.set(i, closes.get(i).floatValue() + (shares.get(j) * ohlcRows.get(i).getClose().floatValue()));
                    volumes.set(i, volumes.get(i).floatValue() + (shares.get(j) * ohlcRows.get(i).getVolume().floatValue()));
                    adjCloses.set(i, adjCloses.get(i).floatValue() + (shares.get(j) * ohlcRows.get(i).getAdjClose().floatValue()));
                }
            }
        }

        for (int i = 0; i < dates.size(); i++) {
            historicalData.append(
                    dates.get(i),
                    opens.get(i).floatValue()/totalShares,
                    highs.get(i).floatValue()/totalShares,
                    lows.get(i).floatValue()/totalShares,
                    closes.get(i).floatValue()/totalShares,
                    volumes.get(i).floatValue()/totalShares,
                    adjCloses.get(i).floatValue()/totalShares
            );
        }
    }

    /**
     * @return HistoricalData obtained calculating the weighted average.
     */
    public HistoricalData getHistoricalData() {
        return historicalData;
    }
}
