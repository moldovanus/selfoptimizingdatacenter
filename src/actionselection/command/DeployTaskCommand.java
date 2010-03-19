/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package actionselection.command;

import greenContextOntology.ProtegeFactory;
import greenContextOntology.Server;
import greenContextOntology.Task;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import jade.core.Agent;

import java.io.IOException;

import contextawaremodel.GlobalVars;
import com.hp.hpl.jena.ontology.OntModel;

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
        server.addRunningTasks(task,model);
    }

    /**
     * Implements Undo capability
     */
    @Override
    public void rewind(OntModel model) {
        Server server = protegeFactory.getServer(serverName);
        Task task = protegeFactory.getTask(taskName);
        server.removeRunningTasks(task,model);
    }

    @Override
    public String toString() {
        String description;
        description = "Deploy task \"" + taskName + "\" to server \"" + serverName + "\"";
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
        Server server = protegeFactory.getServer(serverName);

        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        try {
            message.setContentObject(new Object[]{"addTask",taskName, serverName, server.getRunningTasks().size() + 1});
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
