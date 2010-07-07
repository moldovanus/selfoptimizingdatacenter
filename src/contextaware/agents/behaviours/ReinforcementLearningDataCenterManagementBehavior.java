/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contextaware.agents.behaviours;

import actionEnforcement.command.Command;
import actionEnforcement.command.selfHealingCommand.IncrementCommand;
import actionEnforcement.command.selfOptimizingCommand.*;
import com.hp.hpl.jena.ontology.OntModel;
import contextaware.GlobalVars;
import contextaware.agents.ReinforcementLearningAgent;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import ontologyRepresentations.greenContextOntology.*;
import ontologyRepresentations.greenContextOntology.Component;
import ontologyRepresentations.greenContextOntology.impl.DefaultServer;
import ontologyRepresentations.greenContextOntology.impl.DefaultTask;
import ontologyRepresentations.selfHealingOntology.SelfHealingProtegeFactory;
import org.apache.log4j.Logger;
import utils.Pair;
import utils.X3DMessageDispatcher;
import utils.context.ContextSnapshot;
import utils.context.DatacenterMemory;
import utils.context.DatacenterMockupContext;
import utils.context.EnvironmentMemory;
import utils.negotiator.Negotiator;
import utils.negotiator.impl.NegotiatorFactory;
import utils.workLoadGenerator.TaskLifeManager;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.Queue;

/**
 * @author Administrator
 */
public class ReinforcementLearningDataCenterManagementBehavior extends TickerBehaviour {

    private OWLModel datacenterOwlModel;
    private OntModel datacenterPolicyConversionModel;
    private OntModel selfHealingPolicyConversionModel;

    private JenaOWLModel selfHealingOwlModel;
    private ContextSnapshot smallestEntropyContext;
    private EnvironmentMemory memory;
    private DatacenterMemory datacenterMemory;
    private ReinforcementLearningAgent agent;
    private boolean contextBroken = false;
    private DatacenterProtegeFactory protegeFactory;
    private SWRLFactory swrlFactory;
    private Logger logger;
    private Negotiator negotiator;
    //  private TaskManagement taskManagementWindow;

    public ReinforcementLearningDataCenterManagementBehavior(Agent a, int interval, OWLModel datacenterOwlModel, OntModel datacenterPolicyConversionModel,
                                                             JenaOWLModel jenaDatacenterOwlModel, OntModel selfHealingPolicyConversionModel,
                                                             JenaOWLModel selfHealingOwlModel, EnvironmentMemory memory, DatacenterMemory datacenterMemory) {
        super(a, interval);
        agent = (ReinforcementLearningAgent) a;
        this.datacenterOwlModel = datacenterOwlModel;
        this.datacenterPolicyConversionModel = datacenterPolicyConversionModel;
        this.selfHealingPolicyConversionModel = selfHealingPolicyConversionModel;
        this.selfHealingOwlModel = selfHealingOwlModel;
        this.datacenterMemory = datacenterMemory;
        this.memory = memory;
        protegeFactory = new DatacenterProtegeFactory(datacenterOwlModel);
        Collection<Task> tasks = protegeFactory.getAllTaskInstances();

//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//        for (Task task : tasks) {
//            TaskLifeManager.addTask(protegeFactory, task, 10, datacenterPolicyConversionModel);
//        }
//
//        WorkLoadLoader generator = RandomWorkLoadGenerator.generateRandomWorkLoad(10, 5, 300000, protegeFactory, datacenterPolicyConversionModel);
//        try {
//            WorkLoadFileIO.writeWorkLoadToFile(generator, "WorkLoadFile");
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }

        /* Task task = protegeFactory.createTask("TestTask");
                RequestedTaskInfo requestedTaskInfo = protegeFactory.createRequestedTaskInfo("TestRequested_1");
                ReceivedTaskInfo receivedTaskInfo = protegeFactory.createReceivedTaskInfo("TestReceived_1");
                requestedTaskInfo.setCores(1);
                requestedTaskInfo.setCpuMaxAcceptableValue(1750);
                requestedTaskInfo.setCpuMinAcceptableValue(1650);
                requestedTaskInfo.setMemoryMaxAcceptableValue(500);
                requestedTaskInfo.setMemoryMinAcceptableValue(200);
                requestedTaskInfo.setStorageMaxAcceptableValue(3);
                requestedTaskInfo.setStorageMinAcceptableValue(1);

                receivedTaskInfo.setCpuReceived(0);
                receivedTaskInfo.setMemoryReceived(0);
                receivedTaskInfo.setStorageReceived(0);

                task.setReceivedInfo(receivedTaskInfo);
                task.setRequestedInfo(requestedTaskInfo);

                TaskMonitor monitor = new TaskMonitor(task);
                monitor.executeStandaloneWindow();
        */
        //TasksQueueMonitor tasksQueueMonitor = new TasksQueueMonitor(protegeFactory);
        //tasksQueueMonitor.executeStandaloneWindow();

