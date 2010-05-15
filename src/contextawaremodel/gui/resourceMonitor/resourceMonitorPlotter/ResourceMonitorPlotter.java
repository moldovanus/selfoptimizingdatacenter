package contextawaremodel.gui.resourceMonitor.resourceMonitorPlotter;

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
public class ResourceMonitorPlotter {

    private String resourceName;

    private int snapshotCount = 0;
    private int minTimeRange = 0;
    private int maxTimeRange = 60;
    private int snapshotIncrement = 1;

    private static final Font LABEL_FONT = new Font("sherif", Font.BOLD, 20);

    private JPanel graphPanel;

    private XYSeries series = new XYSeries("Used");
    private XYPlot plot;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setBounds(300, 300, 300, 200);
        frame.setLayout(new BorderLayout());
        ResourceMonitorPlotter resourceMonitorPlotter = new ResourceMonitorPlotter("CPU 0", 0, 100);
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
    }

    public ResourceMonitorPlotter(String resourceName, int minimumValue, int maximumValue) {
        this.resourceName = resourceName;
        setupLiveGraph(minimumValue, maximumValue);
        //setupDisplayPanel();
    }

    public int getMinTimeRange() {
        return minTimeRange;
    }

    public void setMinTimeRange(int minTimeRange) {
        this.minTimeRange = minTimeRange;
    }

    public int getMaxTimeRange() {
        return maxTimeRange;
    }

    public void setMaxTimeRange(int maxTimeRange) {
        this.maxTimeRange = maxTimeRange;
    }


    private void setupLiveGraph(int minimumValue, int maximumValue) {

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

    public JPanel getGraphPanel() {
        return graphPanel;
    }


    public void setCurrentValue(int currentValue) {
        if (snapshotCount > maxTimeRange - 1) {
            series.remove(0);
            ValueAxis domain = plot.getDomainAxis();
            minTimeRange += snapshotIncrement;
            // domain.setAutoRange(false);
            domain.setRange(minTimeRange, minTimeRange + maxTimeRange);
        }
        series.add(snapshotCount, currentValue);
        snapshotCount += snapshotIncrement;
    }

    public int getSnapshotIncrement() {
        return snapshotIncrement;
    }

    public void setSnapshotIncrement(int snapshotIncrement) {
        this.snapshotIncrement = snapshotIncrement;
    }

    public void setDisplayTimeRange(int minim, int max) {

    }
}
