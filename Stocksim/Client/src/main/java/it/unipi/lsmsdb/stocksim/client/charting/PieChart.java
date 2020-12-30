package it.unipi.lsmsdb.stocksim.client.charting;

import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.chart.ui.UIUtils;

import java.util.ArrayList;

public class PieChart extends Chart{
	
	// title of the chart
	private final String title;
	
	// list of all the slices that make up the pie
	private final String[] slices;
	
	// list of all the values associated with slices
	private final Number[] values;
	
	private ApplicationFrame applicationFrame;
	
	/**
	 * Default constructor.
	 *
	 * @param title  title of the chart
	 * @param slices list of all the names of the slices that make up the pie
	 * @param values list of all the values associated with slices
	 */
	public PieChart(final String title,
	                final ArrayList<String> slices,
	                final ArrayList<Number> values){
		if(slices.size() != values.size()){
			throw new IllegalArgumentException("slices and values must have the same length");
		}
		applicationFrame = new ApplicationFrame(title);
		this.title = title;
		this.slices = slices.toArray(new String[slices.size()]);
		this.values = values.toArray(new Number[values.size()]);
		
		
	}
	
	/**
	 *  Feed data into dataset
	 */
	private PieDataset createDataset(){
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (int i = 0; i < slices.length; i++) {
			dataset.setValue(slices[i], values[i]);
		}
		
		return dataset;
	}
	
	private JFreeChart createChart(PieDataset dataset){
		JFreeChart chart = ChartFactory.createPieChart(
			title,          // chart title
			dataset,        // data
			true,    // include legend
			true,
			false);
		
		return chart;
	}
	
	private JPanel createPanel(){
		JFreeChart chart = createChart(createDataset());
		return new ChartPanel(chart);
	}
	
	/**
	 * Spawn new window
	 */
	public void showChart(){
		applicationFrame.setSize(600, 400);
		UIUtils.centerFrameOnScreen(this.applicationFrame);
		applicationFrame.setContentPane(createPanel());
		applicationFrame.setVisible(true);
	}
	
	public static void main(String[] args) {
		ArrayList<String> slices = new ArrayList<String>();
		slices.add("Monday");
		slices.add("Tuesday");
		slices.add("Wednesday");
		slices.add("Thursday");
		
		ArrayList<Number> values = new ArrayList<Number>();
		
		values.add(10);
		values.add(20);
		values.add(30);
		values.add(40);
		
		PieChart pieChart = new PieChart(
				"Torta di prova",
				slices,
				values
				);
		
		if(pieChart != null){
			System.out.println("Instance created successfully.");
			pieChart.showChart();
		}
		else{
			System.out.println("No.");
		}
		
	}
}
