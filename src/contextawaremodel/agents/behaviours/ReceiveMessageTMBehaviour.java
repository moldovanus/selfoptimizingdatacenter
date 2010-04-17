package contextawaremodel.agents.behaviours;

import contextawaremodel.agents.ReinforcementLearningAgent;
import contextawaremodel.agents.TaskManagementAgent;
import contextawaremodel.GlobalVars;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import greenContextOntology.ProtegeFactory;
import greenContextOntology.Task;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import actionselection.command.RemoveTaskFromServerCommand;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: Apr 15, 2010
 * Time: 5:42:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReceiveMessageTMBehaviour extends CyclicBehaviour {

    private TaskManagementAgent agent;

    public ReceiveMessageTMBehaviour(Agent agent) {
        super(agent);
        this.agent=(TaskManagementAgent)agent;
    }

    @Override
    public void action() {
        ACLMessage message = agent.receive();
        if (message == null) {
            return;
        }

        try {
            switch (message.getPerformative()) {
                case ACLMessage.INFORM_REF:
                    String mes = message.getContent();
                    String parts[]= mes.split("/");
                    String names[]= new String[parts.length];
                    String cpuReceived[]= new String[parts.length];
                    String memoryReceived[]= new String[parts.length];
                    String storageReceived[]= new String[parts.length];

                     String minCpuRequested[]= new String[parts.length];
                    String minMemoryRequested[]= new String[parts.length];
                    String minStorageRequested[]= new String[parts.length];
                    String maxCpuRequested[]= new String[parts.length];
                    String maxMemoryRequested[]= new String[parts.length];
                    String maxStorageRequested[]= new String[parts.length];
                    int index=0;
                    System.out.println("ala male !!!!!!!!" + mes);
                    for (String s: parts)
                    {
                        System.out.println(s);
                        String str[]=s.split("#");
                        names[index]=str[0];
                        minCpuRequested[index] = str[1] ;
                        maxCpuRequested[index] = str[2] ;
                        minMemoryRequested[index] = str[3];
                        maxMemoryRequested[index] = str[4] ;
                        minStorageRequested[index] = str[5] ;
                        maxStorageRequested[index] = str[6];
                        cpuReceived[index] = str[7] ;
                        memoryReceived[index] = str[8];
                        storageReceived[index] = str[9];
                        index++;
                    }
                    agent.clear();
                    agent.populateTaskWindow(names,minCpuRequested,maxCpuRequested,minMemoryRequested,maxMemoryRequested,minStorageRequested,maxStorageRequested,cpuReceived,memoryReceived,storageReceived);
                    break;

                case ACLMessage.INFORM:

                    break;

                case ACLMessage.SUBSCRIBE:
                   
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
}
