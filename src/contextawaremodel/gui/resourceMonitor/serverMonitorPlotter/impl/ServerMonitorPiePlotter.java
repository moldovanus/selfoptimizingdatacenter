package contextawaremodel.gui.resourceMonitor.serverMonitorPlotter.impl;

import contextawaremodel.gui.resourceMonitor.resourceMonitorPlotter.impl.ResourceMonitorPieChartPlotter;
import contextawaremodel.gui.resourceMonitor.resourceMonitorPlotter.ResourceMonitorPlotter;
import contextawaremodel.gui.resourceMonitor.serverMonitorPlotter.ServerMonitor;
import contextawaremodel.worldInterface.datacenterInterface.proxies.ServerManagementProxyInterface;
import contextawaremodel.worldInterface.dtos.ServerDto;
import contextawaremodel.worldInterface.dtos.StorageDto;
import greenContextOntology.*;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 16, 2010
 * Time: 1:07:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerMonitorPiePlotter extends ServerMonitor {


    public ServerMonitorPiePlotter(Server server, ServerManagementProxyInterface proxy) {
        super(server, proxy);
        setup();
    }

    public ServerMonitorPiePlotter(Server server, ServerManagementProxyInterface proxy, int refreshRate) {
        super(proxy, server, refreshRate);
        setup();
    }

    @Override
    protected void setup() {

        serverMonitorPanel = new JPanel();
        // serverMonitorPanel.setLayout(new GridLayout(2, 1));

        // JPanel coresPanel = new JPanel();
        //JPanel storageAndMemoryPanel = new JPanel();

        Collection cores = server.getAssociatedCPU().getAssociatedCore();
        int coresCount = cores.size();

        coresMonitors = new ArrayList<ResourceMonitorPlotter>();

        serverMonitorPanel.setLayout(new GridLayout(coresCount / 2 + 1, coresCount / (coresCount / 2) + 2));
        //coresPanel.setSize(400, 150);
        //JScrollPane coresScrollPanel = new JScrollPane(coresPanel);

        // storageAndMemoryPanel.setLayout(new GridLayout(1,2));

        for (Object o : cores) {
            Core core = (Core) o;
            ResourceMonitorPlotter plotter = new ResourceMonitorPieChartPlotter(core.getLocalName(), 0, core.getTotal());
            plotter.setSnapshotIncrement(refreshRate / 1000);
            JPanel graphPanel = plotter.getGraphPanel();
            graphPanel.setSize(250, 150);
            serverMonitorPanel.add(graphPanel);
            coresMonitors.add(plotter);
        }

        //serverMonitorPanel.add(coresPanel);

        Memory memory = server.getAssociatedMemory();
        memoryMonitor = new ResourceMonitorPieChartPlotter("Memory", 0, memory.getTotal());
        memoryMonitor.setSnapshotIncrement(refreshRate / 1000);
        serverMonitorPanel.add(memoryMonitor.getGraphPanel());

        Storage storage = server.getAssociatedStorage();
        storageMonitor = new ResourceMonitorPieChartPlotter("Storage", 0, storage.getTotal());
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
        java.util.List<Integer> freeCPU = serverDto.getFreeCPU();
        int totalCPU = serverDto.getTotalCPU();
        int totalUsedMemory = serverDto.getTotalMemory() - serverDto.getFreeMemory();
        int totalUsedStorage = 0;

        Collection<Task> runningTasks = server.getRunningTasks();
        java.util.List<Map<String, Integer>> coreInformation = new java.util.ArrayList<Map<String, Integer>>();
        Map<String, Integer> memoryInformation = new HashMap<String, Integer>();
        Map<String, Integer> storageInformation = new HashMap<String, Integer>();


        Storage storage = server.getAssociatedStorage();
        java.util.List<StorageDto> storageList = serverDto.getStorage();
        StorageDto targetStorage = null;
        String storagePath = (String) server.getVirtualMachinesPath().iterator().next();
        for (StorageDto storageDto : storageList) {
            if (storageDto.getName().charAt(0) == storagePath.charAt(0)) {
                targetStorage = storageDto;
                break;
            }
        }

        totalUsedStorage = targetStorage.getSize() - targetStorage.getFreeSpace();

        storageInformation.put("Free", targetStorage.getFreeSpace());
        memoryInformation.put("Free", serverDto.getFreeMemory());


        int coresCount = coresMonitors.size();
        for (int i = 0; i < coresCount; i++) {
            //TODO :  after adding info for all cores in C# modify this
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("Free", freeCPU.get(i));
            coreInformation.add(map);

        }


        int[] totalCPUUsedByTasks = new int[coresCount];
        int totalMemoryUsedByTasks = 0;
        int totalStorageUsedByTasks = 0;

        for (Task task : runningTasks) {
            ReceivedTaskInfo receivedInfo = task.getReceivedInfo();
            Iterator<Integer> coresIterator = receivedInfo.listReceivedCoreIndex();
            int usedCPUByTask = receivedInfo.getCpuReceived();
            String taskName = task.getTaskName();
            while (coresIterator.hasNext()) {
                Integer index = coresIterator.next();
                Map<String, Integer> map = coreInformation.get(index);
                map.put(taskName, usedCPUByTask);
                totalCPUUsedByTasks[index] += usedCPUByTask;
            }
            int usedMemory = receivedInfo.getMemoryReceived();
            memoryInformation.put(taskName, usedMemory);
            totalMemoryUsedByTasks += usedMemory;

            int usedStorage = receivedInfo.getStorageReceived();
            storageInformation.put(taskName, usedStorage);
            totalStorageUsedByTasks += usedStorage;

        }


        for (int i = 0; i < coresCount; i++) {
            Map<String, Integer> map = coreInformation.get(i);
            map.put("OS", totalCPU - freeCPU.get(i) - totalCPUUsedByTasks[i]);
            coresMonitors.get(i).setCurrentValue(map);
        }

        memoryInformation.put("OS", totalUsedMemory - totalMemoryUsedByTasks);
        memoryMonitor.setCurrentValue(memoryInformation);

        storageInformation.put("OS", totalUsedStorage - totalStorageUsedByTasks);
        storageMonitor.setCurrentValue(storageInformation);

    }
}
