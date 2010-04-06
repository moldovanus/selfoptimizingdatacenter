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
import contextawaremodel.gui.TaskManagement;
import contextawaremodel.agents.ReinforcementLearningAgent;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import greenContextOntology.*;
import greenContextOntology.Component;
import greenContextOntology.impl.DefaultServer;
import greenContextOntology.impl.DefaultTask;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.wrapper.ControllerException;

import java.util.*;
import java.awt.*;

import selfHealingOntology.SelfHealingProtegeFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * @author Administrator
 */
public class ReinforcementLearningDataCenterBehavior extends TickerBehaviour {

    private OWLModel contextAwareModel;
    private OntModel policyConversionModel;
    private OntModel selfHealingPolicyConversionModel;
    private JenaOWLModel owlModel;
    private JenaOWLModel selfHealingOwlModel;
    private ContextSnapshot smallestEntropyContext ;
    private Memory memory;
    private Property evaluatePolicyProperty;
    private ActionsOutputFrame resultsFrame;
    private ReinforcementLearningAgent agent;
    private boolean contextBroken = false;
    private ProtegeFactory protegeFactory;
    private SWRLFactory swrlFactory;
    private TaskManagement taskManagementWindow;
    private Logger logger;

    public ReinforcementLearningDataCenterBehavior(Agent a, int interval, OWLModel contextAwareModel, OntModel policyConversionModel, JenaOWLModel owlModel, OntModel selfHealingPolicyConversionModel, JenaOWLModel selfHealingOwlModel, Memory memory) {
        super(a, interval);
        agent = (ReinforcementLearningAgent) a;
        evaluatePolicyProperty = policyConversionModel.getDatatypeProperty("http://www.owl-ontologies.com/Datacenter.owl#respected");
        this.contextAwareModel = contextAwareModel;
        this.policyConversionModel = policyConversionModel;
        this.selfHealingPolicyConversionModel = selfHealingPolicyConversionModel;
        this.selfHealingOwlModel = selfHealingOwlModel;
        protegeFactory = new ProtegeFactory(owlModel);
        this.owlModel = owlModel;
        resultsFrame = new ActionsOutputFrame("Datacenter");
        this.memory = memory;
        swrlFactory = new SWRLFactory(contextAwareModel);

        /*for (SWRLImp imp : swrlFactory.getEnabledImps()) {
            System.out.println(imp.toString());
        }*/

        //System.exit(1);

        taskManagementWindow = new TaskManagement(protegeFactory, swrlFactory, policyConversionModel, agent);

        /*
       Simulate task 1 ending activity\
        */

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {

                //sinchronize X3D display values with ontology values
                Collection<Server> servers = protegeFactory.getAllServerInstances();


                for (Server server : servers) {
                    if (server.getIsInLowPowerState()) {
                        SendServerToLowPowerStateCommand sendServerToLowPowerStateCommand = new SendServerToLowPowerStateCommand(protegeFactory, server.getName());
                        sendServerToLowPowerStateCommand.executeOnX3D(agent);
                    } else {
                        WakeUpServerCommand wakeUpServerCommand = new WakeUpServerCommand(protegeFactory, server.getName());
                        wakeUpServerCommand.executeOnX3D(agent);
                    }
                }

                Collection<Task> tasks = protegeFactory.getAllTaskInstances();
                taskManagementWindow.setTasks(tasks);
                taskManagementWindow.setVisible(true);
                resultsFrame.setVisible(true);

            }
        });
        logger = Logger.getLogger(ReinforcementLearningDataCenterBehavior.class);
    }

    // function for computing the contribution to the entropy of task @param task
    private double taskRespectanceDegree(Task task) {
        TaskInfo received = task.getReceivedInfo();
        TaskInfo requested = task.getRequestedInfo();
        double respectance = task.getCpuWeight()
                * (requested.getCores() - received.getCores() + requested.getCpu() - received.getCpu())
                + task.getMemoryWeight() * (requested.getMemory() - received.getMemory())
                + task.getStorageWeight() * (requested.getStorage() - received.getStorage());
        return respectance;
    }

    private double energyRespectanceDegree(Server server) {
        double respectance = 0.0;
        CPU cpu = server.getAssociatedCPU();
        greenContextOntology.Memory serverMemory = server.getAssociatedMemory();
        Storage storage = server.getAssociatedStorage();
        double cpuCores = 0.0;
        Collection<Component> cores = cpu.getAssociatedCore();
        double diff = 0.0;
        for (Component core : cores) {
            diff = 0.0;
            int usedCore = core.getUsed();
            int coreMaxAcceptableValue = core.getMaxAcceptableValue();

            if (usedCore > coreMaxAcceptableValue) {
                diff = usedCore - coreMaxAcceptableValue;
            } else if (usedCore < coreMaxAcceptableValue) {
                //TODO:changed din usedCore - coreMaxAcceptableValue pentru ca dadea entropie negativa
                diff = coreMaxAcceptableValue - usedCore;
            }
            cpuCores += diff;
        }
        cpuCores /= cores.size();
        respectance += cpu.getWeight() * cpuCores;
        diff = 0.0;

        int usedMemory = serverMemory.getUsed();
        int memoryMaxAcceptableValue = serverMemory.getMaxAcceptableValue();
        int memoryMinAcceptableValue = serverMemory.getMinAcceptableValue();

        if (usedMemory > memoryMaxAcceptableValue) {
            diff = usedMemory - memoryMaxAcceptableValue;
        } else if (usedMemory < memoryMinAcceptableValue) {
            diff = usedMemory - memoryMinAcceptableValue;
        }
        respectance += serverMemory.getWeight() * diff;
        diff = 0.0;

        int usedStorage = storage.getUsed();
        int storageMaxAcceptableValue = storage.getMaxAcceptableValue();
        int storageMinAcceptableValue = storage.getMinAcceptableValue();

        if (usedStorage > storageMaxAcceptableValue) {
            diff = usedStorage - storageMaxAcceptableValue;
        } else if (usedStorage < storageMinAcceptableValue) {
            //TODO:changed din usedStorage - storageMinAcceptableValue pentru ca dadea entropie negativa
            diff = storageMinAcceptableValue - usedStorage;
        }

        respectance += storage.getWeight() * diff;
        return respectance;
    }

    private Pair<Double, Policy> computeEntropy() {
        Policy brokenPolicy = null;
        double entropy = 0.0;
        Collection<QoSPolicy> qosPolicies = protegeFactory.getAllQoSPolicyInstances();

        for (QoSPolicy policy : qosPolicies) {

            Task task = policy.getReferenced();

            //if task has been deleted
            if (task == null) {
                continue;
            }

            //System.out.println(task.getName().split("#")[1] + " " + policy.getRespected(policyConversionModel)); 
            if (!policy.getRespected(policyConversionModel)) {
                //if (!task.requestsSatisfied()) {
                //System.out.println("Broken policy : " + policy.getName().substring(policy.getName().lastIndexOf('#'), policy.getName().length()));
                if (brokenPolicy == null) {
                    brokenPolicy = policy;
                }

                if (policy.hasPriority()) {
                    entropy += policy.getPriority() * taskRespectanceDegree(task);
                }
            }
        }

        //Pus aici cel putin temporary s afaca intai deploy si apoi sa vada de energie k altfel o ia razna
        // cu move si move si move si nu i iese
        if (brokenPolicy == null) {

            Collection<EnergyPolicy> policies = protegeFactory.getAllEnergyPolicyInstances();
            for (EnergyPolicy policy : policies) {
                Server server = policy.getReferenced();
                //System.out.println(server);
                /*if (server.getIsInLowPowerState()) {
                    continue;
                }*/

                // if (getEvaluateProp( policyConversionModel.getIndividual(policy.getURI()) ) ){
                if (!policy.getRespected(policyConversionModel)) {

                    //System.out.println("Broken server : " + server);
                    if (brokenPolicy == null) {
                        brokenPolicy = policy;
                    }
                    if (policy.hasPriority()) {
                        entropy += policy.getPriority() * energyRespectanceDegree(server);
                    }
                    //entropy += policy.getPriority();
                }
            }
        }
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
        if (newContext == null) return smallestEntropyContext;
        System.out.println("---A");
        Collection<Server> servers = protegeFactory.getAllServerInstances();
        newContext.executeActions(policyConversionModel);
        Pair<Double, Policy> entropyAndPolicy = computeEntropy();
        if (smallestEntropyContext!= null)
        {
            if (entropyAndPolicy.getFirst()<smallestEntropyContext.getContextEntropy())
                smallestEntropyContext = newContext;
        }else
        {
        smallestEntropyContext = newContext;
        }

        System.out.println("\n" + entropyAndPolicy.getFirst() + "  " + newContext.getRewardFunction() + "\n");//+ "  " + entropyAndPolicy.getSecond() + "\n");
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
                    if (!serverInstance.getIsInLowPowerState() && serverInstance.hasResourcesFor(task)
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
                    if (!serverInstance.getIsInLowPowerState()) {
                        Iterator it = serverInstance.listRunningTasks();
                        while (it.hasNext()) {
                            Task myTask = (DefaultTask) it.next();
                            for (Server otherServerInstance : servers1) {
                                if (!otherServerInstance.getIsInLowPowerState() && otherServerInstance.hasResourcesFor(myTask)
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
                        if (!serverInstance.getIsInLowPowerState() && !serverInstance.containsTask(myTask)
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
                    if (serverInstance.getIsInLowPowerState()){ //&& (task!=null) && serverInstance.hasResourcesFor(task)) {
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
                if (!serverInstance.getIsInLowPowerState() && !serverInstance.hasRunningTasks()) {
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
            
            logger.debug("Size " + queue.size());
           
            newContext = reinforcementLearning(queue);
        }
        newContext.rewind(policyConversionModel);
        return newContext;
    }

    @Override
    protected void onTick() {
        taskManagementWindow.executeCommands();
        System.out.println("Datacenter behavior on Tick");
        PriorityQueue<ContextSnapshot> queue = new PriorityQueue<ContextSnapshot>();
        ContextSnapshot initialContext = new ContextSnapshot(new LinkedList<Command>());
        Pair<Double, Policy> entropyAndPolicy = computeEntropy();

        System.out.println(entropyAndPolicy.getFirst() + " " + entropyAndPolicy.getSecond());

        initialContext.setContextEntropy(entropyAndPolicy.getFirst());
        initialContext.setRewardFunction(computeRewardFunction(null, initialContext, null));
        queue.add(initialContext);
        resultsFrame.setActionsList(null);

        if (entropyAndPolicy.getSecond() != null) {

            contextBroken = true;
            /**
             * Gather data for logging purposes
             */

            ArrayList<String> brokenQoSPolicies = new ArrayList<String>();
            Collection<QoSPolicy> qoSPolicies = protegeFactory.getAllQoSPolicyInstances();
            for (Policy policy : qoSPolicies) {
                if(policy.getReferenced() == null){
                    continue;
                }
                if (!policy.getRespected(policyConversionModel)) {
                    brokenQoSPolicies.add(policy.getName().split("#")[1]);
                }
            }
            Collection<EnergyPolicy> brokenEnergyPolicies = protegeFactory.getAllEnergyPolicyInstances();
            for (Policy policy : brokenEnergyPolicies) {
                if (!policy.getRespected(policyConversionModel)) {
                    brokenQoSPolicies.add(policy.getName().split("#")[1]);
                }
            }

            agent.getSelfOptimizingLogger().log(Color.ORANGE, "Broken policies", brokenQoSPolicies);

            Collection<Server> servers = protegeFactory.getAllServerInstances();
            Collection<Task> tasks = protegeFactory.getAllTaskInstances();
            ArrayList<String> currentState = new ArrayList(servers.size() + tasks.size());

            for (Server server : servers) {
                currentState.add(server.toString());
            }

            for (Task task : tasks) {
                currentState.add(task.toString());
            }

            agent.getSelfOptimizingLogger().log(Color.red, "Current state", currentState);
            /**
             * End of logging
             */

            ContextSnapshot result = reinforcementLearning(queue);

            Collection<Command> resultQueue = result.getActions();

            ArrayList<String> message = new ArrayList<String>();
            for (Command o : resultQueue) {
                message.add(o.toString());
                System.out.println(o.toString());
                /*if (o instanceof DeployTaskCommand) {
                    DeployTaskCommand command = (DeployTaskCommand) o;
                    String[] data = o.toStringArray();
                    //just test if i remove task
                   if (data[1].equals("Task_1")) {
                        final Task t = protegeFactory.getTask(data[1]);
                        final Server s = protegeFactory.getServer(data[2]);

                        final javax.swing.Timer timer = new javax.swing.Timer(10000, new ActionListener() {

                            public void actionPerformed(ActionEvent e) {
                                System.err.println("Removiiing");
                                RemoveTaskFromServerCommand command = new RemoveTaskFromServerCommand(protegeFactory, t.getName(), s.getName());
                                command.execute(policyConversionModel);
                                command.executeOnX3D(agent);
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();

                }}*/
                o.execute(policyConversionModel);
                o.executeOnX3D(agent);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }


            agent.getSelfOptimizingLogger().log(Color.BLUE, "Corrective actions", message);

            int resultsSize = resultQueue.size();
            if (resultsSize > 0) {
                taskManagementWindow.setTasks(protegeFactory.getAllTaskInstances());
                //datacenter load influences temperature
                SelfHealingProtegeFactory selfHealingProtegeFactory = new SelfHealingProtegeFactory(selfHealingOwlModel);
                IncrementCommand c = new IncrementCommand(selfHealingProtegeFactory, selfHealingProtegeFactory.getSensor("TemperatureSensorI").getName(), resultsSize);

                System.out.println("\nDatacenter load temperature influence: ");
                System.out.println(c);
                c.execute(selfHealingPolicyConversionModel);
                c.executeOnX3D(myAgent);
            }
            //wait for effect to be noticeable on X3D
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        } else {
            if (contextBroken) {
                contextBroken = false;
                Collection<Server> servers = protegeFactory.getAllServerInstances();
                Collection<Task> tasks = protegeFactory.getAllTaskInstances();
                ArrayList<String> currentState = new ArrayList(servers.size() + tasks.size());

                for (Server server : servers) {
                    currentState.add(server.toString());
                }

                for (Task task : tasks) {
                    currentState.add(task.toString());
                }

                agent.getSelfOptimizingLogger().log(Color.red, "Current state", currentState);
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