        // taskManagementWindow = new TaskManagement(protegeFactory, swrlFactory, datacenterPolicyConversionModel, agent);

        // FuzzyLogicNegotiator test

//        Task task = protegeFactory.createTask("TestTask");
//        RequestedTaskInfo requestedTaskInfo = protegeFactory.createRequestedTaskInfo("TestRequested_1");
//        ReceivedTaskInfo receivedTaskInfo = protegeFactory.createReceivedTaskInfo("TestReceived_1");
//        requestedTaskInfo.setCores(1);
//        requestedTaskInfo.setCpuMaxAcceptableValue(1950);
//        requestedTaskInfo.setCpuMinAcceptableValue(1650);
//        requestedTaskInfo.setMemoryMaxAcceptableValue(500);
//        requestedTaskInfo.setMemoryMinAcceptableValue(200);
//        requestedTaskInfo.setStorageMaxAcceptableValue(3);
//        requestedTaskInfo.setStorageMinAcceptableValue(1);
//
//
//        task.setReceivedInfo(receivedTaskInfo);
//        task.setRequestedInfo(requestedTaskInfo);
//
//        Server server = protegeFactory.createServer("TestServer_1");
//        CPU cpu = protegeFactory.createCPU("TestCPU_1");
//        ontologyRepresentations.greenContextOntology.Memory serverMemory = protegeFactory.createMemory("TestMemory_1");
//        Storage storage = protegeFactory.createStorage("TestStorage_1");
//        Core core_1 = protegeFactory.createCore("TestCore_1");
//        core_1.setMaxAcceptableValue(1200);
//        core_1.setMinAcceptableValue(1100);
//        core_1.setTotal(2200);
//        core_1.setUsed(1);
//
//        serverMemory.setMaxAcceptableValue(600);
//        serverMemory.setMinAcceptableValue(1);
//        serverMemory.setUsed(1);
//        serverMemory.setTotal(600);
//
//        storage.setMaxAcceptableValue(600);
//        storage.setMinAcceptableValue(1);
//        storage.setUsed(1);
//        storage.setTotal(700);
//
//        cpu.addAssociatedCore(core_1);
//        server.setAssociatedCPU(cpu);
//        server.setAssociatedMemory(serverMemory);
//        server.setAssociatedStorage(storage);
//
//        Negotiator utils.negotiator = NegotiatorFactory.getFuzzyLogicNegotiator();
//
//
//        utils.negotiator.negotiate(server, task);
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
        // System.exit(1);

        // resultsFrame = new ActionsOutputFrame("Datacenter");
        this.memory = memory;
        swrlFactory = new SWRLFactory(datacenterOwlModel);

        negotiator = NegotiatorFactory.getNashNegotiator();

        /*for (SWRLImp imp : swrlFactory.getEnabledImps()) {
            System.out.println(imp.toString());
        }*/

        //System.exit(1);
        agent.sendAllTasksToClient();

        /*  *//*
       Simulate task 1 ending activity\
        */

