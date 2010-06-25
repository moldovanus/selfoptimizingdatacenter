package actionEnforcement.command.selfOptimizingCommand;

import com.hp.hpl.jena.ontology.OntModel;
import contextaware.GlobalVars;
import contextaware.agents.X3DAgent;
import contextaware.worldInterface.datacenterInterface.proxies.ServerManagementProxyInterface;
import contextaware.worldInterface.datacenterInterface.proxies.impl.ProxyFactory;
import jade.core.Agent;
import ontologyRepresentations.greenContextOntology.*;
import utils.X3DMessageDispatcher;
import utils.workLoadGenerator.TaskLifeManager;

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


    public DeployNegotiatedTaskCommand(DatacenterProtegeFactory protegeFactory, String serverName, String taskName, int negotiatedCPU, int negotiatedMemory, int negotiatedStorage) {
        super(protegeFactory);
        this.serverName = serverName;
        this.taskName = taskName;
        Task task = protegeFactory.getTask(taskName);
        RequestedTaskInfo taskInfo = task.getRequestedInfo();
        this.negotiatedCPU = (negotiatedCPU != 0) ? negotiatedCPU : taskInfo.getCpuMaxAcceptableValue();
        this.negotiatedMemory = (negotiatedMemory != 0) ? negotiatedMemory : taskInfo.getMemoryMaxAcceptableValue();
        this.negotiatedStorage = (negotiatedStorage != 0) ? negotiatedStorage : taskInfo.getStorageMaxAcceptableValue();
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
        server.removeRunningTask(task, model);
    }

    @Override
    public String toString() {
        String description;
        description = "Negotiated: memory " + negotiatedMemory + " cpu " + negotiatedCPU + " storage " + negotiatedStorage;
        description += "Deploy task \"" + taskName.split("#")[1] + "\" to server \"" + serverName.split("#")[1] + "\"";
        return description;
    }

    @Override
    public void executeOnWebService() {
        Server server = protegeFactory.getServer(serverName);
        Task task = protegeFactory.getTask(taskName);
        ServerManagementProxyInterface proxy = ProxyFactory.createServerManagementProxy(server.getServerIPAddress());
        if (proxy != null) {
            int procTime = (negotiatedCPU * 100) / ((Core) server.getAssociatedCPU().getAssociatedCore().iterator().next()).getTotal();
            String path = (String) server.getVirtualMachinesPath().iterator().next();
            System.out.println("Deploying ...");
            proxy.deployVirtualMachineWithCustomResources("\\\\WINDOWS-L90ZRJH\\SharedStorage",
                    "\\\\WINDOWS-L90ZRJH\\SharedStorage\\" + server.getServerName(),
                    task.getTaskName(), task.getLocalName(), negotiatedMemory, procTime, task.getRequestedInfo().getCores());

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
            X3DMessageDispatcher.sendMessage(agent, GlobalVars.X3DAGENT_NAME, new Object[]{X3DAgent.ADD_TASK_COMMAND, taskName.split("#")[1], serverName.split("#")[1], server.getRunningTasks().size() + 1});


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
