package greenContextOntology;

import edu.stanford.smi.protegex.owl.model.*;

import java.util.*;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Datacenter.owl#QoSPolicy
 *
 * @version generated on Sun Mar 07 13:11:11 EET 2010
 */
public interface QoSPolicy extends Policy {

    // Property http://www.owl-ontologies.com/Datacenter.owl#references

    Collection getReferences();

    RDFProperty getReferencesProperty();

    boolean hasReferences();

    Iterator listReferences();

    void addReferences(Task newReferences);

    void removeReferences(Task oldReferences);

    void setReferences(Collection newReferences);
}
