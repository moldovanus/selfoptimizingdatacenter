package contextaware.gui.resourceMonitor.serverMonitorPlotter.impl;

import contextaware.GlobalVars;
import contextaware.gui.resourceMonitor.resourceMonitorPlotter.ResourceMonitorPlotter;
import contextaware.gui.resourceMonitor.resourceMonitorPlotter.impl.ResourceMonitorPieChartPlotter;
import contextaware.gui.resourceMonitor.serverMonitorPlotter.ServerMonitor;
import contextaware.worldInterface.datacenterInterface.proxies.ServerManagementProxyInterface;
import contextaware.worldInterface.dtos.ServerDto;
import contextaware.worldInterface.dtos.StorageDto;
import ontologyRepresentations.greenContextOntology.*;

import javax.swing.*;
import java.awt.*;
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

        monitorPanel = new JPanel();
        // monitorPanel.setLayout(new GridLayout(2, 1));

        // JPanel coresPanel = new JPanel();
        //JPanel storageAndMemoryPanel = new JPanel();

        Collection cores = server.getAssociatedCPU().getAssociatedCore();
        int coresCount = cores.size();
        if (coresCount % 2 != 0) {
            coresCount++;
        }
        coresMonitors = new ArrayList<ResourceMonitorPlotter>();

        monitorPanel.setLayout(new GridLayout(coresCount / 2 + 1, coresCount / (coresCount / 2) + 2));
        //coresPanel.setSize(400, 150);
        //JScrollPane coresScrollPanel = new JScrollPane(coresPanel);

        // storageAndMemoryPanel.setLayout(new GridLayout(1,2));

        for (Object o : cores) {
            Core core = (Core) o;
            ResourceMonitorPlotter plotter = new ResourceMonitorPieChartPlotter(core.getLocalName(), 0, core.getTotal());
            plotter.setSnapshotIncrement(refreshRate / 1000);
            JPanel graphPanel = plotter.getGraphPanel();
            graphPanel.setSize(250, 150);
            monitorPanel.add(graphPanel);
            coresMonitors.add(plotter);
        }

        //monitorPanel.add(coresPanel);

        Memory memory = server.getAssociatedMemory();
        memoryMonitor = new ResourceMonitorPieChartPlotter("Memory", 0, memory.getTotal());
        memoryMonitor.setSnapshotIncrement(refreshRate / 1000);
        monitorPanel.add(memoryMonitor.getGraphPanel());

        Storage storage = server.getAssociatedStorage();
        storageMonitor = new ResourceMonitorPieChartPlotter("Storage", 0, storage.getTotal());
        storageMonitor.setSnapshotIncrement(refreshRate / 1000);
        monitorPanel.add(storageMonitor.getGraphPanel());
    }

    protected void refreshData() {
        if (server.getIsInLowPowerState()) {
            return;
        }
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
            if ( task.getTaskName().equals("Task_3")){
                System.out.println("Task_3");
                System.out.println("Cores:" + task.getReceivedInfo().getCores());
                System.out.println("CPU:" + task.getReceivedInfo().getCpuReceived());
                Collection<Integer> cores = task.getReceivedInfo().getReceivedCoreIndex();
                for (Integer core : cores){
                      System.out.println("core_index:" + core);
                }
                System.out.println("Memory:" + task.getReceivedInfo().getMemoryReceived());
                System.out.println("Storage:" + task.getReceivedInfo().getStorageReceived());

            }
            ReceivedTaskInfo receivedInfo = task.getReceivedInfo();
            Iterator<Integer> receivedCoresIndexIterator = receivedInfo.listReceivedCoreIndex();
            int usedCPUByTask = receivedInfo.getCpuReceived();
            String taskName = task.getLocalName();
            String newTaskName = (taskName.length() > GlobalVars.MAX_NAME_LENGTH) ? taskName.substring(0, GlobalVars.MAX_NAME_LENGTH) + "..." : taskName;

            while (receivedCoresIndexIterator.hasNext()) {
                Integer index = receivedCoresIndexIterator.next();
                Map<String, Integer> map = coreInformation.get(index);
                map.put(newTaskName, usedCPUByTask);
                totalCPUUsedByTasks[index] += usedCPUByTask;
            }
            int usedMemory = receivedInfo.getMemoryReceived();
            memoryInformation.put(newTaskName, usedMemory);
            totalMemoryUsedByTasks += usedMemory;

            int usedStorage = receivedInfo.getStorageReceived();
            storageInformation.put(newTaskName, usedStorage);
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
