package contextawaremodel.gui.resourceMonitor.resourceMonitorPlotter.impl;

import contextawaremodel.gui.resourceMonitor.resourceMonitorPlotter.ResourceMonitorPlotter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 16, 2010
 * Time: 12:33:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResourceMonitorPieChartPlotter extends ResourceMonitorPlotter {

    private JFreeChart chart;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setBounds(300, 300, 200, 100);
        frame.setLayout(new BorderLayout());
        ResourceMonitorPlotter resourceMonitorPlotter = new ResourceMonitorPieChartPlotter("CPU 0", 0, 100);


        Map<String, Integer> map = new HashMap<String, Integer>(3);
        for (int i = 0; i < 3; i++) {
            map.put("" + i, i);
        }

        //while (true) {
        resourceMonitorPlotter.setCurrentValue(map);
        frame.add(resourceMonitorPlotter.getGraphPanel(), "Center");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        // }
    }

    public ResourceMonitorPieChartPlotter(String resourceName, int minimumValue, int maximumValue) {
        super(resourceName);
        setup(minimumValue, maximumValue);
    }

    /**
     * @param currentValue Map<Integer,String> representing <value,label> pairs
     */
    @Override
    public void setCurrentValue(Object currentValue) {
        DefaultPieDataset dataset = new DefaultPieDataset();


        Map<String, Integer> values = null;

        values = (Map<String, Integer>) currentValue;

        for (Map.Entry<String, Integer> entry : values.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setDataset(dataset);
        plot.setSectionPaint(dataset.getIndex("Free"), Color.BLUE);
        plot.setSectionPaint(dataset.getIndex("OS"), Color.BLACK);
    }

    @Override
    protected void setup(int minimumValue, int maximumValue) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        chart = ChartFactory.createPieChart(
                resourceName,  // chart title
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
