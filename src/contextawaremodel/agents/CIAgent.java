package contextawaremodel.agents;

import contextawaremodel.agents.behaviours.ReceiveMessagesCIABehaviour;
import contextawaremodel.sensorapi.SensorAPI;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import greenContextOntology.ProtegeFactory;
import greenContextOntology.Server;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.mobility.MobilityOntology;

import java.util.Collection;

public class CIAgent extends Agent {

    private OWLModel environmentOwlModel;
    private OWLModel datacenterOwlModel;

    public OWLModel getEnvironmentOwlModel() {
        return this.environmentOwlModel;
    }

    public OWLModel getDatacenterOwlModel() {
        return datacenterOwlModel;
    }

    @Override
    protected void setup() {
        System.out.println("[CIA] Hello!");

        //the owl model is passed as an argument by the Administrator Agent
        Object[] args = getArguments();
        if (args != null) {
            this.environmentOwlModel = (OWLModel) args[0];
            this.datacenterOwlModel = (OWLModel) args[1];
            
            ProtegeFactory protegeFactory = new ProtegeFactory(datacenterOwlModel);
            Collection<Server> servers = protegeFactory.getAllServerInstances();
            for (Server server : servers) {
                SensorAPI.addServerListener(server, protegeFactory);
            }

            getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
            getContentManager().registerOntology(MobilityOntology.getInstance());

            addBehaviour(new ReceiveMessagesCIABehaviour(this, environmentOwlModel));
            //addBehaviour(new BasicCIABehaviour(environmentOwlModel , this));
        } else {
            this.environmentOwlModel = null;
            System.out.println("[CIA] CIA Agent failed, not enough or null arguments!");
        }
    }
}
