/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package actionselection.command;

import greenContextOntology.ProtegeFactory;
import greenContextOntology.Server;

/**
 *
 * @author Me
 */
public class SendServerToLowPowerStateCommand extends Command {

    private String serverName;

    public SendServerToLowPowerStateCommand(ProtegeFactory protegeFactory, String serverName) {
        super(protegeFactory);
        this.serverName = serverName;
    }

    /**
     * Sets the lowPowerState property of a Server to <code>true</code>
     */
    @Override
    public void execute() {
        Server server = protegeFactory.getServer(serverName);
        server.setLowPowerState(true);
    }


    @Override
    public void rewind() {
        Server server = protegeFactory.getServer(serverName);
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
}
