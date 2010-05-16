package contextawaremodel.gui.resourceMonitor.serverMonitorPlotter;

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
public abstract class ServerMonitor implements IServerMonitor {
    protected ServerManagementProxyInterface proxy;

    protected int refreshRate = 1000;

    protected JPanel serverMonitorPanel;

    protected List<ResourceMonitorPlotter> coresMonitors;
    protected ResourceMonitorPlotter memoryMonitor;
    protected ResourceMonitorPlotter storageMonitor;

    private Timer refreshInfoTimer;

    protected Server server;

    protected ServerMonitor(Server server, ServerManagementProxyInterface proxy) {
        this.server = server;
        this.proxy = proxy;

        refreshInfoTimer = new Timer(refreshRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
        refreshInfoTimer.start();

    }

    protected ServerMonitor(ServerManagementProxyInterface proxy, Server server, int refreshRate) {
        this.proxy = proxy;
        this.server = server;
        this.refreshRate = refreshRate;

        refreshInfoTimer = new Timer(refreshRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
        refreshInfoTimer.start();

    }

    protected abstract void setup();

    protected abstract void refreshData();

    public void executeStandaloneWindow() {
        JFrame frame = new JFrame(server.getLocalName() + " Monitor");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(serverMonitorPanel, "Center");
        frame.setSize(400, 600);
        frame.setVisible(true);
    }

    /**
     * @return information refresh rate in milliseconds
     */
    public int getRefreshRate() {
        return refreshRate;
    }

    /**
     * @param refreshRate information refresh rate in milliseconds
     */
    public void setRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
    }


    public JPanel getServerMonitorPanel() {
        return serverMonitorPanel;
    }

    public void setServerMonitorPanel(JPanel serverMonitorPanel) {
        this.serverMonitorPanel = serverMonitorPanel;
    }

}
