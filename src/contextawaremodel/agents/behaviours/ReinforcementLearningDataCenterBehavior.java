/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contextawaremodel.agents.behaviours;

import actionselection.command.Command;
import actionselection.command.selfHealingCommand.IncrementCommand;
import actionselection.command.selfOptimizingCommand.DeployTaskCommand;
import actionselection.command.selfOptimizingCommand.MoveTaskCommand;
import actionselection.command.selfOptimizingCommand.SendServerToLowPowerStateCommand;
import actionselection.command.selfOptimizingCommand.WakeUpServerCommand;
import actionselection.context.ContextSnapshot;
import actionselection.context.Memory;
import actionselection.gui.ActionsOutputFrame;
import actionselection.utils.Pair;
import com.hp.hpl.jena.ontology.OntModel;
import contextawaremodel.agents.ReinforcementLearningAgent;
import contextawaremodel.gui.resourceMonitor.IServerMonitor;
import contextawaremodel.gui.resourceMonitor.serverMonitorPlotter.impl.FullServerMonitor;
import contextawaremodel.worldInterface.datacenterInterface.proxies.impl.HyperVServerManagementProxy;
import contextawaremodel.worldInterface.dtos.ServerDto;
import contextawaremodel.worldInterface.dtos.StorageDto;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import greenContextOntology.*;
import greenContextOntology.Component;
import greenContextOntology.impl.DefaultMemory;
import greenContextOntology.impl.DefaultServer;
import greenContextOntology.impl.DefaultTask;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import negotiator.Negotiator;
import negotiator.impl.NegotiatorFactory;
import org.apache.log4j.Logger;
import selfHealingOntology.SelfHealingProtegeFactory;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author Administrator
 */
public class ReinforcementLearningDataCenterBehavior extends TickerBehaviour {

    private OWLModel contextAwareModel;
    private OntModel policyConversionModel;
    private OntModel selfHealingPolicyConversionModel;
    private JenaOWLModel owlModel;
    private JenaOWLModel selfHealingOwlModel;
    private ContextSnapshot smallestEntropyContext;
    private Memory memory;
    private ActionsOutputFrame resultsFrame;
    private ReinforcementLearningAgent agent;
    private boolean contextBroken = false;
    private ProtegeFactory protegeFactory;
    private SWRLFactory swrlFactory;
    private Logger logger;
    private Negotiator negotiator;
    //  private TaskManagement taskManagementWindow;

