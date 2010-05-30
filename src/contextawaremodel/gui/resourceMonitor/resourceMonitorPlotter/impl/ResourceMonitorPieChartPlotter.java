package contextawaremodel.gui.resourceMonitor.resourceMonitorPlotter.impl;

import contextawaremodel.gui.resourceMonitor.resourceMonitorPlotter.ResourceMonitorPlotter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 16, 2010
 * Time: 12:33:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResourceMonitorPieChartPlotter extends ResourceMonitorPlotter {

    private JFreeChart chart;
    private Random random;
    private int oldDatasetSize = 0;


   /* public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setBounds(300, 300, 200, 100);
        frame.setLayout(new BorderLayout());
        ResourceMonitorPlotter resourceMonitorPlotter = new ResourceMonitorPieChartPlotter("CPU 0", 0, 100);


        Map<String, Integer> map = new HashMap<String, Integer>(3);
        map.put("Free", 2);
        map.put("OS", 2);
        resourceMonitorPlotter.setCurrentValue(map);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(resourceMonitorPlotter.getGraphPanel(), "Center");
        frame.pack();
        frame.setVisible(true);

        while (true) {
            map.clear();
            map.put("Free", 2);
            map.put("OS", 2);
            for (int j = 0; j < 5; j++) {
                map.put("" + j, j);
            }
            resourceMonitorPlotter.setCurrentValue(map);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }*/

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

        for (Object key : dataset.getKeys()) {
            if (!((String) key).equals("Free") && !((String) key).equals("OS")) {
                plot.setSectionPaint(dataset.getIndex((String) key), new Color(25 + random.nextInt(230), 25 + random.nextInt(230), 25));
            }
        }
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
