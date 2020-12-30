package it.unipi.lsmsdb.stocksim.client.charting;

import java.util.ArrayList;

public class PieChartFactory {
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