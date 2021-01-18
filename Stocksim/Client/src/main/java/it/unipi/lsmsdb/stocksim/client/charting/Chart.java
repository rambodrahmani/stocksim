package it.unipi.lsmsdb.stocksim.client.charting;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.AbstractDataset;

import javax.swing.*;

/**
 * Abstract Chart class. All charting classes must extend this.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public abstract class Chart {
	// title of the chart window
	protected String chartTitle;

	// creates the dataset
	protected abstract AbstractDataset createDataset();

	// creates the JFreeChart using the dataset
	protected abstract JFreeChart createChart(final AbstractDataset dataset);

	// creates the JPanel with the chart
	protected abstract JPanel createPanel();
}
