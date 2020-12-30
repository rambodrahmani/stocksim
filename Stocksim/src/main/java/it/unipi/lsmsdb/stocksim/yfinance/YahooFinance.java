package it.unipi.lsmsdb.stocksim.yfinance;

import it.unipi.lsmsdb.stocksim.util.Util;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    // Yahoo Finance v7 API base URL
    private final static String BASE_URL_V7 = "https://query1.finance.yahoo.com/v7/finance/quote?lang=en-US&symbols=";

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
    private final String V7URL;

    // Yahoo Finance v8 API full URL
    private final String V8URL;

    // Yahoo Finance v10 API full URL with summaryDetail module
    private final String V10URLSummaryDetail;

    // Yahoo Finance v10 API full URL with Asset Profile module
    private final String V10URLAssetProfile;

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

        V7URL = BASE_URL_V7 + symbol;

        V8URL = BASE_URL_V8 + symbol + "?"
                + "period1=" + this.period1
                + "&period2=" + this.period2
                + "&interval=1d"
                + "&events=history";

        V10URLSummaryDetail = BASE_URL_V10 + symbol + "?"
                + "modules=summaryDetail";

        V10URLAssetProfile = BASE_URL_V10 + symbol + "?"
                + "modules=assetProfile";
    }

    /**
     * @return the full Yahoo Finance v7 API URL.
     */
    public String getV7URL() {
        return V7URL;
    }

    /**
     * @return the full Yahoo Finance v8 API URL.
     */
    public String getV8URL() {
        return V8URL;
    }

    /**
     * @return the full Yahoo Finance v10 API summary detail URL.
     */
    public String getV10URLSummaryDetail() {
        return V10URLSummaryDetail;
    }

    /**
     * @return the full Yahoo Finance v10 API asset profile URL.
     */
    public String getV10URLAssetProfile() {
        return V10URLAssetProfile;
    }

    /**
     * Retrieves summary details data.
     *
     * @return retrieved {@link YFAssetProfile}.
     *
     * @throws JSONException
     * @throws IOException
     */
    public YFAssetProfile getAssetProfile() throws JSONException {
        final YFAssetProfile ret = new YFAssetProfile();

        try {
            // fetch summary details using Yahoo Finance v7 API
            final JSONObject quoteResponse = new JSONObject(IOUtils.toString(new URL(getV7URL()), StandardCharsets.UTF_8));
            final JSONObject quoteResponseResult = quoteResponse.getJSONObject("quoteResponse").getJSONArray("result").getJSONObject(0);

            if (quoteResponseResult.has("currency")) {
                ret.setCurrency(quoteResponseResult.getString("currency"));
            } else {
                ret.setCurrency("");
            }

            if (quoteResponseResult.has("shortName")) {
                ret.setShortName(quoteResponseResult.getString("shortName"));
            } else {
                ret.setShortName("");
            }

            if (quoteResponseResult.has("longName")) {
                ret.setLongName(quoteResponseResult.getString("longName"));
            } else {
                ret.setLongName("");
            }

            if (quoteResponseResult.has("exchangeTimezoneName")) {
                ret.setExchangeTimezoneName(quoteResponseResult.getString("exchangeTimezoneName"));
            } else {
                ret.setExchangeTimezoneName("");
            }

            if (quoteResponseResult.has("exchangeTimezoneShortName")) {
                ret.setExchangeTimezoneShortName(quoteResponseResult.getString("exchangeTimezoneShortName"));
            } else {
                ret.setExchangeTimezoneShortName("");
            }

            if (quoteResponseResult.has("quoteType")) {
                ret.setQuoteType(quoteResponseResult.getString("quoteType"));
            } else {
                ret.setQuoteType("");
            }

            if (quoteResponseResult.has("symbol")) {
                ret.setSymbol(quoteResponseResult.getString("symbol"));
            } else {
                ret.setSymbol("");
            }

            if (quoteResponseResult.has("market")) {
                ret.setMarket(quoteResponseResult.getString("market"));
            } else {
                ret.setMarket("");
            }

            if (quoteResponseResult.has("marketCap")) {
                ret.setMarketCap(quoteResponseResult.getDouble("marketCap"));
            } else {
                ret.setMarketCap(0);
            }

            if (quoteResponseResult.has("trailingPE")) {
                ret.setTrailingPE(quoteResponseResult.getDouble("trailingPE"));
            } else {
                ret.setTrailingPE(0);
            }
        } catch (final IOException e) {
            Util.println("Yahoo Finance Quotes not found. Some information will be missing.");
        }

        try {
            // fetch summary details using Yahoo Finance v10 API
            final JSONObject quoteSummary = new JSONObject(IOUtils.toString(new URL(getV10URLAssetProfile()), StandardCharsets.UTF_8));
            final JSONObject assetProfile = quoteSummary.getJSONObject("quoteSummary").getJSONArray("result").getJSONObject(0).getJSONObject("assetProfile");

            if (assetProfile.has("website")) {
                ret.setWebsite(assetProfile.getString("website"));
                ret.setLogoURL("https://logo.clearbit.com/" + ret.getWebsite().substring(11));
            } else {
                ret.setWebsite("");
                ret.setLogoURL("");
            }

            if (assetProfile.has("sector")) {
                ret.setSector(assetProfile.getString("sector"));
            } else {
                ret.setSector("");
            }

            if (assetProfile.has("city")) {
                ret.setCity(assetProfile.getString("city"));
            } else {
                ret.setCity("");
            }

            if (assetProfile.has("website")) {
                ret.setWebsite(assetProfile.getString("website"));
            } else {
                ret.setWebsite("");
            }

            if (assetProfile.has("industry")) {
                ret.setIndustry(assetProfile.getString("industry"));
            } else {
                ret.setIndustry("");
            }

            if (assetProfile.has("longBusinessSummary")) {
                ret.setLongBusinessSummary(assetProfile.getString("longBusinessSummary"));
            } else {
                ret.setLongBusinessSummary("");
            }

            if (assetProfile.has("state")) {
                ret.setState(assetProfile.getString("state"));
            } else {
                ret.setState("");
            }

            if (assetProfile.has("country")) {
                ret.setCountry(assetProfile.getString("country"));
            } else {
                ret.setCountry("");
            }

            if (assetProfile.has("phone")) {
                ret.setPhone(assetProfile.getString("phone"));
            } else {
                ret.setPhone("");
            }

            if (assetProfile.has("address")) {
                ret.setAddress(assetProfile.getString("address"));
            } else {
                ret.setAddress("");
            }
        } catch (final IOException e) {
            Util.println("Yahoo Finance Asset Profile not found. Some information will be missing.");
        }

        return ret;
    }

    /**
     * Retrieves summary update data.
     *
     * @return retrieved {@link YFSummaryData}.
     *
     * @throws JSONException
     * @throws IOException
     */
    public YFSummaryData getSummaryData() throws JSONException, IOException {
        final YFSummaryData ret = new YFSummaryData();

        // fetch summary details for market capitalization and trailing PE
        final JSONObject summaryDetails = new JSONObject(IOUtils.toString(new URL(getV10URLSummaryDetail()), StandardCharsets.UTF_8));
        final JSONObject summaryDetail = summaryDetails.getJSONObject("quoteSummary").getJSONArray("result").getJSONObject(0).getJSONObject("summaryDetail");

        // check if the required field is available
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

        // check if the required field is available
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
        final JSONObject historicalData = new JSONObject(IOUtils.toString(new URL(getV8URL()), StandardCharsets.UTF_8));
        final JSONArray timestamp = historicalData.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONArray("timestamp");
        final JSONObject quote = historicalData.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONObject("indicators").getJSONArray("quote").getJSONObject(0);
        final JSONArray adjclose = historicalData.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONObject("indicators").getJSONArray("adjclose").getJSONObject(0).getJSONArray("adjclose");

        for (int i = 0 ; i < timestamp.length(); i++) {
            final int epochSecs = timestamp.getInt(i);
            final LocalDate updateDate = Instant.ofEpochSecond(epochSecs).atZone(ZoneId.systemDefault()).toLocalDate();

            double adj_close = 0;
            if (!adjclose.isNull(i)) {
                adj_close = adjclose.getDouble(i);
            }

            double close = 0;
            if (!quote.getJSONArray("close").isNull(i)) {
                close = quote.getJSONArray("close").getDouble(i);
            }

            double high = 0;
            if (!quote.getJSONArray("high").isNull(i)) {
                high = quote.getJSONArray("high").getDouble(i);
            }

            double low = 0;
            if (!quote.getJSONArray("low").isNull(i)) {
                low = quote.getJSONArray("low").getDouble(i);
            }

            double open = 0;
            if (!quote.getJSONArray("open").isNull(i)) {
                open = quote.getJSONArray("open").getDouble(i);
            }

            double volume = 0;
            if (!quote.getJSONArray("volume").isNull(i)) {
                volume = quote.getJSONArray("volume").getDouble(i);
            }

            ret.add(new YFHistoricalData(updateDate, adj_close, close, high, low, open, volume));
        }

        return ret;
    }
}
