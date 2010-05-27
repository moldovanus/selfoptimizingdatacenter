package greenContextOntology;

import com.hp.hpl.jena.ontology.OntModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;

import java.util.Collection;
import java.util.Iterator;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Datacenter.owl#ReceivedTaskInfo
 *
 * @version generated on Thu Apr 15 14:24:33 EEST 2010
 */
public interface ReceivedTaskInfo extends ContextElement ,CloneableOntologyElement{

    // Property http://www.owl-ontologies.com/Datacenter.owl#cores
    int getCores();

    RDFProperty getCoresProperty();

    boolean hasCores();

    void setCores(int newCores);
    void setCores(int newCores,OntModel model);

    // Property http://www.owl-ontologies.com/Datacenter.owl#cpuReceived
    int getCpuReceived();

    RDFProperty getCpuReceivedProperty();

    boolean hasCpuReceived();

    void setCpuReceived(int newCpuReceived, OntModel model);

    void setCpuReceived(int newCpuReceived);

    // Property http://www.owl-ontologies.com/Datacenter.owl#memoryReceived
    int getMemoryReceived();

    RDFProperty getMemoryReceivedProperty();

    boolean hasMemoryReceived();

    void setMemoryReceived(int newMemoryReceived, OntModel model);

    void setMemoryReceived(int newMemoryReceived);

    // Property http://www.owl-ontologies.com/Datacenter.owl#storageReceived
    int getStorageReceived();

    RDFProperty getStorageReceivedProperty();

    boolean hasStorageReceived();

    void setStorageReceived(int newStorageReceived, OntModel model);

    void setStorageReceived(int newStorageReceived);

// Property http://www.owl-ontologies.com/Datacenter.owl#receivedCoreIndex
    Collection getReceivedCoreIndex();

    RDFProperty getReceivedCoreIndexProperty();

    boolean hasReceivedCoreIndex();

    Iterator listReceivedCoreIndex();

    void addReceivedCoreIndex(int newReceivedCoreIndex);

    void removeReceivedCoreIndex(int oldReceivedCoreIndex);

    void setReceivedCoreIndex(Collection newReceivedCoreIndex);
}
