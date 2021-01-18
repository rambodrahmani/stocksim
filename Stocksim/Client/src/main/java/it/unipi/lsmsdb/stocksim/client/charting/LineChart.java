package it.unipi.lsmsdb.stocksim.client.charting;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.AbstractDataset;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Line Chart implementation using {@link JFreeChart}.
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
	 * @param chartTitle {@link Chart} title;
	 * @param xAxisLabel label for the X axis;
	 * @param yAxisLabel label for the Y axis;
	 * @param xAxisData data for the X axis;
	 * @param yAxisData data for the Y axis.
	 */
	protected LineChart(final String chartTitle, final String xAxisLabel, final String yAxisLabel,
					    final ArrayList<LocalDate> xAxisData, final ArrayList<Number> yAxisData) throws IllegalArgumentException {
		if (xAxisData.size() != yAxisData.size()) {
			throw new IllegalArgumentException("xAxisData and yAxisData must have the same length");
		}
		this.chartTitle = chartTitle;
		this.xAxisLabel = xAxisLabel;
		this.yAxisLabel = yAxisLabel;
		this.xAxisData = xAxisData;
		this.yAxisData = yAxisData;
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
	protected JFreeChart createChart(final AbstractDataset dataset) {
		final JFreeChart ret = ChartFactory.createLineChart(chartTitle, xAxisLabel, yAxisLabel, (CategoryDataset)dataset,
				PlotOrientation.VERTICAL, true, true, false);
		return ret;
	}

	/**
	 * @return the {@link ChartPanel} that displays the specified chart.
	 */
	@Override
	protected JPanel createPanel() {
		final JFreeChart chart = createChart(createDataset());
		return new ChartPanel(chart);
	}
}
