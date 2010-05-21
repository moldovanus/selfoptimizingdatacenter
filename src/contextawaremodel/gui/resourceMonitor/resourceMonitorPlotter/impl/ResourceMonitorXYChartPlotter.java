package contextawaremodel.gui.resourceMonitor.resourceMonitorPlotter.impl;

import contextawaremodel.gui.resourceMonitor.resourceMonitorPlotter.ResourceMonitorPlotter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 15, 2010
 * Time: 5:18:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResourceMonitorXYChartPlotter extends ResourceMonitorPlotter {


    private XYSeries series = new XYSeries("Used");
    private XYPlot plot;

   /* public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setBounds(300, 300, 300, 200);
        frame.setLayout(new BorderLayout());
        ResourceMonitorXYChartPlotter resourceMonitorPlotter = new ResourceMonitorXYChartPlotter("CPU 0", 0, 100);
        JPanel panel = resourceMonitorPlotter.getGraphPanel();

        frame.add(panel, "Center");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.pack();
        frame.setVisible(true);


        while (true) {
            resourceMonitorPlotter.setCurrentValue(new Random().nextInt(100));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }*/

    public ResourceMonitorXYChartPlotter(String resourceName, int minimumValue, int maximumValue) {
        super(resourceName);
        setup(minimumValue, maximumValue);
    }


    @Override
    protected void setup(int minimumValue, int maximumValue) {
        XYDataset xyDataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                resourceName,  // Title
                "Time(s)",          // X-Axis label
                "Usage(%)",           // Y-Axis label
                xyDataset,          // Dataset
                PlotOrientation.VERTICAL,
                false,                // Show legend
                false, false
        );


        plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.BLACK);
        XYItemRenderer renderer = plot.getRenderer();
        renderer.setPaint(Color.GREEN);

        ValueAxis yAxis = plot.getRangeAxis();
        yAxis.setLabelFont(LABEL_FONT);
        yAxis.setRange(minimumValue, maximumValue);

        ValueAxis xAxis = plot.getDomainAxis();
        xAxis.setLabelFont(LABEL_FONT);
        xAxis.setRange(minTimeRange, maxTimeRange);


        ChartPanel panel = new ChartPanel(chart);
        graphPanel = panel;

    }


    public void setCurrentValue(Object currentValue) {
        if (snapshotCount > maxTimeRange - 1) {
            series.remove(0);
            ValueAxis domain = plot.getDomainAxis();
            minTimeRange += snapshotIncrement;
            // domain.setAutoRange(false);
            domain.setRange(minTimeRange, minTimeRange + maxTimeRange);
        }
        series.add(snapshotCount, (Integer) currentValue);
        snapshotCount += snapshotIncrement;
    }

}
