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
import contextawaremodel.agents.X3DAgent;
import com.hp.hpl.jena.ontology.OntModel;

/**
 * @author Me
 */
public class MoveTaskCommand extends SelfOptimizingCommand {

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
    public void execute(OntModel model) {
        Server oldServer = protegeFactory.getServer(oldServerName);
        Server newServer = protegeFactory.getServer(newServerName);
        Task task = protegeFactory.getTask(taskName);
        task.setAssociatedServer(newServer);
        oldServer.removeRunningTasks(task, model);
        newServer.addRunningTasks(task, model);
    }

    @Override
    public void rewind(OntModel model) {
        Server oldServer = protegeFactory.getServer(oldServerName);
        Server newServer = protegeFactory.getServer(newServerName);
        Task task = protegeFactory.getTask(taskName);
        task.setAssociatedServer(oldServer);
        newServer.removeRunningTasks(task, model);
        oldServer.addRunningTasks(task, model);

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
        throw new UnsupportedOperationException("Not supported yet.");
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

        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        try {
            message.setContentObject(new Object[]{X3DAgent.MOVE_TASK_COMMAND, taskName.split("#")[1], newServerName.split("#")[1], server.getRunningTasks().size() + 1});
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
            message.setContentObject(new Object[]{X3DAgent.REMOVE_TASK_COMMAND, taskName.split("#")[1]});
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        message.addReceiver(new AID(GlobalVars.X3DAGENT_NAME + "@" + agent.getContainerController().getPlatformName()));
        message.setLanguage("JavaSerialization");
        agent.send(message);


        Server server = protegeFactory.getServer(oldServerName);
        ACLMessage deployMessage = new ACLMessage(ACLMessage.INFORM);
        try {
            deployMessage.setContentObject(new Object[]{X3DAgent.ADD_TASK_COMMAND, taskName.split("#")[1], oldServerName.split("#")[1], server.getRunningTasks().size() + 1});
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        deployMessage.addReceiver(new AID(GlobalVars.X3DAGENT_NAME + "@" + agent.getContainerController().getPlatformName()));
        deployMessage.setLanguage("JavaSerialization");
        agent.send(deployMessage);

    }
}
