package it.unipi.lsmsdb.stocksim.client.charting;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.swing.*;
import java.util.ArrayList;

/**
 * PieChart implementation.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class PieChart extends Chart {
	// list of all the names of the slices that make up the pie
	private final ArrayList<String> names;
	
	// list of all the values of the slices that make up the pie
	private final ArrayList<Number> values;
	
	/**
	 * Default constructor.
	 *
	 * @param chartTitle  title of the {@link ApplicationFrame} chart;
	 * @param names list of the names of the slices that make up the pie;
	 * @param values list of the values of the slices that make up the pie.
	 */
	public PieChart(final String chartTitle, final ArrayList<String> names, final ArrayList<Number> values) throws IllegalArgumentException {
		if (names.size() != values.size()) {
			throw new IllegalArgumentException("Slices names and values must have the same length.");
		}

		this.chartTitle = chartTitle;
		this.names = names;
		this.values = values;
	}

	/**
	 * @return the {@link DefaultPieDataset} obtained from the raw data.
	 */
	protected AbstractDataset createDataset() {
		final DefaultPieDataset dataset = new DefaultPieDataset();

		for (int i = 0; i < names.size(); i++) {
			dataset.setValue(names.get(i), values.get(i));
		}

		return dataset;
	}

	/**
	 * @param dataset the {@link PieDataset} used to build the pie chart.
	 *
	 * @return the {@link JFreeChart} created using the given {@link PieDataset}.
	 */
	private JFreeChart createChart(final PieDataset dataset) {
		final JFreeChart pieChart = ChartFactory.createPieChart(this.chartTitle, dataset, true, true, false);
		return pieChart;
	}

	/**
	 * @return the {@link ChartPanel} that displays the specified chart.
	 */
	protected JPanel createPanel() {
		final JFreeChart chart = createChart((PieDataset) createDataset());
		return new ChartPanel(chart);
	}
}