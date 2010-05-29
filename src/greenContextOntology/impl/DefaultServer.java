package greenContextOntology.impl;

import com.hp.hpl.jena.ontology.OntModel;
import contextawaremodel.worldInterface.datacenterInterface.proxies.ServerManagementProxyInterface;
import contextawaremodel.worldInterface.datacenterInterface.proxies.impl.ServerManagementProxy;
import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import greenContextOntology.*;

import java.util.Collection;
import java.util.Iterator;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Datacenter.owl#Server
 *
 * @version generated on Sun Mar 07 13:11:11 EET 2010
 */
public class DefaultServer extends DefaultResource
        implements Server {

    private ServerManagementProxyInterface proxy;

    public DefaultServer(OWLModel owlModel, FrameID id) {
        super(owlModel, id);
    }

    public DefaultServer() {
    }

    // Property http://www.owl-ontologies.com/Datacenter.owl#serverMacAddress

    public String getServerMacAddress() {
        return (String) getPropertyValue(getServerMacAddressProperty());
    }


    public RDFProperty getServerMacAddressProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#serverMacAddress";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }


    public boolean hasServerMacAddress() {
        return getPropertyValueCount(getServerMacAddressProperty()) > 0;
    }


    public void setServerMacAddress(String newServerMacAddress) {
        setPropertyValue(getServerMacAddressProperty(), newServerMacAddress);
    }

    // Property http://www.owl-ontologies.com/Datacenter.owl#serverWakeUpPort

    public int getServerWakeUpPort() {
        return getPropertyValueLiteral(getServerWakeUpPortProperty()).getInt();
    }


    public RDFProperty getServerWakeUpPortProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#serverWakeUpPort";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }


    public boolean hasServerWakeUpPort() {
        return getPropertyValueCount(getServerWakeUpPortProperty()) > 0;
    }


    public void setServerWakeUpPort(int newServerWakeUpPort) {
        setPropertyValue(getServerWakeUpPortProperty(), new java.lang.Integer(newServerWakeUpPort));
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

    // Property http://www.owl-ontologies.com/Datacenter.owl#isInLowPowerState

    public boolean getIsInLowPowerState() {
        return getPropertyValueLiteral(getIsInLowPowerStateProperty()).getBoolean();
    }

    public RDFProperty getIsInLowPowerStateProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#isInLowPowerState";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasIsInLowPowerState() {
        return getPropertyValueCount(getIsInLowPowerStateProperty()) > 0;
    }

    public void setIsInLowPowerState(boolean newIsInLowPowerState, OntModel ontModel) {
        setPropertyValue(getIsInLowPowerStateProperty(), new java.lang.Boolean(newIsInLowPowerState), ontModel);
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
    public void addRunningTasks(Task newRunningTasks, OntModel model) {

        RequestedTaskInfo requestedSLA = newRunningTasks.getRequestedInfo();
        ReceivedTaskInfo receivedSLA = newRunningTasks.getReceivedInfo();

        //System.out.println("-----------\n before" + newRunningTasks.toString() + "\n");

        CPU cpu = this.getAssociatedCPU();
        Collection<Core> cores = cpu.getAssociatedCore();
        Iterator<Core> coresIterator = cores.iterator();
        int coreCount = requestedSLA.getCores();
        int availableCores = cores.size();

        coreCount = (coreCount > availableCores) ? availableCores : coreCount;
        int index = 0;
        while (coresIterator.hasNext() && coreCount > 0) {
            Core core = coresIterator.next();
            //TODO : to be modified for more core flexibility
            int availableCore = core.getTotal() - core.getUsed();
            int requestedCPU = requestedSLA.getCpuMaxAcceptableValue();
            //TODO : remove size checks if performance needed
            if (requestedCPU > availableCore) {
                //    i--;
                index++;
                continue;
            }
            //int receivedCPU = (requestedCPU < availableCore) ? requestedCPU : availableCore;
            coreCount--;
            receivedSLA.setCpuReceived(requestedCPU, model);
            core.setUsed(core.getUsed() + requestedCPU);
            receivedSLA.addReceivedCoreIndex(index);
            index++;
        }

        receivedSLA.setCores(receivedSLA.getReceivedCoreIndex().size(), model);

        Memory memory = this.getAssociatedMemory();
        int availableMemory = memory.getTotal() - memory.getUsed();
        int requestedMemory = requestedSLA.getMemoryMaxAcceptableValue();
        int receivedMemory = (requestedMemory < availableMemory) ? requestedMemory : availableMemory;
        receivedSLA.setMemoryReceived(receivedMemory, model);
        memory.setUsed(memory.getUsed() + receivedMemory);

        Storage storage = this.getAssociatedStorage();
        int availableStorage = storage.getTotal() - storage.getUsed();
        int requestedStorage = requestedSLA.getStorageMaxAcceptableValue();
        int receivedStorage = (requestedStorage < availableStorage) ? requestedStorage : availableStorage;
        receivedSLA.setStorageReceived(receivedStorage, model);
        storage.setUsed(storage.getUsed() + receivedStorage);

        //add task to ontology
        Collection tasks = getRunningTasks();
        removePropertyValue(getRunningTasksProperty(), tasks);
        tasks.add(newRunningTasks);

        //setPropertyValue(getRunningTasksProperty(), newRunningTasks);
        addPropertyValue(getRunningTasksProperty(), newRunningTasks);

    }

    public void addNegotiatedTasks(Task newRunningTasks, OntModel model, int negotiatedCPU, int negotiatedMemory, int negotiatedStorage) {
        RequestedTaskInfo requestedSLA = newRunningTasks.getRequestedInfo();
        ReceivedTaskInfo receivedSLA = newRunningTasks.getReceivedInfo();

        if (negotiatedCPU == 0) {
            negotiatedCPU = requestedSLA.getCpuMaxAcceptableValue();
        }
        if (negotiatedMemory == 0) {
            negotiatedMemory = requestedSLA.getMemoryMaxAcceptableValue();
        }
        if (negotiatedStorage == 0) {
            negotiatedStorage = requestedSLA.getStorageMaxAcceptableValue();
        }

        //System.out.println("-----------\n before" + newRunningTasks.toString() + "\n");

        CPU cpu = this.getAssociatedCPU();
        Collection<Core> cores = cpu.getAssociatedCore();
        Iterator<Core> coresIterator = cores.iterator();
        int coreCount = requestedSLA.getCores();
        int availableCores = cores.size();

        coreCount = (coreCount > availableCores) ? availableCores : coreCount;
        int index = 0;
        while (coresIterator.hasNext() && coreCount > 0) {
            Core core = coresIterator.next();
            //TODO : to be modified for more core flexibility
            int availableCore = core.getTotal() - core.getUsed();
            // int requestedCPU = requestedSLA.getCpuMaxAcceptableValue();
            //TODO : remove size checks if performance needed
            if (negotiatedCPU > availableCore) {
                //    i--;
                index++;
                continue;
            }
            //int receivedCPU = (requestedCPU < availableCore) ? requestedCPU : availableCore;
            coreCount--;
            receivedSLA.setCpuReceived(negotiatedCPU, model);
            core.setUsed(core.getUsed() + negotiatedCPU);
            receivedSLA.addReceivedCoreIndex(index);
            index++;
        }

        receivedSLA.setCores(receivedSLA.getReceivedCoreIndex().size(), model);

        Memory memory = this.getAssociatedMemory();
        int availableMemory = memory.getTotal() - memory.getUsed();
        // int requestedMemory = requestedSLA.getMemoryMaxAcceptableValue();
        int receivedMemory = (negotiatedMemory < availableMemory) ? negotiatedMemory : availableMemory;
        receivedSLA.setMemoryReceived(receivedMemory, model);
        memory.setUsed(memory.getUsed() + receivedMemory);

        Storage storage = this.getAssociatedStorage();
        int availableStorage = storage.getTotal() - storage.getUsed();
        //int requestedStorage = requestedSLA.getStorageMaxAcceptableValue();
        int receivedStorage = (negotiatedStorage < availableStorage) ? negotiatedStorage : availableStorage;
        receivedSLA.setStorageReceived(receivedStorage, model);
        storage.setUsed(storage.getUsed() + receivedStorage);

        //add task to ontology
        Collection tasks = getRunningTasks();
        removePropertyValue(getRunningTasksProperty(), tasks);
        tasks.add(newRunningTasks);

        //setPropertyValue(getRunningTasksProperty(), newRunningTasks);
        addPropertyValue(getRunningTasksProperty(), newRunningTasks);
    }

    public void addRunningTasks(Task newRunningTasks) {


        addPropertyValue(getRunningTasksProperty(), newRunningTasks);

        //System.out.println("-----------\n after" + newRunningTasks.toString() + "\n");

    }

    public void removeRunningTasks(Task oldRunningTasks, OntModel model) {


        ReceivedTaskInfo receivedSLA = oldRunningTasks.getReceivedInfo();

        CPU cpu = this.getAssociatedCPU();
        Collection<Core> cores = cpu.getAssociatedCore();
        Iterator<Core> coresIterator = cores.iterator();

        Collection receivedCoresIndexes = receivedSLA.getReceivedCoreIndex();
        receivedSLA.setCores(0, model);
        int coreCount = cores.size();
        for (int i = 0; i < coreCount; i++) {
            Core core = coresIterator.next();
            //TODO : to check the multicore modification if it's ok
            if (receivedCoresIndexes.contains(i)) {
                int receivedCPU = receivedSLA.getCpuReceived();
                core.setUsed(core.getUsed() - receivedCPU);
                receivedSLA.removeReceivedCoreIndex(i);
            }

        }

        receivedSLA.setCpuReceived(0, model);

        Memory memory = this.getAssociatedMemory();
        memory.setUsed(memory.getUsed() - receivedSLA.getMemoryReceived());
        receivedSLA.setMemoryReceived(0, model);

        Storage storage = this.getAssociatedStorage();

        storage.setUsed(storage.getUsed() - receivedSLA.getStorageReceived());
        receivedSLA.setStorageReceived(0, model);

        //remove task from ontology
        //getRunningTasks().remove(oldRunningTasks);
        removePropertyValue(getRunningTasksProperty(), oldRunningTasks);

    }

    public void removeRunningTasks(Task oldRunningTasks) {

        //remove task from ontology
        removePropertyValue(getRunningTasksProperty(), oldRunningTasks);
    }

    // Property http://www.owl-ontologies.com/Datacenter.owl#serverIPAddress

    public String getServerIPAddress() {
        return (String) getPropertyValue(getServerIPAddressProperty());
    }


    public RDFProperty getServerIPAddressProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#serverIPAddress";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }


    public boolean hasServerIPAddress() {
        return getPropertyValueCount(getServerIPAddressProperty()) > 0;
    }


    public void setServerIPAddress(String newServerIPAddress) {
        setPropertyValue(getServerIPAddressProperty(), newServerIPAddress);
    }

    // Property http://www.owl-ontologies.com/Datacenter.owl#virtualMachinesPath

    public Collection getVirtualMachinesPath() {
        return getPropertyValues(getVirtualMachinesPathProperty());
    }


    public RDFProperty getVirtualMachinesPathProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#virtualMachinesPath";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }


    public boolean hasVirtualMachinesPath() {
        return getPropertyValueCount(getVirtualMachinesPathProperty()) > 0;
    }


    public Iterator listVirtualMachinesPath() {
        return listPropertyValues(getVirtualMachinesPathProperty());
    }


    public void addVirtualMachinesPath(String newVirtualMachinesPath) {
        addPropertyValue(getVirtualMachinesPathProperty(), newVirtualMachinesPath);
    }


    public void removeVirtualMachinesPath(String oldVirtualMachinesPath) {
        removePropertyValue(getVirtualMachinesPathProperty(), oldVirtualMachinesPath);
    }


    public void setVirtualMachinesPath(Collection newVirtualMachinesPath) {
        setPropertyValues(getVirtualMachinesPathProperty(), newVirtualMachinesPath);
    }

    /**
     * @param task task to be accomodated on the server
     * @return true if there are enough resoruces to satify the task's
     *         requirements and false otherwise
     */
    public boolean hasResourcesFor(Task task) {

        RequestedTaskInfo requestedSLA = task.getRequestedInfo();


        CPU cpu = this.getAssociatedCPU();
        Collection cores = cpu.getAssociatedCore();
        int requestedCores = requestedSLA.getCores();
        if (cores.size() < requestedCores) {
            return false;
        }
        for (Object coreInst : cores) {

            Core core = (Core) coreInst;
            if (core.getUsed() + requestedSLA.getCpuMaxAcceptableValue() > core.getMaxAcceptableValue()) {
                continue;
            } else {
                requestedCores--;
            }
        }

        if (requestedCores > 0) {
            return false;
        }

        Memory memory = this.getAssociatedMemory();
        if (memory.getUsed() + requestedSLA.getMemoryMaxAcceptableValue() > memory.getMaxAcceptableValue()) {
            return false;
        }

        Storage storage = this.getAssociatedStorage();

        if (storage.getUsed() + requestedSLA.getStorageMaxAcceptableValue() > storage.getMaxAcceptableValue()) {
            return false;
        }

        return true;
    }

    public boolean hasResourcesToBeNegotiatedFor(Task task) {

        RequestedTaskInfo requestedSLA = task.getRequestedInfo();


        CPU cpu = this.getAssociatedCPU();
        Collection cores = cpu.getAssociatedCore();
        int requestedCores = requestedSLA.getCores();
        if (cores.size() < requestedCores) {
            return false;
        }
        for (Object coreInst : cores) {

            Core core = (Core) coreInst;
            if (core.getUsed() + requestedSLA.getCpuMinAcceptableValue() > core.getTotal()) {
                continue;
            } else {
                requestedCores--;
            }
        }

        if (requestedCores > 0) {
            return false;
        }

        Memory memory = this.getAssociatedMemory();
        if (memory.getUsed() + requestedSLA.getMemoryMinAcceptableValue() > memory.getTotal()) {
            return false;
        }

        Storage storage = this.getAssociatedStorage();

        if (storage.getUsed() + requestedSLA.getStorageMinAcceptableValue() > storage.getTotal()) {
            return false;
        }

        return true;
    }

    public void setRunningTasks(Collection newRunningTasks, OntModel model) {
        setPropertyValues(getRunningTasksProperty(), newRunningTasks);
        for (Object task : newRunningTasks) {
            addRunningTasks((greenContextOntology.Task) task, model);
        }
    }

    public void setRunningTasks(Collection newRunningTasks) {
        setPropertyValues(getRunningTasksProperty(), newRunningTasks);
        for (Object task : newRunningTasks) {
            addRunningTasks((greenContextOntology.Task) task);
        }
    }
    // Property http://www.owl-ontologies.com/Datacenter.owl#serverName

    public String getServerName() {
        return (String) getPropertyValue(getServerNameProperty());
    }

    public RDFProperty getServerNameProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#serverName";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasServerName() {
        return getPropertyValueCount(getServerNameProperty()) > 0;
    }

    public void setServerName(String newServerName) {
        setPropertyValue(getServerNameProperty(), newServerName);
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
        description = "Server " + this.getName().split("#")[1] + "\n";
        description += "Inactive = " + this.getIsInLowPowerState() + "\n";
        Collection cores = this.getAssociatedCPU().getAssociatedCore();
        Iterator iterator = cores.iterator();

        description += "Cores " + cores.size() + "\n";

        while (iterator.hasNext()) {
            Core core = (Core) iterator.next();
            description += "CPU used " + core.getUsed() + " total " + core.getTotal() + " range [ " + core.getMinAcceptableValue() + ".." + core.getMaxAcceptableValue() + " ]\n";
        }

        description += "Memory used " + this.getAssociatedMemory().getUsed() + " total " + this.getAssociatedMemory().getTotal()
                + " range [ " + this.getAssociatedMemory().getMinAcceptableValue() + ".." + this.getAssociatedMemory().getMaxAcceptableValue() + " ]\n";
        description += "Storage used " + this.getAssociatedStorage().getUsed() + " total " + this.getAssociatedStorage().getTotal()
                + " range [ " + this.getAssociatedStorage().getMinAcceptableValue() + ".." + this.getAssociatedStorage().getMaxAcceptableValue() + " ]\n";
        return description;
    }

    /**
     * To check if a task is already present and such avoid adding it twice
     *
     * @param task
     * @return
     */
    public boolean containsTask(Task task) {
        Collection tasks = this.getRunningTasks();
        for (Object o : tasks) {
            Task t = (Task) o;
            if (t.getName().equals(task.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean optimumValuesRespected() {
        CPU cpu = this.getAssociatedCPU();
        for (Object o : cpu.getAssociatedCore()) {
            Core core = (Core) o;
            int usedCore = core.getUsed();
            if (usedCore < core.getMinAcceptableValue() || usedCore > core.getMaxAcceptableValue()) {
                return false;
            }
        }
        Memory memory = this.getAssociatedMemory();
        int usedMemory = memory.getUsed();
        if (usedMemory < memory.getMinAcceptableValue() || usedMemory > memory.getMaxAcceptableValue()) {
            return false;
        }

        Storage storage = this.getAssociatedStorage();
        int usedStorage = storage.getUsed();
        if (usedStorage < storage.getMinAcceptableValue() || usedStorage > storage.getMaxAcceptableValue()) {
            return false;
        }

        return true;
    }

    public void collectPreviouslyDistributedResources(OntModel model) {
        Collection runningTasks = getRunningTasks();
        for (Object t : runningTasks) {
            Task task = (Task) t;
            removeRunningTasks(task, model);
        }

        for (Object t : runningTasks) {
            Task task = (Task) t;
            addRunningTasks(task, model);
        }

    }

    public void distributeRemainingResources(OntModel model) {
        Collection runningTasks = getRunningTasks();
        if (runningTasks.size() == 0) {
            return;
        }
        CPU cpu = getAssociatedCPU();

        for (Object task : runningTasks) {
            for (Object object : cpu.getAssociatedCore()) {
                Core core = (Core) object;

                //TODO: modify. For now the empty cpu-s are assigned in order to the running tasks : which is wrong
                if (core.getUsed() == 0) {
                    //if core used in optimum parameters continue
                    //throtelling used to reduce bla bla bla
                    continue;
                } else {
                    ReceivedTaskInfo received = ((Task) task).getReceivedInfo();
                    received.setCores(received.getCores() + 1, model);
                    //TODO: sinchronize used with given to task  - maybe finer control - CPU per core per task not just global cpu anr cor no
                    core.setUsed(core.getMinAcceptableValue());

                    //in order to continue and assign the next empty core to the next task
                    break;
                }


            }
        }

        Memory memory = getAssociatedMemory();
        int usedMemory = memory.getUsed();
        int memoryMinAcceptableValue = memory.getMinAcceptableValue();
        if (usedMemory < memoryMinAcceptableValue) {
            int remaining = memoryMinAcceptableValue - usedMemory;
            int partition = remaining / runningTasks.size();
            if (partition == 0) {
                partition = 1;
            }
            for (Object task : runningTasks) {
                ReceivedTaskInfo received = ((Task) task).getReceivedInfo();
                received.setMemoryReceived(received.getMemoryReceived() + partition, model);
            }
            memory.setUsed(remaining);
        }

        Storage storage = getAssociatedStorage();
        int usedStorage = storage.getUsed();
        int storageMinAcceptableValue = storage.getMinAcceptableValue();
        if (usedStorage < storageMinAcceptableValue) {
            int remaining = storageMinAcceptableValue - usedStorage;
            int partition = remaining / runningTasks.size();
            if (partition == 0) {
                partition = 1;
            }
            for (Object task : runningTasks) {
                ReceivedTaskInfo received = ((Task) task).getReceivedInfo();
                received.setStorageReceived(received.getStorageReceived() + partition, model);
            }
            storage.setUsed(remaining);
        }
    }

    public void giveMoreResourcesToTask(double[] resources, Task task, OntModel model) {

        ReceivedTaskInfo receivedTaskInfo = task.getReceivedInfo();

        Collection<Integer> receivedCores = receivedTaskInfo.getReceivedCoreIndex();
        Object[] cores = this.getAssociatedCPU().getAssociatedCore().toArray();
        for (Integer index : receivedCores) {
            Core core = (Core) cores[index];
            core.setUsed(core.getUsed() + (int) resources[0]);
        }
        receivedTaskInfo.setCpuReceived(((Core) cores[0]).getUsed() + (int) resources[0], model);

        Memory memory = this.getAssociatedMemory();
        int newMemoryValue = (memory.getUsed() + (int) resources[1]);
        memory.setUsed(newMemoryValue);
        receivedTaskInfo.setMemoryReceived(newMemoryValue);

        Storage storage = this.getAssociatedStorage();
        int newStorageValue = (storage.getUsed() + (int) resources[2]);
        storage.setUsed(newStorageValue);
        receivedTaskInfo.setStorageReceived(newStorageValue);

        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeExtraResourcesGivenToTask(Task task, OntModel model) {
        removeRunningTasks(task, model);
        addRunningTasks(task, model);
    }

    public void changeOptimumCPURange(int max) {
        //TODO: optimum ranges for a specific core can be negotiated so implement method that does that
        CPU cpu = getAssociatedCPU();
        Collection<Core> cores = cpu.getAssociatedCore();
        for (Core core : cores) {

            core.setMaxAcceptableValue(max);
        }

    }

    public void changeOptimumMemoryRange(int max) {
        Memory memory = getAssociatedMemory();
        memory.setMaxAcceptableValue(max);

    }

    public void changeOptimumStorageRange(int max) {
        Storage storage = getAssociatedStorage();
        storage.setMaxAcceptableValue(max);

    }

    public void resetOptimumValues() {
        CPU cpu = getAssociatedCPU();
        Collection<Core> cores = cpu.getAssociatedCore();
        for (Core core : cores) {
            core.restoreDefaultOptimumValues();
        }
        getAssociatedMemory().restoreDefaultOptimumValues();
        getAssociatedStorage().restoreDefaultOptimumValues();

    }


    public ServerManagementProxyInterface getProxy() {
        return proxy;
    }

    public void setProxy(ServerManagementProxyInterface proxy) {
        this.proxy = proxy;
    }
}
