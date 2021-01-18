package it.unipi.lsmsdb.stocksim.client.charting;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * JFreeChart Charts factory.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class ChartingFactory {
	/**
	 * @param chartTitle title of the chart;
	 * @param names list of all the names of the slices that make up the pie;
	 * @param values list of all the values associated with slices;
	 *
	 * @return instance of a {@link PieChart} if input data is consistent (i.e. slices names
	 *         and values have the same length).
	 */
	public static PieChart getPieChart(final String chartTitle, final ArrayList<String> names, final ArrayList<Number> values) throws IllegalArgumentException
	{
		final PieChart ret = new PieChart(chartTitle, names, values);
		return ret;
	}

	/**
	 * @param chartTitle title of the chart;
	 * @param xAxisLabel label for the X axis;
	 * @param yAxisLabel label for the Y axis;
	 * @param xAxisData  data for the X axis;
	 * @param yAxisData  data for the Y axis.
	 *
	 * @return instance of a {@link LineChart} if input data is consistent (i.e.
	 *         xAxisData and yAxisData have the same length), null otherwise
	 */
	public static LineChart getLineChart(final String chartTitle, final String xAxisLabel, final String yAxisLabel,
										 final ArrayList<LocalDate> xAxisData, final ArrayList<Number> yAxisData) throws IllegalArgumentException
	{
		final LineChart ret = new LineChart(chartTitle, xAxisLabel, yAxisLabel, xAxisData, yAxisData);
		return ret;
	}

	/**
	 * Creates and returns a candle stick with the given parameters.
	 *
	 * @param chartTitle the {@link org.jfree.chart.JFreeChart} title.
	 * @param stockSymbol candle stick chart stock symbol.
	 * @param dataset historical data.
	 *
	 * @return instance of the created {@link CandlestickChart}.
	 */
	public static CandlestickChart getCandlestickChart(final String chartTitle, final String timeAxisLabel, final String valuesAxisLabel,
													   final String stockSymbol, final ArrayList<OHLCRow> dataset) {
		final CandlestickChart ret = new CandlestickChart(chartTitle, timeAxisLabel, valuesAxisLabel, stockSymbol, dataset);
		return ret;
	};

	/**
	 * Creates and returns a time series chart with the given parameters.
	 *
	 * @param chartTitle the {@link org.jfree.chart.JFreeChart} title;
	 * @param timeAxisLabel {@link org.jfree.chart.plot.XYPlot} time axis label;
	 * @param valuesAxisLabel {@link org.jfree.chart.plot.XYPlot} values axis label;
	 * @param xAxisData {@link org.jfree.chart.plot.XYPlot} time axis data;
	 * @param yAxisData {@link org.jfree.chart.plot.XYPlot} vaues axis data.
	 *
	 * @return instance of the created {@link TimeSeriesChart}.
	 */
	public static TimeSeriesChart getTimeSeriesChart(final String chartTitle, final String timeAxisLabel, final String valuesAxisLabel,
													 final ArrayList<LocalDate> xAxisData, final ArrayList<Number> yAxisData) {
		final TimeSeriesChart ret = new TimeSeriesChart(chartTitle, timeAxisLabel, valuesAxisLabel, xAxisData, yAxisData);
		return ret;
	}

	public static BarChart getBarChart(final String chartTitle, final String categoryAxisLabel, final String valueAxisLabel) {
		final BarChart ret = new BarChart(chartTitle, categoryAxisLabel, valueAxisLabel);
		return ret;
	}
}
