package contextawaremodel;

public class Main {

    public static void main(String[] args) {
        String[] jadeArgs = new String[]{"-mtp jamr.jademtp.http.MessageTransportProtocol","-gui", GlobalVars.CMAGENT_NAME + ":" + contextawaremodel.agents.CMAAgent.class.getName()};
        jade.Boot.main(jadeArgs);
    }

}
