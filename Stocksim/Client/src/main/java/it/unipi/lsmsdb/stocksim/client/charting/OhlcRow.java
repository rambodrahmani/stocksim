package it.unipi.lsmsdb.stocksim.client.charting;

import org.jfree.data.xy.OHLCDataItem;

import java.time.LocalDate;
import java.util.Date;

public class OhlcRow {
	
	private OHLCDataItem item;
	
	public OhlcRow(Date date,
	               Float open,
	               Float high,
	               Float low,
	               Float close,
	               Float volume) {
		item = new OHLCDataItem(date, open, high, low, close, volume);
	}
	
	public Date getDate(){
		return item.getDate();
	}
	
	public Double getOpen(){
		return item.getOpen().doubleValue();
	}
	
	public Double getHigh(){
		return item.getHigh().doubleValue();
	}
	
	public Double getLow(){
		return item.getLow().doubleValue();
	}
	
	public Double getClose(){
		return item.getClose().doubleValue();
	}
	
	public Double getVolume(){
		return item.getVolume().doubleValue();
	}
}
