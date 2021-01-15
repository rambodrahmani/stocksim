package it.unipi.lsmsdb.stocksim.client.charting;

import org.jfree.data.xy.OHLCDataItem;

import java.util.Date;

/**
 * Represents a single (open-high-low-close) data item
 * implemented using {@link Double} instead of {@link Float}.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class OHLCRow extends OHLCDataItem{
	/**
	 * Default constructor wrapping the driver class constructor
	 */
	public OHLCRow(final Date date, final Float open, final Float high,
				   final Float low, final Float close, final Float volume) {
		super(date, open, high, low, close, volume);
	}
}
