/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contextaware.agents.behaviours;

import contextaware.agents.X3DAgent;
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
                    if (targetAction.equals(X3DAgent.ADD_TASK_COMMAND)) {
                        agent.addTask((String) actionInfo[1], (String) actionInfo[2], (Integer) actionInfo[3]);
                        String[] data = ((String) actionInfo[2]).split("_");
                        agent.addObjectLabel("Watts: " + ((Integer) actionInfo[3] * 10), "PowerMeterGroup_0" + (String) data[1], X3DAgent.POWER_LABEL_COLOR, X3DAgent.POWER_METER_LABEL_TRANSLATION);
                    } else if (targetAction.equals(X3DAgent.REMOVE_TASK_COMMAND)) {
                        agent.removeTask((String) actionInfo[1]);
                    } else if (targetAction.equals(X3DAgent.MOVE_TASK_COMMAND)) {
                        agent.removeTask((String) actionInfo[1]);
                        String[] data = ((String) actionInfo[2]).split("_");
                        agent.addObjectLabel("Watts: " + ((Integer) actionInfo[3] * 10), "PowerMeterGroup_0" + (String) data[1], X3DAgent.POWER_LABEL_COLOR, X3DAgent.POWER_METER_LABEL_TRANSLATION);
                        agent.addTask((String) actionInfo[1], (String) actionInfo[2], (Integer) actionInfo[3]);
                    } else if (targetAction.equals(X3DAgent.SEND_SERVER_TO_LOW_POWER_COMMAND)) {
                        agent.sendServerToLowPower((String) actionInfo[1]);
                    } else if (targetAction.equals(X3DAgent.WAKE_UP_SERVER_COMMAND)) {
                        agent.wakeUpServer((String) actionInfo[1]);
                    } else if (targetAction.equals(X3DAgent.SET_TEMPERATURE_COMMAND)) {
                        agent.addObjectLabel("Temperature: " + actionInfo[1], "SensorSphere_01", X3DAgent.SENSOR_LABEL_COLOR, X3DAgent.SENSOR_LABEL_TRANSLATION);
                        agent.animateSensor("SensorTube_01_XFORM");
                    } else if (targetAction.equals(X3DAgent.SET_HUMIDITY_COMMAND)) {
                        agent.addObjectLabel("Humidity: " + actionInfo[1], "SensorSphere_02", X3DAgent.SENSOR_LABEL_COLOR, X3DAgent.SENSOR_LABEL_TRANSLATION);
                        agent.animateSensor("SensorTube_02_XFORM");
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
