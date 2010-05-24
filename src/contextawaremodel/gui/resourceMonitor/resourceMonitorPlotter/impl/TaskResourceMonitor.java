package contextawaremodel.gui.resourceMonitor.resourceMonitorPlotter.impl;

import contextawaremodel.gui.resourceMonitor.resourceMonitorPlotter.ResourceMonitorPlotter;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: May 24, 2010
 * Time: 11:39:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class TaskResourceMonitor extends ResourceMonitorPlotter {
    private JLabel currentValueLabel;

    public TaskResourceMonitor(String resourceName, int minimumValue, int maximumValue) {
        super(resourceName);
        setup(minimumValue, maximumValue);
    }

    public void setCurrentValue(Object currentValue) {
        currentValueLabel.setText("" + currentValue);
    }

    protected void setup(int minimumValue, int maximumValue) {
        currentValueLabel = new JLabel("0");
        graphPanel.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        JPanel requestedPanel = new JPanel();
        JPanel receivedPanel = new JPanel();

        JPanel requestedValuePanel = new JPanel();
        JPanel receivedValuePanel = new JPanel();

        requestedValuePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        receivedValuePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        receivedValuePanel.setBackground(new Color(255,255,255));
        requestedValuePanel.setBackground(new Color(255,255,255));

        requestedPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        receivedPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        requestedPanel.setBackground(new Color(255,255,255));
        receivedPanel.setBackground(new Color(255,255,255));

        centerPanel.setBorder(BorderFactory.createRaisedBevelBorder());

        centerPanel.setLayout(new GridLayout(1, 2));
        receivedValuePanel.setLayout(new BorderLayout());
        requestedValuePanel.setLayout(new BorderLayout());
        requestedPanel.setLayout(new BorderLayout());
        receivedPanel.setLayout(new BorderLayout());

        requestedPanel.add(new JLabel("Requested"), BorderLayout.NORTH);
        requestedValuePanel.add(new JLabel("[" + minimumValue + ", " + maximumValue + "]"), BorderLayout.CENTER);
        requestedPanel.add(requestedValuePanel, BorderLayout.CENTER);


        receivedPanel.add(new JLabel("Received"), BorderLayout.NORTH);
        receivedValuePanel.add(currentValueLabel, BorderLayout.CENTER);
        receivedPanel.add(receivedValuePanel, BorderLayout.CENTER);

        centerPanel.add(requestedPanel);
        centerPanel.add(receivedPanel);
        
        graphPanel.add(new JLabel(resourceName), BorderLayout.NORTH);
        graphPanel.add(centerPanel, BorderLayout.CENTER);

    }
}
