package greenContextOntology.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protegex.owl.model.*;
 
import java.util.*;

import greenContextOntology.*;
import greenContextOntology.Task;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Datacenter.owl#QoSPolicy
 *
 * @version generated on Sat Mar 06 16:14:39 EET 2010
 */
public class DefaultQoSPolicy extends DefaultPolicy
         implements QoSPolicy {

    public DefaultQoSPolicy(OWLModel owlModel, FrameID id) {
        super(owlModel, id);
    }


    public DefaultQoSPolicy() {
    }



    // Property http://www.owl-ontologies.com/Datacenter.owl#references

    public Collection getReferences() {
        return getPropertyValuesAs(getReferencesProperty(), Task.class);
    }


    public RDFProperty getReferencesProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#references";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }


    public boolean hasReferences() {
        return getPropertyValueCount(getReferencesProperty()) > 0;
    }


    public Iterator listReferences() {
        return listPropertyValuesAs(getReferencesProperty(), Task.class);
    }


    public void addReferences(Task newReferences) {
        addPropertyValue(getReferencesProperty(), newReferences);
    }


    public void removeReferences(Task oldReferences) {
        removePropertyValue(getReferencesProperty(), oldReferences);
    }


    public void setReferences(Collection newReferences) {
        setPropertyValues(getReferencesProperty(), newReferences);
    }
}
