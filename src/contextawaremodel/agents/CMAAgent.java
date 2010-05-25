package contextawaremodel.agents;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import contextawaremodel.GlobalVars;
import contextawaremodel.agents.behaviours.InformCIACMAABehaviour;
import contextawaremodel.gui.GUIAgent;
import contextawaremodel.ontology.MyFactory;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import policyconversioncore.PoliciesHandler;

import java.io.File;
//import contextawaremodel.model.SimulatedContext;
//import contextawaremodel.gui.SimulatorMainWindow;

public class CMAAgent extends Agent implements CMAAExternal {

    //the owl model
    private JenaOWLModel owlModelDataCenter;
    private OntModel policyConversionModelDataCenter;
    private JenaOWLModel jenaOwlModelDataCenter;
    private JenaOWLModel owlModeSelfHealing;
    private OntModel policyConversionModelSelfHealing;
    private JenaOWLModel jenaOwlModelSelfHealing;

    //the protege factory
    public MyFactory datacenterFactory;
    //the protege factory
    public MyFactory factory;
    //the main window of the simulator
    //private SimulatorMainWindow smw;
    //the real world implemented model
    //the object used for executing in the Swing thread
    private Runnable addIt;
    //Controller for the CIAgent which is created by the CMMA agent
    private AgentController cia = null;
    //Controller for the RPAgent which is created by the CMMA agent
    private AgentController rpa = null;
    //Controller for the EMAgent which is created by the CMMA agent
    private AgentController ema = null;
    //Controller for the EMAgent which is created by the CMMA agent
    private AgentController gui = null;
    //rl agent
    private AgentController rl = null;
    //x3d agent
    private AgentController x3d = null;
    //task management agent
    private AgentController tm = null;

    @Override
    protected void setup() {
        System.out.println("CMA Agent " + getLocalName() + " started.");

        try {
            //create owlModeSelfHealing from Ontology
            File ontologyFile = new File(GlobalVars.ONTOLOGY_FILE);
            this.owlModeSelfHealing = ProtegeOWL.createJenaOWLModelFromURI(ontologyFile.toURI().toString());
            this.factory = new MyFactory(owlModeSelfHealing);
            
            File file = new File(GlobalVars.ONTOLOGY_FILE);
            jenaOwlModelSelfHealing = ProtegeOWL.createJenaOWLModelFromURI(file.toURI().toString());

            //create owlModeSelfHealing from Ontology
            File ontologyDataCenterFile = new File(GlobalVars.ONTOLOGY_DATACENTER_FILE);
            this.owlModelDataCenter = ProtegeOWL.createJenaOWLModelFromURI(ontologyDataCenterFile.toURI().toString());
            this.datacenterFactory = new MyFactory(owlModelDataCenter);


            File dataCenterFile = new File(GlobalVars.ONTOLOGY_DATACENTER_FILE);
            jenaOwlModelDataCenter = ProtegeOWL.createJenaOWLModelFromURI(dataCenterFile.toURI().toString());

            // politici
            PoliciesHandler policiesHandler = new PoliciesHandler();
            policiesHandler.loadPolicies(GlobalVars.POLICIES_FILE);
            //List<String> swrlCode = policiesHandler.getPoliciesConverter().convertAllPolicies();

            // adaugare reguli in ontologie
            /*SWRLFactory factory = new SWRLFactory(owlModeSelfHealing);
            for (String s : swrlCode) {
            System.out.println("Adding rule: " + s);
            factory.createImp(s);
            }*/

            policyConversionModelSelfHealing = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
            policyConversionModelSelfHealing.add(jenaOwlModelSelfHealing.getJenaModel());


            policyConversionModelDataCenter = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
            policyConversionModelDataCenter.add(jenaOwlModelDataCenter.getJenaModel());

            //runnable object for refreshing the GUI
            //this.addIt = new Runnable() {
            //	public void run() {
            //		smw.updateImage();
            //	}
            //};

            //initialize the simulator GUI
            //this.smw = new SimulatorMainWindow( new SimulatedContext(this.owlModeSelfHealing, this) );
            //this.smw.setLocationRelativeTo(null);
            //this.smw.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            //this.smw.setVisible(true);
            //refreshGui ();

            //star the Context Interpreting Agent
            AgentContainer container = (AgentContainer) getContainerController(); // get a container controller for creating new agents
            //createNewAgent(Name, Class name, arguments to the agent)
            //cia = container.createNewAgent(GlobalVars.CIAGENT_NAME, CIAgent.class.getName(), new Object[]{this.owlModeSelfHealing});
            //cia.start();
            //this.getContainerController().createNewAgent("RMA", "jade.tools.rma.rma", null).start();

            gui = container.createNewAgent(GlobalVars.GUIAGENT_NAME, GUIAgent.class.getName(), new Object[]{this.owlModelDataCenter});
            gui.start();

            rl = container.createNewAgent(GlobalVars.RLAGENT_NAME, ReinforcementLearningAgent.class.getName(), new Object[]{this.owlModeSelfHealing, this.policyConversionModelSelfHealing, this.jenaOwlModelSelfHealing, this.owlModelDataCenter, this.policyConversionModelDataCenter, this.jenaOwlModelDataCenter});
            rl.start();

            //tm = container.createNewAgent(GlobalVars.TMAGENT_NAME, TaskManagementAgent.class.getName(), new Object[]{});
            //tm.start();

            //todo : change when running X3DAgent remotely
            /* String platformName = this.getContainerController().getPlatformName();
            GlobalVars.setX3DPlatformName(platformName);
            GlobalVars.setX3DPlatformAddress("http://" + platformName.split(":")[0] + ":7778/acc");
            x3d = container.createNewAgent(GlobalVars.X3DAGENT_NAME, X3DAgent.class.getName(), null);
            x3d.start();*/
            //x3d = container.createNewAgent(GlobalVars.X3DAGENT_NAME, X3DAgent.class.getName(), null);
            //x3d.start();

            //star the Request Processing Agent

            //createNewAgent(Name, Class name, arguments to the agent)
            //rpa = container.createNewAgent(GlobalVars.RPAGENT_NAME, RPAgent.class.getName(), new Object[] {this.owlModeSelfHealing});
            //rpa.start();

            //start the execution and monitoring agent
            //ema = container.createNewAgent(GlobalVars.EMAGENT_NAME, EMAgent.class.getName(), new Object[] {this.owlModeSelfHealing});
            //ema.start();

            // addBehaviour(new BasicCMAABehaviour(this, this.owlModeSelfHealing));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void addNewBehaviour(Behaviour newB) {
        this.addBehaviour(newB);
    }

    //send a message to context interpreting agent (context instance has changed)
    public void informCia(String indvName, int aCode) {
        addBehaviour(new InformCIACMAABehaviour(this, indvName, aCode));
    }
    //send message to request processing agent (new request is available)
    //public void informRPA(actor a, String request) {
    //	addBehaviour( new InformRPACMAABehaviour(this, a, request) );
    //}
}
