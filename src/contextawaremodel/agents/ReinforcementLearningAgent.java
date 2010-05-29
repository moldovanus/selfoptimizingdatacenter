/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contextawaremodel.agents;

import actionselection.context.DatacenterMemory;
import actionselection.context.Memory;
import com.hp.hpl.jena.ontology.OntModel;
import contextawaremodel.GlobalVars;
import contextawaremodel.agents.behaviours.ReceiveMessageRLBehaviour;
import contextawaremodel.agents.behaviours.ReinforcementLearningBasicBehaviour;
import contextawaremodel.agents.behaviours.ReinforcementLearningDataCenterBehavior;
import contextawaremodel.worldInterface.dtos.TaskDto;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import greenContextOntology.ProtegeFactory;
import greenContextOntology.ReceivedTaskInfo;
import greenContextOntology.RequestedTaskInfo;
import greenContextOntology.Task;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import logger.LoggerGUI;

import java.io.*;
import java.util.Collection;

/**
 * @author Administrator
 */
public class ReinforcementLearningAgent extends Agent {
    private OWLModel owlModelDataCenter;
    private OntModel policyConversionModelDataCenter;
    private JenaOWLModel jenaOwlModelDataCenter;

    private JenaOWLModel contextAwareModel;
    private OntModel policyConversionModel;
    private JenaOWLModel jenaOwlModel;
    private Memory memorySelfHealing;
    private DatacenterMemory memorySelfOptimizing;
    private int rlTime;
    private int totalRunningTime;
    private int runCount = 0;
    private boolean contextIsOK = true;
    private boolean contextDirty = false;
    // private LoggerGUI selfHealingLogger;
    // private LoggerGUI selfOptimizingLogger;

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
        ProtegeFactory protegeFactory = new ProtegeFactory(owlModelDataCenter);
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


            //selfOptimizingLogger = new LoggerGUI("selfOptimizingLog");
            //selfOptimizingLogger.setLogPath("logs/");

            this.contextAwareModel = (JenaOWLModel) args[0];
            this.policyConversionModel = (OntModel) args[1];
            jenaOwlModel = (JenaOWLModel) args[2];

