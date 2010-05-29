package actionselection.command.selfOptimizingCommand;

import com.hp.hpl.jena.ontology.OntModel;
import greenContextOntology.ProtegeFactory;
import greenContextOntology.ReceivedTaskInfo;
import greenContextOntology.Server;
import greenContextOntology.Task;
import jade.core.Agent;
import negotiator.Negotiator;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Apr 17, 2010
 * Time: 11:29:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class NegotiateResourcesCommand extends SelfOptimizingCommand {
    private Negotiator negotiator;
    private String serverName;
    private String taskName;

    public NegotiateResourcesCommand(ProtegeFactory protegeFactory, Negotiator negotiator, String serverName, String taskName) {
        super(protegeFactory);
        cost = 0;
        this.negotiator = negotiator;
        this.serverName = serverName;
        this.taskName = taskName;
    }

    public void execute(OntModel model) {
        Server server = protegeFactory.getServer(serverName);
        Task task = protegeFactory.getTask(taskName);
        negotiator.negotiate(server, task);
        ReceivedTaskInfo rti = task.getReceivedInfo();

        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void rewind(OntModel model) {
        Server server = protegeFactory.getServer(serverName);
        server.resetOptimumValues();

        RemoveTaskFromServerCommand c = new RemoveTaskFromServerCommand(protegeFactory, taskName, serverName);
        c.execute(model);

    }

    public String toString() {
        String description;
        description = "Negotiate resources between tasks on server \"" + serverName.split("#")[1];
        return description;
    }

    public void executeOnWebService() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String[] toStringArray() {
        String[] array = new String[3];
        array[0] = "Negotiate";
        array[1] = "tasks on";
        array[2] = serverName.split("#")[1];
        return array;
    }

    public void executeOnX3D(Agent agent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void rewindOnX3D(Agent agent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
