/*
 * Apache Cassandra Stocksim CQL aggregation function.
 *
 * author: Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */

 /* State function to be executed for every row*/
CREATE OR REPLACE FUNCTION PeriodStateParam ( 
     /* the state, containing the aggregation result till this row */
    state map<date,frozen<map<text, float>>>,
     /* the parameter ndays indicate the duration of the period aggregation, in days */
    ndays int, data date,  
        open float,  close float , high float,  low float, volume float ,  adj_close float)
CALLED ON NULL INPUT 
RETURNS map<date,frozen<map<text, float>>> 
LANGUAGE java AS '
if (data != null) { 
    int d=0;
    Map<String, Float> statemap=null;
   
    for(d=1; d<ndays;){
        if((statemap=state.get(data.add(Calendar.DAY_OF_MONTH,d)))!=null)
            break;
        d++;
    }
    if(d==ndays){
        statemap=new HashMap<String, Float>();
        statemap.put("open", open);
        statemap.put("close", close);
        statemap.put("high", high);
        statemap.put("low", low);
        statemap.put("volume", volume);
        statemap.put("adj_close", adj_close);
        state.put(data,statemap);
    }
    else{
            if(high>statemap.get("high"))
                statemap.replace("high", high);
            if(low<statemap.get("low"))
                statemap.replace("low", low); 
            statemap.replace("volume",statemap.get("volume")+ volume);
            statemap.replace("open",open);
            state.replace(data, statemap); 
    }
} 
return state;'
 ;

/* aggregate declartation
*   this aggregation geenrate a map data structure (JSON like):
*   the key is the end date of each period of nday days, and the value is
*   another map containing the aggregate values of the period as:
        the open of the first day
        the close and adjusted close of the last day
        the maximun of the highs
        the minimum of the lows
        the sum of the volumes
*
*
*/
CREATE OR REPLACE AGGREGATE PeriodParam 
    ( int, date,float, float,float, float,float, float ) 
SFUNC PeriodStateParam
STYPE map<date,frozen<map<text, float>>>
INITCOND {}; /* no initial condition is necessary */

  /* example of usage, it can be used also with grouping by symbol */
select PeriodParam(
    20, date, open, close, high, low, volume, adj_close)
    as Period from tickers where 
    date<'2020-12-1' and date>'2020-6-10' 
    and symbol='TSLA'
