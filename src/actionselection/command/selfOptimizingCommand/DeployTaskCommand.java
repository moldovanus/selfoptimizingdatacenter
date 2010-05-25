/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package actionselection.command.selfOptimizingCommand;

import actionselection.utils.MessageDispatcher;
import com.hp.hpl.jena.ontology.OntModel;
import contextawaremodel.GlobalVars;
import contextawaremodel.agents.X3DAgent;
import contextawaremodel.worldInterface.datacenterInterface.proxies.impl.HyperVServerManagementProxy;
import contextawaremodel.worldInterface.datacenterInterface.proxies.impl.ServerManagementProxy;
import greenContextOntology.ProtegeFactory;
import greenContextOntology.Server;
import greenContextOntology.Task;
import jade.core.Agent;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Me
 */
public class DeployTaskCommand extends SelfOptimizingCommand {

    private String serverName;
    private String taskName;

    public DeployTaskCommand(ProtegeFactory protegeFactory, String serverName, String taskName) {
        super(protegeFactory);
        this.serverName = serverName;
        this.taskName = taskName;
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
        server.addRunningTasks(task, model);
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
        ServerManagementProxy proxy = new HyperVServerManagementProxy(server.getServerIPAddress());
        if (proxy != null) {
            String path = (String) server.getVirtualMachinesPath().iterator().next();
            proxy.deployVirtualMachine("\\\\192.168.2.110\\SharedStorage",//+server.getServerName(),//USING SAME LOCATION FOR TASK QUEUE
                    // "\\\\" + server.getServerIPAddress() + "\\" +   path.split(":")[1].substring(1),
                    "\\\\192.168.2.110\\SharedStorage\\" + server.getServerName(),
                    task.getTaskName(), task.getTaskName() + UUID.randomUUID());

        } else {
            System.err.println("Proxy is null");
        }
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
}
