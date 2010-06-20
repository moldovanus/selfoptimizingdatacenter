package ontologyRepresentations.greenContextOntology.impl;

import com.hp.hpl.jena.ontology.OntModel;
import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLIndividual;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLFactoryException;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import ontologyRepresentations.greenContextOntology.ContextElement;

/**
 * Base class for every class which represents a datacenter ontology entity
 */
public class DefaultContextElement extends DefaultOWLIndividual
        implements ContextElement {

    public DefaultContextElement(OWLModel owlModel, FrameID id) {
        super(owlModel, id);
    }


    public DefaultContextElement() {
    }

    /**
     * Sets the property value both on the OWL model and on the ONT model
     * to trigger SWRL rule evaluation 
     * @param rdfProperty the slot of the entity
     * @param o the value of the property to be inserted in slot rdfProperty
     * @param ontModel the ont model on which this property will also be set
     */
    public void setPropertyValue(RDFProperty rdfProperty, Object o, OntModel ontModel) {
        super.setPropertyValue(rdfProperty, o);

        //to be commented to avoid SWRL rule evaluation
//        Individual targetIndividual = ontModel.getIndividual(this.getName());
//
//        Property targetProperty = ontModel.getProperty(rdfProperty.getName());
//        if (targetIndividual.getPropertyValue(targetProperty) != null) {
//            targetIndividual.removeAll(targetProperty);
//        }
//        targetIndividual.setPropertyValue(targetProperty, ontModel.createLiteralStatement(
//                targetIndividual, targetProperty, o).getLiteral().as(RDFNode.class));
    }

    public final void deleteInstance(OntModel ontModel, SWRLFactory swrlFactory)
            throws SWRLFactoryException {
        super.delete();

        //to be commented to avoid SWRL rule evaluation
        //remove instance from underlying Ont model
//        Individual i = ontModel.getIndividual(getName());
//        i.remove();
//
//        //remove swrl rule associated to task
//        SWRLImp rule = swrlFactory
//                .getImp("http://www.owl-ontologies.com/Datacenter.owl#QoS_Policy_"
//                + this.getName().split("_")[1] + "_swrl_rule");
//        rule.delete();
    }

    

}

