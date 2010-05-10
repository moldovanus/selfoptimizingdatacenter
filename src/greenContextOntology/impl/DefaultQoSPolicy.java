package greenContextOntology.impl;

import com.hp.hpl.jena.ontology.OntModel;
import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import greenContextOntology.QoSPolicy;
import greenContextOntology.ReceivedTaskInfo;
import greenContextOntology.RequestedTaskInfo;
import greenContextOntology.Task;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Datacenter.owl#QoSPolicy
 *
 * @version generated on Sun Mar 07 13:11:11 EET 2010
 */
public class DefaultQoSPolicy extends DefaultPolicy
        implements QoSPolicy {

    public DefaultQoSPolicy(OWLModel owlModel, FrameID id) {
        super(owlModel, id);
    }


    public DefaultQoSPolicy() {
    }


    public boolean getRespected(OntModel ontModel) {
        Task task = this.getReferenced();
        ReceivedTaskInfo receivedInfo = task.getReceivedInfo();
        RequestedTaskInfo requestedInfo = task.getRequestedInfo();

        

        if (receivedInfo.getCores() < requestedInfo.getCores()) {
            return false;
        }
        if (receivedInfo.getCpuReceived() < requestedInfo.getCpuMinAcceptableValue()) {
            return false;
        }
        if (receivedInfo.getMemoryReceived() < requestedInfo.getMemoryMinAcceptableValue()) {
            return false;
        }
        if (receivedInfo.getStorageReceived() < requestedInfo.getStorageMinAcceptableValue()) {
            return false;
        }
        
        return true;
    }

// Property http://www.owl-ontologies.com/Datacenter.owl#referenced

    public Task getReferenced() {
        return (Task) getPropertyValueAs(getReferencedProperty(), Task.class);
    }


    public RDFProperty getReferencedProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#referenced";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }


    public boolean hasReferenced() {
        return getPropertyValueCount(getReferencedProperty()) > 0;
    }


    public void setReferenced(Task newReferenced) {
        setPropertyValue(getReferencedProperty(), newReferenced);
    }
}
