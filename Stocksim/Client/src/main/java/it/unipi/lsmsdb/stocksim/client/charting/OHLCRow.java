package it.unipi.lsmsdb.stocksim.client.charting;

import org.jfree.data.xy.OHLCDataItem;

import java.util.Date;

/**
 * Represents a single (open-high-low-close) data item
 * implemented using {@link Double} instead of {@link Float}.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class OHLCRow {
	// represents a single (open-high-low-close) data item
	private OHLCDataItem ohlcDataItem;

	/**
	 * Default constructor.
	 */
	public OHLCRow(final Date date, final Float open, final Float high, final Float low, final Float close, final Float volume) {
		ohlcDataItem = new OHLCDataItem(date, open, high, low, close, volume);
	}
	
	public Date getDate() {
		return ohlcDataItem.getDate();
	}
	
	public Double getOpen() {
		return ohlcDataItem.getOpen().doubleValue();
	}
	
	public Double getHigh() {
		return ohlcDataItem.getHigh().doubleValue();
	}
	
	public Double getLow() {
		return ohlcDataItem.getLow().doubleValue();
	}
	
	public Double getClose() {
		return ohlcDataItem.getClose().doubleValue();
	}
	
	public Double getVolume() {
		return ohlcDataItem.getVolume().doubleValue();
	}
}
