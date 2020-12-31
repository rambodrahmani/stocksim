package it.unipi.lsmsdb.stocksim.client.charting;

import java.time.LocalDate;
import java.util.ArrayList;

public class ChartFactory {
	
	public static CandlestickChart getCandlestickChart(
			final String title,
			final String stockSymbol,
			final ArrayList<OhlcRow> dataset){
		CandlestickChart c = new CandlestickChart(title, stockSymbol, dataset);
		return c;
	};
	
	/**
	 *
	 * @param title         title of the chart
	 * @param xAxisLabel    label for the X axis
	 * @param yAxisLabel    label for the Y axis
	 * @param xAxisData     data for the X axis
	 * @param yAxisData     data for the Y axis
	 * @return              instance of a LineChart if input data is consistent (i.e.
	 *                      xAxisData and yAxisData have the same length), NULL otherwise
	 */
	public static LineChart getLineChart(
			final String title,
			final String xAxisLabel,
			final String yAxisLabel,
			final ArrayList<LocalDate> xAxisData,
			final ArrayList<Number> yAxisData)
	{
		LineChart l;
		try{
			l = new LineChart(title, xAxisLabel, yAxisLabel, xAxisData, yAxisData);
		} catch (IllegalArgumentException e){
			l = null;
		}
		return l;
	}
	
	/**
	 *
	 * @param title  title of the chart
	 * @param slices list of all the names of the slices that make up the pie
	 * @param values list of all the values associated with slices
	 * @return instance of a PieChart if input data is consistent (i.e. slices and values have the same length),
	 * NULL otherwise
	 */
	public static PieChart getPieChart(
			final String title,
			final ArrayList<String> slices,
			final ArrayList<Number> values)
	{
		PieChart p;
		try{
			p = new PieChart(title, slices, values);
		} catch (IllegalArgumentException e){
			p = null;
		}
		return p;
	}
}
