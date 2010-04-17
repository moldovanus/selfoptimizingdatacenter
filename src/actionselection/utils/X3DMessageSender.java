package actionselection.utils;

import jade.lang.acl.ACLMessage;
import jade.core.AID;
import jade.core.Agent;

import java.io.IOException;
import java.io.Serializable;

import contextawaremodel.GlobalVars;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: Apr 9, 2010
 * Time: 9:59:14 AM
 * To change this template use File | Settings | File Templates.
 */
public final class X3DMessageSender {

    private X3DMessageSender() {
    }

    public static void sendX3DMessage(Agent agent, Serializable messageContent) throws IOException {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContentObject(messageContent);

        //TODO :  change when running remote
        //AID aid = new AID(GlobalVars.X3DAGENT_NAME + "@" + GlobalVars.getX3DPlatformName());
        //aid.addAddresses(GlobalVars.getX3DPlatformAddress());
        AID aid = new AID(GlobalVars.X3DAGENT_NAME + "@" + agent.getContainerController().getPlatformName());

        message.addReceiver(aid);
        message.setLanguage("JavaSerialization");
        agent.send(message);
    }
}
