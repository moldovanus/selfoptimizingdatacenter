package greenContextOntology;

import edu.stanford.smi.protegex.owl.model.*;
 
import com.hp.hpl.jena.ontology.OntModel;


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

    TaskInfo getReceivedInfo();

    RDFProperty getReceivedInfoProperty();

    boolean hasReceivedInfo();

    void setReceivedInfo(TaskInfo newReceivedInfo, OntModel model);
    void setReceivedInfo(TaskInfo newReceivedInfo);


    // Property http://www.owl-ontologies.com/Datacenter.owl#requestedInfo

    TaskInfo getRequestedInfo();

    RDFProperty getRequestedInfoProperty();

    boolean hasRequestedInfo();

    void setRequestedInfo(TaskInfo newRequestedInfo, OntModel model);
    void setRequestedInfo(TaskInfo newRequestedInfo);

    @Override
    String toString();

    //boolean requestsSatisfied();

    boolean isRunning();
	
    float getStorageWeight();

    RDFProperty getStorageWeightProperty();

    boolean hasStorageWeight();

    void setStorageWeight(float newStorageWeight);
}
