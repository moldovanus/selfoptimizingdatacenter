/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package contextawaremodel.agents.behaviours;

import actionselection.command.Command;
import actionselection.context.ContextSnapshot;
import actionselection.context.Memory;
import actionselection.gui.ActionsOutputFrame;
import actionselection.utils.Pair;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Statement;
import contextawaremodel.GlobalVars;
import contextawaremodel.agents.ReinforcementLearningAgent;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import greenContextOntology.ProtegeFactory;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Administrator
 */
public class ReinforcementLearningDataCenterBehavior extends TickerBehaviour {
      private OWLModel contextAwareModel;
    private OntModel policyConversionModel;
    private JenaOWLModel owlModel;
    private double smallestEntropy = 10000;
    private Memory memory;
    private Property evaluatePolicyProperty;
    private ActionsOutputFrame resultsFrame;
    private ReinforcementLearningAgent agent;
    private boolean contextBroken = false;
    private ProtegeFactory protegeFactory;
    public ReinforcementLearningDataCenterBehavior(Agent a, int interval, OWLModel contextAwareModel, OntModel policyConversionModel, JenaOWLModel owlModel, Memory memory) {
        super(a, interval);
        agent = (ReinforcementLearningAgent) a;
        this.contextAwareModel = contextAwareModel;
        this.policyConversionModel = policyConversionModel;
        protegeFactory = new ProtegeFactory(owlModel);
        this.owlModel = owlModel;
        resultsFrame = new ActionsOutputFrame();
        this.memory = memory;
             evaluatePolicyProperty = policyConversionModel.getDatatypeProperty(GlobalVars.base + "#EvaluatePolicyP");
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                resultsFrame.setVisible(true);

            }
        });
    }
    private Pair<Double, Individual> computeEntropy(){
            Individual brokenPolicy = null;
        double entropy = 0.0;
        Collection<RDFResource> resources = owlModel.getRDFResources();

        for (RDFResource resource : resources) {

            if (resource.getProtegeType().getNamedSuperclasses(true).contains(owlModel.getRDFSNamedClass("policy"))) {
                Individual policy = policyConversionModel.getIndividual(GlobalVars.base + "#" + resource.getProtegeType().getName() + "I").asIndividual();
                if (!getEvaluateProp(policy)) {
                    if (brokenPolicy == null) {
                        brokenPolicy = policy;
                    }
                    //System.err.println("Broken " + policy);
                    entropy++;
                }
            }
        }
         return new Pair<Double, Individual>(entropy, brokenPolicy);
    }
        public boolean getEvaluateProp(Individual policy) {
        //System.out.println(base + "#EvaluatePolicyP");
        Statement property = policy.getProperty(evaluatePolicyProperty);

        if (property == null) {
            return false;
        }

        return property.getBoolean();
    }

    @Override
    protected void onTick() {
        Queue<ContextSnapshot> queue = new LinkedList<ContextSnapshot>();
        ContextSnapshot initialContext = new ContextSnapshot( new LinkedList<Command>());
        queue.add(initialContext);
        resultsFrame.setActionsList(null);
        smallestEntropy = 10000;
        ContextSnapshot contextSnapshot;
        
    }

}