        //add server web service information pooling mechanism

//
//        for (Server server : servers) {
//            if (server.hasServerIPAddress()) {
//                ServerManagementProxyInterface proxy = ProxyFactory.createServerManagementProxy(server.getServerIPAddress());
//                server.setProxy(proxy);
//                ServerDto serverInfo = proxy.getServerInfo();
//
//                CPU cpu = server.getAssociatedCPU();
//                int coreCount = serverInfo.getCoreCount();
//                Collection cores = new ArrayList(coreCount);
//                for (int i = 0; i < coreCount; i++) {
//                    cores.add(protegeFactory.createCore(server.getLocalName() + "_Core_" + i));
//                }
//                cpu.setAssociatedCore(cores);
//
//                int totalCPU = serverInfo.getTotalCPU();
//                Object[] freeCPUValues = serverInfo.getFreeCPU().toArray();
//                int index = 0;
//                int freeCPU = totalCPU - (Integer) freeCPUValues[0];
//                for (Object item : cores) {
//                    Core core = (Core) item;
//                    core.setMaxAcceptableValue(totalCPU);
//                    core.setMinAcceptableValue(1);
//                    core.setTotal(totalCPU);
//                    core.setUsed(freeCPU);
//                }
//
//                ontologyRepresentations.greenContextOntology.Memory serverMemory = server.getAssociatedMemory();
//                int totalMemory = serverInfo.getTotalMemory();
//                serverMemory.setMaxAcceptableValue(totalMemory);
//                serverMemory.setTotal(totalMemory);
//                serverMemory.setUsed(totalMemory - serverInfo.getFreeMemory());
//
//                Storage storage = server.getAssociatedStorage();
//                List<StorageDto> storageList = serverInfo.getStorage();
//                StorageDto targetStorage = null;
//                String storagePath = (String) server.getVirtualMachinesPath().iterator().next();
//                for (StorageDto storageDto : storageList) {
//                    if (storageDto.getName().charAt(0) == storagePath.charAt(0)) {
//                        targetStorage = storageDto;
//                        break;
//                    }
//                }
//
//                int storageSize = targetStorage.getSize();
//                storage.setMaxAcceptableValue(storageSize);
//                storage.setTotal(storageSize);
//                storage.setUsed(storageSize - targetStorage.getFreeSpace());
//            }
//            //IMonitor serverMonitor = new FullServerMonitor(server, new HyperVServerManagementProxy(server.getServerIPAddress()));
//            //serverMonitor.executeStandaloneWindow();
//            /*  try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }*/
//
//            /* if (server.getIsInLowPowerState()) {
//                SendServerToLowPowerStateCommand sendServerToLowPowerStateCommand = new SendServerToLowPowerStateCommand(protegeFactory, server.getName());
//                sendServerToLowPowerStateCommand.executeOnX3D(agent);
//            } else {
//                WakeUpServerCommand wakeUpServerCommand = new WakeUpServerCommand(protegeFactory, server.getName());
//                wakeUpServerCommand.executeOnX3D(agent);
//            }*/
//        }

        //taskManagementWindow.setTasks(tasks);
        // taskManagementWindow.setVisible(true);
        // resultsFrame.setVisible(true);


        logger = Logger.getLogger(ReinforcementLearningDataCenterManagementBehavior.class);
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
        ontologyRepresentations.greenContextOntology.Memory serverMemory = server.getAssociatedMemory();
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

