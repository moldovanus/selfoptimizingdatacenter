package contextaware.gui.resourceMonitor.serverMonitorPlotter;

import contextaware.gui.resourceMonitor.AbstractMonitor;
import contextaware.gui.resourceMonitor.resourceMonitorPlotter.ResourceMonitorPlotter;
import contextaware.worldInterface.datacenterInterface.proxies.ServerManagementProxyInterface;
import ontologyRepresentations.greenContextOntology.Server;

import javax.swing.*;
import java.awt.*;
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

    protected abstract void refreshData();
}
