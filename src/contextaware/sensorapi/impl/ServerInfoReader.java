package contextaware.sensorapi.impl;

import contextaware.worldInterface.datacenterInterface.proxies.impl.ProxyFactory;
import contextaware.worldInterface.datacenterInterface.proxies.ServerManagementProxyInterface;
import contextaware.worldInterface.dtos.ServerDto;
import contextaware.worldInterface.dtos.StorageDto;
import ontologyRepresentations.greenContextOntology.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: May 25, 2010
 * Time: 10:00:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServerInfoReader {

    private Server server;
    private DatacenterProtegeFactory protegeFactory;
    private int refreshInterval = 1000;

    private Timer timer;

    public ServerInfoReader(Server server, DatacenterProtegeFactory factory, int refreshInterval) {
        this.server = server;
        this.protegeFactory = factory;
        this.refreshInterval = refreshInterval;
        timer = new Timer(refreshInterval, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread() {
                    public void run() {
                        refreshServerInfo();
                    }
                };
                thread.start();
            }
        });
    }


    private void refreshServerInfo() {
        ServerManagementProxyInterface proxy = ProxyFactory.createServerManagementProxy(server.getServerIPAddress());
        server.setProxy(proxy);
        ServerDto serverInfo = proxy.getServerInfo();

        CPU cpu = server.getAssociatedCPU();
        int coreCount = serverInfo.getCoreCount();
        Collection cores = cpu.getAssociatedCore();
        /*for (int i = 0; i < coreCount; i++) {
            cores.add(protegeFactory.createCore(server.getLocalName() + "_Core_" + i));
        }*/
        //cpu.setAssociatedCore(cores);

        int totalCPU = serverInfo.getTotalCPU();
        Object[] freeCPUValues = serverInfo.getFreeCPU().toArray();
        int index = 0;
        int freeCPU = totalCPU - (Integer) freeCPUValues[0];
        for (Object item : cores) {
            Core core = (Core) item;
            // core.setMaxAcceptableValue(totalCPU);
            //core.setMinAcceptableValue(1);
            core.setTotal(totalCPU);
            core.setUsed(freeCPU);
        }

        ontologyRepresentations.greenContextOntology.Memory serverMemory = server.getAssociatedMemory();
        int totalMemory = serverInfo.getTotalMemory();
        //serverMemory.setMaxAcceptableValue(totalMemory);
        serverMemory.setTotal(totalMemory);
        serverMemory.setUsed(totalMemory - serverInfo.getFreeMemory());

        Storage storage = server.getAssociatedStorage();
        List<StorageDto> storageList = serverInfo.getStorage();
        StorageDto targetStorage = null;
        String storagePath = (String) server.getVirtualMachinesPath().iterator().next();
        for (StorageDto storageDto : storageList) {
            if (storageDto.getName().charAt(0) == storagePath.charAt(0)) {
                targetStorage = storageDto;
                break;
            }
        }

        int storageSize = targetStorage.getSize();
        // storage.setMaxAcceptableValue(storageSize);
        storage.setTotal(storageSize);
        storage.setUsed(storageSize - targetStorage.getFreeSpace());
    }


}