            //System.out.println(task.getName().split("#")[1] + " " + policy.getRespected(datacenterPolicyConversionModel));
            if (!policy.getRespected(datacenterPolicyConversionModel)) {
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

        Collection<EnergyPolicy> policies = protegeFactory.getAllEnergyPolicyInstances();
        for (EnergyPolicy policy : policies) {
            Server server = policy.getReferenced();
            //System.out.println(server);
            /*if (server.getIsInLowPowerState()) {
                continue;
            }*/

            // if (getEvaluateProp( datacenterPolicyConversionModel.getIndividual(policy.getURI()) ) ){
            if (!server.getIsInLowPowerState())
                if (!policy.getRespected(datacenterPolicyConversionModel)) {

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

        return new Pair<Double, Policy>(entropy, brokenPolicy);
    }

    private double computeRewardFunction(ContextSnapshot previous, ContextSnapshot current, Command c) {
        double function = 0.0d;
        if (previous != null) {
            function += previous.getRewardFunction();
            double temp = previous.getRewardFunction() - current.getContextEntropy() - c.getCost() - current.getActions().size() * 100;
            function = ContextSnapshot.gamma * temp;
        } else {
            function = -current.getContextEntropy();
        }

        return function;
    }


    private ContextSnapshot reinforcementLearning(PriorityQueue<ContextSnapshot> queue) {

        ContextSnapshot newContext = queue.poll();
        if (newContext == null) {
            Pair<Double, Policy> entropyAndPolicy = computeEntropy();

            System.out.println("Could not repair the context totally. Returning best solution:");
            Queue<Command> commands = smallestEntropyContext.getActions();
            for (Command command : commands) {
                System.out.println(command.toString());
            }

            System.out.println("Broken " + entropyAndPolicy.getSecond().getLocalName() + "\n Referenced " + entropyAndPolicy.getSecond().getReferenced().toString());

            //agent.getSelfOptimizingLogger().log(Color.red, "No solution found", "Could not repair the context totally. Returning best solution.");
            return smallestEntropyContext;
        }
        System.out.println("---A");
        Collection<Server> servers = protegeFactory.getAllServerInstances();
        newContext.executeActions(datacenterPolicyConversionModel);
        datacenterMemory.restoreProtegeFactory(protegeFactory);

        Queue<Command> commands = datacenterMemory.getActionsForTasks(protegeFactory);
        if (commands != null) {
            System.out.println("Remembered...!!");
            newContext.addActions(commands);
        }
        Pair<Double, Policy> entropyAndPolicy = computeEntropy();

        if (smallestEntropyContext != null) {
            if (entropyAndPolicy.getFirst() < smallestEntropyContext.getContextEntropy())
                smallestEntropyContext = newContext;
            else if (entropyAndPolicy.getFirst() == smallestEntropyContext.getContextEntropy() && newContext.getActions().size() < smallestEntropyContext.getActions().size())
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
                        SelfOptimizingCommand newAction = new DeployTaskCommand(protegeFactory, serverInstance.getName(), task.getName());
                        if (!newContext.getActions().contains(newAction)) {
                            ContextSnapshot cs = new ContextSnapshot(new LinkedList(newContext.getActions()));
                            cs.getActions().add(newAction);
                            deployed = true;
                            newAction.execute(datacenterPolicyConversionModel);

                            Double afterExecuteEntropy = computeEntropy().getFirst();
                            cs.setContextEntropy(afterExecuteEntropy);
                            cs.setRewardFunction(computeRewardFunction(newContext, cs, newAction));
                            newAction.rewind(datacenterPolicyConversionModel);

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
                                    SelfOptimizingCommand newAction = new MoveTaskCommand(protegeFactory, serverInstance.getName(), otherServerInstance.getName(), myTask.getName());

                                    ContextSnapshot cs = new ContextSnapshot(new LinkedList(newContext.getActions()));
                                    //if action is not already in the actions list
                                    if (!cs.getActions().contains(newAction)) {
                                        cs.getActions().add(newAction);
                                        newAction.execute(datacenterPolicyConversionModel);

                                        cs.setContextEntropy(computeEntropy().getFirst());
                                        cs.setRewardFunction(computeRewardFunction(newContext, cs, newAction));
                                        newAction.rewind(datacenterPolicyConversionModel);

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

                                newAction.execute(datacenterPolicyConversionModel);
                                cs.setContextEntropy(computeEntropy().getFirst());
                                cs.setRewardFunction(computeRewardFunction(newContext, cs, newAction));
                                newAction.rewind(datacenterPolicyConversionModel);

                                queue.add(cs);
                            }
                        }
                    }
                }
            }
            // wake up

            for (Server serverInstance : servers) {
                if (serverInstance.getIsInLowPowerState()) { //&& (task!=null) && serverInstance.hasResourcesFor(task)) {
                    System.out.println(serverInstance.getLocalName() + " " + serverInstance.getIsInLowPowerState() + " is waking up");
                    Command newAction = new WakeUpServerCommand(protegeFactory, serverInstance.getName());
                    ContextSnapshot cs = new ContextSnapshot(new LinkedList(newContext.getActions()));
                    //if action is not already in the actions list
                    if (!cs.getActions().contains(newAction)) {
                        cs.getActions().add(newAction);

                        newAction.execute(datacenterPolicyConversionModel);
                        cs.setContextEntropy(computeEntropy().getFirst());
                        cs.setRewardFunction(computeRewardFunction(newContext, cs, newAction));
                        newAction.rewind(datacenterPolicyConversionModel);

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

                        newAction.execute(datacenterPolicyConversionModel);
                        cs.setContextEntropy(computeEntropy().getFirst());
                        cs.setRewardFunction(computeRewardFunction(newContext, cs, newAction));

                        newAction.rewind(datacenterPolicyConversionModel);
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
                NegotiateResourcesCommand negotiateResourcesCommand = new NegotiateResourcesCommand(protegeFactory, utils.negotiator,server.getName());

                if (!newContext.getActions().contains(negotiateResourcesCommand)) {
                    ContextSnapshot cs = new ContextSnapshot(new LinkedList(newContext.getActions()));
                    cs.getActions().add(negotiateResourcesCommand);
                    //  cs.executeActions();
                    negotiateResourcesCommand.execute(datacenterPolicyConversionModel);
                    cs.setContextEntropy(computeEntropy().getFirst());
                    Pair<Double,Policy> e = computeEntropy();

                    System.out.println("After negotiation " + negotiateResourcesCommand + "\n For server : " + server +   + e.getFirst() + "  " + e.getSecond());
                    cs.setRewardFunction(computeRewardFunction(newContext, cs, negotiateResourcesCommand));
                    // cs.rewind();
                    negotiateResourcesCommand.rewind(datacenterPolicyConversionModel);
                    queue.add(cs);
                }
            }*/

            newContext.rewind(datacenterPolicyConversionModel);

            newContext = reinforcementLearning(queue);
        } else {
            newContext.rewind(datacenterPolicyConversionModel);
        }
        return newContext;
    }

    protected Server getMinDistanceServer(Task task) {
        Server retServer = null;
        RequestedTaskInfo requestedTask = task.getRequestedInfo();
        double difference;
        double minDif = 10000000.0d;
        Collection<Server> servers = protegeFactory.getAllServerInstances();
        for (Server server : servers) {
            Collection<Core> cores = server.getAssociatedCPU().getAssociatedCore();

            difference = 0.0d;
            for (Core core : cores) {
                difference += Math.pow(core.getTotal() - core.getUsed() - requestedTask.getCpuMinAcceptableValue(), 2);
            }
            ontologyRepresentations.greenContextOntology.Memory mem = server.getAssociatedMemory();
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
        System.out.println("BEGIN:" + new java.util.Date());

        /*  try {
            Thread.sleep(1000000000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/

        smallestEntropyContext = null;

        TaskLifeManager.kill(protegeFactory, datacenterPolicyConversionModel);

        DatacenterMockupContext initialDataCenterContext = new DatacenterMockupContext();
        initialDataCenterContext.createMockupContextFromOntology(protegeFactory);
        System.out.println("Datacenter behavior on Tick");
        PriorityQueue<ContextSnapshot> queue = new PriorityQueue<ContextSnapshot>();
        ContextSnapshot initialContext = new ContextSnapshot(new LinkedList<Command>());
        Pair<Double, Policy> entropyAndPolicy = computeEntropy();

        System.out.println(entropyAndPolicy.getFirst() + " " + entropyAndPolicy.getSecond());

        initialContext.setContextEntropy(entropyAndPolicy.getFirst());
        initialContext.setRewardFunction(computeRewardFunction(null, initialContext, null));
        queue.add(initialContext);
        //   resultsFrame.setActionsList(null);

        if (entropyAndPolicy.getSecond() != null) {

            contextBroken = true;

            //TODO: activate only after a deploy or delete  to recollect
            //if context broken gather the extra resources allocated to tasks in order to properly evaluate the context
            //            for (Server server : protegeFactory.getAllServerInstances()) {
            //                server.collectPreviouslyDistributedResources(datacenterPolicyConversionModel);
            //            }

            //Gather data for logging purposes

            ArrayList<String> brokenQoSPolicies = new ArrayList<String>();
            Collection<QoSPolicy> qoSPolicies = protegeFactory.getAllQoSPolicyInstances();
            for (Policy policy : qoSPolicies) {
                if (policy.getReferenced() == null) {
                    continue;
                }
                if (!policy.getRespected(datacenterPolicyConversionModel)) {
                    brokenQoSPolicies.add(policy.getName().split("#")[1]);
                }
            }
            Collection<EnergyPolicy> brokenEnergyPolicies = protegeFactory.getAllEnergyPolicyInstances();
            for (Policy policy : brokenEnergyPolicies) {
                if (!policy.getRespected(datacenterPolicyConversionModel)) {
                    brokenQoSPolicies.add(policy.getName().split("#")[1]);
                }
            }
            try {
                X3DMessageDispatcher.sendMessage(agent, GlobalVars.GUIAGENT_NAME, new Object[]{"DatacenterLogger", Color.ORANGE, "Broken policies", brokenQoSPolicies});
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            //agent.getSelfOptimizingLogger().log(Color.ORANGE, "Broken policies", brokenQoSPolicies);

            Collection<Server> servers = protegeFactory.getAllServerInstances();
            Collection<Task> tasks = protegeFactory.getAllTaskInstances();
            ArrayList<String> currentState = new ArrayList(servers.size() + tasks.size());

            for (Server server : servers) {
                currentState.add(server.toString());
            }

            for (Task task : tasks) {
                currentState.add(task.toString());
            }
            try {
                X3DMessageDispatcher.sendMessage(agent, GlobalVars.GUIAGENT_NAME, new Object[]{"DatacenterLogger", Color.red, "Current state", currentState});
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            // agent.getSelfOptimizingLogger().log(Color.red, "Current state", currentState);

            try {
                X3DMessageDispatcher.sendMessage(agent, GlobalVars.GUIAGENT_NAME, new Object[]{"DatacenterLogger", Color.red, "Current state", currentState});
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            // End of logging

            //avoid addin new tasks when querying ontology
            //TODO: check check check!  TM
            //taskManagementWindow.setClearForAdding(false);
            long startSeconds = new java.util.Date().getTime();
            ContextSnapshot result = reinforcementLearning(queue);
            long endSeconds = new java.util.Date().getTime();

            int value = (int) ((endSeconds - startSeconds) / 1000);

            agent.setRlTime(value);
            System.err.println("Datacenter alg running time: " + value + " seconds");

            Collection<Command> resultQueue = result.getActions();
            ArrayList<String> message = new ArrayList<String>();
            for (Command o : resultQueue) {
                message.add(o.toString());
                System.out.println(o.toString());
                o.executeOnX3D(agent);
                o.executeOnWebService();   //---> to be decommented when running on servers
                o.execute(datacenterPolicyConversionModel);
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
                        ContextSnapshot cs = new ContextSnapshot(new LinkedList(result.getActions()));

                        Server server = getMinDistanceServer(task);
                        if (server == null) {
                            continue;
                        }
                        NegotiateResourcesCommand negotiateResourcesCommand = new NegotiateResourcesCommand(protegeFactory, negotiator, server.getServerName(), task.getName());
                        negotiateResourcesCommand.execute(datacenterPolicyConversionModel);
                        negotiateResourcesCommand.executeOnWebService();
                        /*  try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        */

                        cs.getActions().add(negotiateResourcesCommand);
                        cs.setContextEntropy(computeEntropy().getFirst());

                        if (cs.getContextEntropy() < result.getContextEntropy()) {
                            result = cs;
                        }
                        //  negotiateResourcesCommand.rewind(datacenterPolicyConversionModel);
                    }
                }

                datacenterMemory.memorize(initialDataCenterContext, result.getActions());
                //  System.out.println("Distributing empty resources : This should not happen anymore");
                //                for (Server server : servers) {
                //                    server.distributeRemainingResources(datacenterPolicyConversionModel);
                //                }
            }

