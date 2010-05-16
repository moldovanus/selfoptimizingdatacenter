package contextawaremodel.gui.resourceMonitor.serverMonitorPlotter.impl;

import contextawaremodel.gui.resourceMonitor.serverMonitorPlotter.IServerMonitor;
import contextawaremodel.gui.resourceMonitor.serverMonitorPlotter.ServerMonitor;
import contextawaremodel.worldInterface.datacenterInterface.proxies.ServerManagementProxyInterface;
import greenContextOntology.Server;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 16, 2010
 * Time: 2:25:43 PM
 * To change this template use File | Settings | File Templates.
 * This class created a tabbed panel containing on one tab a ServerMonitorXYPlotter and on the other a ServerMonitorPiePlotter
 */
public class FullServerMonitor implements IServerMonitor {

    private int refreshRate = 1000;
    private JPanel serverMonitorPanel;
    private Server server;

    public FullServerMonitor(Server server, ServerManagementProxyInterface proxy, int refreshRate) {
        this.refreshRate = refreshRate;
        this.server = server;
        serverMonitorPanel = new JPanel();
        JTabbedPane tabbedPane = new JTabbedPane();
        ServerMonitor xyMonitor = new ServerMonitorXYPlotter(server, proxy, refreshRate);
        ServerMonitor pieMonitor = new ServerMonitorPiePlotter(server, proxy, refreshRate);
        tabbedPane.addTab("Total resources usage", xyMonitor.getServerMonitorPanel());
        tabbedPane.addTab("Tasks resources usage", pieMonitor.getServerMonitorPanel());
        serverMonitorPanel.setLayout(new BorderLayout());
        serverMonitorPanel.add(tabbedPane, "Center");
    }

    public FullServerMonitor(Server server, ServerManagementProxyInterface proxy) {
        this.server = server;
        serverMonitorPanel = new JPanel();
        JTabbedPane tabbedPane = new JTabbedPane();
        ServerMonitor xyMonitor = new ServerMonitorXYPlotter(server, proxy, refreshRate);
        ServerMonitor pieMonitor = new ServerMonitorPiePlotter(server, proxy, refreshRate);
        tabbedPane.addTab("Total resources usage", xyMonitor.getServerMonitorPanel());
        tabbedPane.addTab("Tasks resources usage", pieMonitor.getServerMonitorPanel());
        serverMonitorPanel.setLayout(new BorderLayout());
        serverMonitorPanel.add(tabbedPane, "Center");
    }


    public void executeStandaloneWindow() {
        JFrame frame = new JFrame(server.getLocalName() + " Monitor");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(serverMonitorPanel, "Center");
         frame.setSize(400,600);
        frame.setVisible(true);
    }

    public JPanel getServerMonitorPanel() {
        return serverMonitorPanel;
    }
}
