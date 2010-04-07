package greenContextOntology.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;

import java.util.*;

import greenContextOntology.*;
import greenContextOntology.Task;
import com.hp.hpl.jena.ontology.OntModel;


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
        if (hasAssociatedServer()){
            removePropertyValue(getAssociatedServerProperty(), newAssociatedServer);
        }
        setPropertyValue(getAssociatedServerProperty(), newAssociatedServer);
    }


    // Property http://www.owl-ontologies.com/Datacenter.owl#cpuWeight

    public float getCpuWeight() {
        return getPropertyValueLiteral(getCpuWeightProperty()).getFloat();
    }


    public RDFProperty getCpuWeightProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#cpuWeight";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }


    public boolean hasCpuWeight() {
        return getPropertyValueCount(getCpuWeightProperty()) > 0;
    }


    public void setCpuWeight(float newCpuWeight) {
        setPropertyValue(getCpuWeightProperty(), new java.lang.Float(newCpuWeight));
    }


    // Property http://www.owl-ontologies.com/Datacenter.owl#memoryWeight

    public float getMemoryWeight() {
        return getPropertyValueLiteral(getMemoryWeightProperty()).getFloat();
    }


    public RDFProperty getMemoryWeightProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#memoryWeight";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }


    public boolean hasMemoryWeight() {
        return getPropertyValueCount(getMemoryWeightProperty()) > 0;
    }


    public void setMemoryWeight(float newMemoryWeight) {
        setPropertyValue(getMemoryWeightProperty(), new java.lang.Float(newMemoryWeight));
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

    public void setReceivedInfo(TaskInfo newReceivedInfo, OntModel model) {
        setPropertyValue(getReceivedInfoProperty(), newReceivedInfo, model);
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

    public void setRequestedInfo(TaskInfo newRequestedInfo, OntModel model) {
        setPropertyValue(getRequestedInfoProperty(), newRequestedInfo, model);
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
        description += this.getName().split("#")[1];
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

    public float getStorageWeight() {
        return getPropertyValueLiteral(getStorageWeightProperty()).getFloat();
    }


    public RDFProperty getStorageWeightProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#storageWeight";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }


    public boolean hasStorageWeight() {
        return getPropertyValueCount(getStorageWeightProperty()) > 0;
    }


    public void setStorageWeight(float newStorageWeight) {
        setPropertyValue(getStorageWeightProperty(), new java.lang.Float(newStorageWeight));
    }



    // Property http://www.owl-ontologies.com/Datacenter.owl#taskName

    public String getTaskName() {
        return (String) getPropertyValue(getTaskNameProperty());
    }


    public RDFProperty getTaskNameProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#taskName";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }


    public boolean hasTaskName() {
        return getPropertyValueCount(getTaskNameProperty()) > 0;
    }


    public void setTaskName(String newTaskName) {
        setPropertyValue(getTaskNameProperty(), newTaskName);
    }

    public void createSWRLRule(SWRLFactory swrlFactory) {
        TaskInfo receivedInfo = this.getReceivedInfo();
        String rule = "Task(?TaskRef)  ?  taskName(?TaskRef, "
                + this.getName().split("#")[1] + ")  ?  receivedInfo(?TaskRef, ?ReceivedInfo)  " +
                "?  cores(?ReceivedInfo, ?ReceivedInfoCores)  ?  swrlb:equal(?ReceivedInfoCores, " + receivedInfo.getCores()+ ") " +
                "?  cpu(?ReceivedInfo, ?ReceivedInfoCPU)  ?  swrlb:equal(?ReceivedInfoCPU," + receivedInfo.getCpu()+ ") " +
                "?  memory(?ReceivedInfo, ?ReceivedInfoMemory)  ?  swrlb:equal(?ReceivedInfoMemory, " +  receivedInfo.getMemory()+")" +
                "?  storage(?ReceivedInfo, ?ReceivedInfoStorage)  ?  swrlb:equal(?ReceivedInfoStorage, " + + receivedInfo.getStorage() + ")" +
                "? respected(QoSPolicy_1, true)";
        try {
            swrlFactory.createImp(rule);
        } catch (SWRLParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
