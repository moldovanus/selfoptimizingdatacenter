/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package actionselection.command;

import greenContextOntology.ProtegeFactory;
import greenContextOntology.Server;
import greenContextOntology.Task;

import java.util.Collection;
import java.util.Iterator;
import java.io.IOException;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import contextawaremodel.GlobalVars;

/**
 * @author Me
 */
public class SendServerToLowPowerStateCommand extends Command {

    private String serverName;
    private Collection oldTasks;

    public SendServerToLowPowerStateCommand(ProtegeFactory protegeFactory, String serverName) {
        super(protegeFactory);
        this.serverName = serverName;
        cost =2;
    }

    /**
     * Sets the lowPowerState property of a Server to <code>true</code>
     */
    @Override
    public void execute() {
        Server server = protegeFactory.getServer(serverName);
        oldTasks = server.getRunningTasks();
        Iterator iterator = oldTasks.iterator();
        while (iterator.hasNext()) {
            server.removeRunningTasks((Task) iterator.next());
        }
        server.setLowPowerState(true);

    }


    @Override
    public void rewind() {
        Server server = protegeFactory.getServer(serverName);
        Iterator iterator = oldTasks.iterator();
        while (iterator.hasNext()) {
            server.addRunningTasks((Task) iterator.next());
        }
        server.setLowPowerState(false);
    }

    @Override
    public String toString() {
        String description;
        description = "Send server \"" + serverName + "\" to low power state ";
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
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        try {
            message.setContentObject(new Object[]{"sendToLowPower",serverName});
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
            message.setContentObject(new Object[]{"wakeUpServer",serverName});
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        message.addReceiver(new AID(GlobalVars.X3DAGENT_NAME + "@" + agent.getContainerController().getPlatformName()));
        message.setLanguage("JavaSerialization");
        agent.send(message);
    }
}
