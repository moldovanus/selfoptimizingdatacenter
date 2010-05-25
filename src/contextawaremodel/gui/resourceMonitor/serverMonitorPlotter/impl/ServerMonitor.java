package contextawaremodel.gui.resourceMonitor.serverMonitorPlotter.impl;

import contextawaremodel.gui.resourceMonitor.AbstractMonitor;
import contextawaremodel.gui.resourceMonitor.resourceMonitorPlotter.ResourceMonitorPlotter;
import contextawaremodel.worldInterface.datacenterInterface.proxies.ServerManagementProxyInterface;
import greenContextOntology.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 16, 2010
 * Time: 1:03:38 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ServerMonitor extends AbstractMonitor {

    protected ServerManagementProxyInterface proxy;

    protected List<ResourceMonitorPlotter> coresMonitors;

    protected ResourceMonitorPlotter memoryMonitor;
    protected ResourceMonitorPlotter storageMonitor;

    protected Server server;

    protected ServerMonitor(Server server, ServerManagementProxyInterface proxy) {
        this.server = server;
        this.proxy = proxy;
    }

    protected ServerMonitor(ServerManagementProxyInterface proxy, Server server, int refreshRate) {
        this.proxy = proxy;
        this.server = server;
        this.refreshRate = refreshRate;
    }

    public void executeStandaloneWindow() {
        JFrame frame = new JFrame(server.getLocalName() + " Monitor");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(monitorPanel, "Center");
        frame.setSize(400, 600);
        frame.setVisible(true);
    }

    protected void refreshData() {
        if (server.getIsInLowPowerState()) {
           // return;
        }
    }
}
