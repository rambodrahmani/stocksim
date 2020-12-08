import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.xy.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.text.*;
import java.util.*;
import java.util.List;
import java.text.SimpleDateFormat;

import java.net.InetSocketAddress;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

public class Main extends JFrame {
	public Main(String stockSymbol) {
		super("CandlestickDemo");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		DateAxis    domainAxis       = new DateAxis("Date");
		NumberAxis  rangeAxis        = new NumberAxis("Price");
		CandlestickRenderer renderer = new CandlestickRenderer();
		XYDataset   dataset          = getDataSet(stockSymbol);
		
		XYPlot mainPlot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);
		
		//Do some setting up, see the API Doc
		renderer.setSeriesPaint(0, Color.BLACK);
		renderer.setDrawVolume(false);
		rangeAxis.setAutoRangeIncludesZero(false);
		//domainAxis.setTimeline( SegmentedTimeline.newMondayThroughFridayTimeline() );
		
		//Now create the chart and chart panel
		JFreeChart chart = new JFreeChart(stockSymbol, null, mainPlot, false);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(600, 300));
		
		this.add(chartPanel);
		this.pack();
	}
	protected AbstractXYDataset getDataSet(String stockSymbol) {
		//This is the dataset we are going to create
		DefaultOHLCDataset result = null;
		//This is the data needed for the dataset
		OHLCDataItem[] data;
		
		//This is where we go get the data, replace with your own data source
		data = getData();
		
		//Create a dataset, an Open, High, Low, Close dataset
		result = new DefaultOHLCDataset(stockSymbol, data);
		
		return result;
	}
	//This method connects to cassandra DB get the OHLC data
	protected OHLCDataItem[] getData() {
		List<OHLCDataItem> dataItems = new ArrayList<OHLCDataItem>();
		
		try (CqlSession session = CqlSession.builder()
				.addContactPoint(new InetSocketAddress("172.16.3.94", 9042))
				//.addContactPoint(new InetSocketAddress("172.16.3.95", 9042))
				//.addContactPoint(new InetSocketAddress("172.16.3.96", 9042))
				.withLocalDatacenter("datacenter1")
				.build();) {
			String query = "select * from test.stocks where id = 1";
			final ResultSet rs = session.execute(query);
			for (Row row : rs) {
				String datestring = row.getString("date");
				Date date = new SimpleDateFormat("yyyy-mm-dd").parse(datestring);
				double open = row.getFloat("open");
				double high = row.getFloat("high");
				double low = row.getFloat("low");
				double close = row.getFloat("close");
				double volume = row.getFloat("volume");
				
				OHLCDataItem item = new OHLCDataItem(date, open, high, low, close, volume);
				dataItems.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Convert the list into an array
		OHLCDataItem[] data = dataItems.toArray(new OHLCDataItem[dataItems.size()]);
		
		return data;
	}
	public static void main(String[] args) {
		new Main("MSFT").setVisible(true);
	}
}