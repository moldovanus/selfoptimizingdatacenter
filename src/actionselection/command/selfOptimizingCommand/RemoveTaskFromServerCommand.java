package actionselection.command.selfOptimizingCommand;

import actionselection.utils.MessageDispatcher;

import com.hp.hpl.jena.ontology.OntModel;
import contextawaremodel.GlobalVars;
import contextawaremodel.agents.X3DAgent;
import contextawaremodel.worldInterface.datacenterInterface.proxies.impl.ServerManagementProxy;
import contextawaremodel.worldInterface.datacenterInterface.proxies.impl.HyperVServerManagementProxy;
import contextawaremodel.worldInterface.datacenterInterface.proxies.impl.ServerManagementProxy;
import contextawaremodel.worldInterface.datacenterInterface.proxies.impl.ProxyFactory;
import contextawaremodel.worldInterface.datacenterInterface.proxies.ServerManagementProxyInterface;
import greenContextOntology.ProtegeFactory;
import greenContextOntology.Server;
import greenContextOntology.Task;
import jade.core.Agent;

import java.io.IOException;

import benchmark.TaskLifeManager;

/**
 * Created by IntelliJ IDEA.
 * User: Moldovanus
 * Date: Mar 22, 2010
 * Time: 2:56:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class RemoveTaskFromServerCommand extends SelfOptimizingCommand {
    private String taskName;
    private String serverName;


    public RemoveTaskFromServerCommand(ProtegeFactory protegeFactory, String taskName, String serverName) {
        super(protegeFactory);
        this.taskName = taskName;
        this.serverName = serverName;
        cost = 300;
    }

    public void execute(OntModel model) {
        Server server = protegeFactory.getServer(serverName);
        Task task = protegeFactory.getTask(taskName);
        task.setAssociatedServer(null);
        server.removeRunningTasks(task, model);
    }

    public void rewind(OntModel model) {
        Server server = protegeFactory.getServer(serverName);
        Task task = protegeFactory.getTask(taskName);
        task.setAssociatedServer(server);
        server.addRunningTasks(task, model);
    }

    public String toString() {
        String description;
        description = "Remove task \"" + taskName.split("#")[1] + "\" from server \"" + serverName.split("#")[1];
        return description;
    }

    public void executeOnWebService() {
        Server server = protegeFactory.getServer(serverName);
        Task task = protegeFactory.getTask(taskName);
        ServerManagementProxy proxy = new HyperVServerManagementProxy(server.getServerIPAddress());

        if (proxy != null) {
            //TODO:proxy.deleteVirtualMachine(vmName);
            proxy.deleteVirtualMachine(task.getName());
        } else {
            System.err.println("Proxy is null");
        }
        //   throw new UnsupportedOperationException("Not supported yet.");
    }

    public String[] toStringArray() {
        String[] array = new String[3];
        array[0] = "Remove";
        array[1] = taskName.split("#")[1];
        array[2] = serverName.split("#")[1];
        return array;
    }

    public void executeOnX3D(Agent agent) {

        try {
            MessageDispatcher.sendMessage(agent, GlobalVars.X3DAGENT_NAME, new Object[]{X3DAgent.REMOVE_TASK_COMMAND, taskName.split("#")[1]});
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void rewindOnX3D(Agent agent) {
        Server server = protegeFactory.getServer(serverName);

        try {
            MessageDispatcher.sendMessage(agent, GlobalVars.X3DAGENT_NAME, new Object[]{X3DAgent.ADD_TASK_COMMAND, taskName.split("#")[1], serverName.split("#")[1], server.getRunningTasks().size() + 1});
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
