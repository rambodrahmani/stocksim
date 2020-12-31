package it.unipi.lsmsdb.stocksim.client.charting;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * LineChart implementation.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class LineChart extends Chart {
	
	// label of the X axis
	private final String xAxisLabel;
	
	// label of the Y axis
	private final String yAxisLabel;
	
	// data for independent variable
	private final LocalDate[] xAxisData;
	
	// data for dependent variable
	private final Number[] yAxisData;
	
	/**
	 * Default constructor.
	 *
	 * @param title       title of the chart {@link ApplicationFrame};
	 * @param xAxisLabel  label for the X axis;
	 * @param yAxisLabel  label for the Y axis;
	 * @param xAxisData   data for the X axis;
	 * @param yAxisData   data for the Y axis.
	 */
	public LineChart(final String title, final String xAxisLabel, final String yAxisLabel,
	             final ArrayList<LocalDate> xAxisData, final ArrayList<Number> yAxisData) {
		if (xAxisData.size() != yAxisData.size()) {
			throw new IllegalArgumentException("xAxisData and yAxisData must have the same length");
		}
		this.title = title;
		this.xAxisLabel = xAxisLabel;
		this.yAxisLabel = yAxisLabel;
		this.xAxisData = xAxisData.toArray(new LocalDate[xAxisData.size()]);
		this.yAxisData = yAxisData.toArray(new Number[yAxisData.size()]);
		applicationFrame = new ApplicationFrame(title);
	}

	/**
	 * @return the {@link DefaultCategoryDataset} obtained from the raw data.
	 */
	protected AbstractDataset createDataset() {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int i = 0; i < yAxisData.length; i++) {
			dataset.addValue(yAxisData[i], "value", xAxisData[i]);
		}

		return dataset;
	}
	
	private JFreeChart createChart(CategoryDataset dataset){
		JFreeChart lineChart = ChartFactory.createLineChart(
				this.title,
				xAxisLabel,
				yAxisLabel,
				dataset,
				PlotOrientation.VERTICAL,
				true, true, false);
		
		return lineChart;
	}
	
	protected JPanel createPanel() {
		JFreeChart chart = createChart((CategoryDataset) createDataset());
		return new ChartPanel(chart);
	}
	
	public static void main(String[] args) {
		ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
		ArrayList<Number> values = new ArrayList<Number>();
		for (int i = 1; i < 31; i++) {
			LocalDate date = LocalDate.of(2020, 12, i);
			dates.add(date);
			
			values.add((float) Math.random());
		}
		
		LineChart c = it.unipi.lsmsdb.stocksim.client.charting.ChartFactory.getLineChart("prova",
				"Asse X",
				"Asse Y",
				dates,
				values
		);
		
		if (c != null) {
			c.showChart();
		}
	}
}
