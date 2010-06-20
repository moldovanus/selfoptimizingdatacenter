package contextaware.gui.resourceMonitor.resourceMonitorPlotter.impl;

import contextaware.gui.resourceMonitor.resourceMonitorPlotter.ResourceMonitorPlotter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.util.Map;
import java.util.Random;

/**
 * Pie chart used for displaying the resources used by each task
 * on a server toghether with the OS used and Free amount under the form of pie slices
 */
public class ResourceMonitorPieChartPlotter extends ResourceMonitorPlotter {

    private JFreeChart chart;
    private Random random;
    private int oldDatasetSize = 0;

    public ResourceMonitorPieChartPlotter(String resourceName, int minimumValue, int maximumValue) {
        super(resourceName);
        random = new Random();
        setup(minimumValue, maximumValue);

    }

    /**
     * @param currentValue Map<Integer,String> representing <value,label> pairs
     */
    @Override
    public void setCurrentValue(Object currentValue) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        Map<String, Integer> values = (Map<String, Integer>) currentValue;
        for (Map.Entry<String, Integer> entry : values.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setDataset(dataset);

        if (values.size() == oldDatasetSize) {
            return;
        }

        oldDatasetSize = values.size();

        plot.setSectionPaint(dataset.getIndex("Free"), Color.BLUE);
        plot.setSectionPaint(dataset.getIndex("OS"), Color.BLACK);

        for (int i = 0; i < values.size(); i++) {
            plot.setExplodePercent(i, 0.025);
        }

        /**
         * Find the representation pie slices different than OS and Free
         * and assign random colours
         */
        for (Object key : dataset.getKeys()) {
            if (!key.equals("Free") && !key.equals("OS")) {
                plot.setSectionPaint(dataset.getIndex((String) key),
                        new Color(25 + random.nextInt(230), 25 + random.nextInt(230), 25));
            }
        }
    }

    @Override
    protected void setup(int minimumValue, int maximumValue) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        chart = ChartFactory.createPieChart(
                resourceName,        // chart title
                dataset,             // data
                false,               // include legend
                true,
                false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(LABEL_FONT);
        plot.setNoDataMessage("No data available");
        graphPanel = new ChartPanel(chart);
    }
}
