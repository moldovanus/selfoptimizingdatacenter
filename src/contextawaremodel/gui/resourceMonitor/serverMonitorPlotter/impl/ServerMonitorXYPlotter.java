package contextawaremodel.gui.resourceMonitor.serverMonitorPlotter.impl;

import contextawaremodel.gui.resourceMonitor.resourceMonitorPlotter.ResourceMonitorPlotter;
import contextawaremodel.gui.resourceMonitor.resourceMonitorPlotter.impl.ResourceMonitorXYChartPlotter;
import contextawaremodel.gui.resourceMonitor.ServerMonitor;
import contextawaremodel.worldInterface.datacenterInterface.proxies.ServerManagementProxyInterface;
import contextawaremodel.worldInterface.dtos.ServerDto;
import contextawaremodel.worldInterface.dtos.StorageDto;
import greenContextOntology.Core;
import greenContextOntology.Memory;
import greenContextOntology.Server;
import greenContextOntology.Storage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 15, 2010
 * Time: 8:44:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerMonitorXYPlotter extends ServerMonitor {


    public ServerMonitorXYPlotter(Server server, ServerManagementProxyInterface proxy) {
        super(server, proxy);
        setup();
    }

    public ServerMonitorXYPlotter(Server server, ServerManagementProxyInterface proxy, int refreshRate) {
        super(proxy, server, refreshRate);
        setup();
    }

    protected void setup() {


        serverMonitorPanel = new JPanel();
        serverMonitorPanel.setLayout(new GridLayout(2, 1));

        Collection cores = server.getAssociatedCPU().getAssociatedCore();
        int coresCount = cores.size();

        coresMonitors = new ArrayList<ResourceMonitorPlotter>();

        serverMonitorPanel.setLayout(new GridLayout(coresCount / 2 + 1, coresCount / (coresCount / 2) + 2));

        for (Object o : cores) {
            Core core = (Core) o;
            ResourceMonitorPlotter plotter = new ResourceMonitorXYChartPlotter(core.getLocalName(), 0, core.getTotal());
            plotter.setSnapshotIncrement(refreshRate / 1000);
            JPanel graphPanel = plotter.getGraphPanel();
            graphPanel.setSize(250, 150);
            serverMonitorPanel.add(graphPanel);
            coresMonitors.add(plotter);
        }

        Memory memory = server.getAssociatedMemory();
        memoryMonitor = new ResourceMonitorXYChartPlotter("Memory", 0, memory.getTotal());
        memoryMonitor.setSnapshotIncrement(refreshRate / 1000);
        serverMonitorPanel.add(memoryMonitor.getGraphPanel());

        Storage storage = server.getAssociatedStorage();
        storageMonitor = new ResourceMonitorXYChartPlotter("Storage", 0, storage.getTotal());
        storageMonitor.setSnapshotIncrement(refreshRate / 1000);
        serverMonitorPanel.add(storageMonitor.getGraphPanel());

    }

    protected void refreshData() {

        //TODO: place if Server Is In SLEEP
        //System.err.println("After finishing with tests check if sever is in sleep and do not query if it is");
        /*if ( server.getIsInLowPowerState()){
            return;
        }*/
        ServerDto serverDto = proxy.getServerInfo();
        List<Integer> freeCPU = serverDto.getFreeCPU();
        int totalCPU = serverDto.getTotalCPU();
        int coresCount = coresMonitors.size();
        for (int i = 0; i < coresCount; i++) {
            coresMonitors.get(i).setCurrentValue(totalCPU - freeCPU.get(i));
        }

        Storage storage = server.getAssociatedStorage();
        List<StorageDto> storageList = serverDto.getStorage();
        StorageDto targetStorage = null;
        String storagePath = (String) server.getVirtualMachinesPath().iterator().next();
        for (StorageDto storageDto : storageList) {
            if (storageDto.getName().charAt(0) == storagePath.charAt(0)) {
                targetStorage = storageDto;
                break;
            }
        }

        storageMonitor.setCurrentValue(targetStorage.getSize() - targetStorage.getFreeSpace());

        memoryMonitor.setCurrentValue(serverDto.getTotalMemory() - serverDto.getFreeMemory());

    }


}
