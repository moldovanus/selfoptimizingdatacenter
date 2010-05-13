package contextawaremodel.agents;

import contextawaremodel.GlobalVars;
import contextawaremodel.agents.behaviours.ReceiveMessageTMBehaviour;
import contextawaremodel.gui.TaskDto;
import contextawaremodel.gui.TaskManagement;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: Apr 15, 2010
 * Time: 2:18:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaskManagementAgent extends Agent {
    private TaskManagement taskManagementWindow;

    protected void setup() {
        System.out.println("Task Management Agent " + getLocalName() + " started.");
        this.addBehaviour(new ReceiveMessageTMBehaviour(this));
        taskManagementWindow = new TaskManagement(this);
        taskManagementWindow.setVisible(true);
    }

    public void reenableTasks() {
        taskManagementWindow.reenableTasksList();
    }

    public void clearFields() {
        taskManagementWindow.setEmptyFields();
    }

    public void populateTaskWindow(TaskDto[] tasks) {
        taskManagementWindow.setTasks(tasks);
    }


    public void sendTaskMessageToRL(Object message, int aCode) {
        ACLMessage msg;

        System.out.println("Code" + aCode);
        switch (aCode) {
            case GlobalVars.INDIVIDUAL_CREATED:
                msg = new ACLMessage(ACLMessage.INFORM);
                try {
                    msg.setContentObject((Serializable) message);
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                break;
            case GlobalVars.INDIVIDUAL_DELETED:
                msg = new ACLMessage(ACLMessage.INFORM_IF);
                msg.setContent((String) message);

                break;
            case GlobalVars.INDIVIDUAL_MODIFIED:
                msg = new ACLMessage(ACLMessage.INFORM_REF);
                break;
            default:
                msg = null;
                break;
        }

        if (msg != null) {
            msg.addReceiver(new AID(GlobalVars.RLAGENT_NAME + "@" + this.getContainerController().getPlatformName()));
            /*
            try {
                msg.setContentObject(indvName);
                msg.setLanguage("JavaSerialization");
            } catch (IOException e) {
                e.printStackTrace();
            }   */
            System.out.println("Sending" + msg);
            this.send(msg);
        }
    }
}
