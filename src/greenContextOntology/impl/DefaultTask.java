package greenContextOntology.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protegex.owl.model.*;
import java.util.*;

import greenContextOntology.*;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Datacenter.owl#Task
 *
 * @version generated on Sun Mar 07 13:11:11 EET 2010
 */
public class DefaultTask extends DefaultContextElement
        implements greenContextOntology.Task {

    public DefaultTask(OWLModel owlModel, FrameID id) {
        super(owlModel, id);
    }

    public DefaultTask() {
    }

// Property http://www.owl-ontologies.com/Datacenter.owl#associatedInfo
    public Collection getAssociatedInfo() {
        return getPropertyValuesAs(getAssociatedInfoProperty(), TaskInfo.class);
    }

    public RDFProperty getAssociatedInfoProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#associatedInfo";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasAssociatedInfo() {
        return getPropertyValueCount(getAssociatedInfoProperty()) > 0;
    }

    public Iterator listAssociatedInfo() {
        return listPropertyValuesAs(getAssociatedInfoProperty(), TaskInfo.class);
    }

    public void addAssociatedInfo(TaskInfo newAssociatedInfo) {
        addPropertyValue(getAssociatedInfoProperty(), newAssociatedInfo);
    }

    public void removeAssociatedInfo(TaskInfo oldAssociatedInfo) {
        removePropertyValue(getAssociatedInfoProperty(), oldAssociatedInfo);
    }

    public void setAssociatedInfo(Collection newAssociatedInfo) {
        setPropertyValues(getAssociatedInfoProperty(), newAssociatedInfo);
    }

    // Property http://www.owl-ontologies.com/Datacenter.owl#associatedServer
    public Server getAssociatedServer() {
        return (Server) getPropertyValueAs(getAssociatedServerProperty(), Server.class);
    }

    public RDFProperty getAssociatedServerProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#associatedServer";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasAssociatedServer() {
        return getPropertyValueCount(getAssociatedServerProperty()) > 0;
    }

    public void setAssociatedServer(Server newAssociatedServer) {
        setPropertyValue(getAssociatedServerProperty(), newAssociatedServer);
    }
}