    public ReinforcementLearningDataCenterBehavior(Agent a, int interval, OWLModel contextAwareModel, OntModel policyConversionModel, JenaOWLModel owlModel, OntModel selfHealingPolicyConversionModel, JenaOWLModel selfHealingOwlModel, Memory memory) {
        super(a, interval);
        agent = (ReinforcementLearningAgent) a;
        this.contextAwareModel = contextAwareModel;
        this.policyConversionModel = policyConversionModel;
        this.selfHealingPolicyConversionModel = selfHealingPolicyConversionModel;
        this.selfHealingOwlModel = selfHealingOwlModel;
        protegeFactory = new ProtegeFactory(owlModel);
        // taskManagementWindow = new TaskManagement(protegeFactory, swrlFactory, policyConversionModel, agent);

        // FuzzyLogicNegotiator test
        /*
                Task task = protegeFactory.createTask("TestTask");
                RequestedTaskInfo  requestedTaskInfo = protegeFactory.createRequestedTaskInfo("TestRequested_1");
                ReceivedTaskInfo receivedTaskInfo = protegeFactory.createReceivedTaskInfo("TestReceived_1");
                requestedTaskInfo.setCores(1);
                requestedTaskInfo.setCpuMaxAcceptableValue(1750);
                requestedTaskInfo.setCpuMinAcceptableValue(1650);
                requestedTaskInfo.setMemoryMaxAcceptableValue(500);
                requestedTaskInfo.setMemoryMinAcceptableValue(200);
                requestedTaskInfo.setStorageMaxAcceptableValue(3);
                requestedTaskInfo.setStorageMinAcceptableValue(1);


                task.setReceivedInfo(receivedTaskInfo);
                task.setRequestedInfo(requestedTaskInfo);

                Server server = protegeFactory.createServer("TestServer_1");
                CPU cpu = protegeFactory.createCPU("TestCPU_1");
                greenContextOntology.Memory serverMemory = protegeFactory.createMemory("TestMemory_1");
                Storage storage = protegeFactory.createStorage("TestStorage_1");
                Core core_1 = protegeFactory.createCore("TestCore_1");
                core_1.setMaxAcceptableValue(1500);
                core_1.setMinAcceptableValue(1100);
                core_1.setTotal(1655);
                core_1.setUsed(1,policyConversionModel);

                serverMemory.setMaxAcceptableValue(600);
                serverMemory.setMinAcceptableValue(1);
                serverMemory.setUsed(1,policyConversionModel);
                serverMemory.setTotal(600);

                storage.setMaxAcceptableValue(600);
                storage.setMinAcceptableValue(1);
                storage.setUsed(1,policyConversionModel);
                storage.setTotal(700);

                cpu.addAssociatedCore(core_1);
                server.setAssociatedCPU(cpu);
                server.setAssociatedMemory(serverMemory);
                server.setAssociatedStorage(storage);

                Negotiator negotiator = NegotiatorFactory.getFuzzyLogicNegotiator();


                negotiator.negotiate(server,task);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                System.exit(1);
        */

        this.owlModel = owlModel;
        resultsFrame = new ActionsOutputFrame("Datacenter");
        this.memory = memory;
        swrlFactory = new SWRLFactory(contextAwareModel);

        negotiator = NegotiatorFactory.getFuzzyLogicNegotiator();

        /*for (SWRLImp imp : swrlFactory.getEnabledImps()) {
            System.out.println(imp.toString());
        }*/

        //System.exit(1);
        agent.sendAllTasksToClient();

        /*  *//*
       Simulate task 1 ending activity\
        */

        //synchronize X3D display values with ontology values
        Collection<Server> servers = protegeFactory.getAllServerInstances();


        for (Server server : servers) {
            if (server.hasServerIPAddress()) {
                HyperVServerManagementProxy proxy = new HyperVServerManagementProxy(server.getServerIPAddress());
                server.setProxy(proxy);
                ServerDto serverInfo = proxy.getServerInfo();

                CPU cpu = server.getAssociatedCPU();
                int coreCount = serverInfo.getCoreCount();
                Collection cores = new ArrayList(coreCount);
                for (int i = 0; i < coreCount; i++) {
                    cores.add(protegeFactory.createCore(server.getLocalName() + "_Core_" + i));
                }
                cpu.setAssociatedCore(cores);

                int totalCPU = serverInfo.getTotalCPU();
                Object[] freeCPUValues = serverInfo.getFreeCPU().toArray();
                int index = 0;
                int freeCPU = totalCPU - (Integer) freeCPUValues[0];
                for (Object item : cores) {
                    Core core = (Core) item;
                    core.setMaxAcceptableValue(totalCPU);
                    core.setMinAcceptableValue(1);
                    core.setTotal(totalCPU);
                    core.setUsed(freeCPU, policyConversionModel);
                }

                greenContextOntology.Memory serverMemory = server.getAssociatedMemory();
                int totalMemory = serverInfo.getTotalMemory();
                serverMemory.setMaxAcceptableValue(totalMemory);
                serverMemory.setTotal(totalMemory);
                serverMemory.setUsed(totalMemory - serverInfo.getFreeMemory(), policyConversionModel);

                Storage storage = server.getAssociatedStorage();
                List<StorageDto> storageList = serverInfo.getStorage();
                StorageDto targetStorage = null;
                String storagePath = (String) server.getVirtualMachinesPath().iterator().next();
                for (StorageDto storageDto : storageList) {
                    if (storageDto.getName().charAt(0) == storagePath.charAt(0)) {
                        targetStorage = storageDto;
                        break;
                    }
                }

                int storageSize = targetStorage.getSize();
                storage.setMaxAcceptableValue(storageSize);
                storage.setTotal(storageSize);
                storage.setUsed(storageSize - targetStorage.getFreeSpace(), policyConversionModel);
            }
            IServerMonitor serverMonitor = new FullServerMonitor(server, new HyperVServerManagementProxy(server.getServerIPAddress()));
            serverMonitor.executeStandaloneWindow();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            /* if (server.getIsInLowPowerState()) {
                SendServerToLowPowerStateCommand sendServerToLowPowerStateCommand = new SendServerToLowPowerStateCommand(protegeFactory, server.getName());
                sendServerToLowPowerStateCommand.executeOnX3D(agent);
            } else {
                WakeUpServerCommand wakeUpServerCommand = new WakeUpServerCommand(protegeFactory, server.getName());
                wakeUpServerCommand.executeOnX3D(agent);
            }*/
        }


        Collection<Task> tasks = protegeFactory.getAllTaskInstances();

        //taskManagementWindow.setTasks(tasks);
        // taskManagementWindow.setVisible(true);
        resultsFrame.setVisible(true);


        logger = Logger.getLogger(ReinforcementLearningDataCenterBehavior.class);
    }

