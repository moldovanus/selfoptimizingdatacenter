package actionselection.utils;

import contextawaremodel.GlobalVars;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: Apr 9, 2010
 * Time: 9:59:14 AM
 * To change this template use File | Settings | File Templates.
 */
public final class MessageDispatcher {

    private MessageDispatcher() {
    }

    public static void sendMessage(Agent sender, String destinationName, Serializable messageContent) throws IOException {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContentObject(messageContent);

        //TODO :  change when running remote
        //AID aid = new AID(GlobalVars.X3DAGENT_NAME + "@" + GlobalVars.getX3DPlatformName());
        //aid.addAddresses(GlobalVars.getX3DPlatformAddress());
        AID aid = new AID(destinationName + "@" + sender.getContainerController().getPlatformName());

        message.addReceiver(aid);
        message.setLanguage("JavaSerialization");
        sender.send(message);
    }
}
