package it.unipi.lsmsdb.stocksim.client.charting;

import org.jfree.chart.ui.UIUtils;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Utility class for {@link org.jfree.chart.JFreeChart}.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class ChartUtil {
    /**
     * Opens a {@link JFrame} containing the given {@link Chart} in a {@link JSplitPane}.
     *
     * @param charts array of {@link Chart} to be displayed.
     * @param winTitle {@link JFrame} window title.
     */
    public final static void showCharts(final ArrayList<Chart> charts, final String winTitle) {
        // application frame where to show the charts
        final JFrame jFrame = new JFrame(winTitle);
        jFrame.setSize(800, 600);
        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        UIUtils.centerFrameOnScreen(jFrame);

        // create a split pane with the two scroll panes in it
        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // populate split pane
        for (final Chart chart : charts) {
            splitPane.add(chart.createPanel());
        }

        splitPane.setOneTouchExpandable(true);

        jFrame.setContentPane(splitPane);
        jFrame.setVisible(true);
        jFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        splitPane.setDividerLocation(splitPane.getSize().width / charts.size());
                    }
                });
            }
        });
    }

    /**
     * Developer harness test.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        try {
            // CANDLESTICK CHART
            final ArrayList<OHLCRow> testRows = new ArrayList<>();
            for (int i = 10; i < 31; i++) {
                if (i % 3 != 0) {
                    continue;
                }
                final String dateString = "2020-11-" + i;
                final Date testDate = new SimpleDateFormat("yyyy-mm-dd").parse(dateString);

                final Random rand = new Random();

                final Float testOpen = (float) 5 + i;
                final Float testHigh = (float) 15 + i;
                final Float testLow = (float) 2 + i;
                final Float testClose = (float) 10 + i;
                final Float testVolume = rand.nextFloat() * i;
                final Float testAdjClose = (float) 30 + i;

                final OHLCRow row = new OHLCRow(testDate, testOpen, testHigh, testLow, testClose, testVolume, testAdjClose);
                testRows.add(row);
            }
            final CandlestickChart candlestickChart = ChartingFactory.getCandlestickChart("Candela di prova",
                    "Time", "Values","MSFT", testRows);

            // LINE CHART
            final ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
            final ArrayList<Number> values = new ArrayList<Number>();
            for (int i = 1; i < 31; i++) {
                final LocalDate date = LocalDate.of(2020, 12, i);
                dates.add(date);
                values.add((float) Math.random());
            }
            final LineChart lineChart = ChartingFactory.getLineChart("LineChart di prova", "Asse X", "Asse Y", dates, values);

            // PIE CHART
            final ArrayList<String> slicesNames = new ArrayList<String>();
            slicesNames.add("Monday");
            slicesNames.add("Tuesday");
            slicesNames.add("Wednesday");
            slicesNames.add("Thursday");
            final ArrayList<Number> slicesValues = new ArrayList<Number>();
            slicesValues.add(10);
            slicesValues.add(20);
            slicesValues.add(30);
            slicesValues.add(40);
            final PieChart pieChart = ChartingFactory.getPieChart("Torta di prova", slicesNames, slicesValues);

            // TIME SERIES CHART
            final TimeSeriesChart timeSeriesChart = ChartingFactory.getTimeSeriesChart("Time Series di prova", "Time",
                    "Price", dates, values);

            // BAR CHART
            final BarChart barChart = ChartingFactory.getBarChart("Bar Chart di Prova", "Category", "Score");

            final ArrayList<Chart> charts = new ArrayList<>();
            if (pieChart != null) {
                //charts.add(pieChart);
            }
            if (lineChart != null) {
                //charts.add(lineChart);
            }
            if (candlestickChart != null) {
                //charts.add(candlestickChart);
            }
            if (timeSeriesChart != null) {
                charts.add(timeSeriesChart);
            }
            if (barChart != null) {
                charts.add(barChart);
            }

            ChartUtil.showCharts(charts, "Developer Harness Test");
        } catch (final ParseException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
