package ontologyRepresentations.selfHealingOntology.impl;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import ontologyRepresentations.selfHealingOntology.Policy;
import ontologyRepresentations.selfHealingOntology.Sensor;

import java.util.Collection;
import java.util.Iterator;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Ontology1230214892.owl#Policy
 *
 * @version generated on Wed Mar 17 12:47:46 EET 2010
 */
public class DefaultPolicy extends DefaultContextElement
         implements Policy {

    public DefaultPolicy(OWLModel owlModel, FrameID id) {
        super(owlModel, id);
    }


    public DefaultPolicy() {
    }



    // Property http://www.owl-ontologies.com/Ontology1230214892.owl#EvaluatePolicyP

    public boolean getEvaluatePolicyP(OntModel ontModel) {
        Individual ind = ontModel.getIndividual(this.getName());
        Property isOK = ontModel.getProperty(getEvaluatePolicyPProperty().getName());
        RDFNode ok = ind.getPropertyValue(isOK);
        if (ok == null) {
            return false;
        } else {
            return ok.toString().contains("true");
        }
    }


    public RDFProperty getEvaluatePolicyPProperty() {
        final String uri = "http://www.owl-ontologies.com/Ontology1230214892.owl#EvaluatePolicyP";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }


    public boolean hasEvaluatePolicyP() {
        return getPropertyValueCount(getEvaluatePolicyPProperty()) > 0;
    }


    public void setEvaluatePolicyP(boolean newEvaluatePolicyP) {
        setPropertyValue(getEvaluatePolicyPProperty(), new java.lang.Boolean(newEvaluatePolicyP));
    }



    // Property http://www.owl-ontologies.com/Ontology1230214892.owl#associatedResources

    public Collection getAssociatedResources() {
        return getPropertyValuesAs(getAssociatedResourcesProperty(), Sensor.class);
    }


    public RDFProperty getAssociatedResourcesProperty() {
        final String uri = "http://www.owl-ontologies.com/Ontology1230214892.owl#associatedResources";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }


    public boolean hasAssociatedResources() {
        return getPropertyValueCount(getAssociatedResourcesProperty()) > 0;
    }


    public Iterator listAssociatedResources() {
        return listPropertyValuesAs(getAssociatedResourcesProperty(), Sensor.class);
    }


    public void addAssociatedResources(Sensor newAssociatedResources) {
        addPropertyValue(getAssociatedResourcesProperty(), newAssociatedResources);
    }


    public void removeAssociatedResources(Sensor oldAssociatedResources) {
        removePropertyValue(getAssociatedResourcesProperty(), oldAssociatedResources);
    }


    public void setAssociatedResources(Collection newAssociatedResources) {
        setPropertyValues(getAssociatedResourcesProperty(), newAssociatedResources);
    }
}
