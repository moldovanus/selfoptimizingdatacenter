/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package actionselection.command;

import greenContextOntology.ProtegeFactory;
import greenContextOntology.Server;
import greenContextOntology.Task;
import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

import contextawaremodel.GlobalVars;

/**
 * @author Me
 */
public class MoveTaskCommand extends Command {

    private String oldServerName;
    private String newServerName;
    private String taskName;

    public MoveTaskCommand(ProtegeFactory protegeFactory, String oldServerName, String newServerName, String taskName) {
        super(protegeFactory);
        this.oldServerName = oldServerName;
        this.newServerName = newServerName;
        this.taskName = taskName;
        cost = 3;
    }

    /**
     * Moves the task
     */
    @Override
    public void execute() {
        Server oldServer = protegeFactory.getServer(oldServerName);
        Server newServer = protegeFactory.getServer(newServerName);
        Task task = protegeFactory.getTask(taskName);
        task.setAssociatedServer(newServer);
        oldServer.removeRunningTasks(task);
        newServer.addRunningTasks(task);
    }

    @Override
    public void rewind() {
        Server oldServer = protegeFactory.getServer(oldServerName);
        Server newServer = protegeFactory.getServer(newServerName);
        Task task = protegeFactory.getTask(taskName);
        task.setAssociatedServer(oldServer);
        newServer.removeRunningTasks(task);
        oldServer.addRunningTasks(task);

    }

    @Override
    public String toString() {
        String description;
        description = "Move task \"" + taskName + "\" from server \"" + oldServerName
                + "\" to server \"" + newServerName;
        return description;
    }

    @Override
    public void executeOnWebService() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] toStringArray() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void executeOnX3D(Agent agent) {
        Server server = protegeFactory.getServer(newServerName);

        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        try {
            message.setContentObject(new Object[]{"moveTask",taskName, newServerName, server.getRunningTasks().size() + 1});
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        message.addReceiver(new AID(GlobalVars.X3DAGENT_NAME + "@" + agent.getContainerController().getPlatformName()));
        message.setLanguage("JavaSerialization");
        agent.send(message);
    }


    public void rewindOnX3D(Agent agent) {


        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        try {
            message.setContentObject(new Object[]{"removeTask",taskName});
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        message.addReceiver(new AID(GlobalVars.X3DAGENT_NAME + "@" + agent.getContainerController().getPlatformName()));
        message.setLanguage("JavaSerialization");
        agent.send(message);
    }
}
