package greenContextOntology.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protegex.owl.model.*;

import java.util.*;

import greenContextOntology.*;
import greenContextOntology.Task;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Datacenter.owl#Task
 *
 * @version generated on Sun Mar 07 13:11:11 EET 2010
 */
public class DefaultTask extends DefaultContextElement
        implements greenContextOntology.Task {

    //to check if it is already running
    private boolean deployed = false;

    public DefaultTask(OWLModel owlModel, FrameID id) {
        super(owlModel, id);
    }

    public DefaultTask() {
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

    // Property http://www.owl-ontologies.com/Datacenter.owl#receivedInfo
    public TaskInfo getReceivedInfo() {
        return (TaskInfo) getPropertyValueAs(getReceivedInfoProperty(), TaskInfo.class);
    }

    public RDFProperty getReceivedInfoProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#receivedInfo";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasReceivedInfo() {
        return getPropertyValueCount(getReceivedInfoProperty()) > 0;
    }

    public void setReceivedInfo(TaskInfo newReceivedInfo) {
        setPropertyValue(getReceivedInfoProperty(), newReceivedInfo);
    }

    // Property http://www.owl-ontologies.com/Datacenter.owl#requestedInfo
    public TaskInfo getRequestedInfo() {
        return (TaskInfo) getPropertyValueAs(getRequestedInfoProperty(), TaskInfo.class);
    }

    public RDFProperty getRequestedInfoProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#requestedInfo";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasRequestedInfo() {
        return getPropertyValueCount(getRequestedInfoProperty()) > 0;
    }

    public void setRequestedInfo(TaskInfo newRequestedInfo) {
        setPropertyValue(getRequestedInfoProperty(), newRequestedInfo);
    }

    public boolean requestsSatisfied() {
        TaskInfo requestedSLA = this.getRequestedInfo();
        TaskInfo receivedSLA = this.getReceivedInfo();
        if (requestedSLA.getCores() != receivedSLA.getCores()) {
            return false;
        }
        if (requestedSLA.getCpu() != receivedSLA.getCpu()) {
            return false;
        }
        if (requestedSLA.getMemory() != receivedSLA.getMemory()) {
            return false;
        }
        if (requestedSLA.getStorage() != receivedSLA.getStorage()) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        String description = "";
        description += this.getName();
        TaskInfo received = this.getReceivedInfo();
        TaskInfo requested = this.getRequestedInfo();

        description += "\n Received Cores = " + received.getCores();
        description += "\n Received CPU = " + received.getCpu();
        description += "\n Received Memory = " + received.getMemory();
        description += "\n Received Storage = " + received.getStorage();
        description += "\n Requested Cores = " + requested.getCores();
        description += "\n Requested CPU = " + requested.getCpu();
        description += "\n Requested Memory = " + requested.getMemory();
        description += "\n Requested Storage = " + requested.getStorage();

        return description;
    }

    public boolean isRunning() {
        TaskInfo received = this.getReceivedInfo();
        if (received.getCpu() > 0 || received.getMemory() > 0 || received.getStorage() > 0) {
            return true;
        } else {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
