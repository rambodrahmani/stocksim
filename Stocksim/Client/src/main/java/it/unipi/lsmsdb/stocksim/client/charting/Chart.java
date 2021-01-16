package it.unipi.lsmsdb.stocksim.client.charting;

import javax.swing.*;

/**
 * Abstract Chart class. All charting classes must extend this.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public abstract class Chart {
	// title of the chart window
	protected String chartTitle;

	// JPanel for the chart
	protected abstract JPanel createPanel();
}
