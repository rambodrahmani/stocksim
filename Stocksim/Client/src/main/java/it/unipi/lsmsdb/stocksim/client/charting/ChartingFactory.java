package it.unipi.lsmsdb.stocksim.client.charting;

import org.jfree.chart.ui.ApplicationFrame;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * JFreeChart Charts factory.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class ChartingFactory {

	/**
	 * Creates and returns a candle stick with the given parameters.
	 *
	 * @param title       {@link ApplicationFrame} window title.
	 * @param stockSymbol candle stick chart stock symbol.
	 * @param dataset     historical data.
	 *
	 * @return            instance of the created {@link CandlestickChart}.
	 */
	public static CandlestickChart getCandlestickChart(final String title, final String stockSymbol, final ArrayList<OHLCRow> dataset) {
		final CandlestickChart ret = new CandlestickChart(title, stockSymbol, dataset);
		return ret;
	};
	
	/**
	 * @param title       title of the chart;
	 * @param xAxisLabel  label for the X axis;
	 * @param yAxisLabel  label for the Y axis;
	 * @param xAxisData   data for the X axis;
	 * @param yAxisData   data for the Y axis.
	 *
	 * @return            instance of a {@link LineChart} if input data is consistent (i.e.
	 *                    xAxisData and yAxisData have the same length), null otherwise
	 */
	public static LineChart getLineChart(final String title, final String xAxisLabel, final String yAxisLabel,
										 final ArrayList<LocalDate> xAxisData, final ArrayList<Number> yAxisData) throws IllegalArgumentException
	{
		final LineChart ret = new LineChart(title, xAxisLabel, yAxisLabel, xAxisData, yAxisData);
		return ret;
	}
	
	/**
	 * @param title  title of the chart;
	 * @param names  list of all the names of the slices that make up the pie;
	 * @param values list of all the values associated with slices;
	 *
	 * @return       instance of a {@link PieChart} if input data is consistent (i.e. slices names
	 *               and values have the same length).
	 */
	public static PieChart getPieChart(final String title, final ArrayList<String> names, final ArrayList<Number> values) throws IllegalArgumentException
	{
		final PieChart ret = new PieChart(title, names, values);
		return ret;
	}
}
