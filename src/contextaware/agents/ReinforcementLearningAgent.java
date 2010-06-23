/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contextaware.agents;

import com.hp.hpl.jena.ontology.OntModel;
import contextaware.GlobalVars;
import contextaware.agents.behaviours.ReceiveMessageRLBehaviour;
import contextaware.agents.behaviours.ReinforcementLearningDataCenterManagementBehavior;
import contextaware.agents.behaviours.ReinforcementLearningEnvironmentManagementBehaviour;
import contextaware.agents.behaviours.StoreMemoryBehaviour;
import contextaware.worldInterface.dtos.TaskDto;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import ontologyRepresentations.greenContextOntology.DatacenterProtegeFactory;
import ontologyRepresentations.greenContextOntology.ReceivedTaskInfo;
import ontologyRepresentations.greenContextOntology.RequestedTaskInfo;
import ontologyRepresentations.greenContextOntology.Task;
import ontologyRepresentations.selfHealingOntology.SelfHealingProtegeFactory;
import utils.context.DatacenterMemory;
import utils.context.EnvironmentMemory;

import java.io.*;
import java.util.Collection;

/**
 * @author Administrator
 */
public class ReinforcementLearningAgent extends Agent {
    private OWLModel datacenterOwlModel;
    private OntModel datacenterOntModel;
    private JenaOWLModel datacenterJenaOwlModel;

    private JenaOWLModel environmentOwlModel;
    private OntModel environmentOntModel;
    private JenaOWLModel environmentJenaOwlModel;
    private EnvironmentMemory memorySelfHealing;
    private DatacenterMemory memorySelfOptimizing;
    private int rlTime;
    private int totalRunningTime;
    private int runCount = 0;
    private boolean contextIsOK = true;
    private boolean contextDirty = false;


    public boolean isContextDirty() {
        return contextDirty;
    }

    public void setContextDirty(boolean dirtyContext) {
        this.contextDirty = dirtyContext;
    }

    public boolean isContextIsOK() {
        return contextIsOK;
    }

    public void setContextIsOK(boolean contextIsOK) {
        this.contextIsOK = contextIsOK;
    }

    public int getTotalRunningTime() {
        return totalRunningTime;
    }

    public int getRlAverageTime() {
        if (runCount == 0) {
            return 0;
        } else {
            return totalRunningTime / runCount;
        }
    }

    public int getRlTime() {
        return rlTime;
    }

    public void setRlTime(int rlTime) {
        this.rlTime = rlTime;
        this.totalRunningTime += rlTime;
        runCount++;
    }

    public void sendRefuseMessage() {
        ACLMessage msg = new ACLMessage(ACLMessage.REFUSE);
        msg.setContent("Server not found");
        msg.addReceiver(new AID(GlobalVars.TMAGENT_NAME + "@" + this.getContainerController().getPlatformName()));

        this.send(msg);

    }

    public void sendAllTasksToClient() {
        DatacenterProtegeFactory protegeFactory = new DatacenterProtegeFactory(datacenterOwlModel);
        Collection<Task> tasks = protegeFactory.getAllTaskInstances();

        TaskDto[] t = new TaskDto[tasks.size()];
        int i = 0;
        for (Task task : tasks) {
            /*  receivedInfo = task.getReceivedInfo();
            requestedInfo = task.getRequestedInfo();
            s += task.getLocalName() + "-" + task.isRunning() + "=" + requestedInfo.getCores() + "=" + requestedInfo.getCpuMinAcceptableValue() + "=" + requestedInfo.getCpuMaxAcceptableValue() + "=" + requestedInfo.getMemoryMinAcceptableValue() + "=" + requestedInfo.getMemoryMaxAcceptableValue() + "=" + requestedInfo.getStorageMinAcceptableValue() + "=" + requestedInfo.getStorageMaxAcceptableValue();
            s += "=" + receivedInfo.getCores() + "=" + receivedInfo.getCpuReceived() + "=" + receivedInfo.getMemoryReceived() + "=" + receivedInfo.getStorageReceived() + "<";
            */
            t[i] = new TaskDto();
            t[i].setTaskName(task.getTaskName());
            t[i].setRunning(task.isRunning());
            RequestedTaskInfo requestedInfo = task.getRequestedInfo();
            ReceivedTaskInfo receivedInfo = task.getReceivedInfo();

            t[i].setRequestedCores(requestedInfo.getCores());
            t[i].setRequestedCPUMax(requestedInfo.getCpuMaxAcceptableValue());
            t[i].setRequestedCPUMin(requestedInfo.getCpuMinAcceptableValue());
            t[i].setRequestedMemoryMax(requestedInfo.getMemoryMaxAcceptableValue());
            t[i].setRequestedMemoryMin(requestedInfo.getMemoryMinAcceptableValue());
            t[i].setRequestedStorageMax(requestedInfo.getStorageMaxAcceptableValue());
            t[i].setRequestedStorageMin(requestedInfo.getStorageMinAcceptableValue());

            t[i].setReceivedCores(receivedInfo.getCores());
            t[i].setReceivedCPU(receivedInfo.getCpuReceived());
            t[i].setReceivedMemory(receivedInfo.getMemoryReceived());
            t[i].setReceivedStorage(receivedInfo.getStorageReceived());

            i++;
        }
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM_REF);
        if (msg != null) {
            try {
                msg.setContentObject(t);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            msg.addReceiver(new AID(GlobalVars.TMAGENT_NAME + "@" + this.getContainerController().getPlatformName()));
            /*
            try {
                msg.setContentObject(indvName);
                msg.setLanguage("JavaSerialization");
            } catch (IOException e) {
                e.printStackTrace();
            }   */
            this.send(msg);
        }
    }

