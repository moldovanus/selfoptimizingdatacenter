package greenContextOntology;

import edu.stanford.smi.protegex.owl.model.*;


/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Datacenter.owl#Task
 *
 * @version generated on Sun Mar 07 13:11:11 EET 2010
 */
public interface Task extends ContextElement {
 // Property http://www.owl-ontologies.com/Datacenter.owl#associatedServer

    Server getAssociatedServer();

    RDFProperty getAssociatedServerProperty();

    boolean hasAssociatedServer();

    void setAssociatedServer(Server newAssociatedServer);


    // Property http://www.owl-ontologies.com/Datacenter.owl#receivedInfo

    TaskInfo getReceivedInfo();

    RDFProperty getReceivedInfoProperty();

    boolean hasReceivedInfo();

    void setReceivedInfo(TaskInfo newReceivedInfo);


    // Property http://www.owl-ontologies.com/Datacenter.owl#requestedInfo

    TaskInfo getRequestedInfo();

    RDFProperty getRequestedInfoProperty();

    boolean hasRequestedInfo();

    void setRequestedInfo(TaskInfo newRequestedInfo);

    @Override
    String toString();

    boolean requestsSatisfied();

    boolean isRunning();
}
