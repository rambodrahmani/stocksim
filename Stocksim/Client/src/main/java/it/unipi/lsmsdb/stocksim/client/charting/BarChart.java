package it.unipi.lsmsdb.stocksim.client.charting;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.AbstractDataset;

import javax.swing.*;

/**
 * Bar Chart implementation using {@link JFreeChart}.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class BarChart extends Chart {
    final String categoryAxisLabel;
    final String valueAxisLabel;

    protected BarChart(final String chartTitle, final String categoryAxisLabel, final String valueAxisLabel) {
        this.chartTitle = chartTitle;
        this.categoryAxisLabel = categoryAxisLabel;
        this.valueAxisLabel = valueAxisLabel;
    }

    @Override
    protected AbstractDataset createDataset() {
        final String fiat = "FIAT";
        final String audi = "AUDI";
        final String ford = "FORD";
        final String speed = "Speed";
        final String millage = "Millage";
        final String userrating = "User Rating";
        final String safety = "safety";
        final DefaultCategoryDataset dataset =
                new DefaultCategoryDataset( );

        dataset.addValue( 1.0 , fiat , speed );
        dataset.addValue( 3.0 , fiat , userrating );
        dataset.addValue( 5.0 , fiat , millage );
        dataset.addValue( 5.0 , fiat , safety );

        dataset.addValue( 5.0 , audi , speed );
        dataset.addValue( 6.0 , audi , userrating );
        dataset.addValue( 10.0 , audi , millage );
        dataset.addValue( 4.0 , audi , safety );

        dataset.addValue( 4.0 , ford , speed );
        dataset.addValue( 2.0 , ford , userrating );
        dataset.addValue( 3.0 , ford , millage );
        dataset.addValue( 6.0 , ford , safety );

        return dataset;
    }

    @Override
    protected JFreeChart createChart(AbstractDataset dataset) {
        final JFreeChart pieChart = ChartFactory.createBarChart(this.chartTitle, this.categoryAxisLabel, this.valueAxisLabel,
                (CategoryDataset) dataset, PlotOrientation.VERTICAL, true, true, false);
        return pieChart;
    }

    @Override
    protected JPanel createPanel() {
        final JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }
}
