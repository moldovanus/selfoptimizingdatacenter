package ontologyRepresentations.greenContextOntology;

import com.hp.hpl.jena.ontology.OntModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;


/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Datacenter.owl#Task
 *
 * @version generated on Sun Mar 07 13:11:11 EET 2010
 */
public interface Task extends ContextElement, CloneableOntologyElement {
 // Property http://www.owl-ontologies.com/Datacenter.owl#associatedServer

    Server getAssociatedServer();

    RDFProperty getAssociatedServerProperty();

    boolean hasAssociatedServer();

    void setAssociatedServer(Server newAssociatedServer);


    // Property http://www.owl-ontologies.com/Datacenter.owl#cpuWeight

    float getCpuWeight();

    RDFProperty getCpuWeightProperty();

    boolean hasCpuWeight();

    void setCpuWeight(float newCpuWeight);


    // Property http://www.owl-ontologies.com/Datacenter.owl#memoryWeight

    float getMemoryWeight();

    RDFProperty getMemoryWeightProperty();

    boolean hasMemoryWeight();

    void setMemoryWeight(float newMemoryWeight);


    // Property http://www.owl-ontologies.com/Datacenter.owl#receivedInfo

    ReceivedTaskInfo getReceivedInfo();

    RDFProperty getReceivedInfoProperty();

    boolean hasReceivedInfo();

    void setReceivedInfo(ReceivedTaskInfo newReceivedInfo, OntModel model);
    void setReceivedInfo(ReceivedTaskInfo newReceivedInfo);


    // Property http://www.owl-ontologies.com/Datacenter.owl#requestedInfo

    RequestedTaskInfo getRequestedInfo();

    RDFProperty getRequestedInfoProperty();

    boolean hasRequestedInfo();

    void setRequestedInfo(RequestedTaskInfo newRequestedInfo, OntModel model);
    void setRequestedInfo(RequestedTaskInfo newRequestedInfo);

    @Override
    String toString();

    //boolean requestsSatisfied();

    boolean isRunning();
	
    float getStorageWeight();

    RDFProperty getStorageWeightProperty();

    boolean hasStorageWeight();

    void setStorageWeight(float newStorageWeight);


    // Property http://www.owl-ontologies.com/Datacenter.owl#taskName

    String getTaskName();

    RDFProperty getTaskNameProperty();

    boolean hasTaskName();

    void setTaskName(String newTaskName);

    void createSWRLRule(SWRLFactory swrlFactory);
}
