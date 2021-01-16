package it.unipi.lsmsdb.stocksim.client.charting;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.OHLCDataset;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Candlestick Chart implementation using a {@link JFreeChart}.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class CandlestickChart extends Chart {
	// historical data
	private final OHLCDataset dataset;

	// time axis label string
	private final String timeAxisLabel;

	// value axis label string
	private final String valuesAxisLabel;

	/**
	 * Default constructor.
	 *
	 * @param chartTitle {@link Chart} title;
	 * @param timeAxisLabel time axis label string;
	 * @param valuesAxisLabel value axis label string;
	 * @param stockSymbol candle stick chart stock symbol;
	 * @param dataRows historical data.
	 */
	public CandlestickChart(final String chartTitle, final String timeAxisLabel, final String valuesAxisLabel,
							final String stockSymbol, final ArrayList<OHLCRow> dataRows) {
		this.chartTitle = chartTitle;
		this.timeAxisLabel = timeAxisLabel;
		this.valuesAxisLabel = valuesAxisLabel;
		this.dataset = new DefaultOHLCDataset(stockSymbol, dataRows.toArray(new OHLCDataItem[0]));
	}

	/**
	 * @param dataset the {@link OHLCDataset} used to build the candle stick chart.
	 *
	 * @return the {@link JFreeChart} created using the given {@link OHLCDataset}.
	 */
	private JFreeChart createChart(final OHLCDataset dataset) {
		final JFreeChart ret = ChartFactory.createCandlestickChart(this.chartTitle, this.timeAxisLabel,
				this.valuesAxisLabel, dataset, true);
		// adjusting candles width
		final XYPlot plot = ret.getXYPlot();
		final CandlestickRenderer renderer = (CandlestickRenderer) plot.getRenderer();
		renderer.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_AVERAGE);
		renderer.setMaxCandleWidthInMilliseconds(900719925474099.0);
		return ret;
	}

	/**
	 * @return the {@link ChartPanel} that displays the specified chart.
	 */
	@Override
	protected JPanel createPanel() {
		final JFreeChart jFreeChart = createChart(this.dataset);
		return new ChartPanel(jFreeChart);
	}
}
