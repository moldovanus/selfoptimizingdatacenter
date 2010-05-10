package contextawaremodel.agents;

import contextawaremodel.GlobalVars;
import contextawaremodel.agents.behaviours.ReceiveMessageTMBehaviour;
import contextawaremodel.gui.TaskManagement;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;


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
        //taskManagementWindow = new TaskManagement(this);
       // taskManagementWindow.setVisible(true);
    }
  /*  public void reenableTasks(){
        taskManagementWindow.reenableTasksList();
    }
    public void clearFields(){
        taskManagementWindow.setEmptyFields();
    }
    public void populateTaskWindow(String[] names,String [] coresRequested, String[] minCpuRequested, String[] maxCpuRequested, String[] minMemoryRequested, String[] maxMemoryRequested, String[] minStorageRequested, String[] maxStorageRequested,String[] coresReceived,String[] cpuReceived, String[] memoryReceived, String[] storageReceived) {
        taskManagementWindow.populate(names, coresRequested,minCpuRequested , maxCpuRequested, minMemoryRequested, maxMemoryRequested, minStorageRequested, maxStorageRequested,coresReceived, cpuReceived, memoryReceived, storageReceived);
    }*/

    

    public void sendTaskMessageToRL(String message, int aCode) {
        ACLMessage msg;

        System.out.println("Code" + aCode);
        switch (aCode) {
            case GlobalVars.INDIVIDUAL_CREATED:
                msg = new ACLMessage(ACLMessage.INFORM);
                break;
            case GlobalVars.INDIVIDUAL_DELETED:
                msg = new ACLMessage(ACLMessage.INFORM_IF);
                break;
            case GlobalVars.INDIVIDUAL_MODIFIED:
                msg = new ACLMessage(ACLMessage.INFORM_REF);
                break;
            default:
                msg = null;
                break;
        }

        if (msg != null) {
            msg.setContent(message);
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
