package it.unipi.lsmsdb.stocksim.server.yfinance;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * YahooFinance API implementation.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class YahooFinance {
    /**
     * Yahoo Finance API base URL.
     */
    private final static String BASE_URL = "https://query1.finance.yahoo.com/v8/finance/chart/";

    /**
     * Ticker symbol.
     */
    private final String symbol;

    /**
     * Yahoo Finance period1 parameter (start date).
     */
    private final long period1;

    /**
     * Yahoo Finance period2 parameter (end date).
     */
    private final long period2;

    /**
     * Yahoo Finance API full URL.
     */
    private final String YFinanceURL;

    /**
     * Default constructor.
     *
     * @param symbol the ticker symbol to be used.
     * @param period1 start date.
     * @param period2 end date.
     */
    public YahooFinance(final String symbol, final long period1, final long period2) {
        this.symbol = symbol;
        this.period1 = period1;
        this.period2 = period2;

        YFinanceURL = BASE_URL + symbol + "?"
                + "period1=" + this.period1
                + "&period2=" + this.period2
                + "&interval=1d"
                + "&events=history";
    }

    /**
     * @return the full Yahoo Finance API URL.
     */
    public String getURL() {
        return YFinanceURL;
    }

    /**
     * Retrieve historical data.
     *
     * @return retrieved {@link ArrayList} if {@link YFHistoricalData}.
     *
     * @throws JSONException
     * @throws IOException
     */
    public ArrayList<YFHistoricalData> getHistoricalData() throws JSONException, IOException {
        final ArrayList<YFHistoricalData> ret = new ArrayList<>();

        final JSONObject historicalData = new JSONObject(IOUtils.toString(new URL(YFinanceURL), StandardCharsets.UTF_8));
        final JSONArray timestamp = historicalData.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONArray("timestamp");
        final JSONObject quote = historicalData.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONObject("indicators").getJSONArray("quote").getJSONObject(0);
        final JSONArray adjclose = historicalData.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONObject("indicators").getJSONArray("adjclose").getJSONObject(0).getJSONArray("adjclose");

        for (int i = 0 ; i < timestamp.length(); i++) {
            final int epochSecs = timestamp.getInt(i);
            final LocalDate updateDate = Instant.ofEpochSecond(epochSecs).atZone(ZoneId.systemDefault()).toLocalDate();
            final BigDecimal adj_close = new BigDecimal(adjclose.getDouble(i), MathContext.DECIMAL64);
            final BigDecimal close = new BigDecimal(quote.getJSONArray("close").getDouble(i), MathContext.DECIMAL64);
            final BigDecimal high = new BigDecimal(quote.getJSONArray("high").getDouble(i), MathContext.DECIMAL64);
            final BigDecimal low = new BigDecimal(quote.getJSONArray("low").getDouble(i), MathContext.DECIMAL64);
            final BigDecimal open = new BigDecimal(quote.getJSONArray("open").getDouble(i), MathContext.DECIMAL64);
            final BigDecimal volume = new BigDecimal(quote.getJSONArray("volume").getDouble(i), MathContext.DECIMAL64);

            ret.add(new YFHistoricalData(updateDate, adj_close, close, high, low, open, volume));
        }

        return ret;
    }
}
