package actionselection.command.selfOptimizingCommand;

import com.hp.hpl.jena.ontology.OntModel;
import greenContextOntology.ProtegeFactory;
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

    public NegotiateResourcesCommand(ProtegeFactory protegeFactory, Negotiator negotiator, String serverName) {
        super(protegeFactory);
        cost = 5;
        this.negotiator = negotiator;
        this.serverName = serverName;
    }

    public void execute(OntModel model) {
        Server server = protegeFactory.getServer(serverName);

        Collection<Task> tasks = server.getRunningTasks();
        for (Task task : tasks) {
            negotiator.negotiate(server, task);
           /* server.changeOptimumCPURange((int) result[0]);
            server.changeOptimumMemoryRange((int) result[1]);
            server.changeOptimumStorageRange((int) result[2]);*/
            //server.giveMoreResourcesToTask(result, task, model);

        }
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void rewind(OntModel model) {
        Server server = protegeFactory.getServer(serverName);
        server.resetOptimumValues();
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
}
