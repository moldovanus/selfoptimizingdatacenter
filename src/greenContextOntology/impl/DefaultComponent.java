package greenContextOntology.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protegex.owl.model.*;
import greenContextOntology.*;
import com.hp.hpl.jena.ontology.OntModel;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Datacenter.owl#Component
 *
 * @version generated on Sun Mar 07 13:11:11 EET 2010
 */
public class DefaultComponent extends DefaultResource
         implements Component {

    public DefaultComponent(OWLModel owlModel, FrameID id) {
        super(owlModel, id);
    }


    public DefaultComponent() {
    }

	    // Property http://www.owl-ontologies.com/Datacenter.owl#maxAcceptableValue

    public int getMaxAcceptableValue() {
        return getPropertyValueLiteral(getMaxAcceptableValueProperty()).getInt();
    }


    public RDFProperty getMaxAcceptableValueProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#maxAcceptableValue";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }


    public boolean hasMaxAcceptableValue() {
        return getPropertyValueCount(getMaxAcceptableValueProperty()) > 0;
    }


    public void setMaxAcceptableValue(int newMaxAcceptableValue) {
        setPropertyValue(getMaxAcceptableValueProperty(), new java.lang.Integer(newMaxAcceptableValue));
    }



    // Property http://www.owl-ontologies.com/Datacenter.owl#minAcceptableValue

    public int getMinAcceptableValue() {
        return getPropertyValueLiteral(getMinAcceptableValueProperty()).getInt();
    }


    public RDFProperty getMinAcceptableValueProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#minAcceptableValue";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }


    public boolean hasMinAcceptableValue() {
        return getPropertyValueCount(getMinAcceptableValueProperty()) > 0;
    }


    public void setMinAcceptableValue(int newMinAcceptableValue) {
        setPropertyValue(getMinAcceptableValueProperty(), new java.lang.Integer(newMinAcceptableValue));
    }

  

    // Property http://www.owl-ontologies.com/Datacenter.owl#total

    public int getTotal() {
        return getPropertyValueLiteral(getTotalProperty()).getInt();
    }


    public RDFProperty getTotalProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#total";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }


    public boolean hasTotal() {
        return getPropertyValueCount(getTotalProperty()) > 0;
    }


    public void setTotal(int newTotal) {
        setPropertyValue(getTotalProperty(), new java.lang.Integer(newTotal));
    }



    // Property http://www.owl-ontologies.com/Datacenter.owl#used

    public int getUsed() {
        return getPropertyValueLiteral(getUsedProperty()).getInt();
    }


    public RDFProperty getUsedProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#used";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }


    public boolean hasUsed() {
        return getPropertyValueCount(getUsedProperty()) > 0;
    }


    public void setUsed(int newUsed,OntModel model) {
        setPropertyValue(getUsedProperty(), new java.lang.Integer(newUsed),model);
    }

     // Property http://www.owl-ontologies.com/Datacenter.owl#weight

    public float getWeight() {
        return getPropertyValueLiteral(getWeightProperty()).getFloat();
    }


    public RDFProperty getWeightProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#weight";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }


    public boolean hasWeight() {
        return getPropertyValueCount(getWeightProperty()) > 0;
    }


    public void setWeight(float newWeight) {
        setPropertyValue(getWeightProperty(), new java.lang.Float(newWeight));
    }
    
}
