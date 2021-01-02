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

/**
 * Candlestick Chart implementation using a {@link JFreeChart}.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class CandlestickChart extends Chart {

	// candle stick chart stock symbol
	private final String stockSymbol;

	// historical data
	private final ArrayList<OHLCRow> dataRows;

	/**
	 * Default constructor.
	 *
	 * @param title {@link ApplicationFrame} window title.
	 * @param stockSymbol candle stick chart stock symbol.
	 * @param dataRows historical data.
	 */
	public CandlestickChart(final String title, final String stockSymbol, final ArrayList<OHLCRow> dataRows) {
		this.title = title;
		this.stockSymbol = stockSymbol;
		this.dataRows = dataRows;
		this.applicationFrame = new ApplicationFrame(title);
	}

	/**
	 * @return the {@link DefaultOHLCDataset} obtained from the raw data.
	 */
	protected AbstractDataset createDataset() {
		final List<OHLCDataItem> dataItems = new ArrayList<OHLCDataItem>();
		
		for (final OHLCRow row : dataRows) {
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
		
		final OHLCDataItem[] data = dataItems.toArray(new OHLCDataItem[dataItems.size()]);
		final DefaultOHLCDataset ret = new DefaultOHLCDataset(stockSymbol, data);

		return ret;
	}

	/**
	 * @param dataset the {@link OHLCDataset} used to build the candle stick chart.
	 *
	 * @return the {@link JFreeChart} created using the given {@link OHLCDataset}.
	 */
	private JFreeChart createChart(final OHLCDataset dataset) {
		final JFreeChart ret = ChartFactory.createCandlestickChart(this.title, "Time",
				"Price", dataset, true);
		return ret;
	}

	/**
	 * @return the {@link ChartPanel} that displays the specified chart.
	 */
	protected JPanel createPanel() {
		final JFreeChart jFreeChart = createChart((OHLCDataset) createDataset());
		return new ChartPanel(jFreeChart);
	}

	/**
	 * Developer harness test entry point.
	 *
	 * @param args command line arguments.
	 */
	public static void main(String[] args) throws ParseException {
		final ArrayList<OHLCRow> testRows = new ArrayList<>();

		for (int i = 10; i < 31; i++) {
			String dateString = "2020-11-" + i;
			Date testDate = new SimpleDateFormat("yyyy-mm-dd").parse(dateString);
			
			Random rand = new Random();
			
			Float testOpen = (float)5 + i;
			Float testHigh = (float)15 + i;
			Float testLow = (float)2 + i;
			Float testClose = (float)10 + i;
			Float testVolume = rand.nextFloat()*i;
			
			final OHLCRow row = new OHLCRow(testDate, testOpen, testHigh, testLow, testClose, testVolume);
			testRows.add(row);
		}
		
		final CandlestickChart candlestickChart = ChartingFactory.getCandlestickChart("Candela di prova", "MSFT", testRows);
		candlestickChart.showChart();
	}
}
