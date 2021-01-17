/*
 * Apache Cassandra Stocksim CQL aggregation function.
 *
 * author: Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */

 /**
  * AggregateOHLC State function.
  * Executed for every row.
  */
CREATE OR REPLACE FUNCTION AggregateOHLCSFunc (
    state map<date, frozen<map<text, float>>>,
    ndays int, data date, open float,  close float , high float,  low float, volume float ,  adj_close float
)
CALLED ON NULL INPUT
RETURNS map<date, frozen<map<text, float>>>
LANGUAGE java
AS '
    if (data != null) {
        int d = 0;
        Map<String, Float> statemap = null;

        for (d = 1; d < ndays;) {
            if((statemap=state.get(data.add(Calendar.DAY_OF_MONTH,d)))!=null)
                break;
            d++;
        }
        if (d == ndays) {
            statemap=new HashMap<String, Float>();
            statemap.put("open", open);
            statemap.put("close", close);
            statemap.put("high", high);
            statemap.put("low", low);
            statemap.put("volume", volume);
            statemap.put("adj_close", adj_close);
            state.put(data,statemap);
        }
        else {
                if (high > statemap.get("high"))
                    statemap.replace("high", high);
                if (low < statemap.get("low"))
                    statemap.replace("low", low);
                statemap.replace("volume", statemap.get("volume") + volume);
                statemap.replace("open", open);
                state.replace(data, statemap);
        }
    }
    return state;
';

/**
 * AGGREGATE DECLARATION
 * this aggregation generates a map data structure (JSON like) where
 * the key is the end date of each time interval of ndays days, and the value is
 * a map containing the aggregation of the OHLC values of the period as follows:
 *  - the open price is the one of the first day of the time interval;
 *  - the close and adjusted close prices are the ones of the last day;
 *  - the high price is the highest price of the time interval;
 *  - the low price is the minimum price of the time interval;
 *  - the volume is the sum of the volumes;
 */
CREATE OR REPLACE AGGREGATE AggregateOHLC(int, date, float, float, float, float, float, float)
SFUNC AggregateOHLCSFunc
STYPE map<date, frozen<map<text, float>>>
INITCOND {}; /* no initial condition is necessary */

/**
 * Usage example.
 * Aggregates the OHLC prices for the TSLA (Tesla) stock on time intervals of
 * 20 days starting from 2020-06-10 till 2020-12-01.
 * It can also be used with the group by directive.
 */
SELECT AggregateOHLC(20, date, open, close, high, low, volume, adj_close)
AS OHLCAggregation from tickers WHERE 
date < '2020-12-01' and date > '2020-06-10'
AND symbol = 'TSLA';
