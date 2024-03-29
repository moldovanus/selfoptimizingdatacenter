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
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Me
 */
public class SendServerToLowPowerStateCommand extends SelfOptimizingCommand {

    private String serverName;
    private Collection oldTasks;

    public SendServerToLowPowerStateCommand(DatacenterProtegeFactory protegeFactory, String serverName) {
        super(protegeFactory);
        this.serverName = serverName;
        cost = 400;
    }

    /**
     * Sets the lowPowerState property of a Server to <code>true</code>
     */
    @Override
    public void execute(OntModel model) {
        Server server = protegeFactory.getServer(serverName);
        oldTasks = server.getRunningTasks();
        Iterator iterator = oldTasks.iterator();
        while (iterator.hasNext()) {
            server.removeRunningTask((Task) iterator.next(), model);
        }
        server.setIsInLowPowerState(true, model);

    }


    @Override
    public void rewind(OntModel model) {
        Server server = protegeFactory.getServer(serverName);
        Iterator iterator = oldTasks.iterator();
        while (iterator.hasNext()) {
            server.addRunningTask((Task) iterator.next(), model);
        }
        server.setIsInLowPowerState(false, model);
    }

    @Override
    public String toString() {
        String description;
        description = "Send server \"" + serverName.split("#")[1] + "\" to low power state ";
        return description;
    }


    @Override
    public void executeOnWebService() {
        Server server = protegeFactory.getServer(serverName);
        ServerManagementProxyInterface proxy = ProxyFactory.createServerManagementProxy(server.getServerIPAddress());
        if (proxy != null) {
            proxy.sendServerToSleep();
        } else {
            System.err.println("Proxy is null");
        }
    }

    @Override
    public String[] toStringArray() {
        String[] array = new String[3];
        array[0] = "Send";
        array[1] = serverName.split("#")[1];
        array[2] = "to low power state";
        return array;
    }

    public void executeOnX3D(Agent agent) {
        try {
            X3DMessageDispatcher.sendMessage(agent, GlobalVars.X3DAGENT_NAME, new Object[]{X3DAgent.SEND_SERVER_TO_LOW_POWER_COMMAND, serverName.split("#")[1]});
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void rewindOnX3D(Agent agent) {
        try {
            X3DMessageDispatcher.sendMessage(agent, GlobalVars.X3DAGENT_NAME, new Object[]{X3DAgent.WAKE_UP_SERVER_COMMAND, serverName.split("#")[1]});
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
