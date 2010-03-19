/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contextawaremodel.agents.behaviours;

import actionselection.command.*;
import actionselection.context.ContextSnapshot;
import actionselection.context.Memory;
import actionselection.gui.ActionsOutputFrame;
import actionselection.utils.Pair;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Statement;
import contextawaremodel.GlobalVars;
import contextawaremodel.agents.ReinforcementLearningAgent;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import greenContextOntology.*;
import greenContextOntology.impl.DefaultQoSPolicy;
import greenContextOntology.impl.DefaultServer;
import greenContextOntology.impl.DefaultTask;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
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
        evaluatePolicyProperty = policyConversionModel.getDatatypeProperty("http://www.owl-ontologies.com/Datacenter.owl#respected");
        this.contextAwareModel = contextAwareModel;
        this.policyConversionModel = policyConversionModel;
        protegeFactory = new ProtegeFactory(owlModel);
        this.owlModel = owlModel;
        resultsFrame = new ActionsOutputFrame();
        this.memory = memory;


        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                resultsFrame.setVisible(true);

            }
        });
    }

    // function for computing the contribution to the entropy of task @param task
    private double taskRespectanceDegree(Task task) {
        TaskInfo received = task.getReceivedInfo();
        TaskInfo requested = task.getRequestedInfo();
        double respectance = task.getCpuWeight() * (requested.getCores() - received.getCores() + requested.getCpu() - received.getCpu()) + task.getMemoryWeight() * (requested.getMemory() - received.getMemory()) + task.getStorageWeight() * (requested.getStorage() - received.getStorage());
        return respectance;
    }

    /*private double energyRespectanceDegree(Server server) {
        double respectance = 0.0;

        CPU cpu = server.getAssociatedCPU();
        greenContextOntology.Memory memory = server.getAssociatedMemory();
        Storage storage = server.getAssociatedStorage();
        double cpuCores = 0.0;
        Collection<Component> cores = cpu.getAssociatedCore();
        double diff = 0.0;
        for (Component core : cores) {
            diff = core.getUsed() - core.getOptimum();
            if (diff > 0)
                cpuCores += diff;
        }
        cpuCores /= cores.size();
        respectance += cpu.getWeight() * cpuCores;
        diff = memory.getUsed() - memory.getOptimum();
        if (diff > 0)
            respectance += memory.getWeight() * diff;
        diff = storage.getUsed() - storage.getOptimum();
        if (diff > 0)
            respectance += storage.getWeight() * diff;
        return respectance;
    }*/

    private Pair<Double, Policy> computeEntropy() {
        Policy brokenPolicy = null;
        double entropy = 0.0;
        Collection<QoSPolicy> qosPolicies = protegeFactory.getAllQoSPolicyInstances();

        for (QoSPolicy policy : qosPolicies) {
            Task task = policy.getReferenced();
            //if (!Policy.getRespected()) {
            if (!task.requestsSatisfied()) {
                if (brokenPolicy == null) {
                    brokenPolicy = policy;
                }

                if (policy.hasPriority())
                    entropy += policy.getPriority() * taskRespectanceDegree(task);
            }
        }

        Collection<EnergyPolicy> policies = protegeFactory.getAllEnergyPolicyInstances();
        for (EnergyPolicy policy : policies) {
            Server server = policy.getReferenced();
            if (server.getLowPowerState()) {
                continue;
            }

            // if (getEvaluateProp( policyConversionModel.getIndividual(policy.getURI()) ) ){
            if (!policy.getRespected(policyConversionModel)) {
                if (brokenPolicy == null) {
                    brokenPolicy = policy;
                }
                if (policy.hasPriority())
                    // entropy+=policy.getPriority()*energyRespectanceDegree(server);
                    entropy += policy.getPriority();
            }
        }
        //System.out.println(" " + entropy);
        return new Pair<Double, Policy>(entropy, brokenPolicy);
    }

    private double computeRewardFunction(ContextSnapshot previous, ContextSnapshot current, Command c) {
        double function = 0.0d;
        if (previous != null) {
            function += previous.getRewardFunction();
            //TODO: solve -1 la infinit :P
            double temp = previous.getContextEntropy() - current.getContextEntropy() - c.getCost();
            function += ContextSnapshot.gamma * temp;
        } else {
            function -= current.getContextEntropy();
        }

        return function;
    }


    private ContextSnapshot reinforcementLearning(PriorityQueue<ContextSnapshot> queue) {

        ContextSnapshot newContext = queue.poll();
        System.out.println("---A");
        Collection<Server> servers = protegeFactory.getAllServerInstances();
        newContext.executeActions(policyConversionModel);
        Pair<Double, Policy> entropyAndPolicy = computeEntropy();
        /* if (entropyAndPolicy.getFirst() < 3) {
            for (int i = 0; i < 5; i++)
                System.err.println("\n ------------------------------------------------------------------------------");
        }*/
        System.out.println("\n" + entropyAndPolicy.getFirst() + "  " + newContext.getRewardFunction() + "\n");
        System.out.println("---B");
        DefaultTask task = null;
        DefaultServer server = null;

        if (entropyAndPolicy.getFirst() > 0) {
            if (entropyAndPolicy.getSecond() != null) {
                Policy policy = entropyAndPolicy.getSecond();
                if (policy instanceof QoSPolicy) {
                    task = (DefaultTask) policy.getReferenced();
                } else {
                    server = (DefaultServer) policy.getReferenced();
                }
            }
            Boolean deployed = false;     /// sa zica daca ii  deployed sau nu
            if (task != null) {
                // deploy actions
                for (Server serverInstance : servers) {
                    if (!serverInstance.getLowPowerState() && serverInstance.hasResourcesFor(task )
                            && !serverInstance.containsTask(task) && !task.isRunning()) {
                        Command newAction = new DeployTaskCommand(protegeFactory, serverInstance.getName(), task.getName());
                        ContextSnapshot cs = new ContextSnapshot(new LinkedList(newContext.getActions()));
                        deployed = true;
                        //verific peste tot daca nu cumva exista actiunea
                        if (!cs.getActions().contains(newAction)) {
                            cs.getActions().add(newAction);

                            newAction.execute(policyConversionModel);
                            //cs.executeActions();
                            Double afterExecuteEntropy = computeEntropy().getFirst();
                            cs.setContextEntropy(afterExecuteEntropy);
                            cs.setRewardFunction(computeRewardFunction(newContext, cs, newAction));
                            newAction.rewind(policyConversionModel);
                            //cs.rewind();
                            queue.add(cs);
                        }
                    }
                }

                // move actions
                Collection<Server> servers1 = protegeFactory.getAllServerInstances();
                for (Server serverInstance : servers) {
                    if (!serverInstance.getLowPowerState()) {
                        Iterator it = serverInstance.listRunningTasks();
                        while (it.hasNext()) {
                            Task myTask = (DefaultTask) it.next();
                            for (Server otherServerInstance : servers1) {
                                if (!otherServerInstance.getLowPowerState() && otherServerInstance.hasResourcesFor(myTask)
                                        && !otherServerInstance.containsTask(myTask)) {
                                    Command newAction = new MoveTaskCommand(protegeFactory, serverInstance.getName(), otherServerInstance.getName(), myTask.getName());
                                    ///de vazut daca a fost posibila
                                    ContextSnapshot cs = new ContextSnapshot(new LinkedList(newContext.getActions()));
                                    //verific peste tot daca nu cumva exista actiunea
                                    if (!cs.getActions().contains(newAction)) {
                                        cs.getActions().add(newAction);
                                        newAction.execute(policyConversionModel);
                                        //cs.executeActions();
                                        cs.setContextEntropy(computeEntropy().getFirst());
                                        cs.setRewardFunction(computeRewardFunction(newContext, cs, newAction));
                                        newAction.rewind(policyConversionModel);
                                        // cs.rewind();
                                        queue.add(cs);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (server != null) {
                Iterator it = server.listRunningTasks();
                // move tasks from server
                while (it.hasNext()) {
                    Task myTask = (DefaultTask) it.next();
                    for (Server serverInstance : servers) {
                        if (!serverInstance.getLowPowerState() && !serverInstance.containsTask(myTask)
                                && serverInstance.hasResourcesFor(myTask)) {
                            Command newAction = new MoveTaskCommand(protegeFactory, server.getName(), serverInstance.getName(), myTask.getName());
                            ContextSnapshot cs = new ContextSnapshot(new LinkedList(newContext.getActions()));

                            //verific peste tot daca nu cumva exista actiunea
                            if (!cs.getActions().contains(newAction)) {
                                cs.getActions().add(newAction);
                                //cs.executeActions();
                                newAction.execute(policyConversionModel);
                                cs.setContextEntropy(computeEntropy().getFirst());
                                cs.setRewardFunction(computeRewardFunction(newContext, cs, newAction));
                                newAction.rewind(policyConversionModel);
                                //cs.rewind();
                                queue.add(cs);
                            }
                        }
                    }
                }
            }
            // wake up

            for (Server serverInstance : servers) {
                if (serverInstance.getLowPowerState() && task != null && serverInstance.hasResourcesFor(task)) {
                    Command newAction = new WakeUpServerCommand(protegeFactory, serverInstance.getName());
                    ContextSnapshot cs = new ContextSnapshot(new LinkedList(newContext.getActions()));
                    //verific peste tot daca nu cumva exista actiunea
                    if (!cs.getActions().contains(newAction)) {
                        cs.getActions().add(newAction);
                        // cs.executeActions();
                        newAction.execute(policyConversionModel);
                        cs.setContextEntropy(computeEntropy().getFirst());
                        cs.setRewardFunction(computeRewardFunction(newContext, cs, newAction));
                        newAction.rewind(policyConversionModel);
                        // cs.rewind();
                        queue.add(cs);
                    }
                }
            }
            // sleep
            for (Server serverInstance : servers) {
                if (!serverInstance.getLowPowerState() && !serverInstance.hasRunningTasks()) {
                    Command newAction = new SendServerToLowPowerStateCommand(protegeFactory, serverInstance.getName());
                    ContextSnapshot cs = new ContextSnapshot(new LinkedList(newContext.getActions()));
                    if (!cs.getActions().contains(newAction)) {
                        cs.getActions().add(newAction);
                        //  cs.executeActions();
                        newAction.execute(policyConversionModel);
                        cs.setContextEntropy(computeEntropy().getFirst());
                        cs.setRewardFunction(computeRewardFunction(newContext, cs, newAction));
                        // cs.rewind();
                        newAction.rewind(policyConversionModel);
                        queue.add(cs);
                    }
                }
            }
            newContext.rewind(policyConversionModel);
            newContext = reinforcementLearning(queue);
        }
        newContext.rewind(policyConversionModel);
        return newContext;
    }

    @Override
    protected void onTick() {

        PriorityQueue<ContextSnapshot> queue = new PriorityQueue<ContextSnapshot>();
        ContextSnapshot initialContext = new ContextSnapshot(new LinkedList<Command>());
        Pair<Double, Policy> entropyAndPolicy = computeEntropy();
        initialContext.setContextEntropy(entropyAndPolicy.getFirst());
        initialContext.setRewardFunction(computeRewardFunction(null, initialContext, null));
        queue.add(initialContext);
        resultsFrame.setActionsList(null);

        if (entropyAndPolicy.getSecond() != null) {
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA" + entropyAndPolicy.getSecond());
            ContextSnapshot result = reinforcementLearning(queue);
            System.out.println("Gasit rezultat ");
            Collection<Command> resultQueue = result.getActions();
            for (Command o : resultQueue) {
                System.out.println(o.toString());
                o.execute(policyConversionModel);
                o.executeOnX3D(agent);
            }
        }


    }

    public boolean getEvaluateProp(Individual policy) {
        //System.out.println(base + "#EvaluatePolicyP");
        Statement property = policy.getProperty(evaluatePolicyProperty);

        if (property == null) {
            return false;
        }

        return property.getBoolean();
    }
}
