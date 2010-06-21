package contextaware.worldInterface.datacenterInterface.proxies;

import contextaware.worldInterface.dtos.ServerDto;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 8, 2010
 * Time: 10:50:01 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ServerManagementProxyInterface {


    ServerDto getServerInfo();

    void moveDestinationActions(String path1, String path2, String vmName);

    void moveSourceActions(String path, String vmName);

    void deployVirtualMachine(String from, String to, String vmName, String newName);

    void startVirtualMachine(String vmName);

    void stopVirtualMachine(String vmName);

    void deleteVirtualMachine(String vmName);

    void modifyVirtualMachine(String vmName, int memory, int procPercentage, int cores);

    void deployVirtualMachineWithCustomResources(String from, String to, String vmName, String vmCopyName,
                                                 int memory, int processorPercentage, int nrCores);

    /**
     * @param mac       - mac address of the server to be woken up
     * @param ipAddress - ip of router
     * @param port      - port forwarded to the server to be waken up
     */
    void wakeUpServer(String mac, String ipAddress, int port);

    void sendServerToSleep();

}
