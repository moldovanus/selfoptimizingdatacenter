package actionselection.command.selfOptimizingCommand;

import actionselection.utils.MessageDispatcher;
import benchmark.TaskLifeManager;
import com.hp.hpl.jena.ontology.OntModel;
import contextawaremodel.GlobalVars;
import contextawaremodel.agents.X3DAgent;
import contextawaremodel.worldInterface.datacenterInterface.proxies.ServerManagementProxyInterface;
import contextawaremodel.worldInterface.datacenterInterface.proxies.impl.ProxyFactory;
import greenContextOntology.ProtegeFactory;
import greenContextOntology.Server;
import greenContextOntology.Task;
import jade.core.Agent;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: LAPTOP
 * Date: Jun 1, 2010
 * Time: 1:39:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class DeployNegotiatedTaskCommand extends SelfOptimizingCommand {
    private String serverName;
    private String taskName;
    private int negotiatedCPU;
    private int negotiatedMemory;
    private int negotiatedStorage;


    public DeployNegotiatedTaskCommand(ProtegeFactory protegeFactory, String serverName, String taskName, int negotiatedCPU, int negotiatedMemory, int negotiatedStorage) {
        super(protegeFactory);
        this.serverName = serverName;
        this.taskName = taskName;
        this.negotiatedCPU = negotiatedCPU;
        this.negotiatedMemory = negotiatedMemory;
        this.negotiatedStorage = negotiatedStorage;
        cost = 100;
    }

    /**
     * Sets the task <code>taskName</code> associated server to <code>serverName</code>
     */
    @Override
    public void execute(OntModel model) {
        Server server = protegeFactory.getServer(serverName);
        Task task = protegeFactory.getTask(taskName);
        task.setAssociatedServer(server);
        server.addNegotiatedTasks(task, model, negotiatedCPU, negotiatedMemory, negotiatedStorage);
    }

    /**
     * Implements Undo capability
     */
    @Override
    public void rewind(OntModel model) {
        Server server = protegeFactory.getServer(serverName);
        Task task = protegeFactory.getTask(taskName);
        server.removeRunningTasks(task, model);
    }

    @Override
    public String toString() {
        String description;
        description = "Deploy task \"" + taskName.split("#")[1] + "\" to server \"" + serverName.split("#")[1] + "\"";
        return description;
    }

    @Override
    public void executeOnWebService() {
        Server server = protegeFactory.getServer(serverName);
        Task task = protegeFactory.getTask(taskName);
        ServerManagementProxyInterface proxy = ProxyFactory.createServerManagementProxy(server.getServerIPAddress());
        if (proxy != null) {
            String path = (String) server.getVirtualMachinesPath().iterator().next();
            proxy.deployVirtualMachine("\\\\192.168.2.110\\SharedStorage",
                    "\\\\192.168.2.110\\SharedStorage\\" + server.getServerName(),
                    task.getTaskName(), task.getLocalName());

        } else {
            System.err.println("Proxy is null");
        }
        TaskLifeManager.startTaskTimer(task);
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] toStringArray() {
        String[] array = new String[3];
        array[0] = "Deploy";
        array[1] = taskName.split("#")[1];
        array[2] = serverName.split("#")[1];
        return array;
    }

    public void executeOnX3D(Agent agent) {
        Server server = protegeFactory.getServer(serverName);
        try {
            MessageDispatcher.sendMessage(agent, GlobalVars.X3DAGENT_NAME, new Object[]{X3DAgent.ADD_TASK_COMMAND, taskName.split("#")[1], serverName.split("#")[1], server.getRunningTasks().size() + 1});


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    public void rewindOnX3D(Agent agent) {
        try {
            MessageDispatcher.sendMessage(agent, GlobalVars.X3DAGENT_NAME, new Object[]{X3DAgent.REMOVE_TASK_COMMAND, taskName.split("#")[1]});


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
