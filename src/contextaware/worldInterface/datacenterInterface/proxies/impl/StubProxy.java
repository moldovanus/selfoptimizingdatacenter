package contextaware.worldInterface.datacenterInterface.proxies.impl;

import contextaware.worldInterface.dtos.ServerDto;

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
//        ServerDto dto = new ServerDto();
//        StorageDto storageDto = new StorageDto();
//        ArrayList<Integer> cpu = new ArrayList<Integer>();
//        ArrayList<StorageDto> storageDtos = new ArrayList<StorageDto>();
//
//        cpu.add(3000);
//        cpu.add(3000);
//        cpu.add(3000);
//        storageDto.setName("C:");
//
//        cpu.add(3000);
//        storageDtos.add(storageDto);
//
//        dto.setFreeCPU(cpu);
//        dto.setStorage(storageDtos);
//        dto.setCoreCount(1);
//
//        return dto;  //To change body of implemented methods use File | Settings | File Templates.
        return new HyperVServerManagementProxy(hostName).getServerInfo();
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

    public void modifyVirtualMachine(String vmName, int memory, int procPercentage, int cores) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deployVirtualMachineWithCustomResources(String from, String to, String vmName, String vmCopyName, int memory, int processorPercentage, int nrCores) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void wakeUpServer(String mac, String ipAddress, int port) {
        //new HyperVServerManagementProxy(hostName).wakeUpServer(mac,ipAddress,port);
    }

    public void sendServerToSleep() {
        //new HyperVServerManagementProxy(hostName).sendServerToSleep();
    }
}