    // function for computing the contribution to the entropy of task @param task

    private double taskRespectanceDegree(Task task) {
        ReceivedTaskInfo received = task.getReceivedInfo();
        RequestedTaskInfo requested = task.getRequestedInfo();

        //TODO : poate trebe bagat si minimu in task respectance cum is range-uri

        double respectance = task.getCpuWeight()
                * (requested.getCores() - received.getCores() + requested.getCpuMaxAcceptableValue() - received.getCpuReceived())
                + task.getMemoryWeight() * (requested.getMemoryMaxAcceptableValue() - received.getMemoryReceived())
                + task.getStorageWeight() * (requested.getStorageMaxAcceptableValue() - received.getStorageReceived());
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
                //System.out.println("Broken");
                //System.out.println(task);
                //if (!task.requestsSatisfied()) {
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
            double temp = previous.getContextEntropy() - current.getContextEntropy() - c.getCost() - current.getActions().size();
            function += ContextSnapshot.gamma * temp;
        } else {
            function -= current.getContextEntropy();
        }

        return function;
    }


    private ContextSnapshot reinforcementLearning(PriorityQueue<ContextSnapshot> queue) {

        ContextSnapshot newContext = queue.poll();
        if (newContext == null) {
            System.out.println("Could not repair the context totally. Returning best solution.");
            //agent.getSelfOptimizingLogger().log(Color.red, "No solution found", "Could not repair the context totally. Returning best solution.");
            return smallestEntropyContext;
        }
        System.out.println("---A");
        Collection<Server> servers = protegeFactory.getAllServerInstances();
        newContext.executeActions(policyConversionModel);
        Pair<Double, Policy> entropyAndPolicy = computeEntropy();

        if (smallestEntropyContext != null) {
            if (entropyAndPolicy.getFirst() < smallestEntropyContext.getContextEntropy())
                smallestEntropyContext = newContext;
        } else {
            smallestEntropyContext = newContext;
        }

        System.out.println("\n" + entropyAndPolicy.getFirst() + "  " + newContext.getRewardFunction() + "  " + entropyAndPolicy.getSecond() + "\n");
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
            boolean deployed = false;     /// sa zica daca ii  deployed sau nu
            if (task != null) {
                // deploy actions
                for (Server serverInstance : servers) {
                    if (!serverInstance.getIsInLowPowerState() && serverInstance.hasResourcesFor(task)
                            && !serverInstance.containsTask(task) && !task.isRunning()) {
                        Command newAction = new DeployTaskCommand(protegeFactory, serverInstance.getName(), task.getName());
                        if (!newContext.getActions().contains(newAction)) {
                            ContextSnapshot cs = new ContextSnapshot(new LinkedList(newContext.getActions()));
                            cs.getActions().add(newAction);

                            newAction.execute(policyConversionModel);

                            Double afterExecuteEntropy = computeEntropy().getFirst();
                            cs.setContextEntropy(afterExecuteEntropy);
                            cs.setRewardFunction(computeRewardFunction(newContext, cs, newAction));
                            newAction.rewind(policyConversionModel);

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

                                    ContextSnapshot cs = new ContextSnapshot(new LinkedList(newContext.getActions()));
                                    //if action is not already in the actions list
                                    if (!cs.getActions().contains(newAction)) {
                                        cs.getActions().add(newAction);
                                        newAction.execute(policyConversionModel);

                                        cs.setContextEntropy(computeEntropy().getFirst());
                                        cs.setRewardFunction(computeRewardFunction(newContext, cs, newAction));
                                        newAction.rewind(policyConversionModel);

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
                            if (!newContext.getActions().contains(newAction)) {
                                ContextSnapshot cs = new ContextSnapshot(new LinkedList(newContext.getActions()));
                                cs.getActions().add(newAction);

                                newAction.execute(policyConversionModel);
                                cs.setContextEntropy(computeEntropy().getFirst());
                                cs.setRewardFunction(computeRewardFunction(newContext, cs, newAction));
                                newAction.rewind(policyConversionModel);

                                queue.add(cs);
                            }
                        }
                    }
                }
            }
            // wake up

            for (Server serverInstance : servers) {
                if (serverInstance.getIsInLowPowerState()) { //&& (task!=null) && serverInstance.hasResourcesFor(task)) {
                    Command newAction = new WakeUpServerCommand(protegeFactory, serverInstance.getName());
                    ContextSnapshot cs = new ContextSnapshot(new LinkedList(newContext.getActions()));
                    //if action is not already in the actions list
                    if (!cs.getActions().contains(newAction)) {
                        cs.getActions().add(newAction);

                        newAction.execute(policyConversionModel);
                        cs.setContextEntropy(computeEntropy().getFirst());
                        cs.setRewardFunction(computeRewardFunction(newContext, cs, newAction));
                        newAction.rewind(policyConversionModel);

                        queue.add(cs);
                    }
                }
            }
            // sleep
            for (Server serverInstance : servers) {
                if (!serverInstance.getIsInLowPowerState() && !serverInstance.hasRunningTasks()) {
                    Command newAction = new SendServerToLowPowerStateCommand(protegeFactory, serverInstance.getName());

                    if (!newContext.getActions().contains(newAction)) {
                        ContextSnapshot cs = new ContextSnapshot(new LinkedList(newContext.getActions()));
                        cs.getActions().add(newAction);

                        newAction.execute(policyConversionModel);
                        cs.setContextEntropy(computeEntropy().getFirst());
                        cs.setRewardFunction(computeRewardFunction(newContext, cs, newAction));

                        newAction.rewind(policyConversionModel);
                        queue.add(cs);
                    }
                }
            }

            /*

            //TODO : to be changed to allow allocating less than maximum also ? nush ce am vrut sa zic aici
            //negotiate allocating more resources only if all the tasks have been deployed
            //if (allDeployed &&
            //always try a negotiation in order to solve problems :P
            if( server != null) {
                NegotiateResourcesCommand negotiateResourcesCommand = new NegotiateResourcesCommand(protegeFactory, negotiator,server.getName());

                if (!newContext.getActions().contains(negotiateResourcesCommand)) {
                    ContextSnapshot cs = new ContextSnapshot(new LinkedList(newContext.getActions()));
                    cs.getActions().add(negotiateResourcesCommand);
                    //  cs.executeActions();
                    negotiateResourcesCommand.execute(policyConversionModel);
                    cs.setContextEntropy(computeEntropy().getFirst());
                    Pair<Double,Policy> e = computeEntropy();

                    System.out.println("After negotiation " + negotiateResourcesCommand + "\n For server : " + server +   + e.getFirst() + "  " + e.getSecond());
                    cs.setRewardFunction(computeRewardFunction(newContext, cs, negotiateResourcesCommand));
                    // cs.rewind();
                    negotiateResourcesCommand.rewind(policyConversionModel);
                    queue.add(cs);
                }
            }*/

            newContext.rewind(policyConversionModel);

            newContext = reinforcementLearning(queue);
        } else {
            newContext.rewind(policyConversionModel);
        }
        return newContext;
    }

    protected Server getMinDistanceServer(Task task) {
        Server retServer = null;
        RequestedTaskInfo requestedTask = task.getRequestedInfo();
        double difference;
        double minDif = 1000000.0d;
        Collection<Server> servers = protegeFactory.getAllServerInstances();
        for (Server server : servers) {
            Collection<Core> cores = server.getAssociatedCPU().getAssociatedCore();
            difference = 0.0d;
            for (Core core : cores) {
                difference += Math.pow(core.getTotal() - core.getUsed() - requestedTask.getCpuMinAcceptableValue(), 2);
            }
            greenContextOntology.Memory mem = server.getAssociatedMemory();
            difference += Math.pow(mem.getTotal() - mem.getUsed() - requestedTask.getMemoryMinAcceptableValue(), 2);
            Storage storage = server.getAssociatedStorage();
            difference += Math.pow(storage.getTotal() - storage.getUsed() - requestedTask.getStorageMinAcceptableValue(), 2);
            difference = Math.sqrt(difference);
            if (server.hasResourcesToBeNegotiatedFor(task))
                if (difference < minDif) {
                    minDif = difference;
                    retServer = server;
                }
        }
        return retServer;

    }

    @Override
    protected void onTick() {

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        //returns true if there were commands to execute
        //TODO: refresh after receiving new commands from it - > or sth
        /*
        if (taskManagementWindow.executeCommands()) {
            taskManagementWindow.setTasks(protegeFactory.getAllTaskInstances());
        }

        //TODO: check this !
        synchronized (this) {
            taskManagementWindow.setClearForAdding(true);
            notifyAll();
        }
        */
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

            //TODO: activate only after a deploy or delete  to recollect
            //if context broken gather the extra resources allocated to tasks in order to properly evaluate the context
            /*for (Server server : protegeFactory.getAllServerInstances()) {
                server.collectPreviouslyDistributedResources(policyConversionModel);
            }*/

            //Gather data for logging purposes

            ArrayList<String> brokenQoSPolicies = new ArrayList<String>();
            Collection<QoSPolicy> qoSPolicies = protegeFactory.getAllQoSPolicyInstances();
            for (Policy policy : qoSPolicies) {
                if (policy.getReferenced() == null) {
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

            // End of logging


            //avoid addin new tasks when querying ontology
            //TODO: check check check!  TM
            //taskManagementWindow.setClearForAdding(false);
            ContextSnapshot result = reinforcementLearning(queue);
            Collection<Command> resultQueue = result.getActions();
            ArrayList<String> message = new ArrayList<String>();
            for (Command o : resultQueue) {
                message.add(o.toString());
                System.out.println(o.toString());
                o.execute(policyConversionModel);
                o.executeOnX3D(agent);
                // o.executeOnWebService();   ---> to be decommented when running on servers
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }
            if (result.getContextEntropy() > 0) {
                System.out.println("Negotiating....");
                Collection<Task> allTasks = protegeFactory.getAllTaskInstances();
                for (Task task : allTasks) {
                    if (!task.isRunning()) {

                        Negotiator negotiator = NegotiatorFactory.getFuzzyLogicNegotiator();
                        Server server = getMinDistanceServer(task);
                        negotiator.negotiate(server, task);
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        Command newAction = new DeployTaskCommand(protegeFactory, server.getName(), task.getName());
                        ContextSnapshot cs = new ContextSnapshot(new LinkedList(result.getActions()));
                        Command wakeUp = null;
                        if (server.getIsInLowPowerState()) {
                            wakeUp = new WakeUpServerCommand(protegeFactory, server.getName());
                            cs.getActions().add(wakeUp);
                            wakeUp.execute(policyConversionModel);
                        }
                        cs.getActions().add(newAction);
                        newAction.execute(policyConversionModel);
                        cs.setContextEntropy(computeEntropy().getFirst());
                        cs.setRewardFunction(computeRewardFunction(result, cs, newAction));
                        if (cs.getContextEntropy() < result.getContextEntropy()) {
                            result = cs;
                            newAction.executeOnX3D(agent);
                            //newAction.executeOnWebService();  // ---> to be decommented when running on servers
                            if (wakeUp != null) {
                                wakeUp.executeOnX3D(agent);
                                //wakeUp.executeOnWebService();
                            }
                        } else {

                            newAction.rewind(policyConversionModel);
                            if (wakeUp != null)
                                wakeUp.rewind(policyConversionModel);
                        }

                    }
                }
                //  System.out.println("Distributing empty resources : This should not happen anymore");
                /*for (Server server : servers) {
                    server.distributeRemainingResources(policyConversionModel);
                }*/
            }

            agent.getSelfOptimizingLogger().log(Color.BLUE, "Corrective actions", message);

            int resultsSize = resultQueue.size();
            if (resultsSize > 0) {

                //datacenter load influences temperature
                SelfHealingProtegeFactory selfHealingProtegeFactory = new SelfHealingProtegeFactory(selfHealingOwlModel);
                IncrementCommand c = new IncrementCommand(selfHealingProtegeFactory, selfHealingProtegeFactory.getSensor("TemperatureSensorI").getName(), resultsSize);

                System.out.println("\nDatacenter load temperature influence: ");
                System.out.println(c);
                c.execute(selfHealingPolicyConversionModel);
                c.executeOnX3D(myAgent);

                //refresh tasks list if context has been repaired
                //TODO : check TM
                //   taskManagementWindow.setTasks(protegeFactory.getAllTaskInstances());
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

                agent.getSelfOptimizingLogger().log(Color.GREEN, "Current state", currentState);
            }
        }

    }

}