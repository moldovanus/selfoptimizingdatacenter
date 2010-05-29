package actionselection.command.selfOptimizingCommand;

import com.hp.hpl.jena.ontology.OntModel;
import greenContextOntology.ProtegeFactory;
import greenContextOntology.Server;
import greenContextOntology.Task;
import jade.core.Agent;
import negotiator.Negotiator;

import java.util.Map;

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
        Map<String, Double> negotiatedValues = negotiator.negotiate(server, task);
        int negotiatedCPU = (negotiatedValues.containsKey(Negotiator.NEGOTIATED_CPU))? negotiatedValues.get(Negotiator.NEGOTIATED_CPU).intValue() : 0;
        int negotiatedMemory = (negotiatedValues.containsKey(Negotiator.NEGOTIATED_MEMORY))? negotiatedValues.get(Negotiator.NEGOTIATED_MEMORY).intValue() : 0;
        int negotiatedStorage = (negotiatedValues.containsKey(Negotiator.NEGOTIATED_STORAGE))? negotiatedValues.get(Negotiator.NEGOTIATED_STORAGE).intValue() : 0;

        server.addNegotiatedTasks(task, null, (int) negotiatedCPU, (int) negotiatedMemory, (int) negotiatedStorage);

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
