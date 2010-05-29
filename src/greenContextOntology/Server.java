package greenContextOntology;

import com.hp.hpl.jena.ontology.OntModel;
import contextawaremodel.worldInterface.datacenterInterface.proxies.ServerManagementProxyInterface;
import contextawaremodel.worldInterface.datacenterInterface.proxies.impl.ServerManagementProxy;
import edu.stanford.smi.protegex.owl.model.RDFProperty;

import java.util.Collection;
import java.util.Iterator;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Datacenter.owl#Server
 *
 * @version generated on Sun Mar 07 13:11:11 EET 2010
 */
public interface Server extends Resource {
    // Property http://www.owl-ontologies.com/Datacenter.owl#serverMacAddress

    String getServerMacAddress();

    RDFProperty getServerMacAddressProperty();

    boolean hasServerMacAddress();

    void setServerMacAddress(String newServerMacAddress);

    // Property http://www.owl-ontologies.com/Datacenter.owl#serverWakeUpPort

    int getServerWakeUpPort();

    RDFProperty getServerWakeUpPortProperty();

    boolean hasServerWakeUpPort();

    void setServerWakeUpPort(int newServerWakeUpPort);

    // Property http://www.owl-ontologies.com/Datacenter.owl#associatedCPU

    CPU getAssociatedCPU();

    RDFProperty getAssociatedCPUProperty();

    boolean hasAssociatedCPU();

    void setAssociatedCPU(CPU newAssociatedCPU);

    // Property http://www.owl-ontologies.com/Datacenter.owl#associatedMemory

    Memory getAssociatedMemory();

    RDFProperty getAssociatedMemoryProperty();

    boolean hasAssociatedMemory();

    void setAssociatedMemory(Memory newAssociatedMemory);

    // Property http://www.owl-ontologies.com/Datacenter.owl#associatedStorage

    Storage getAssociatedStorage();

    RDFProperty getAssociatedStorageProperty();

    boolean hasAssociatedStorage();

    void setAssociatedStorage(Storage newAssociatedStorage);

    // Property http://www.owl-ontologies.com/Datacenter.owl#isInLowPowerState

    boolean getIsInLowPowerState();

    RDFProperty getIsInLowPowerStateProperty();

    boolean hasIsInLowPowerState();

    void setIsInLowPowerState(boolean newIsInLowPowerState, OntModel ontModel);

    // Property http://www.owl-ontologies.com/Datacenter.owl#runningTasks

    Collection getRunningTasks();

    RDFProperty getRunningTasksProperty();

    boolean hasRunningTasks();

    Iterator listRunningTasks();

    void addRunningTasks(Task newRunningTasks, OntModel model);

    void addNegotiatedTasks(Task newRunningTasks, OntModel model, int negotiatedCPU, int negotiatedMemory, int negotiatedStorage);
    //void addRunningTasks(Task newRunningTasks);

    void removeRunningTasks(Task oldRunningTasks, OntModel model);

    void removeRunningTasks(Task oldRunningTasks);

    void setRunningTasks(Collection newRunningTasks, OntModel model);

    void setRunningTasks(Collection newRunningTasks);
// Property http://www.owl-ontologies.com/Datacenter.owl#serverName

    String getServerName();

    RDFProperty getServerNameProperty();

    boolean hasServerName();

    void setServerName(String newServerName);

    // Property http://www.owl-ontologies.com/Datacenter.owl#webService

    String getWebService();

    RDFProperty getWebServiceProperty();

    boolean hasWebService();

    void setWebService(String newWebService);

    public boolean hasResourcesFor(Task task);

    public boolean hasResourcesToBeNegotiatedFor(Task task);
    // Property http://www.owl-ontologies.com/Datacenter.owl#virtualMachinesPath

    Collection getVirtualMachinesPath();

    RDFProperty getVirtualMachinesPathProperty();

    boolean hasVirtualMachinesPath();

    Iterator listVirtualMachinesPath();

    void addVirtualMachinesPath(String newVirtualMachinesPath);

    void removeVirtualMachinesPath(String oldVirtualMachinesPath);

    void setVirtualMachinesPath(Collection newVirtualMachinesPath);

    // Property http://www.owl-ontologies.com/Datacenter.owl#serverIPAddress

    String getServerIPAddress();

    RDFProperty getServerIPAddressProperty();

    boolean hasServerIPAddress();

    void setServerIPAddress(String newServerIPAddress);

    @Override
    String toString();

    public boolean containsTask(Task task);

    boolean optimumValuesRespected();

    void distributeRemainingResources(OntModel model);

    void collectPreviouslyDistributedResources(OntModel model);

    void giveMoreResourcesToTask(double[] resources, Task task, OntModel model);

    void removeExtraResourcesGivenToTask(Task task, OntModel model);

    void changeOptimumCPURange(int max);

    void changeOptimumMemoryRange(int max);

    void changeOptimumStorageRange(int max);

    void resetOptimumValues();

    public ServerManagementProxyInterface getProxy();

    public void setProxy(ServerManagementProxyInterface proxy);


}

