/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package actionselection.command;

import greenContextOntology.ProtegeFactory;
import greenContextOntology.Server;
import greenContextOntology.Task;

/**
 *
 * @author Me
 */
public class DeployTaskCommand extends Command {

    private String serverName;
    private String taskName;
    private String oldServerName;

    public DeployTaskCommand(ProtegeFactory protegeFactory, String serverName, String taskName) {
        super(protegeFactory);
        this.serverName = serverName;
        this.taskName = taskName;
    }

    /**
     * Sets the task <code>taskName</code> associated server to <code>serverName</code> 
     */
    @Override
    public void execute() {
        Server server = protegeFactory.getServer(serverName);
        Task task = protegeFactory.getTask(taskName);
        oldServerName = task.getAssociatedServer().getName();
        task.setAssociatedServer(server);
        server.addRunningTasks(task);
    }

    /**
     * Implements Undo capability
     */
    @Override
    public void rewind() {
        Server server = protegeFactory.getServer(oldServerName);
        Task task = protegeFactory.getTask(taskName);
        task.setAssociatedServer(server);
        server.removeRunningTasks(task);
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
}
