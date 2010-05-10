package contextawaremodel.agents.behaviours;

import contextawaremodel.GlobalVars;
import contextawaremodel.agents.CMAAgent;
import contextawaremodel.model.AgentRequest;
import contextawaremodel.ontology.actor;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

public class InformRPACMAABehaviour extends OneShotBehaviour {

	private CMAAgent agent;
	private actor a;
	private String requestValue;
	
	public InformRPACMAABehaviour (CMAAgent agent, actor a, String requestValue) {
		this.a = a;
		this.agent = agent;
		this.requestValue = requestValue;
	}
	
	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver( new AID(GlobalVars.RPAGENT_NAME + "@" + agent.getContainerController().getPlatformName())  );
	
		AgentRequest request = new AgentRequest(this.requestValue);
		try {
			msg.setContentObject(request);
			msg.setLanguage("JavaSerialization");
		} catch (IOException e) {
			e.printStackTrace();
		}
		agent.send(msg);

	}

}