            datacenterMemory.memorize(initialDataCenterContext, result.getActions());


            try {
                X3DMessageDispatcher.sendMessage(agent, GlobalVars.GUIAGENT_NAME, new Object[]{"DatacenterLogger", Color.BLUE, "Corrective actions", currentState});
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
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

            /* try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            */

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

                try {
                    X3DMessageDispatcher.sendMessage(agent, GlobalVars.GUIAGENT_NAME, new Object[]{"DatacenterLogger", Color.GREEN, "Current state", currentState});
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                // agent.getSelfOptimizingLogger().log(Color.GREEN, "Current state", currentState);

            }
        }

//
//        Collection<Task> tasks = protegeFactory.getAllTaskInstances();
//        Collection<Server> servers = protegeFactory.getAllServerInstances();
//        Boolean deployed = false;
//        for (Server server : servers) {
//            WakeUpServerCommand wakeUpServer = new WakeUpServerCommand(protegeFactory, server.getServerName());
//            wakeUpServer.execute(datacenterPolicyConversionModel);
//            //  wakeUpServer.executeOnX3D(agent);
//            wakeUpServer.executeOnWebService();
//        }
//        int undeployed = 0;
//        for (Task task : tasks) {
//            if (!task.isRunning()) {
//                deployed = false;
//                for (Server server : servers) {
//                    if (server.hasResourcesFor(task) && (deployed == false)) {
//                        SelfOptimizingCommand deployTask = new DeployTaskCommand(protegeFactory, server.getName(), task.getName());
//                        deployTask.execute(datacenterPolicyConversionModel);
//                        //           deployTask.executeOnX3D(agent);
//                        deployTask.executeOnWebService();
//                        deployed = true;
//                    }
//                }
//                if (deployed == false) undeployed++;
//            }
//
//        }

        Collection<Server> servers = protegeFactory.getAllServerInstances();
        Collection<Task> tasks = protegeFactory.getAllTaskInstances();

        int d = 0;
        for (Task task : tasks) {
            if (task.isRunning()) {
                d++;
            }
        }

        //System.out.println("Servers no: " + servers.size() + "Deployed: " + d + "Undeployed: " + (tasks.size() - d));
        for (Task server : tasks) {
            System.out.println("" + server.toString());
        }


        System.out.println("END:" + new java.util.Date());
        agent.sendAllTasksToClient();

    }

}