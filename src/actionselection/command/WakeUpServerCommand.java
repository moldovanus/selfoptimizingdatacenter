/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package actionselection.command;

import greenContextOntology.ProtegeFactory;
import greenContextOntology.Server;
import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

import contextawaremodel.GlobalVars;
import contextawaremodel.agents.X3DAgent;
import com.hp.hpl.jena.ontology.OntModel;
import actionselection.utils.X3DMessageSender;

/**
 * @author Me
 */
public class WakeUpServerCommand extends SelfOptimizingCommand {

    private String serverName;

    public WakeUpServerCommand(ProtegeFactory protegeFactory, String serverName) {
        super(protegeFactory);
        this.serverName = serverName;
        cost = 4;
    }

    /**
     * Sets the lowPowerState property of a Server to <code>true</code>
     */
    @Override
    public void execute(OntModel model) {
        Server server = protegeFactory.getServer(serverName);
        server.setIsInLowPowerState(false, model);
    }

    @Override
    public void rewind(OntModel model) {
        Server server = protegeFactory.getServer(serverName);
        server.setIsInLowPowerState(true, model);
    }

    @Override
    public String toString() {
        String description;
        description = "Wake up server \"" + serverName.split("#")[1] + "\"";
        return description;
    }

    @Override
    public void executeOnWebService() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] toStringArray() {
        String[] array = new String[3];
        array[0] = "Wake up";
        array[1] = serverName.split("#")[1];
        array[2] = "from low power state";
        return array;
    }

    public void executeOnX3D(Agent agent) {
        try {
            X3DMessageSender.sendX3DMessage(agent,new Object[]{X3DAgent.WAKE_UP_SERVER_COMMAND, serverName.split("#")[1]});
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void rewindOnX3D(Agent agent) {
        try {
            X3DMessageSender.sendX3DMessage(agent,new Object[]{X3DAgent.SEND_SERVER_TO_LOW_POWER_COMMAND, serverName.split("#")[1]});
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