            owlModelDataCenter = (OWLModel) args[3];
            policyConversionModelDataCenter = (OntModel) args[4];
            jenaOwlModelDataCenter = (JenaOWLModel) args[5];
            try {

                File memoryFile = new File(GlobalVars.MEMORY_FILE);
                File memoryDatacenterFile = new File(GlobalVars.MEMORY_SELFOPTIMIZING_FILE);
                try {
                    FileInputStream fileInputStream = new FileInputStream(memoryFile);
                    ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
                    //memory = (Memory) inputStream.readObject();
                    //memory.restoreOwlModel(policyConversionModel);
                    memorySelfHealing = new Memory();
                    memorySelfOptimizing = new DatacenterMemory();
                } catch (FileNotFoundException ex) {
                    System.err.println(ex.getMessage());
                    memorySelfHealing = new Memory();
                    memorySelfOptimizing = new DatacenterMemory();
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                    memorySelfHealing = new Memory();
                    memorySelfOptimizing = new DatacenterMemory();
                }// catch (ClassNotFoundException ex) {
                //  System.err.println(ex.getMessage());
                // memory = new Memory();
                // }
                /*

                Map<String, Map<String, String>> valueMapping = GlobalVars.getValueMapping();

                Map<String, String> mapping = new HashMap<String, String>();
                mapping.put("0.00", "OFF");
                mapping.put("1.00", "ON");

                valueMapping.put("AlarmStateSensorI", mapping);
                valueMapping.put("ComputerStateSensorI", mapping);
                valueMapping.put("LightSensorI", mapping);


                Map<String, String> roomEmpty = new HashMap<String, String>();
                roomEmpty.put("0.00", "EMPTY");
                roomEmpty.put("1.00", "NOT EMPTY");
                valueMapping.put("RoomStateSensorI", roomEmpty);

                //Map<String, String> movementEmpty = new HashMap<String, String>();
                //movementEmpty.put("0.00", "NO MOVEMENT");
                //movementEmpty.put("1.00", "MOVEMENT DETECTED");
                //valueMapping.put("RoomEmptySensorI", movementEmpty);


                Map<String, String> faceRecognition = new HashMap<String, String>();
                faceRecognition.put("0.00", "PROFESSOR");
                faceRecognition.put("1.00", "STUDENT");
                faceRecognition.put("2.00", "UNKNOWN");
                valueMapping.put("FaceRecognitionSensorI", faceRecognition);
                */
                addBehaviour(new ReinforcementLearningBasicBehaviour(this, 1000, policyConversionModel, jenaOwlModel, memorySelfHealing));
                addBehaviour(new ReinforcementLearningDataCenterBehavior(this, 2000, owlModelDataCenter, policyConversionModelDataCenter,
                        jenaOwlModelDataCenter, policyConversionModel,
                        jenaOwlModel, memorySelfHealing, memorySelfOptimizing));
                //addBehaviour(new ContextDisturbingBehaviour(this,5000, policyConversionModel));
                addBehaviour(new ReceiveMessageRLBehaviour(this, jenaOwlModel, policyConversionModel, jenaOwlModelDataCenter, policyConversionModelDataCenter));
                //addBehaviour(new StoreMemoryBehaviour(this, 5000, memory));
                //addBehaviour(new RLPlotterBehaviour(this, 1000));
                //addBehaviour(new GarbadgeCollectForcerAgent(this,60000));

                /* Command c = new SetCommand("http://www.owl-ontologies.com/Ontology1230214892.owl#AlarmStateSensorI",
                        "http://www.owl-ontologies.com/Ontology1230214892.owl#has-value-of-service",
                        "http://www.owl-ontologies.com/Ontology1230214892.owl#has-web-service-URI", policyConversionModel, 0);
                c.execute();
                c.executeOnWebService();

                c = new SetCommand("http://www.owl-ontologies.com/Ontology1230214892.owl#FaceRecognitionSensorI",
                        "http://www.owl-ontologies.com/Ontology1230214892.owl#has-value-of-service",
                        "http://www.owl-ontologies.com/Ontology1230214892.owl#has-web-service-URI", policyConversionModel, 0);
                c.execute();
                c.executeOnWebService();

                c = new SetCommand("http://www.owl-ontologies.com/Ontology1230214892.owl#ComputerStateSensorI",
                        "http://www.owl-ontologies.com/Ontology1230214892.owl#has-value-of-service",
                        "http://www.owl-ontologies.com/Ontology1230214892.owl#has-web-service-URI", policyConversionModel, 1);
                c.execute();
                c.executeOnWebService();



                c = new SetCommand("http://www.owl-ontologies.com/Ontology1230214892.owl#RoomStateSensorI",
                        "http://www.owl-ontologies.com/Ontology1230214892.owl#has-value-of-service",
                        "http://www.owl-ontologies.com/Ontology1230214892.owl#has-web-service-URI", policyConversionModel, 1);
                c.execute();
                c.executeOnWebService();

                c = new SetCommand("http://www.owl-ontologies.com/Ontology1230214892.owl#HumiditySensorI",
                        "http://www.owl-ontologies.com/Ontology1230214892.owl#has-value-of-service",
                        "http://www.owl-ontologies.com/Ontology1230214892.owl#has-web-service-URI", policyConversionModel, 21);
                c.execute();
                c.executeOnWebService();

                c = new SetCommand("http://www.owl-ontologies.com/Ontology1230214892.owl#TemperatureSensorI",
                        "http://www.owl-ontologies.com/Ontology1230214892.owl#has-value-of-service",
                        "http://www.owl-ontologies.com/Ontology1230214892.owl#has-web-service-URI", policyConversionModel, 21);
                c.execute();
                c.executeOnWebService();

                c = new SetCommand("http://www.owl-ontologies.com/Ontology1230214892.owl#LightSensorI",
                        "http://www.owl-ontologies.com/Ontology1230214892.owl#has-value-of-service",
                        "http://www.owl-ontologies.com/Ontology1230214892.owl#has-web-service-URI", policyConversionModel, 1);
                c.execute();
                c.executeOnWebService();*/
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            this.contextAwareModel = null;
            this.policyConversionModel = null;
            System.out.println("[RL] RL Agent failed, owlModel arguments are null!");
        }

    }
}
