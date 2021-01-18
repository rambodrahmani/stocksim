package it.unipi.lsmsdb.stocksim.client.charting;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/**
 * Time series Chart implementation using {@link JFreeChart}.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */public class TimeSeriesChart extends Chart {
    // time axis label string
    private final String timeAxisLabel;

    // value axis label string
    private final String valuesAxisLabel;

    // data for independent variable
    private final ArrayList<LocalDate> xAxisData;

    // data for dependent variable
    private final ArrayList<Number> yAxisData;

    /**
     * Default constructor.
     *
     * @param chartTitle the title for the {@link JFreeChart};
     * @param timeAxisLabel {@link org.jfree.chart.plot.XYPlot} time axis label;
     * @param valuesAxisLabel {@link org.jfree.chart.plot.XYPlot} values axis label;
     */
    protected TimeSeriesChart(final String chartTitle, final String timeAxisLabel, final String valuesAxisLabel,
                           final ArrayList<LocalDate> xAxisData, final ArrayList<Number> yAxisData) {
        this.chartTitle = chartTitle;
        this.timeAxisLabel = timeAxisLabel;
        this.valuesAxisLabel = valuesAxisLabel;
        this.xAxisData = xAxisData;
        this.yAxisData = yAxisData;
    }

    /**
     * @return the {@link XYDataset} obtained from the raw data.
     */
    protected AbstractDataset createDataset( ) {
        final SimpleDateFormat standardDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final TimeSeries series = new TimeSeries("Stock Data");

        for (int i = 0; i < yAxisData.size(); i++) {
            try {
                final Date date = standardDateFormat.parse(xAxisData.get(i).toString());
                series.add(new Day(date), yAxisData.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return new TimeSeriesCollection(series);
    }

    /**
     * @param dataset the {@link XYDataset} used to build the time series chart.
     *
     * @return the {@link JFreeChart} created using the given {@link XYDataset}.
     */
    protected JFreeChart createChart(final AbstractDataset dataset) {
        final JFreeChart ret = ChartFactory.createTimeSeriesChart(this.chartTitle, this.timeAxisLabel, this.valuesAxisLabel,
                (XYDataset)dataset, false, false, false);
        return ret;
    }

    /**
     * @return the {@link ChartPanel} that displays the specified chart.
     */
    @Override
    protected JPanel createPanel() {
        final JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }
}
