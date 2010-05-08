package contextawaremodel.worldInterface.datacenterInterface.proxies;

import contextawaremodel.worldInterface.dtos.ServerDto;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 8, 2010
 * Time: 10:50:01 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ServerManagementProxyInterface{
       

        ServerDto getServerInfo();

        void moveDestinationActions(String path1, String path2, String vmName);

        void moveSourceActions(String path, String vmName);

        void deployVirtualMachine(String from, String to, String vmName);

        void startVirtualMachine(String vmName);

        void stopVirtualMachine(String vmName);

        void deleteVirtualMachine(String vmName);
}
