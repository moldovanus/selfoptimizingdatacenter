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

    private boolean wasSleeping;


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

        if (negotiatedValues.size() == 0) {
            //System.err.println("Nothing found after negotiation");
            return;
        }
        int negotiatedCPU = (negotiatedValues.containsKey(Negotiator.NEGOTIATED_CPU)) ? negotiatedValues.get(Negotiator.NEGOTIATED_CPU).intValue() : 0;
        int negotiatedMemory = (negotiatedValues.containsKey(Negotiator.NEGOTIATED_MEMORY)) ? negotiatedValues.get(Negotiator.NEGOTIATED_MEMORY).intValue() : 0;
        int negotiatedStorage = (negotiatedValues.containsKey(Negotiator.NEGOTIATED_STORAGE)) ? negotiatedValues.get(Negotiator.NEGOTIATED_STORAGE).intValue() : 0;


        DeployNegotiatedTaskCommand deployNegotiatedTaskCommand = new DeployNegotiatedTaskCommand(protegeFactory, serverName, taskName,
                negotiatedCPU, negotiatedMemory, negotiatedStorage);
        deployNegotiatedTaskCommand.execute(model);
        System.out.println(deployNegotiatedTaskCommand.toString());
        if (server.getIsInLowPowerState()) {
            SelfOptimizingCommand wakeUp = new WakeUpServerCommand(protegeFactory, serverName);
            wakeUp.execute(model);
            System.out.println(wakeUp.toString());
            wasSleeping = true;
        }

        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void rewind(OntModel model) {
//        Server server = protegeFactory.getServer(serverName);
//        server.resetOptimumValues();

        /* RemoveTaskFromServerCommand c = new RemoveTaskFromServerCommand(protegeFactory, taskName, serverName);
        c.execute(model);
        if (wasSleeping) {
            wasSleeping = false;
            SelfOptimizingCommand wakeUp = new SendServerToLowPowerStateCommand(protegeFactory, serverName);
            wakeUp.execute(model);
        }*/

    }

    public String toString() {
        String description;
        description = "Negotiate and deploy max resources acceptable values between task " + taskName + " and server \"" + serverName.split("#")[1];
        return description;
    }

    public void executeOnWebService() {
        Server server = protegeFactory.getServer(serverName);
        Task task = protegeFactory.getTask(taskName);
        Map<String, Double> negotiatedValues = negotiator.negotiate(server, task);

        if (negotiatedValues.size() == 0) {
            //System.err.println("Nothing found after negotiation");
            return;
        }
        int negotiatedCPU = (negotiatedValues.containsKey(Negotiator.NEGOTIATED_CPU)) ? negotiatedValues.get(Negotiator.NEGOTIATED_CPU).intValue() : 0;
        int negotiatedMemory = (negotiatedValues.containsKey(Negotiator.NEGOTIATED_MEMORY)) ? negotiatedValues.get(Negotiator.NEGOTIATED_MEMORY).intValue() : 0;
        int negotiatedStorage = (negotiatedValues.containsKey(Negotiator.NEGOTIATED_STORAGE)) ? negotiatedValues.get(Negotiator.NEGOTIATED_STORAGE).intValue() : 0;


        DeployNegotiatedTaskCommand deployNegotiatedTaskCommand = new DeployNegotiatedTaskCommand(protegeFactory, serverName, taskName,
                negotiatedCPU, negotiatedMemory, negotiatedStorage);
        deployNegotiatedTaskCommand.executeOnWebService();
        System.out.println(deployNegotiatedTaskCommand.toString());

        if (server.getIsInLowPowerState()) {
            SelfOptimizingCommand wakeUp = new WakeUpServerCommand(protegeFactory, serverName);
            wakeUp.executeOnWebService();
            System.out.println(wakeUp);
            wasSleeping = true;
        }
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
