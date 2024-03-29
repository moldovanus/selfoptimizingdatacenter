package ontologyRepresentations.greenContextOntology;

import com.hp.hpl.jena.ontology.OntModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;

import java.util.Collection;
import java.util.Iterator;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Datacenter.owl#TaskInfo
 *
 * @version generated on Sun Mar 07 13:11:11 EET 2010
 */
public interface TaskInfo extends ContextElement {
    // Property http://www.owl-ontologies.com/Datacenter.owl#cores

    int getCores();

    RDFProperty getCoresProperty();

    boolean hasCores();

    void setCores(int newCores, OntModel ontModel);
    void setCores(int newCores);

    // Property http://www.owl-ontologies.com/Datacenter.owl#cpu
    int getCpu();

    RDFProperty getCpuProperty();

    boolean hasCpu();

    void setCpu(int newCpu, OntModel ontModel);
    void setCpu(int newCpu);

    // Property http://www.owl-ontologies.com/Datacenter.owl#memory
    int getMemory();

    RDFProperty getMemoryProperty();

    boolean hasMemory();

    void setMemory(int newMemory, OntModel ontModel);
    void setMemory(int newMemory);

    // Property http://www.owl-ontologies.com/Datacenter.owl#storage
    int getStorage();

    RDFProperty getStorageProperty();

    boolean hasStorage();

    void setStorage(int newStorage, OntModel ontModel);
    void setStorage(int newStorage);

    // Property http://www.owl-ontologies.com/Datacenter.owl#taskProperty
    Collection getTaskProperty();

    RDFProperty getTaskPropertyProperty();

    boolean hasTaskProperty();

    Iterator listTaskProperty();

    void addTaskProperty(Object newTaskProperty);

    void removeTaskProperty(Object oldTaskProperty);

    void setTaskProperty(Collection newTaskProperty);

     // Property http://www.owl-ontologies.com/Datacenter.owl#receivedCoreIndex

    Collection getReceivedCoreIndex();

    RDFProperty getReceivedCoreIndexProperty();

    boolean hasReceivedCoreIndex();

    Iterator listReceivedCoreIndex();

    void addReceivedCoreIndex(int newReceivedCoreIndex);

    void removeReceivedCoreIndex(int oldReceivedCoreIndex);

    void setReceivedCoreIndex(Collection newReceivedCoreIndex);
}
