package selfHealingOntology;

import edu.stanford.smi.protegex.owl.model.RDFProperty;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Ontology1230214892.owl#Action
 *
 * @version generated on Wed Mar 17 12:47:46 EET 2010
 */
public interface Action extends ContextElement {

    // Property http://www.owl-ontologies.com/Ontology1230214892.owl#associatedResources

    Sensor getAssociatedResources();

    RDFProperty getAssociatedResourcesProperty();

    boolean hasAssociatedResources();

    void setAssociatedResources(Sensor newAssociatedResources);


    // Property http://www.owl-ontologies.com/Ontology1230214892.owl#effect

    String getEffect();

    RDFProperty getEffectProperty();

    boolean hasEffect();

    void setEffect(String newEffect);
}
