package contextawaremodel.agents;

import contextawaremodel.agents.behaviours.BasicEMABehaviour;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import jade.core.Agent;

public class EMAgent extends Agent {
    //	the owl model

    private JenaOWLModel owlModel;

    protected void setup() {
        System.out.println("EMA Agent " + getLocalName() + " started.");

        //the owl model is passed as an argument by the Administrator Agent
        Object[] args = getArguments();
        if (args != null) {
            this.owlModel = (JenaOWLModel) args[0];

            //getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
            //getContentManager().registerOntology(MobilityOntology.getInstance());

            addBehaviour(new BasicEMABehaviour(this.owlModel, this));
        } else {
            this.owlModel = null;
            System.out.println("EMA Agent failed, owlModel argument is null!");
        }
    }
}
