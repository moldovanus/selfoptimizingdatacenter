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
                    String coresReceived[]= new String[parts.length];
                     String coresRequested[]= new String[parts.length];
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
                        coresRequested[index] = str[1];
                        minCpuRequested[index] = str[2] ;
                        maxCpuRequested[index] = str[3] ;
                        minMemoryRequested[index] = str[4];
                        maxMemoryRequested[index] = str[5] ;
                        minStorageRequested[index] = str[6] ;
                        maxStorageRequested[index] = str[7];
                        coresReceived[index] = str[8]  ;
                        cpuReceived[index] = str[9] ;
                        memoryReceived[index] = str[10];
                        storageReceived[index] = str[11];
                        index++;
                    }
                    agent.clear();
                    agent.populateTaskWindow(names,coresRequested,minCpuRequested,maxCpuRequested,minMemoryRequested,maxMemoryRequested,minStorageRequested,maxStorageRequested,coresReceived ,cpuReceived,memoryReceived,storageReceived);
                    agent.clearFields();
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
