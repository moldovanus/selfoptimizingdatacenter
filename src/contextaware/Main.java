package contextaware;

import contextaware.agents.CMAAgent;

public class Main {

    public static void main(String[] args) {
        String[] jadeArgs = new String[]{"-mtp jamr.jademtp.http.MessageTransportProtocol","-gui", GlobalVars.CMAGENT_NAME + ":" + CMAAgent.class.getName()};
        jade.Boot.main(jadeArgs);
    }

}
