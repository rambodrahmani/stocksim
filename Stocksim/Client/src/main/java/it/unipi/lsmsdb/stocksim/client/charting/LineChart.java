package it.unipi.lsmsdb.stocksim.client.charting;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.AbstractDataset;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Line Chart implementation using a {@link JFreeChart}.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class LineChart extends Chart {
	// label of the X axis
	private final String xAxisLabel;
	
	// label of the Y axis
	private final String yAxisLabel;
	
	// data for independent variable
	private final ArrayList<LocalDate> xAxisData;
	
	// data for dependent variable
	private final ArrayList<Number> yAxisData;
	
	/**
	 * Default constructor.
	 *
	 * @param title       title of the chart {@link ApplicationFrame};
	 * @param xAxisLabel  label for the X axis;
	 * @param yAxisLabel  label for the Y axis;
	 * @param xAxisData   data for the X axis;
	 * @param yAxisData   data for the Y axis.
	 */
	public LineChart(final String title, final String xAxisLabel, final String yAxisLabel, final ArrayList<LocalDate> xAxisData,
					 final ArrayList<Number> yAxisData) throws IllegalArgumentException {
		if (xAxisData.size() != yAxisData.size()) {
			throw new IllegalArgumentException("xAxisData and yAxisData must have the same length");
		}
		this.title = title;
		this.xAxisLabel = xAxisLabel;
		this.yAxisLabel = yAxisLabel;
		this.xAxisData = xAxisData;
		this.yAxisData = yAxisData;
		applicationFrame = new JFrame(title);
	}

	/**
	 * @return the {@link DefaultCategoryDataset} obtained from the raw data.
	 */
	protected AbstractDataset createDataset() {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int i = 0; i < yAxisData.size(); i++) {
			dataset.addValue(yAxisData.get(i), "value", xAxisData.get(i));
		}

		return dataset;
	}

	/**
	 * @param dataset the {@link CategoryDataset} used to build the line chart.
	 *
	 * @return the {@link JFreeChart} created using the given {@link CategoryDataset}.
	 */
	private JFreeChart createChart(final CategoryDataset dataset) {
		final JFreeChart lineChart = ChartFactory.createLineChart(this.title, xAxisLabel, yAxisLabel, dataset, PlotOrientation.VERTICAL, true, true, false);
		return lineChart;
	}

	/**
	 * @return the {@link ChartPanel} that displays the specified chart.
	 */
	protected JPanel createPanel() {
		final JFreeChart chart = createChart((CategoryDataset) createDataset());
		return new ChartPanel(chart);
	}

	/**
	 * Developer harness test entry point.
	 *
	 * @param args command line arguments.
	 */
	public static void main(final String[] args) {
		final ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
		final ArrayList<Number> values = new ArrayList<Number>();

		for (int i = 1; i < 31; i++) {
			final LocalDate date = LocalDate.of(2020, 12, i);
			dates.add(date);
			values.add((float) Math.random());
		}

		try {
			final LineChart lineChart = ChartingFactory.getLineChart("LineChart di prova", "Asse X", "Asse Y", dates, values);
			if (lineChart != null) {
				lineChart.showChart();
			}
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
}
