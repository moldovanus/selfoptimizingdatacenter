package contextawaremodel.worldInterface.datacenterInterface.proxies.impl;

import contextawaremodel.worldInterface.datacenterInterface.proxies.ServerManagementProxyInterface;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: May 29, 2010
 * Time: 10:13:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProxyFactory {
    private ProxyFactory() {
    }

    public static ServerManagementProxyInterface createServerManagementProxy(String hostName) {
        return new HyperVServerManagementProxy(hostName);
    }
}
