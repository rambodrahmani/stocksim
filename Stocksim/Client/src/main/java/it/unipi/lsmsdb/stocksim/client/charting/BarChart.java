package it.unipi.lsmsdb.stocksim.client.charting;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.AbstractDataset;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Bar Chart implementation using {@link JFreeChart}.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class BarChart extends Chart {
    private final String categoryAxisLabel;
    private final String valueAxisLabel;
    private final ArrayList<String> categories;
    private final ArrayList<String> bars;
    private final ArrayList<List<Double>> values;

    /**
     * Default constructor.
     *
     * @param chartTitle the title for the {@link JFreeChart};
     * @param categoryAxisLabel {@link org.jfree.chart.plot.CategoryPlot} category axis label;
     * @param valueAxisLabel {@link org.jfree.chart.plot.CategoryPlot} value axis label;
     * @param categories bar chart categories;
     * @param bars bar chart bars (many for each category);
     * @param values bars values (one for each bar).
     */
    protected BarChart(final String chartTitle, final String categoryAxisLabel, final String valueAxisLabel,
                       final ArrayList<String> categories, final ArrayList<String> bars, final ArrayList<List<Double>> values) {
        this.chartTitle = chartTitle;
        this.categoryAxisLabel = categoryAxisLabel;
        this.valueAxisLabel = valueAxisLabel;
        this.categories = categories;
        this.bars = bars;
        this.values = values;
    }

    @Override
    protected AbstractDataset createDataset() {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < categories.size(); i++) {
            for (int j = 0; j < bars.size(); j++) {
                dataset.addValue(values.get(i).get(j) , categories.get(i) , bars.get(j));
            }
        }

        return dataset;
    }

    @Override
    protected JFreeChart createChart(AbstractDataset dataset) {
        final JFreeChart pieChart = ChartFactory.createBarChart(this.chartTitle, this.categoryAxisLabel, this.valueAxisLabel,
                (DefaultCategoryDataset) dataset, PlotOrientation.VERTICAL, true, true, false);

        // display category axis labels vertically
        final CategoryPlot catPlot = pieChart.getCategoryPlot();
        final CategoryAxis domainAxis = catPlot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        return pieChart;
    }

    @Override
    protected JPanel createPanel() {
        final JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }
}
