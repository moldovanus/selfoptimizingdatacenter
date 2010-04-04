package greenContextOntology.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLIndividual;
import edu.stanford.smi.protegex.owl.javacode.AbstractCodeGeneratorIndividual;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLFactoryException;
import greenContextOntology.*;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Datacenter.owl#ContextElement
 *
 * @version generated on Sun Mar 07 13:11:11 EET 2010
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
        /*Individual targetIndividual = ontModel.getIndividual(this.getName());

        Property targetProperty = ontModel.getProperty(rdfProperty.getName());
        if (targetIndividual.getPropertyValue(targetProperty) != null) {
            targetIndividual.removeAll(targetProperty);
        }
        targetIndividual.setPropertyValue(targetProperty, ontModel.createLiteralStatement(
                targetIndividual, targetProperty, o).getLiteral().as(RDFNode.class));*/
    }

    public final void deleteInstance(OntModel ontModel, SWRLFactory swrlFactory) throws SWRLFactoryException {
        super.delete();

        //remove instance from underlying Ont model
        //Individual i = ontModel.getIndividual(getName());
        //i.remove();

        //remove swrl rule associated to task
        /*System.out.println("Deleting " + "http://www.owl-ontologies.com/Datacenter.owl#QoS_Policy_" + this.getName().split("_")[1] + "_swrl_rule");
        SWRLImp rule = swrlFactory.getImp("http://www.owl-ontologies.com/Datacenter.owl#QoS_Policy_" + this.getName().split("_")[1] + "_swrl_rule");
        rule.delete();*/
    }

}

