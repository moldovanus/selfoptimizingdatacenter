/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package actionEnforcement.command.selfOptimizingCommand;

import com.hp.hpl.jena.ontology.OntModel;
import contextaware.GlobalVars;
import contextaware.agents.X3DAgent;
import contextaware.worldInterface.datacenterInterface.proxies.ServerManagementProxyInterface;
import contextaware.worldInterface.datacenterInterface.proxies.impl.ProxyFactory;
import jade.core.Agent;
import ontologyRepresentations.greenContextOntology.DatacenterProtegeFactory;
import ontologyRepresentations.greenContextOntology.Server;
import ontologyRepresentations.greenContextOntology.Task;
import utils.X3DMessageDispatcher;

import java.io.IOException;

/**
 * @author Me
 */
public class MoveTaskCommand extends SelfOptimizingCommand {

    private String oldServerName;
    private String newServerName;
    private String taskName;

    public MoveTaskCommand(DatacenterProtegeFactory protegeFactory, String oldServerName, String newServerName, String taskName) {
        super(protegeFactory);
        this.oldServerName = oldServerName;
        this.newServerName = newServerName;
        this.taskName = taskName;
        cost = 1000;
    }

    /**
     * Moves the task
     */
    @Override
    public void execute(OntModel model) {
        Server oldServer = protegeFactory.getServer(oldServerName);
        Server newServer = protegeFactory.getServer(newServerName);
        Task task = protegeFactory.getTask(taskName);
        task.setAssociatedServer(newServer);
        oldServer.removeRunningTask(task, model);
        newServer.addRunningTask(task, model);
    }

    @Override
    public void rewind(OntModel model) {
        Server oldServer = protegeFactory.getServer(oldServerName);
        Server newServer = protegeFactory.getServer(newServerName);
        Task task = protegeFactory.getTask(taskName);
        task.setAssociatedServer(oldServer);
        newServer.removeRunningTask(task, model);
        oldServer.addRunningTask(task, model);

    }

    @Override
    public String toString() {
        String description;
        description = "Move task \"" + taskName.split("#")[1] + "\" from server \"" + oldServerName.split("#")[1]
                + "\" to server \"" + newServerName.split("#")[1];
        return description;
    }

    @Override
    public void executeOnWebService() {
        Server oldServer = protegeFactory.getServer(oldServerName);
        Server newServer = protegeFactory.getServer(newServerName);
        Task task = protegeFactory.getTask(taskName);
        ServerManagementProxyInterface oldServerProxy = ProxyFactory.createServerManagementProxy(oldServer.getServerIPAddress());
        ServerManagementProxyInterface newServerProxy = ProxyFactory.createServerManagementProxy(newServer.getServerIPAddress());
        if (oldServerProxy != null && newServerProxy != null) {
            String path = (String) newServer.getVirtualMachinesPath().iterator().next();
            oldServerProxy.moveSourceActions("//HOME-Z5VXXZDRPO/SharedStorage/" + oldServer.getServerName() + task.getTaskName(),
                    task.getName());
            newServerProxy.moveDestinationActions("//HOME-Z5VXXZDRPO/SharedStorage/" + oldServer.getServerName() + task.getTaskName(),
                    "//" + newServer.getServerIPAddress() + "/" + path.split(":")[1].substring(1),
                    task.getName());

        } else {
            System.err.println("Proxy is null");
        }
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] toStringArray() {
        String[] array = new String[4];
        array[0] = "Move";
        array[1] = taskName.split("#")[1];
        array[2] = oldServerName.split("#")[1];
        array[3] = newServerName.split("#")[1];
        return array;
    }

    public void executeOnX3D(Agent agent) {
        Server server = protegeFactory.getServer(newServerName);

        try {
            X3DMessageDispatcher.sendMessage(agent, GlobalVars.X3DAGENT_NAME, new Object[]{X3DAgent.MOVE_TASK_COMMAND, taskName.split("#")[1], newServerName.split("#")[1], server.getRunningTasks().size() + 1});

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    public void rewindOnX3D(Agent agent) {

        try {

            X3DMessageDispatcher.sendMessage(agent, GlobalVars.X3DAGENT_NAME, new Object[]{X3DAgent.REMOVE_TASK_COMMAND, taskName.split("#")[1]});

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        Server server = protegeFactory.getServer(oldServerName);
        try {
            X3DMessageDispatcher.sendMessage(agent, GlobalVars.X3DAGENT_NAME, new Object[]{X3DAgent.ADD_TASK_COMMAND, taskName.split("#")[1], oldServerName.split("#")[1], server.getRunningTasks().size() + 1});
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public String getOldServerName() {
        return oldServerName;
    }

    public void setOldServerName(String oldServerName) {
        this.oldServerName = oldServerName;
    }

    public String getNewServerName() {
        return newServerName;
    }

    public void setNewServerName(String newServerName) {
        this.newServerName = newServerName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
