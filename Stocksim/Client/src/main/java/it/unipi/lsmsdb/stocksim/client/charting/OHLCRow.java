package it.unipi.lsmsdb.stocksim.client.charting;

import org.jfree.data.xy.OHLCDataItem;

import java.util.Date;

/**
 * Represents a single (open-high-low-close) data item
 * implemented using {@link Double} instead of {@link Float}.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class OHLCRow extends OHLCDataItem {
	// additional float value to save the adjusted close value
	final Number adjClose;

	/**
	 * Default constructor wrapping the driver class constructor.
	 */
	public OHLCRow(final Date date, final Float open, final Float high,
				   final Float low, final Float close, final Float volume, final Float adjClose) {
		super(date, open, high, low, close, volume);
		this.adjClose = adjClose;
	}

	/**
	 * @return Adjusted Close price.
	 */
	public Number getAdjClose() {
		return adjClose;
	}
}