    @Override
    protected void setup() {
        System.out.println("[RL agent] Hello!");
        //the owl model is passed as an argument by the Administrator Agent
        Object[] args = getArguments();
        if (args != null) {

            this.environmentOwlModel = (JenaOWLModel) args[0];
            this.environmentOntModel = (OntModel) args[1];
            environmentJenaOwlModel = (JenaOWLModel) args[2];

            datacenterOwlModel = (OWLModel) args[3];
            datacenterOntModel = (OntModel) args[4];
            datacenterJenaOwlModel = (JenaOWLModel) args[5];
            try {

                File memoryFile = new File(GlobalVars.MEMORY_SELFHEALING_FILE);
                File memoryDatacenterFile = new File(GlobalVars.MEMORY_SELFOPTIMIZING_FILE);
                try {
                    FileInputStream fileInputStream = new FileInputStream(memoryFile);
                    ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);

                    memorySelfHealing = (EnvironmentMemory) inputStream.readObject();
                    memorySelfHealing.restoreProtegeFactory(new SelfHealingProtegeFactory(environmentOwlModel));

                    FileInputStream fileInputStreamDatacenter = new FileInputStream(memoryDatacenterFile);
                    ObjectInputStream inputStreamDatacenter = new ObjectInputStream(fileInputStreamDatacenter);

                    memorySelfOptimizing = (DatacenterMemory) inputStreamDatacenter.readObject();
                    memorySelfOptimizing.restoreProtegeFactory(new DatacenterProtegeFactory(datacenterOwlModel));

                    //comment this if memory needed
                    memorySelfHealing = new EnvironmentMemory();
                    memorySelfOptimizing = new DatacenterMemory();

                } catch (FileNotFoundException ex) {
                    System.err.println(ex.getMessage());
                    memorySelfHealing = new EnvironmentMemory();
                    memorySelfOptimizing = new DatacenterMemory();
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                    memorySelfHealing = new EnvironmentMemory();
                    memorySelfOptimizing = new DatacenterMemory();
                } 
 
                addBehaviour(new ReinforcementLearningEnvironmentManagementBehaviour(this, 10000, environmentOntModel, environmentJenaOwlModel, memorySelfHealing));
                addBehaviour(new ReinforcementLearningDataCenterManagementBehavior(this, 10000, datacenterOwlModel, datacenterOntModel,
                        datacenterJenaOwlModel, environmentOntModel,
                        environmentJenaOwlModel, memorySelfHealing, memorySelfOptimizing));
                addBehaviour(new ReceiveMessageRLBehaviour(this, environmentJenaOwlModel, environmentOntModel, datacenterOwlModel, datacenterOntModel));
                addBehaviour(new StoreMemoryBehaviour(this, 5000, memorySelfHealing,memorySelfOptimizing));
                //addBehaviour(new RLPlotterBehaviour(this, 1000));

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            this.environmentOwlModel = null;
            this.environmentOntModel = null;
            System.out.println("[RL] RL Agent failed, owlModel arguments are null!");
        }

    }
}
