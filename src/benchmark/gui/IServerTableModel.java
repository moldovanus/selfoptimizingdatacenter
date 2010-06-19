package benchmark.gui;

import greenContextOntology.ProtegeFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: Jun 18, 2010
 * Time: 1:56:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IServerTableModel extends ITableModel {
    String SERVER_NAME = "Server Name";
    String SERVER_IP_ADDR = "IP Addr";
    String SERVER_MAC_ADDR = "MAC Addr";
    String SERVER_STORAGE_PATH = "Storage Path";
    String SERVER_MIN_CPU = "Min CPU";
    String SERVER_MAX_CPU = "Max CPU";
    String SERVER_MIN_MEMORY = "Min Memory";
    String SERVER_MAX_MEMORY = "Max Memory";
    String SERVER_MIN_STORAGE = "Min Storage";
    String SERVER_MAX_STORAGE = "Max Storage";
    String SERVER_TOTAL_STORAGE = "Total Storage";
    String SERVER_TOTAL_CPU= "Total CPU";
    String SERVER_TOTAL_MEMORY = "Total Memory";
    String SERVER_CORE_COUNT = "Cores No";


    void createServerEntities(ProtegeFactory factory);


}
