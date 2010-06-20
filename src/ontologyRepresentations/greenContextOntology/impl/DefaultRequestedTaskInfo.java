package ontologyRepresentations.greenContextOntology.impl;

import com.hp.hpl.jena.ontology.OntModel;
import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import ontologyRepresentations.greenContextOntology.DatacenterProtegeFactory;
import ontologyRepresentations.greenContextOntology.RequestedTaskInfo;

import java.util.UUID;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Datacenter.owl#RequestedTaskInfo
 *
 * @version generated on Thu Apr 15 14:24:33 EEST 2010
 */
public class DefaultRequestedTaskInfo extends DefaultContextElement
        implements RequestedTaskInfo {

    public DefaultRequestedTaskInfo(OWLModel owlModel, FrameID id) {
        super(owlModel, id);
    }

    public DefaultRequestedTaskInfo() {
    }

    // Property http://www.owl-ontologies.com/Datacenter.owl#cores
    public int getCores() {
        return getPropertyValueLiteral(getCoresProperty()).getInt();
    }

    public RDFProperty getCoresProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#cores";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasCores() {
        return getPropertyValueCount(getCoresProperty()) > 0;
    }

    public void setCores(int newCores) {
        setPropertyValue(getCoresProperty(), new java.lang.Integer(newCores));
    }

    public void setCores(int newCores, OntModel model) {
        setPropertyValue(getCoresProperty(), new java.lang.Integer(newCores), model);
    }

    // Property http://www.owl-ontologies.com/Datacenter.owl#cpuMaxAcceptableValue
    public int getCpuMaxAcceptableValue() {
        return getPropertyValueLiteral(getCpuMaxAcceptableValueProperty()).getInt();
    }

    public RDFProperty getCpuMaxAcceptableValueProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#cpuMaxAcceptableValue";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasCpuMaxAcceptableValue() {
        return getPropertyValueCount(getCpuMaxAcceptableValueProperty()) > 0;
    }

    public void setCpuMaxAcceptableValue(int newCpuMaxAcceptableValue) {
        setPropertyValue(getCpuMaxAcceptableValueProperty(), new java.lang.Integer(newCpuMaxAcceptableValue));
    }

    public void setCpuMaxAcceptableValue(int newCpuMaxAcceptableValue, OntModel model) {
        setPropertyValue(getCpuMaxAcceptableValueProperty(), new java.lang.Integer(newCpuMaxAcceptableValue), model);
    }

    // Property http://www.owl-ontologies.com/Datacenter.owl#cpuMinAcceptableValue
    public int getCpuMinAcceptableValue() {
        return getPropertyValueLiteral(getCpuMinAcceptableValueProperty()).getInt();
    }

    public RDFProperty getCpuMinAcceptableValueProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#cpuMinAcceptableValue";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasCpuMinAcceptableValue() {
        return getPropertyValueCount(getCpuMinAcceptableValueProperty()) > 0;
    }

    public void setCpuMinAcceptableValue(int newCpuMinAcceptableValue) {
        setPropertyValue(getCpuMinAcceptableValueProperty(), new java.lang.Integer(newCpuMinAcceptableValue));
    }

    public void setCpuMinAcceptableValue(int newCpuMinAcceptableValue, OntModel model) {
        setPropertyValue(getCpuMinAcceptableValueProperty(), new java.lang.Integer(newCpuMinAcceptableValue), model);
    }

    // Property http://www.owl-ontologies.com/Datacenter.owl#memoryMaxAcceptableValue
    public int getMemoryMaxAcceptableValue() {
        return getPropertyValueLiteral(getMemoryMaxAcceptableValueProperty()).getInt();
    }

    public RDFProperty getMemoryMaxAcceptableValueProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#memoryMaxAcceptableValue";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasMemoryMaxAcceptableValue() {
        return getPropertyValueCount(getMemoryMaxAcceptableValueProperty()) > 0;
    }

    public void setMemoryMaxAcceptableValue(int newMemoryMaxAcceptableValue) {
        setPropertyValue(getMemoryMaxAcceptableValueProperty(), new java.lang.Integer(newMemoryMaxAcceptableValue));
    }

    public void setMemoryMaxAcceptableValue(int newMemoryMaxAcceptableValue, OntModel model) {
        setPropertyValue(getMemoryMaxAcceptableValueProperty(), new java.lang.Integer(newMemoryMaxAcceptableValue), model);
    }

    // Property http://www.owl-ontologies.com/Datacenter.owl#memoryMinAcceptableValue
    public int getMemoryMinAcceptableValue() {
        return getPropertyValueLiteral(getMemoryMinAcceptableValueProperty()).getInt();
    }

    public RDFProperty getMemoryMinAcceptableValueProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#memoryMinAcceptableValue";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasMemoryMinAcceptableValue() {
        return getPropertyValueCount(getMemoryMinAcceptableValueProperty()) > 0;
    }

    public void setMemoryMinAcceptableValue(int newMemoryMinAcceptableValue) {
        setPropertyValue(getMemoryMinAcceptableValueProperty(), new java.lang.Integer(newMemoryMinAcceptableValue));
    }

    public void setMemoryMinAcceptableValue(int newMemoryMinAcceptableValue, OntModel model) {
        setPropertyValue(getMemoryMinAcceptableValueProperty(), new java.lang.Integer(newMemoryMinAcceptableValue), model);
    }

    // Property http://www.owl-ontologies.com/Datacenter.owl#storageMaxAcceptableValue
    public int getStorageMaxAcceptableValue() {
        return getPropertyValueLiteral(getStorageMaxAcceptableValueProperty()).getInt();
    }

    public RDFProperty getStorageMaxAcceptableValueProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#storageMaxAcceptableValue";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasStorageMaxAcceptableValue() {
        return getPropertyValueCount(getStorageMaxAcceptableValueProperty()) > 0;
    }

    public void setStorageMaxAcceptableValue(int newStorageMaxAcceptableValue) {
        setPropertyValue(getStorageMaxAcceptableValueProperty(), new java.lang.Integer(newStorageMaxAcceptableValue));
    }

    public void setStorageMaxAcceptableValue(int newStorageMaxAcceptableValue, OntModel model) {
        setPropertyValue(getStorageMaxAcceptableValueProperty(), new java.lang.Integer(newStorageMaxAcceptableValue), model);
    }

    // Property http://www.owl-ontologies.com/Datacenter.owl#storageMinAcceptableValue
    public int getStorageMinAcceptableValue() {
        return getPropertyValueLiteral(getStorageMinAcceptableValueProperty()).getInt();
    }

    public RDFProperty getStorageMinAcceptableValueProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#storageMinAcceptableValue";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasStorageMinAcceptableValue() {
        return getPropertyValueCount(getStorageMinAcceptableValueProperty()) > 0;
    }

    public void setStorageMinAcceptableValue(int newStorageMinAcceptableValue) {
        setPropertyValue(getStorageMinAcceptableValueProperty(), new java.lang.Integer(newStorageMinAcceptableValue));
    }

    public void setStorageMinAcceptableValue(int newStorageMinAcceptableValue, OntModel model) {
        setPropertyValue(getStorageMinAcceptableValueProperty(), new java.lang.Integer(newStorageMinAcceptableValue), model);
    }

    public Object clone(DatacenterProtegeFactory protegeFactory) {
        RequestedTaskInfo requestedTaskInfo = protegeFactory.createRequestedTaskInfo(this.getLocalName() + "_" + UUID.randomUUID());
        requestedTaskInfo.setCores(this.getCores());

        requestedTaskInfo.setCpuMaxAcceptableValue(this.getCpuMaxAcceptableValue());
        requestedTaskInfo.setCpuMinAcceptableValue(this.getCpuMinAcceptableValue());

        requestedTaskInfo.setStorageMaxAcceptableValue(this.getStorageMaxAcceptableValue());
        requestedTaskInfo.setStorageMinAcceptableValue(this.getStorageMinAcceptableValue());

        requestedTaskInfo.setMemoryMaxAcceptableValue(this.getMemoryMaxAcceptableValue());
        requestedTaskInfo.setMemoryMinAcceptableValue(this.getMemoryMinAcceptableValue());

        return requestedTaskInfo;
    }
}