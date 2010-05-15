package contextawaremodel.gui.resourceMonitor;

import contextawaremodel.worldInterface.datacenterInterface.proxies.ServerManagementProxyInterface;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 15, 2010
 * Time: 8:44:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerMonitor {
    private ServerManagementProxyInterface proxy;
    private int refreshRate = 1000;
    private JPanel serverMonitorPanel;

    public ServerMonitor(ServerManagementProxyInterface proxy) {
        this.proxy = proxy;
    }

    /**
     *
     * @return information refresh rate in milliseconds
     */
    public int getRefreshRate() {
        return refreshRate;
    }

    /**
     *
     * @param refreshRate information refresh rate in milliseconds
     */
    public void setRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
    }

    private void refreshData(){
        
    }
}
