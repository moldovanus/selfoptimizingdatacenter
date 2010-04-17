package contextawaremodel.agents.behaviours;

import contextawaremodel.agents.TaskManagementAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: Apr 15, 2010
 * Time: 5:42:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReceiveMessageTMBehaviour extends CyclicBehaviour {

    private TaskManagementAgent agent;

    public ReceiveMessageTMBehaviour(Agent agent) {
        super(agent);
        this.agent=(TaskManagementAgent)agent;
    }

    @Override
    public void action() {
        ACLMessage message = agent.receive();
        if (message == null) {
            return;
        }

        try {
            switch (message.getPerformative()) {
                case ACLMessage.INFORM_REF:
                    String mes = message.getContent();
                    String parts[]= mes.split("<");
                    String names[]= new String[parts.length];
                    String cpuReceived[]= new String[parts.length];
                    String memoryReceived[]= new String[parts.length];
                    String storageReceived[]= new String[parts.length];

                     String minCpuRequested[]= new String[parts.length];
                    String minMemoryRequested[]= new String[parts.length];
                    String minStorageRequested[]= new String[parts.length];
                    String maxCpuRequested[]= new String[parts.length];
                    String maxMemoryRequested[]= new String[parts.length];
                    String maxStorageRequested[]= new String[parts.length];
                    int index=0;
                    System.out.println("ala male !!!!!!!!" + mes);
                    for (String s: parts)
                    {
                        System.out.println(s);
                        String str[]=s.split("=");
                        names[index]=str[0];
                        minCpuRequested[index] = str[1] ;
                        maxCpuRequested[index] = str[2] ;
                        minMemoryRequested[index] = str[3];
                        maxMemoryRequested[index] = str[4] ;
                        minStorageRequested[index] = str[5] ;
                        maxStorageRequested[index] = str[6];
                        cpuReceived[index] = str[7] ;
                        memoryReceived[index] = str[8];
                        storageReceived[index] = str[9];
                        index++;
                    }
                    agent.clear();
                    agent.populateTaskWindow(names,minCpuRequested,maxCpuRequested,minMemoryRequested,maxMemoryRequested,minStorageRequested,maxStorageRequested,cpuReceived,memoryReceived,storageReceived);
                    break;

                case ACLMessage.INFORM:

                    break;

                case ACLMessage.SUBSCRIBE:
                   
                    break;
                case ACLMessage.REFUSE:     
                    JOptionPane.showMessageDialog(null,message.getContent(),"The transaction wasn't effectuated",1);
                    agent.reenableTasks();
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
}
