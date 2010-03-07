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
public class MoveTaskCommand extends Command {

    private String oldServerName;
    private String newServerName;
    private String taskName;

    public MoveTaskCommand(ProtegeFactory protegeFactory, String oldServerName, String newServerName, String taskName) {
        super(protegeFactory);
        this.oldServerName = oldServerName;
        this.newServerName = newServerName;
        this.taskName = taskName;
        cost = 2;
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
        oldServer.addRunningTasks(task);
        newServer.removeRunningTasks(task);
    }

    @Override
    public String toString() {
        String description;
        description = "Move task\"" + taskName + "\" from server \"" + oldServerName
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
}
