package greenContextOntology.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLIndividual;

import java.util.*;

import greenContextOntology.*;
import com.hp.hpl.jena.ontology.Individual;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Datacenter.owl#CPU
 *
 * @version generated on Sun Mar 07 13:11:11 EET 2010
 */
public class DefaultCPU extends DefaultComponent
        implements CPU {

    public DefaultCPU(OWLModel owlModel, FrameID id) {
        super(owlModel, id);
    }


    public DefaultCPU() {
    }


    // Property http://www.owl-ontologies.com/Datacenter.owl#associatedCore

    public Collection getAssociatedCore() {
        return getPropertyValuesAs(getAssociatedCoreProperty(), Core.class);
    }


    public RDFProperty getAssociatedCoreProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#associatedCore";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }


    public boolean hasAssociatedCore() {
        return getPropertyValueCount(getAssociatedCoreProperty()) > 0;
    }


    public Iterator listAssociatedCore() {
        return listPropertyValuesAs(getAssociatedCoreProperty(), Core.class);
    }


    public void addAssociatedCore(Core newAssociatedCore) {
        addPropertyValue(getAssociatedCoreProperty(), newAssociatedCore);
    }


    public void removeAssociatedCore(Core oldAssociatedCore) {
        removePropertyValue(getAssociatedCoreProperty(), oldAssociatedCore);
    }


    public void setAssociatedCore(Collection newAssociatedCore) {
        setPropertyValues(getAssociatedCoreProperty(), newAssociatedCore);
    }


    // Property http://www.owl-ontologies.com/Datacenter.owl#numberOfCores

    public int getNumberOfCores() {
        return getPropertyValueLiteral(getNumberOfCoresProperty()).getInt();
    }


    public RDFProperty getNumberOfCoresProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#numberOfCores";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }


    public boolean hasNumberOfCores() {
        return getPropertyValueCount(getNumberOfCoresProperty()) > 0;
    }


    public void setNumberOfCores(int newNumberOfCores) {
        setPropertyValue(getNumberOfCoresProperty(), new java.lang.Integer(newNumberOfCores));
    }

    
}
