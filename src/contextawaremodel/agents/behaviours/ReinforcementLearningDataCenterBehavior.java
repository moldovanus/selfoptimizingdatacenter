/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contextawaremodel.agents.behaviours;

import actionselection.command.Command;
import actionselection.command.DeployTaskCommand;
import actionselection.command.MoveTaskCommand;
import actionselection.command.SendServerToLowPowerStateCommand;
import actionselection.command.WakeUpServerCommand;
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
import edu.stanford.smi.protegex.owl.model.RDFResource;
import greenContextOntology.EnergyPolicy;
import greenContextOntology.Policy;
import greenContextOntology.ProtegeFactory;
import greenContextOntology.QoSPolicy;
import greenContextOntology.Server;
import greenContextOntology.Task;
import greenContextOntology.impl.DefaultQoSPolicy;
import greenContextOntology.impl.DefaultServer;
import greenContextOntology.impl.DefaultTask;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

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
        this.contextAwareModel = contextAwareModel;
        this.policyConversionModel = policyConversionModel;
        protegeFactory = new ProtegeFactory(owlModel);
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
        Policy brokenPolicy = null;
        double entropy = 0.0;
        Collection<QoSPolicy> qosPolicies = protegeFactory.getAllQoSPolicyInstances();

        for (QoSPolicy policy : qosPolicies) {
            Task task = (Task) policy.getReferences().iterator().next();
            //if (!policy.getRespected()) {
            if (!task.requestsSatisfied()) {
                if (brokenPolicy == null) {
                    brokenPolicy = policy;
                }
                entropy++;
            }
        }

        Collection<EnergyPolicy> policies = protegeFactory.getAllEnergyPolicyInstances();
        for (EnergyPolicy policy : policies) {
            boolean b = policy.getRespected();
            if (!policy.getRespected()) {
                if (brokenPolicy == null) {
                    brokenPolicy = policy;
                }
                //TODO : numarat si Energy Policies in Entropie
                entropy++;
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
            //modificat temp-u 
            double temp = previous.getContextEntropy() - current.getContextEntropy() +1.0/(double)c.getCost();
            function += ContextSnapshot.gamma * (temp);
        } else {
            function -= current.getContextEntropy();
        }

        return function;
    }



    private ContextSnapshot reinforcementLearning(PriorityQueue<ContextSnapshot> queue) {
        ContextSnapshot newContext = queue.poll();

        System.out.println("---A");
        Collection<Server> servers = protegeFactory.getAllServerInstances();
        newContext.executeActions();
        Pair<Double, Policy> entropyAndPolicy = computeEntropy();
        /* if (entropyAndPolicy.getFirst() < 3) {
            for (int i = 0; i < 5; i++)
                System.err.println("\n ------------------------------------------------------------------------------");
        }*/
        System.out.println("\n" + entropyAndPolicy.getFirst() + "\n");
        System.out.println("---B");
        DefaultTask task = null;
        DefaultServer server = null;

        if (entropyAndPolicy.getFirst() > 0) {
            if (entropyAndPolicy.getSecond() != null) {
                Policy policy = entropyAndPolicy.getSecond();
                if (policy.getClass().equals(DefaultQoSPolicy.class)) {
                    task = (DefaultTask) policy.listReferences().next();
                } else {
                    server = (DefaultServer) policy.listReferences().next();
                }
            }
            Boolean deployed = false;     /// sa zica daca ii  deployed sau nu
            if (task != null) {
                if (task.getName().equals("Task_2")) {
                    System.out.println("Found");
                }
                // deploy actions
                for (Server serverInstance : servers) {
                    if (!serverInstance.getLowPowerState() && serverInstance.hasResourcesFor(task)
                            && !serverInstance.getRunningTasks().contains(task) && !task.isRunning()) {
                        Command newAction = new DeployTaskCommand(protegeFactory, serverInstance.getName(), task.getName());
                        ContextSnapshot cs = new ContextSnapshot(new LinkedList(newContext.getActions()));
                        deployed=true;
                       //verific peste tot daca nu cumva exista actiunea
                        if (!cs.getActions().contains(newAction)) {
                            cs.getActions().add(newAction);

                            newAction.execute();
                            //cs.executeActions();
                            Double afterExecuteEntropy = computeEntropy().getFirst();
                            cs.setContextEntropy(afterExecuteEntropy);
                            cs.setRewardFunction(computeRewardFunction(newContext, cs, newAction));
                            newAction.rewind();
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
                            for (Server serverInstance1 : servers1) {
                                if (!serverInstance1.getLowPowerState() && serverInstance.hasResourcesFor(task)
                                        && !serverInstance.getRunningTasks().contains(task) && serverInstance1.getName()!=serverInstance.getName()) {
                                    Command newAction = new MoveTaskCommand(protegeFactory, serverInstance.getName(), serverInstance1.getName(), myTask.getName());
                                    ///de vazut daca a fost posibila
                                    ContextSnapshot cs = new ContextSnapshot(new LinkedList(newContext.getActions()));
                                    //verific peste tot daca nu cumva exista actiunea
                                    if (!cs.getActions().contains(newAction)) {
                                        cs.getActions().add(newAction);
                                        newAction.execute();
                                        //cs.executeActions();
                                        cs.setContextEntropy(computeEntropy().getFirst());
                                        cs.setRewardFunction(computeRewardFunction(newContext, cs, newAction));
                                        newAction.rewind();
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
                    for (Server serverInstance1 : servers) {
                        if (!serverInstance1.getLowPowerState()) {
                            Command newAction = new MoveTaskCommand(protegeFactory, server.getName(), serverInstance1.getName(), myTask.getName());
                            ///de vazut daca a fost posibila
                            ContextSnapshot cs = new ContextSnapshot(new LinkedList(newContext.getActions()));

                            //verific peste tot daca nu cumva exista actiunea
                            if (!cs.getActions().contains(newAction)) {
                                cs.getActions().add(newAction);
                                //cs.executeActions();
                                newAction.execute();
                                cs.setContextEntropy(computeEntropy().getFirst());
                                cs.setRewardFunction(computeRewardFunction(newContext, cs, newAction));
                                newAction.rewind();
                                //cs.rewind();
                                queue.add(cs);
                            }
                        }
                    }
                }
            }
            // wake up
            if (deployed==false)
            for (Server serverInstance : servers) {
                if (serverInstance.getLowPowerState() && task!= null && serverInstance.hasResourcesFor(task)) {
                    Command newAction = new WakeUpServerCommand(protegeFactory, serverInstance.getName());
                    ///de vazut daca a fost posibila
                    ContextSnapshot cs = new ContextSnapshot(new LinkedList(newContext.getActions()));
                    //verific peste tot daca nu cumva exista actiunea
                    if (!cs.getActions().contains(newAction)) {
                        cs.getActions().add(newAction);
                        // cs.executeActions();
                        newAction.execute();
                        cs.setContextEntropy(computeEntropy().getFirst());
                        cs.setRewardFunction(computeRewardFunction(newContext, cs, newAction));
                        newAction.rewind();
                        // cs.rewind();
                        queue.add(cs);
                    }
                }
            }
            // sleep
            for (Server serverInstance : servers) {
                if (!serverInstance.getLowPowerState()&& serverInstance.getRunningTasks()==null) {
                    Command newAction = new SendServerToLowPowerStateCommand(protegeFactory, serverInstance.getName());
                    ///de vazut daca a fost posibila
                    ContextSnapshot cs = new ContextSnapshot(new LinkedList(newContext.getActions()));

                    if (!cs.getActions().contains(newAction)) {
                        cs.getActions().add(newAction);
                        //  cs.executeActions();
                        newAction.execute();
                        cs.setContextEntropy(computeEntropy().getFirst());
                        cs.setRewardFunction(computeRewardFunction(newContext, cs, newAction));
                        // cs.rewind();
                        newAction.rewind();
                        queue.add(cs);
                    }
                }
            }
            newContext.rewind();
            newContext = reinforcementLearning(queue);
        }
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
            Collection resultQueue = result.getActions();
            for (Object o : resultQueue) {
                System.out.println(o.toString());
            }
        }


    }
}
