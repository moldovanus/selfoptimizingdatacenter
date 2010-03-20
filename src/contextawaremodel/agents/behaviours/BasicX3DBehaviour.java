/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contextawaremodel.agents.behaviours;

import actionselection.x3dCommand.X3DCommand;
import actionselection.x3dCommand.X3DStringCommand;
import contextawaremodel.agents.X3DAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * @author Administrator
 */
public class BasicX3DBehaviour extends CyclicBehaviour {

    private X3DAgent agent;

    public BasicX3DBehaviour(X3DAgent agent) {
        this.agent = agent;

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

                    Object[] actionInfo = (Object[]) message.getContentObject();
                    String targetAction = (String) actionInfo[0];
                    if (targetAction.equals("addTask")) {
                        agent.addTask((String) actionInfo[1], (String) actionInfo[2], (Integer) actionInfo[3]);
                        String[] data = ((String) actionInfo[2]).split("_");
                        agent.addObjectLabel("Watts: " + ((Integer) actionInfo[3] * 10), "PowerMeterGroup_0" + (String) data[1], X3DAgent.POWER_LABEL_COLOR, X3DAgent.POWER_METER_LABEL_TRANSLATION);
                    } else if (targetAction.equals("removeTask")) {
                        agent.removeTask((String) actionInfo[1]);
                    } else if (targetAction.equals("moveTask")) {
                        agent.removeTask((String) actionInfo[1]);
                        String[] data = ((String) actionInfo[2]).split("_");
                        agent.addObjectLabel("Watts: " + ((Integer) actionInfo[3] * 10), "PowerMeterGroup_0" + (String) data[1], X3DAgent.POWER_LABEL_COLOR, X3DAgent.POWER_METER_LABEL_TRANSLATION);
                        agent.addTask((String) actionInfo[1], (String) actionInfo[2], (Integer) actionInfo[3]);
                    } else if (targetAction.equals("sendToLowPower")) {
                        agent.sendServerToLowPower((String) actionInfo[1]);
                    } else if (targetAction.equals("wakeUpServer")) {
                        agent.wakeUpServer((String) actionInfo[1]);
                    } else if (targetAction.equals("setTemperature")) {
                        agent.addObjectLabel("Temperature: " + actionInfo[1], "SensorSphere_01", X3DAgent.SENSOR_LABEL_COLOR, X3DAgent.SENSOR_LABEL_TRANSLATION);
                    } else if (targetAction.equals("setHumidity")) {
                        agent.addObjectLabel("Humidity: " + actionInfo[1], "SensorSphere_02", X3DAgent.SENSOR_LABEL_COLOR, X3DAgent.SENSOR_LABEL_TRANSLATION);
                    }

                    /* X3DCommand command = (X3DCommand) message.getContentObject();
                    command.execute(agent.getMainScene());
                    if (command instanceof X3DStringCommand) {
                        X3DStringCommand x3dCommand = (X3DStringCommand) command;
                        if (x3dCommand.getNodeName().equals("computerState_STRING")) {
                            agent.flipComputerState(x3dCommand.getContent());
                        }
                    }
                    */
                    break;

                case ACLMessage.INFORM_IF:
                    break;
            }
        }

        catch (
                Exception ex
                )

        {
            ex.printStackTrace(System.err);
        }
    }
}
