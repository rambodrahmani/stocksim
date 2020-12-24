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
    // Yahoo Finance v8 API base URL
    private final static String BASE_URL_V8 = "https://query1.finance.yahoo.com/v8/finance/chart/";

    // Yahoo Finance v10 API base URL
    private final static String BASE_URL_V10 = "https://query1.finance.yahoo.com/v10/finance/quoteSummary/";

    // Ticker symbol
    private final String symbol;

    // Yahoo Finance period1 parameter (start date)
    private final long period1;

    // Yahoo Finance period2 parameter (end date)
    private final long period2;

    // Yahoo Finance v8 API full URL
    private final String V8URL;

    // Yahoo Finance v10 API full URL
    private final String V10URL;

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

        V8URL = BASE_URL_V8 + symbol + "?"
                + "period1=" + this.period1
                + "&period2=" + this.period2
                + "&interval=1d"
                + "&events=history";

        V10URL = BASE_URL_V10 + symbol + "?"
                + "modules=summaryDetail";
    }

    /**
     * @return the full Yahoo Finance v8 API URL.
     */
    public String getV8URL() {
        return V8URL;
    }

    /**
     * @return the full Yahoo Finance v10 API URL.
     */
    public String getV10URL() {
        return V10URL;
    }

    /**
     * Retrieves summary details data.
     *
     * @return retrieved {@link YFSummaryData}.
     *
     * @throws JSONException
     * @throws IOException
     */
    public YFSummaryData getSummaryData() throws JSONException, IOException {
        final YFSummaryData ret = new YFSummaryData();

        // fetch summary details for market capitalization and trailing PE
        final JSONObject summaryDetails = new JSONObject(IOUtils.toString(new URL(V10URL), StandardCharsets.UTF_8));
        final JSONObject summaryDetail = summaryDetails.getJSONObject("quoteSummary").getJSONArray("result").getJSONObject(0).getJSONObject("summaryDetail");

        if (summaryDetail.has("trailingPE")) {
            final JSONObject trailingPE = summaryDetail.getJSONObject("trailingPE");
            if (summaryDetail.has("raw")) {
                ret.setTrailingPE(trailingPE.getDouble("raw"));
            } else {
                ret.setTrailingPE(0);
            }
        } else {
            ret.setTrailingPE(0);
        }

        if (summaryDetail.has("marketCap")) {
            final JSONObject marketCap = summaryDetail.getJSONObject("marketCap");
            if (marketCap.has("raw")) {
                ret.setMarketCap(marketCap.getDouble("raw"));
            } else {
                ret.setMarketCap(0);
            }
        } else {
            ret.setMarketCap(0);
        }

        return ret;
    }

    /**
     * Retrieves historical data.
     *
     * @return retrieved {@link ArrayList} of {@link YFHistoricalData}.
     *
     * @throws JSONException
     * @throws IOException
     */
    public ArrayList<YFHistoricalData> getHistoricalData() throws JSONException, IOException {
        final ArrayList<YFHistoricalData> ret = new ArrayList<>();

        // fetch historical data
        final JSONObject historicalData = new JSONObject(IOUtils.toString(new URL(V8URL), StandardCharsets.UTF_8));
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
