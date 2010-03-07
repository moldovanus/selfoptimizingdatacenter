package greenContextOntology;

import edu.stanford.smi.protegex.owl.model.*;
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

    void setCores(int newCores);

    // Property http://www.owl-ontologies.com/Datacenter.owl#cpu
    int getCpu();

    RDFProperty getCpuProperty();

    boolean hasCpu();

    void setCpu(int newCpu);

    // Property http://www.owl-ontologies.com/Datacenter.owl#memory
    int getMemory();

    RDFProperty getMemoryProperty();

    boolean hasMemory();

    void setMemory(int newMemory);

    // Property http://www.owl-ontologies.com/Datacenter.owl#storage
    int getStorage();

    RDFProperty getStorageProperty();

    boolean hasStorage();

    void setStorage(int newStorage);

    // Property http://www.owl-ontologies.com/Datacenter.owl#taskProperty
    Collection getTaskProperty();

    RDFProperty getTaskPropertyProperty();

    boolean hasTaskProperty();

    Iterator listTaskProperty();

    void addTaskProperty(Object newTaskProperty);

    void removeTaskProperty(Object oldTaskProperty);

    void setTaskProperty(Collection newTaskProperty);
}
