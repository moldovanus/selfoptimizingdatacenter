package contextaware.agents.behaviours;

import actionEnforcement.x3dCommand.X3DOnOffCommand;
import actionEnforcement.x3dCommand.X3DStringCommand;
import contextaware.GlobalVars;
import contextaware.sensorapi.impl.SensorAPI;
import contextaware.sensorapi.SensorListener;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiveMessagesCIABehaviour extends CyclicBehaviour {

    private Agent agent;
    private OWLModel environmentOwlModel;

    public ReceiveMessagesCIABehaviour(Agent agent, OWLModel environmentOwlModel) {
        this.agent = agent;
        this.environmentOwlModel = environmentOwlModel;
    }

    @Override
    public void action() {
        ACLMessage message = agent.receive();
        if (message == null) {
            return;
        }

        try {
            switch (message.getPerformative()) {
                case ACLMessage.INFORM:
                    final String individualName = (String) message.getContentObject();

                    final RDFResource individual = environmentOwlModel.getRDFResource(individualName);
                    if (!individual.getProtegeType().getNamedSuperclasses(true).contains(environmentOwlModel.getRDFSNamedClass("sensor"))) {
                        return;
                    }

                    RDFProperty urlProperty = environmentOwlModel.getRDFProperty("has-web-service-URI");
                    final RDFProperty valueProperty = environmentOwlModel.getRDFProperty("has-value-of-service");
                    String url = individual.getPropertyValue(urlProperty).toString();

                    SensorAPI.addSensorListener(url,
                            new SensorListener() {

                                public void valueChanged(double newValue) {

                                    individual.setPropertyValue(valueProperty, String.format("%1$2.2f", newValue));

                                }
                            });

                    break;
                case ACLMessage.INFORM_REF:

                    final String individualName_2 = (String) message.getContentObject();
                    final RDFResource individual_2 = environmentOwlModel.getRDFResource(individualName_2);
                    if (!individual_2.getProtegeType().getNamedSuperclasses(true).contains(environmentOwlModel.getRDFSNamedClass("sensor"))) {
                        return;
                    }

                    RDFProperty urlProperty_2 = environmentOwlModel.getRDFProperty("has-web-service-URI");
                    final RDFProperty valueProperty_2 = environmentOwlModel.getRDFProperty("has-value-of-service");
                    String url_2 = individual_2.getPropertyValue(urlProperty_2).toString();

                    //System.out.println("Modified " + individualName_2);

                    SensorAPI.addSensorListener(url_2,
                            new SensorListener() {

                                public void valueChanged(double newValue) {
                                    if (individualName_2.equals("LightSensorI")) {
                                        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                                        ACLMessage stringMessage = new ACLMessage(ACLMessage.INFORM);
                                        boolean value = (newValue > 0) ? true : false;
                                        String stringValue = (newValue > 0) ? "ON" : "OFF";

                                        X3DOnOffCommand command = new X3DOnOffCommand("Light", value);
                                        X3DStringCommand stringCommand = new X3DStringCommand("lightState_STRING", "Light: " + stringValue);
                                        try {
                                            message.setContentObject(command);
                                            stringMessage.setContentObject(stringCommand);
                                            message.setLanguage("JavaSerialization");
                                            stringMessage.setLanguage("JavaSerialization");
                                            message.addReceiver(new AID(GlobalVars.X3DAGENT_NAME + "@" + agent.getContainerController().getPlatformName()));
                                            stringMessage.addReceiver(new AID(GlobalVars.X3DAGENT_NAME + "@" + agent.getContainerController().getPlatformName()));

                                        } catch (IOException ex) {
                                            Logger.getLogger(ReceiveMessagesCIABehaviour.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        agent.send(message);
                                        agent.send(stringMessage);
                                    } else if (individualName_2.equals("RoomStateSensorI")) {
                                        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                                        String value = (newValue > 0) ? "FALSE" : "TRUE";
                                        X3DStringCommand command = new X3DStringCommand("roomEmpty_STRING", "Room empty: " + value);
                                        try {
                                            message.setContentObject(command);
                                            message.setLanguage("JavaSerialization");
                                            message.addReceiver(new AID(GlobalVars.X3DAGENT_NAME + "@" + agent.getContainerController().getPlatformName()));
                                        } catch (IOException ex) {
                                            Logger.getLogger(ReceiveMessagesCIABehaviour.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        agent.send(message);
                                    } else if (individualName_2.equals("ComputerStateSensorI")) {
                                        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                                        String value = (newValue > 0) ? "ON" : "OFF";
                                        X3DStringCommand command = new X3DStringCommand("computerState_STRING", "Computer state:  " + value);
                                        try {
                                            message.setContentObject(command);
                                            message.setLanguage("JavaSerialization");
                                            message.addReceiver(new AID(GlobalVars.X3DAGENT_NAME + "@" + agent.getContainerController().getPlatformName()));
                                        } catch (IOException ex) {
                                            Logger.getLogger(ReceiveMessagesCIABehaviour.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        agent.send(message);
                                    } else if (individualName_2.equals("TemperatureSensorI")) {
                                        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                                        int value = (int) newValue;
                                        X3DStringCommand command = new X3DStringCommand("temperatureState_STRING", "Temperature:  " + value);
                                        try {
                                            message.setContentObject(command);
                                            message.setLanguage("JavaSerialization");
                                            message.addReceiver(new AID(GlobalVars.X3DAGENT_NAME + "@" + agent.getContainerController().getPlatformName()));
                                        } catch (IOException ex) {
                                            Logger.getLogger(ReceiveMessagesCIABehaviour.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        agent.send(message);
                                    } else if (individualName_2.equals("AlarmStateSensorI")) {
                                        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                                        String value = (newValue > 0) ? "ON" : "OFF";
                                        X3DStringCommand command = new X3DStringCommand("alarmState_STRING", "Alarm state: " + value);
                                        try {
                                            message.setContentObject(command);
                                            message.setLanguage("JavaSerialization");
                                            message.addReceiver(new AID(GlobalVars.X3DAGENT_NAME + "@" + agent.getContainerController().getPlatformName()));
                                        } catch (IOException ex) {
                                            Logger.getLogger(ReceiveMessagesCIABehaviour.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        agent.send(message);
                                    } else if (individualName_2.equals("FaceRecognitionSensorI")) {
                                        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                                        String value;
                                        if (newValue >= 0 && newValue < 1) {
                                            value = "PROFESSOR";
                                        } else if (newValue >= 1 && newValue < 2) {
                                            value = "STUDENT";
                                        } else {
                                            value = "UNKNOWN";
                                        }
                                        X3DStringCommand command = new X3DStringCommand("faceRecognitionResult_STRING", "Face recognition: " + value);
                                        try {
                                            message.setContentObject(command);
                                            message.setLanguage("JavaSerialization");
                                            message.addReceiver(new AID(GlobalVars.X3DAGENT_NAME + "@" + agent.getContainerController().getPlatformName()));
                                        } catch (IOException ex) {
                                            Logger.getLogger(ReceiveMessagesCIABehaviour.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        agent.send(message);
                                    }

                                    individual_2.setPropertyValue(valueProperty_2, String.format("%1$2.2f", newValue));
                                }
                            });

                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
}
