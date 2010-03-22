package selfHealingOntology.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.impl.*;
import edu.stanford.smi.protegex.owl.javacode.AbstractCodeGeneratorIndividual;
import selfHealingOntology.*;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Ontology1230214892.owl#ContextElement
 *
 * @version generated on Wed Mar 17 12:47:46 EET 2010
 */
public class DefaultContextElement extends DefaultOWLIndividual
        implements ContextElement {

    public DefaultContextElement(OWLModel owlModel, FrameID id) {
        super(owlModel, id);
    }


    public DefaultContextElement() {
    }


    public void setPropertyValue(RDFProperty rdfProperty, Object o, OntModel ontModel) {
        super.setPropertyValue(rdfProperty, o);
        Individual targetIndividual = ontModel.getIndividual(this.getName());
        if (targetIndividual == null ){System.exit(1);}
        Property targetProperty = ontModel.getProperty(rdfProperty.getName());
        targetIndividual.removeAll(targetProperty);
        targetIndividual.setPropertyValue(targetProperty, ontModel.createLiteralStatement(
                targetIndividual, targetProperty, o).getLiteral().as(RDFNode.class));
    }
}
