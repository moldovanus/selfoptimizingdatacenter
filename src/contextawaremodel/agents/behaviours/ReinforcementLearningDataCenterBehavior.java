/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contextawaremodel.agents.behaviours;

import actionselection.command.Command;
import actionselection.context.ContextSnapshot;
import actionselection.context.Memory;
import actionselection.gui.ActionsOutputFrame;
import actionselection.utils.Pair;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import contextawaremodel.GlobalVars;
import contextawaremodel.agents.ReinforcementLearningAgent;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import greenContextOntology.EnergyPolicy;
import greenContextOntology.Policy;
import greenContextOntology.ProtegeFactory;
import greenContextOntology.QoSPolicy;
import greenContextOntology.Received;
import greenContextOntology.Requested;
import greenContextOntology.Server;
import greenContextOntology.Task;
import greenContextOntology.impl.DefaultQoSPolicy;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.util.Collection;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 * @author Administrator
 */
public class ReinforcementLearningDataCenterBehavior extends TickerBehaviour {

    private OWLModel contextAwareModel;
    private OntModel policyConversionModel;
    private JenaOWLModel owlModel;
    private double smallestEntropy = 10000;
    private Memory memory;
    private Property evaluatePolicyProperty;
    private ActionsOutputFrame resultsFrame;
    private ReinforcementLearningAgent agent;
    private boolean contextBroken = false;
    private ProtegeFactory protegeFactory;

    public ReinforcementLearningDataCenterBehavior(Agent a, int interval, OWLModel contextAwareModel, OntModel policyConversionModel, JenaOWLModel owlModel, Memory memory) {
        super(a, interval);
        agent = (ReinforcementLearningAgent) a;
        this.contextAwareModel = contextAwareModel;
        this.policyConversionModel = policyConversionModel;
        protegeFactory = new ProtegeFactory(owlModel);

        Server server = protegeFactory.getServer("Server_1");
        Task task = protegeFactory.createTask("ddd");
        Requested r = protegeFactory.createRequested("RequestedInstance");
        r.setCpu(1000);
        r.setCores(1);
        r.setMemory(256);
        r.setStorage(35);
        Received re = protegeFactory.createReceived("ReceivedInstance");
        task.addAssociatedInfo(r);
        task.addAssociatedInfo(re);
        server.addRunningTasks(task);
        


        this.owlModel = owlModel;
        resultsFrame = new ActionsOutputFrame();
        this.memory = memory;
        evaluatePolicyProperty = policyConversionModel.getDatatypeProperty(GlobalVars.base + "#EvaluatePolicyP");
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                resultsFrame.setVisible(true);

            }
        });
    }

    private Pair<Double, Policy> computeEntropy() {
        Policy brokenPolicy = null ;
        double entropy = 0.0;
        Collection<QoSPolicy> qosPolicies = protegeFactory.getAllQoSPolicyInstances();
        for (QoSPolicy policy : qosPolicies) {
                if (!policy.getRespected()) {
                    if (brokenPolicy == null) {
                        brokenPolicy = policy;
                    }
                    entropy++;            
                }
        }
        Collection<EnergyPolicy> policies = protegeFactory.getAllEnergyPolicyInstances();
        for (EnergyPolicy policy : policies) {
                if (!policy.getRespected()) {
                    if (brokenPolicy == null) {
                        brokenPolicy = policy;
                    }
                    entropy++;
                }
        }
        return new Pair<Double, Policy>(entropy, brokenPolicy);
    }

    private double computeRewardFunction(ContextSnapshot previous, ContextSnapshot current, Command c) {
        double function = 0.0d;
        if (previous != null) {
            function += previous.getRewardFunction();
            function += ContextSnapshot.gamma*(previous.getContextEntropy() - current.getContextEntropy()+c.getCost());

        } else {
            function -= current.getContextEntropy();
        }

        return function;
    }

    @Override
    protected void onTick() {
     
        Queue<ContextSnapshot> queue = new PriorityQueue<ContextSnapshot>();
        ContextSnapshot initialContext = new ContextSnapshot(new LinkedList<Command>());
        Pair <Double, Policy> entropyAndPolicy = computeEntropy();
        initialContext.setContextEntropy(entropyAndPolicy.getFirst());
        initialContext.setRewardFunction(computeRewardFunction(null,initialContext,null));
        queue.add(initialContext);
        resultsFrame.setActionsList(null);
      
        if (entropyAndPolicy.getSecond()!=null){
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA"+entropyAndPolicy.getSecond());
        Policy policy =entropyAndPolicy.getSecond();
        if (policy.getClass().equals(DefaultQoSPolicy.class))
        {
            Collection c = policy.getReferences();
           Task t= (Task) policy.getReferences(0);
        }
        }
        

    }
}
