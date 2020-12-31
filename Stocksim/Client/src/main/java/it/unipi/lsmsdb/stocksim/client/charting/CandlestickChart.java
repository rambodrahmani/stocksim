package it.unipi.lsmsdb.stocksim.client.charting;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.OHLCDataset;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CandlestickChart extends Chart {

	private final String stockSymbol;
	private final OHLCRow[] dataRows;
	
	
	public CandlestickChart(final String title,
	                        final String stockSymbol,
	                        final ArrayList<OHLCRow> data)
	{
		this.title = title;
		this.stockSymbol = stockSymbol;
		this.dataRows = data.toArray(new OHLCRow[data.size()]);
		
		this.applicationFrame = new ApplicationFrame(title);
	}
	
	protected AbstractDataset createDataset() {
		DefaultOHLCDataset set = null;
		List<OHLCDataItem> dataItems = new ArrayList<OHLCDataItem>();
		
		for (OHLCRow row:
				dataRows) {
			OHLCDataItem item = new OHLCDataItem(
				row.getDate(),
				row.getOpen(),
				row.getHigh(),
				row.getLow(),
				row.getClose(),
				row.getVolume()
			);
			
			dataItems.add(item);
		}
		
		OHLCDataItem[] data = dataItems.toArray(new OHLCDataItem[dataItems.size()]);
		
		set = new DefaultOHLCDataset(stockSymbol, data);
		return set;
	}
	private JFreeChart createChart(OHLCDataset dataset){
		JFreeChart candlestickChart = ChartFactory.createCandlestickChart(
				this.title,
				"Time",
				"Price",
				(OHLCDataset) createDataset(),
				true
		);
		
		return candlestickChart;
	}
	
	
	protected JPanel createPanel() {
		JFreeChart chart = createChart((OHLCDataset) createDataset());
		return new ChartPanel(chart);
	}
	
	
	public static void main(String[] args) throws ParseException {
		ArrayList<OHLCRow> testRows = new ArrayList<>();
		for (int i = 10; i < 31; i++) {
			String dateString = "2020-11-" + i;
			Date testDate = new SimpleDateFormat("yyyy-mm-dd").parse(dateString);
			
			Random rand = new Random();
/*			Float testOpen = rand.nextFloat();
			Float testHigh = rand.nextFloat();
			Float testLow = rand.nextFloat();
			Float testClose = rand.nextFloat(); */
			Float testVolume = rand.nextFloat()*i;
			
			
			Float testOpen = (float)5 + i;
			Float testHigh = (float)15 + i;
			Float testLow = (float)2 + i;
			Float testClose = (float)10 + i;
//			Float testVolume = (float)1;
			
			OHLCRow row = new OHLCRow(
					testDate,
					testOpen,
					testHigh,
					testLow,
					testClose,
					testVolume
					);
			
			testRows.add(row);
		}
		
		CandlestickChart c = new CandlestickChart(
				"Candela di prova",
				"MSFT",
				testRows
				);
		
		c.showChart();
	}
}
