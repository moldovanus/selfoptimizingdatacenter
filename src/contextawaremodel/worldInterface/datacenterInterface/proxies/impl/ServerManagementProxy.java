package contextawaremodel.worldInterface.datacenterInterface.proxies.impl;

import contextawaremodel.worldInterface.datacenterInterface.proxies.ServerManagementProxyInterface;
import contextawaremodel.worldInterface.dtos.ServerDto;

import java.io.*;
import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 8, 2010
 * Time: 10:49:04 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ServerManagementProxy implements ServerManagementProxyInterface {
    public static boolean DEBUG = false;
    protected String hostName;

    public ServerManagementProxy(String hostName) {
        this.hostName = hostName;
    }

    public String getHostName() {
        return hostName;
    }

    


}
