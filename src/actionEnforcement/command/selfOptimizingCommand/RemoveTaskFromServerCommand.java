package actionEnforcement.command.selfOptimizingCommand;

import utils.X3DMessageDispatcher;

import com.hp.hpl.jena.ontology.OntModel;
import contextaware.GlobalVars;
import contextaware.agents.X3DAgent;
import contextaware.worldInterface.datacenterInterface.proxies.impl.ProxyFactory;
import contextaware.worldInterface.datacenterInterface.proxies.ServerManagementProxyInterface;
import ontologyRepresentations.greenContextOntology.DatacenterProtegeFactory;
import ontologyRepresentations.greenContextOntology.Server;
import ontologyRepresentations.greenContextOntology.Task;
import jade.core.Agent;

import java.io.IOException;

import utils.workLoadGenerator.TaskLifeManager;

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


    public RemoveTaskFromServerCommand(DatacenterProtegeFactory protegeFactory, String taskName, String serverName) {
        super(protegeFactory);
        this.taskName = taskName;
        this.serverName = serverName;
        cost = 300;
    }

    public void execute(OntModel model) {
        Server server = protegeFactory.getServer(serverName);
        Task task = protegeFactory.getTask(taskName);
        task.setAssociatedServer(null);
        server.removeRunningTask(task, model);
    }

    public void rewind(OntModel model) {
        Server server = protegeFactory.getServer(serverName);
        Task task = protegeFactory.getTask(taskName);
        task.setAssociatedServer(server);
        server.addRunningTask(task, model);
    }

    public String toString() {
        String description;
        description = "Remove task \"" + taskName.split("#")[1] + "\" from server \"" + serverName.split("#")[1];
        return description;
    }

    public void executeOnWebService() {
        Server server = protegeFactory.getServer(serverName);
        Task task = protegeFactory.getTask(taskName);
        ServerManagementProxyInterface proxy = ProxyFactory.createServerManagementProxy(server.getServerIPAddress());

        if (proxy != null) {
            //TODO:proxy.deleteVirtualMachine(vmName);
            proxy.stopVirtualMachine(task.getLocalName());
            proxy.deleteVirtualMachine(task.getLocalName());
        } else {
            System.err.println("Proxy is null");
        }
         TaskLifeManager.stopTaskTimer(task);
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
            X3DMessageDispatcher.sendMessage(agent, GlobalVars.X3DAGENT_NAME, new Object[]{X3DAgent.REMOVE_TASK_COMMAND, taskName.split("#")[1]});
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void rewindOnX3D(Agent agent) {
        Server server = protegeFactory.getServer(serverName);

        try {
            X3DMessageDispatcher.sendMessage(agent, GlobalVars.X3DAGENT_NAME, new Object[]{X3DAgent.ADD_TASK_COMMAND, taskName.split("#")[1], serverName.split("#")[1], server.getRunningTasks().size() + 1});
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
