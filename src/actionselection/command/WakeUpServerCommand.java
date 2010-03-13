/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package actionselection.command;

import greenContextOntology.ProtegeFactory;
import greenContextOntology.Server;
import jade.core.Agent;

/**
 *
 * @author Me
 */
public class WakeUpServerCommand extends Command {

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
    public void execute() {
        Server server = protegeFactory.getServer(serverName);
        server.setLowPowerState(false);
    }

    @Override
    public void rewind() {
        Server server = protegeFactory.getServer(serverName);
        server.setLowPowerState(true);
    }

    @Override
    public String toString() {
        String description;
        description = "Wake up server \"" + serverName + "\"";
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
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void rewindOnX3D(Agent agent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
