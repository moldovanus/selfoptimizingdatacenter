/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package actionselection.command.selfOptimizingCommand;

import greenContextOntology.ProtegeFactory;
import greenContextOntology.Server;
import greenContextOntology.Task;
import jade.core.Agent;

import java.io.IOException;

import contextawaremodel.agents.X3DAgent;
import com.hp.hpl.jena.ontology.OntModel;
import actionselection.utils.X3DMessageSender;

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
        cost = 1;
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
        throw new UnsupportedOperationException("Not supported yet.");
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
            X3DMessageSender.sendX3DMessage(agent, new Object[]{X3DAgent.ADD_TASK_COMMAND, taskName.split("#")[1], serverName.split("#")[1], server.getRunningTasks().size() + 1});
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    public void rewindOnX3D(Agent agent) {
        try {
            X3DMessageSender.sendX3DMessage(agent, new Object[]{X3DAgent.REMOVE_TASK_COMMAND, taskName.split("#")[1]});
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}