package greenContextOntology.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protegex.owl.model.*;

import java.util.*;

import greenContextOntology.*;
import greenContextOntology.Task;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Datacenter.owl#Server
 *
 * @version generated on Sun Mar 07 13:11:11 EET 2010
 */
public class DefaultServer extends DefaultResource
        implements Server {

    public DefaultServer(OWLModel owlModel, FrameID id) {
        super(owlModel, id);
    }

    public DefaultServer() {
    }

    // Property http://www.owl-ontologies.com/Datacenter.owl#associatedCPU
    public CPU getAssociatedCPU() {
        return (CPU) getPropertyValueAs(getAssociatedCPUProperty(), CPU.class);
    }

    public RDFProperty getAssociatedCPUProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#associatedCPU";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasAssociatedCPU() {
        return getPropertyValueCount(getAssociatedCPUProperty()) > 0;
    }

    public void setAssociatedCPU(CPU newAssociatedCPU) {
        setPropertyValue(getAssociatedCPUProperty(), newAssociatedCPU);
    }

    // Property http://www.owl-ontologies.com/Datacenter.owl#associatedMemory
    public Memory getAssociatedMemory() {
        return (Memory) getPropertyValueAs(getAssociatedMemoryProperty(), Memory.class);
    }

    public RDFProperty getAssociatedMemoryProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#associatedMemory";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasAssociatedMemory() {
        return getPropertyValueCount(getAssociatedMemoryProperty()) > 0;
    }

    public void setAssociatedMemory(Memory newAssociatedMemory) {
        setPropertyValue(getAssociatedMemoryProperty(), newAssociatedMemory);
    }

    // Property http://www.owl-ontologies.com/Datacenter.owl#associatedStorage
    public Storage getAssociatedStorage() {
        return (Storage) getPropertyValueAs(getAssociatedStorageProperty(), Storage.class);
    }

    public RDFProperty getAssociatedStorageProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#associatedStorage";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasAssociatedStorage() {
        return getPropertyValueCount(getAssociatedStorageProperty()) > 0;
    }

    public void setAssociatedStorage(Storage newAssociatedStorage) {
        setPropertyValue(getAssociatedStorageProperty(), newAssociatedStorage);
    }

    // Property http://www.owl-ontologies.com/Datacenter.owl#lowPowerState
    public boolean getLowPowerState() {
        return getPropertyValueLiteral(getLowPowerStateProperty()).getBoolean();
    }

    public RDFProperty getLowPowerStateProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#lowPowerState";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasLowPowerState() {
        return getPropertyValueCount(getLowPowerStateProperty()) > 0;
    }

    public void setLowPowerState(boolean newLowPowerState) {
        setPropertyValue(getLowPowerStateProperty(), new java.lang.Boolean(newLowPowerState));
    }

    // Property http://www.owl-ontologies.com/Datacenter.owl#name
    @Override
    public String getName() {
        return (String) getPropertyValue(getNameProperty());
    }

    public RDFProperty getNameProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#name";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasName() {
        return getPropertyValueCount(getNameProperty()) > 0;
    }

    @Override
    public void setName(String newName) {
        setPropertyValue(getNameProperty(), newName);
    }

    // Property http://www.owl-ontologies.com/Datacenter.owl#runningTasks
    public Collection getRunningTasks() {
        return getPropertyValuesAs(getRunningTasksProperty(), Task.class);
    }

    public RDFProperty getRunningTasksProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#runningTasks";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasRunningTasks() {
        return getPropertyValueCount(getRunningTasksProperty()) > 0;
    }

    public Iterator listRunningTasks() {
        return listPropertyValuesAs(getRunningTasksProperty(), Task.class);
    }

    /**
     * It gives to the new task the maximum between the requested and available
     * resources and updates the server's <code>used</code> resources
     *
     * @param newRunningTasks the task to be deployed on the server
     */
    public void addRunningTasks(Task newRunningTasks) {

        TaskInfo requestedSLA = newRunningTasks.getRequestedInfo();
        TaskInfo receivedSLA = newRunningTasks.getReceivedInfo();

        //System.out.println("-----------\n before" + newRunningTasks.toString() + "\n");

        CPU cpu = this.getAssociatedCPU();
        Collection<Core> cores = cpu.getAssociatedCore();
        Iterator<Core> coresIterator = cores.iterator();
        int coreCount = requestedSLA.getCores();
        int availableCores = cores.size();

        coreCount = (coreCount > availableCores) ? availableCores : coreCount;

        receivedSLA.setCores(coreCount);

        for (int i = 0; i < coreCount; i++) {
            Core core = coresIterator.next();
            //TODO : to be modified for more core flexibility
            int availableCore = core.getTotal() - core.getUsed();
            int requestedCPU = requestedSLA.getCpu();
            //TODO : remove size checks if performance needed
            if (requestedCPU > availableCore) {
                continue;
            }
            //int receivedCPU = (requestedCPU < availableCore) ? requestedCPU : availableCore;

            receivedSLA.setCpu(requestedCPU);
            core.setUsed(core.getUsed() + requestedCPU);
            receivedSLA.addReceivedCoreIndex(i);
        }

        Memory memory = this.getAssociatedMemory();
        int availableMemory = memory.getTotal() - memory.getUsed();
        int requestedMemory = requestedSLA.getMemory();
        int receivedMemory = (requestedMemory < availableMemory) ? requestedMemory : availableMemory;
        receivedSLA.setMemory(receivedMemory);
        memory.setUsed(memory.getUsed() + receivedMemory);

        Storage storage = this.getAssociatedStorage();
        int availableStorage = storage.getTotal() - storage.getUsed();
        int requestedStorage = requestedSLA.getStorage();
        int receivedStorage = (requestedStorage < availableStorage) ? requestedStorage : availableStorage;
        receivedSLA.setStorage(receivedStorage);
        storage.setUsed(storage.getUsed() + receivedStorage);

        //add task to ontology
        addPropertyValue(getRunningTasksProperty(), newRunningTasks);

        //System.out.println("-----------\n after" + newRunningTasks.toString() + "\n");

    }

    public void removeRunningTasks(Task oldRunningTasks) {


        TaskInfo receivedSLA = oldRunningTasks.getReceivedInfo();


        CPU cpu = this.getAssociatedCPU();
        Collection<Core> cores = cpu.getAssociatedCore();
        Iterator<Core> coresIterator = cores.iterator();


        Collection receivedCoresIndexes = receivedSLA.getReceivedCoreIndex();
        receivedSLA.setCores(0);
        int coreCount = cores.size();
        for (int i = 0; i < coreCount; i++) {
            Core core = coresIterator.next();
            //TODO : to check the multicore modification if it's ok
            if (receivedCoresIndexes.contains(i)) {
                int receivedCPU = receivedSLA.getCpu();
                core.setUsed(core.getUsed() - receivedCPU);
                receivedSLA.removeReceivedCoreIndex(i);
            }

        }

        receivedSLA.setCpu(0);

        Memory memory = this.getAssociatedMemory();
        memory.setUsed(memory.getUsed() - receivedSLA.getMemory());
        receivedSLA.setMemory(0);

        Storage storage = this.getAssociatedStorage();

        storage.setUsed(storage.getUsed() - receivedSLA.getStorage());
        receivedSLA.setStorage(0);

        //remove task from ontology
        removePropertyValue(getRunningTasksProperty(), oldRunningTasks);
    }

    /**
     * @param task task to be accomodated on the server
     * @return true if there are enough resoruces to satify the task's
     *         requirements and false otherwise
     */
    public boolean hasResourcesFor(Task task) {


        TaskInfo requestedSLA = task.getRequestedInfo();

        CPU cpu = this.getAssociatedCPU();
        Collection<Core> cores = cpu.getAssociatedCore();
        int requestedCores = requestedSLA.getCores();
        if (cores.size() < requestedCores) {
            return false;
        }


        for (Core coreInst : cores) {
            Core core = coreInst;
            if (core.getUsed() + requestedSLA.getCpu() > core.getTotal()) {
                continue;
            }else{
                requestedCores --;
            }
        }

        //TODO: Trace is correct 
        if ( requestedCores > 0 ){
              return false;
        }

        Memory memory = this.getAssociatedMemory();
        if (memory.getUsed() + requestedSLA.getMemory() > memory.getTotal()) {
            return false;
        }

        Storage storage = this.getAssociatedStorage();

        if (storage.getUsed() + requestedSLA.getStorage() > storage.getTotal()) {
            return false;
        }

        return true;
    }

    public void setRunningTasks(Collection newRunningTasks) {
        setPropertyValues(getRunningTasksProperty(), newRunningTasks);
        for (Object task : newRunningTasks) {
            addRunningTasks((greenContextOntology.Task) task);
        }
    }

    // Property http://www.owl-ontologies.com/Datacenter.owl#webService
    public String getWebService() {
        return (String) getPropertyValue(getWebServiceProperty());
    }

    public RDFProperty getWebServiceProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#webService";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasWebService() {
        return getPropertyValueCount(getWebServiceProperty()) > 0;
    }

    public void setWebService(String newWebService) {
        setPropertyValue(getWebServiceProperty(), newWebService);
    }

    @Override
    public String toString() {

        String description;
        description = "Server " + this.getName() + "\n";
        description += "Inactive = " + this.getLowPowerState() + "\n";
        Collection cores = this.getAssociatedCPU().getAssociatedCore();
        Iterator iterator = cores.iterator();

        description += "Cores " + cores.size() + "\n";

        while (iterator.hasNext()) {
            Core core = (Core) iterator.next();
            description += "CPU used " + core.getUsed() + " total " + core.getTotal() + "\n";
        }

        description += "Memory used " + this.getAssociatedMemory().getUsed() + " total " + this.getAssociatedMemory().getTotal() + "\n";
        description += "Storage used " + this.getAssociatedStorage().getUsed() + " total " + this.getAssociatedStorage().getTotal() + "\n";

        return description;
    }
}
