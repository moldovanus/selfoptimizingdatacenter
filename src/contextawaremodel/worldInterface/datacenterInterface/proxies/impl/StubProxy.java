package contextawaremodel.worldInterface.datacenterInterface.proxies.impl;

import contextawaremodel.worldInterface.dtos.ServerDto;
import contextawaremodel.worldInterface.dtos.StorageDto;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: May 29, 2010
 * Time: 10:19:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class StubProxy extends ServerManagementProxy {
    public StubProxy(String hostName) {
        super(hostName);
    }

    public ServerDto getServerInfo() {
        ServerDto dto = new ServerDto();
        StorageDto storageDto = new StorageDto();
        ArrayList<Integer> cpu = new ArrayList<Integer>();
        ArrayList<StorageDto> storageDtos = new ArrayList<StorageDto>();

        cpu.add(1);
        storageDtos.add(storageDto);

        dto.setFreeCPU(cpu);
        dto.setStorage(storageDtos);
        dto.setCoreCount(1);

        return dto;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void moveDestinationActions(String path1, String path2, String vmName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void moveSourceActions(String path, String vmName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deployVirtualMachine(String from, String to, String vmName, String newName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void startVirtualMachine(String vmName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void stopVirtualMachine(String vmName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteVirtualMachine(String vmName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void wakeUpServer(String mac, String ipAddress, int port) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void sendServerToSleep() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
