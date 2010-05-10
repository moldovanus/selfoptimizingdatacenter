package greenContextOntology;

import com.hp.hpl.jena.ontology.OntModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;


/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Datacenter.owl#Policy
 *
 * @version generated on Sun Mar 07 13:11:11 EET 2010
 */
public interface Policy extends ContextElement {

    // Property http://www.owl-ontologies.com/Datacenter.owl#priority

    int getPriority();

    RDFProperty getPriorityProperty();

    boolean hasPriority();

    void setPriority(int newPriority);


    // Property http://www.owl-ontologies.com/Datacenter.owl#referenced

    Object getReferenced();

    RDFProperty getReferencedProperty();

    boolean hasReferenced();

    void setReferenced(Object newReferenced);

    // Property http://www.owl-ontologies.com/Datacenter.owl#respected

    boolean getRespected(OntModel model);

    boolean getRespected();

    RDFProperty getRespectedProperty();

    boolean hasRespected();

    void setRespected(boolean newRespected);
}
