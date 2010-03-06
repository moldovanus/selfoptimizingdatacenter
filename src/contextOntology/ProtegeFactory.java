package greenContextOntology;


import greenContextOntology.impl.*;

import edu.stanford.smi.protegex.owl.model.*;

import java.util.*;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 *
 * @version generated on Sat Mar 06 16:14:39 EET 2010
 */
public class ProtegeFactory {

    private OWLModel owlModel;

    static {
          }

    public ProtegeFactory(OWLModel owlModel) {
        this.owlModel = owlModel;
    }

  
    public RDFSNamedClass getEntityClass() {
        final String uri = "http://swrl.stanford.edu/ontologies/3.3/swrla.owl#Entity";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFSNamedClass(name);
    }

    public Entity createEntity(String name) {
        final RDFSNamedClass cls = getEntityClass();
        if (name == null) {
            name = owlModel.getNextAnonymousResourceName();
        }
        return  new DefaultEntity(owlModel, cls.createInstance(name).getFrameID());
    }

    public Entity getEntity(String name) {
        RDFResource res = owlModel.getRDFResource(name);
        if (res == null) {return null;}
        if (res instanceof Entity) {
            return (Entity) res;
        } else if (res.hasProtegeType(getEntityClass())) {
            return new DefaultEntity(owlModel, res.getFrameID());
        }
        return null;
    }

    public Collection<Entity> getAllEntityInstances() {
        return getAllEntityInstances(false);
    }

    public Collection<Entity> getAllEntityInstances(boolean transitive) {
        Collection<Entity> result = new ArrayList<Entity>();
        final RDFSNamedClass cls = getEntityClass();
        RDFResource owlIndividual;
        for (Iterator it = cls.getInstances(transitive).iterator();it.hasNext();) {
            owlIndividual = (RDFResource) it.next();
            result.add(new DefaultEntity(owlModel, owlIndividual.getFrameID()));
        }
        return result;
    }


    public RDFSNamedClass getRuleGroupClass() {
        final String uri = "http://swrl.stanford.edu/ontologies/3.3/swrla.owl#RuleGroup";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFSNamedClass(name);
    }

    public RuleGroup createRuleGroup(String name) {
        final RDFSNamedClass cls = getRuleGroupClass();
        if (name == null) {
            name = owlModel.getNextAnonymousResourceName();
        }
        return  new DefaultRuleGroup(owlModel, cls.createInstance(name).getFrameID());
    }

    public RuleGroup getRuleGroup(String name) {
        RDFResource res = owlModel.getRDFResource(name);
        if (res == null) {return null;}
        if (res instanceof RuleGroup) {
            return (RuleGroup) res;
        } else if (res.hasProtegeType(getRuleGroupClass())) {
            return new DefaultRuleGroup(owlModel, res.getFrameID());
        }
        return null;
    }

    public Collection<RuleGroup> getAllRuleGroupInstances() {
        return getAllRuleGroupInstances(false);
    }

    public Collection<RuleGroup> getAllRuleGroupInstances(boolean transitive) {
        Collection<RuleGroup> result = new ArrayList<RuleGroup>();
        final RDFSNamedClass cls = getRuleGroupClass();
        RDFResource owlIndividual;
        for (Iterator it = cls.getInstances(transitive).iterator();it.hasNext();) {
            owlIndividual = (RDFResource) it.next();
            result.add(new DefaultRuleGroup(owlModel, owlIndividual.getFrameID()));
        }
        return result;
    }


    public RDFSNamedClass getTaskClass() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#Task";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFSNamedClass(name);
    }

    public Task createTask(String name) {
        final RDFSNamedClass cls = getTaskClass();
        if (name == null) {
            name = owlModel.getNextAnonymousResourceName();
        }
        return  new DefaultTask(owlModel, cls.createInstance(name).getFrameID());
    }

    public Task getTask(String name) {
        RDFResource res = owlModel.getRDFResource(name);
        if (res == null) {return null;}
        if (res instanceof Task) {
            return (Task) res;
        } else if (res.hasProtegeType(getTaskClass())) {
            return new DefaultTask(owlModel, res.getFrameID());
        }
        return null;
    }

    public Collection<Task> getAllTaskInstances() {
        return getAllTaskInstances(false);
    }

    public Collection<Task> getAllTaskInstances(boolean transitive) {
        Collection<Task> result = new ArrayList<Task>();
        final RDFSNamedClass cls = getTaskClass();
        RDFResource owlIndividual;
        for (Iterator it = cls.getInstances(transitive).iterator();it.hasNext();) {
            owlIndividual = (RDFResource) it.next();
            result.add(new DefaultTask(owlModel, owlIndividual.getFrameID()));
        }
        return result;
    }


    public RDFSNamedClass getActorClass() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#Actor";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFSNamedClass(name);
    }

    public Actor createActor(String name) {
        final RDFSNamedClass cls = getActorClass();
        if (name == null) {
            name = owlModel.getNextAnonymousResourceName();
        }
        return  new DefaultActor(owlModel, cls.createInstance(name).getFrameID());
    }

    public Actor getActor(String name) {
        RDFResource res = owlModel.getRDFResource(name);
        if (res == null) {return null;}
        if (res instanceof Actor) {
            return (Actor) res;
        } else if (res.hasProtegeType(getActorClass())) {
            return new DefaultActor(owlModel, res.getFrameID());
        }
        return null;
    }

    public Collection<Actor> getAllActorInstances() {
        return getAllActorInstances(false);
    }

    public Collection<Actor> getAllActorInstances(boolean transitive) {
        Collection<Actor> result = new ArrayList<Actor>();
        final RDFSNamedClass cls = getActorClass();
        RDFResource owlIndividual;
        for (Iterator it = cls.getInstances(transitive).iterator();it.hasNext();) {
            owlIndividual = (RDFResource) it.next();
            result.add(new DefaultActor(owlModel, owlIndividual.getFrameID()));
        }
        return result;
    }


    public RDFSNamedClass getReceivedClass() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#Received";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFSNamedClass(name);
    }

    public Received createReceived(String name) {
        final RDFSNamedClass cls = getReceivedClass();
        if (name == null) {
            name = owlModel.getNextAnonymousResourceName();
        }
        return  new DefaultReceived(owlModel, cls.createInstance(name).getFrameID());
    }

    public Received getReceived(String name) {
        RDFResource res = owlModel.getRDFResource(name);
        if (res == null) {return null;}
        if (res instanceof Received) {
            return (Received) res;
        } else if (res.hasProtegeType(getReceivedClass())) {
            return new DefaultReceived(owlModel, res.getFrameID());
        }
        return null;
    }

    public Collection<Received> getAllReceivedInstances() {
        return getAllReceivedInstances(false);
    }

    public Collection<Received> getAllReceivedInstances(boolean transitive) {
        Collection<Received> result = new ArrayList<Received>();
        final RDFSNamedClass cls = getReceivedClass();
        RDFResource owlIndividual;
        for (Iterator it = cls.getInstances(transitive).iterator();it.hasNext();) {
            owlIndividual = (RDFResource) it.next();
            result.add(new DefaultReceived(owlModel, owlIndividual.getFrameID()));
        }
        return result;
    }


    public RDFSNamedClass getRequestedClass() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#Requested";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFSNamedClass(name);
    }

    public Requested createRequested(String name) {
        final RDFSNamedClass cls = getRequestedClass();
        if (name == null) {
            name = owlModel.getNextAnonymousResourceName();
        }
        return  new DefaultRequested(owlModel, cls.createInstance(name).getFrameID());
    }

    public Requested getRequested(String name) {
        RDFResource res = owlModel.getRDFResource(name);
        if (res == null) {return null;}
        if (res instanceof Requested) {
            return (Requested) res;
        } else if (res.hasProtegeType(getRequestedClass())) {
            return new DefaultRequested(owlModel, res.getFrameID());
        }
        return null;
    }

    public Collection<Requested> getAllRequestedInstances() {
        return getAllRequestedInstances(false);
    }

    public Collection<Requested> getAllRequestedInstances(boolean transitive) {
        Collection<Requested> result = new ArrayList<Requested>();
        final RDFSNamedClass cls = getRequestedClass();
        RDFResource owlIndividual;
        for (Iterator it = cls.getInstances(transitive).iterator();it.hasNext();) {
            owlIndividual = (RDFResource) it.next();
            result.add(new DefaultRequested(owlModel, owlIndividual.getFrameID()));
        }
        return result;
    }


    public RDFSNamedClass getMemoryClass() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#Memory";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFSNamedClass(name);
    }

    public Memory createMemory(String name) {
        final RDFSNamedClass cls = getMemoryClass();
        if (name == null) {
            name = owlModel.getNextAnonymousResourceName();
        }
        return  new DefaultMemory(owlModel, cls.createInstance(name).getFrameID());
    }

    public Memory getMemory(String name) {
        RDFResource res = owlModel.getRDFResource(name);
        if (res == null) {return null;}
        if (res instanceof Memory) {
            return (Memory) res;
        } else if (res.hasProtegeType(getMemoryClass())) {
            return new DefaultMemory(owlModel, res.getFrameID());
        }
        return null;
    }

    public Collection<Memory> getAllMemoryInstances() {
        return getAllMemoryInstances(false);
    }

    public Collection<Memory> getAllMemoryInstances(boolean transitive) {
        Collection<Memory> result = new ArrayList<Memory>();
        final RDFSNamedClass cls = getMemoryClass();
        RDFResource owlIndividual;
        for (Iterator it = cls.getInstances(transitive).iterator();it.hasNext();) {
            owlIndividual = (RDFResource) it.next();
            result.add(new DefaultMemory(owlModel, owlIndividual.getFrameID()));
        }
        return result;
    }


    public RDFSNamedClass getComponentClass() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#Component";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFSNamedClass(name);
    }

    public Component createComponent(String name) {
        final RDFSNamedClass cls = getComponentClass();
        if (name == null) {
            name = owlModel.getNextAnonymousResourceName();
        }
        return  new DefaultComponent(owlModel, cls.createInstance(name).getFrameID());
    }

    public Component getComponent(String name) {
        RDFResource res = owlModel.getRDFResource(name);
        if (res == null) {return null;}
        if (res instanceof Component) {
            return (Component) res;
        } else if (res.hasProtegeType(getComponentClass())) {
            return new DefaultComponent(owlModel, res.getFrameID());
        }
        return null;
    }

    public Collection<Component> getAllComponentInstances() {
        return getAllComponentInstances(false);
    }

    public Collection<Component> getAllComponentInstances(boolean transitive) {
        Collection<Component> result = new ArrayList<Component>();
        final RDFSNamedClass cls = getComponentClass();
        RDFResource owlIndividual;
        for (Iterator it = cls.getInstances(transitive).iterator();it.hasNext();) {
            owlIndividual = (RDFResource) it.next();
            result.add(new DefaultComponent(owlModel, owlIndividual.getFrameID()));
        }
        return result;
    }


    public RDFSNamedClass getCoreClass() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#Core";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFSNamedClass(name);
    }

    public Core createCore(String name) {
        final RDFSNamedClass cls = getCoreClass();
        if (name == null) {
            name = owlModel.getNextAnonymousResourceName();
        }
        return  new DefaultCore(owlModel, cls.createInstance(name).getFrameID());
    }

    public Core getCore(String name) {
        RDFResource res = owlModel.getRDFResource(name);
        if (res == null) {return null;}
        if (res instanceof Core) {
            return (Core) res;
        } else if (res.hasProtegeType(getCoreClass())) {
            return new DefaultCore(owlModel, res.getFrameID());
        }
        return null;
    }

    public Collection<Core> getAllCoreInstances() {
        return getAllCoreInstances(false);
    }

    public Collection<Core> getAllCoreInstances(boolean transitive) {
        Collection<Core> result = new ArrayList<Core>();
        final RDFSNamedClass cls = getCoreClass();
        RDFResource owlIndividual;
        for (Iterator it = cls.getInstances(transitive).iterator();it.hasNext();) {
            owlIndividual = (RDFResource) it.next();
            result.add(new DefaultCore(owlModel, owlIndividual.getFrameID()));
        }
        return result;
    }


    public RDFSNamedClass getTaskInfoClass() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#TaskInfo";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFSNamedClass(name);
    }

    public TaskInfo createTaskInfo(String name) {
        final RDFSNamedClass cls = getTaskInfoClass();
        if (name == null) {
            name = owlModel.getNextAnonymousResourceName();
        }
        return  new DefaultTaskInfo(owlModel, cls.createInstance(name).getFrameID());
    }

    public TaskInfo getTaskInfo(String name) {
        RDFResource res = owlModel.getRDFResource(name);
        if (res == null) {return null;}
        if (res instanceof TaskInfo) {
            return (TaskInfo) res;
        } else if (res.hasProtegeType(getTaskInfoClass())) {
            return new DefaultTaskInfo(owlModel, res.getFrameID());
        }
        return null;
    }

    public Collection<TaskInfo> getAllTaskInfoInstances() {
        return getAllTaskInfoInstances(false);
    }

    public Collection<TaskInfo> getAllTaskInfoInstances(boolean transitive) {
        Collection<TaskInfo> result = new ArrayList<TaskInfo>();
        final RDFSNamedClass cls = getTaskInfoClass();
        RDFResource owlIndividual;
        for (Iterator it = cls.getInstances(transitive).iterator();it.hasNext();) {
            owlIndividual = (RDFResource) it.next();
            result.add(new DefaultTaskInfo(owlModel, owlIndividual.getFrameID()));
        }
        return result;
    }


    public RDFSNamedClass getContextElementClass() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#ContextElement";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFSNamedClass(name);
    }

    public ContextElement createContextElement(String name) {
        final RDFSNamedClass cls = getContextElementClass();
        if (name == null) {
            name = owlModel.getNextAnonymousResourceName();
        }
        return  new DefaultContextElement(owlModel, cls.createInstance(name).getFrameID());
    }

    public ContextElement getContextElement(String name) {
        RDFResource res = owlModel.getRDFResource(name);
        if (res == null) {return null;}
        if (res instanceof ContextElement) {
            return (ContextElement) res;
        } else if (res.hasProtegeType(getContextElementClass())) {
            return new DefaultContextElement(owlModel, res.getFrameID());
        }
        return null;
    }

    public Collection<ContextElement> getAllContextElementInstances() {
        return getAllContextElementInstances(false);
    }

    public Collection<ContextElement> getAllContextElementInstances(boolean transitive) {
        Collection<ContextElement> result = new ArrayList<ContextElement>();
        final RDFSNamedClass cls = getContextElementClass();
        RDFResource owlIndividual;
        for (Iterator it = cls.getInstances(transitive).iterator();it.hasNext();) {
            owlIndividual = (RDFResource) it.next();
            result.add(new DefaultContextElement(owlModel, owlIndividual.getFrameID()));
        }
        return result;
    }


    public RDFSNamedClass getPolicyClass() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#Policy";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFSNamedClass(name);
    }

    public Policy createPolicy(String name) {
        final RDFSNamedClass cls = getPolicyClass();
        if (name == null) {
            name = owlModel.getNextAnonymousResourceName();
        }
        return  new DefaultPolicy(owlModel, cls.createInstance(name).getFrameID());
    }

    public Policy getPolicy(String name) {
        RDFResource res = owlModel.getRDFResource(name);
        if (res == null) {return null;}
        if (res instanceof Policy) {
            return (Policy) res;
        } else if (res.hasProtegeType(getPolicyClass())) {
            return new DefaultPolicy(owlModel, res.getFrameID());
        }
        return null;
    }

    public Collection<Policy> getAllPolicyInstances() {
        return getAllPolicyInstances(false);
    }

    public Collection<Policy> getAllPolicyInstances(boolean transitive) {
        Collection<Policy> result = new ArrayList<Policy>();
        final RDFSNamedClass cls = getPolicyClass();
        RDFResource owlIndividual;
        for (Iterator it = cls.getInstances(transitive).iterator();it.hasNext();) {
            owlIndividual = (RDFResource) it.next();
            result.add(new DefaultPolicy(owlModel, owlIndividual.getFrameID()));
        }
        return result;
    }


    public RDFSNamedClass getStorageClass() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#Storage";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFSNamedClass(name);
    }

    public Storage createStorage(String name) {
        final RDFSNamedClass cls = getStorageClass();
        if (name == null) {
            name = owlModel.getNextAnonymousResourceName();
        }
        return  new DefaultStorage(owlModel, cls.createInstance(name).getFrameID());
    }

    public Storage getStorage(String name) {
        RDFResource res = owlModel.getRDFResource(name);
        if (res == null) {return null;}
        if (res instanceof Storage) {
            return (Storage) res;
        } else if (res.hasProtegeType(getStorageClass())) {
            return new DefaultStorage(owlModel, res.getFrameID());
        }
        return null;
    }

    public Collection<Storage> getAllStorageInstances() {
        return getAllStorageInstances(false);
    }

    public Collection<Storage> getAllStorageInstances(boolean transitive) {
        Collection<Storage> result = new ArrayList<Storage>();
        final RDFSNamedClass cls = getStorageClass();
        RDFResource owlIndividual;
        for (Iterator it = cls.getInstances(transitive).iterator();it.hasNext();) {
            owlIndividual = (RDFResource) it.next();
            result.add(new DefaultStorage(owlModel, owlIndividual.getFrameID()));
        }
        return result;
    }


    public RDFSNamedClass getResourceClass() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#Resource";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFSNamedClass(name);
    }

    public Resource createResource(String name) {
        final RDFSNamedClass cls = getResourceClass();
        if (name == null) {
            name = owlModel.getNextAnonymousResourceName();
        }
        return  new DefaultResource(owlModel, cls.createInstance(name).getFrameID());
    }

    public Resource getResource(String name) {
        RDFResource res = owlModel.getRDFResource(name);
        if (res == null) {return null;}
        if (res instanceof Resource) {
            return (Resource) res;
        } else if (res.hasProtegeType(getResourceClass())) {
            return new DefaultResource(owlModel, res.getFrameID());
        }
        return null;
    }

    public Collection<Resource> getAllResourceInstances() {
        return getAllResourceInstances(false);
    }

    public Collection<Resource> getAllResourceInstances(boolean transitive) {
        Collection<Resource> result = new ArrayList<Resource>();
        final RDFSNamedClass cls = getResourceClass();
        RDFResource owlIndividual;
        for (Iterator it = cls.getInstances(transitive).iterator();it.hasNext();) {
            owlIndividual = (RDFResource) it.next();
            result.add(new DefaultResource(owlModel, owlIndividual.getFrameID()));
        }
        return result;
    }


    public RDFSNamedClass getQoSPolicyClass() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#QoSPolicy";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFSNamedClass(name);
    }

    public QoSPolicy createQoSPolicy(String name) {
        final RDFSNamedClass cls = getQoSPolicyClass();
        if (name == null) {
            name = owlModel.getNextAnonymousResourceName();
        }
        return  new DefaultQoSPolicy(owlModel, cls.createInstance(name).getFrameID());
    }

    public QoSPolicy getQoSPolicy(String name) {
        RDFResource res = owlModel.getRDFResource(name);
        if (res == null) {return null;}
        if (res instanceof QoSPolicy) {
            return (QoSPolicy) res;
        } else if (res.hasProtegeType(getQoSPolicyClass())) {
            return new DefaultQoSPolicy(owlModel, res.getFrameID());
        }
        return null;
    }

    public Collection<QoSPolicy> getAllQoSPolicyInstances() {
        return getAllQoSPolicyInstances(false);
    }

    public Collection<QoSPolicy> getAllQoSPolicyInstances(boolean transitive) {
        Collection<QoSPolicy> result = new ArrayList<QoSPolicy>();
        final RDFSNamedClass cls = getQoSPolicyClass();
        RDFResource owlIndividual;
        for (Iterator it = cls.getInstances(transitive).iterator();it.hasNext();) {
            owlIndividual = (RDFResource) it.next();
            result.add(new DefaultQoSPolicy(owlModel, owlIndividual.getFrameID()));
        }
        return result;
    }


    public RDFSNamedClass getCPUClass() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#CPU";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFSNamedClass(name);
    }

    public CPU createCPU(String name) {
        final RDFSNamedClass cls = getCPUClass();
        if (name == null) {
            name = owlModel.getNextAnonymousResourceName();
        }
        return  new DefaultCPU(owlModel, cls.createInstance(name).getFrameID());
    }

    public CPU getCPU(String name) {
        RDFResource res = owlModel.getRDFResource(name);
        if (res == null) {return null;}
        if (res instanceof CPU) {
            return (CPU) res;
        } else if (res.hasProtegeType(getCPUClass())) {
            return new DefaultCPU(owlModel, res.getFrameID());
        }
        return null;
    }

    public Collection<CPU> getAllCPUInstances() {
        return getAllCPUInstances(false);
    }

    public Collection<CPU> getAllCPUInstances(boolean transitive) {
        Collection<CPU> result = new ArrayList<CPU>();
        final RDFSNamedClass cls = getCPUClass();
        RDFResource owlIndividual;
        for (Iterator it = cls.getInstances(transitive).iterator();it.hasNext();) {
            owlIndividual = (RDFResource) it.next();
            result.add(new DefaultCPU(owlModel, owlIndividual.getFrameID()));
        }
        return result;
    }


    public RDFSNamedClass getEnergyPolicyClass() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#EnergyPolicy";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFSNamedClass(name);
    }

    public EnergyPolicy createEnergyPolicy(String name) {
        final RDFSNamedClass cls = getEnergyPolicyClass();
        if (name == null) {
            name = owlModel.getNextAnonymousResourceName();
        }
        return  new DefaultEnergyPolicy(owlModel, cls.createInstance(name).getFrameID());
    }

    public EnergyPolicy getEnergyPolicy(String name) {
        RDFResource res = owlModel.getRDFResource(name);
        if (res == null) {return null;}
        if (res instanceof EnergyPolicy) {
            return (EnergyPolicy) res;
        } else if (res.hasProtegeType(getEnergyPolicyClass())) {
            return new DefaultEnergyPolicy(owlModel, res.getFrameID());
        }
        return null;
    }

    public Collection<EnergyPolicy> getAllEnergyPolicyInstances() {
        return getAllEnergyPolicyInstances(false);
    }

    public Collection<EnergyPolicy> getAllEnergyPolicyInstances(boolean transitive) {
        Collection<EnergyPolicy> result = new ArrayList<EnergyPolicy>();
        final RDFSNamedClass cls = getEnergyPolicyClass();
        RDFResource owlIndividual;
        for (Iterator it = cls.getInstances(transitive).iterator();it.hasNext();) {
            owlIndividual = (RDFResource) it.next();
            result.add(new DefaultEnergyPolicy(owlModel, owlIndividual.getFrameID()));
        }
        return result;
    }


    public RDFSNamedClass getServerClass() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#Server";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFSNamedClass(name);
    }

    public Server createServer(String name) {
        final RDFSNamedClass cls = getServerClass();
        if (name == null) {
            name = owlModel.getNextAnonymousResourceName();
        }
        return  new DefaultServer(owlModel, cls.createInstance(name).getFrameID());
    }

    public Server getServer(String name) {
        RDFResource res = owlModel.getRDFResource(name);
        if (res == null) {return null;}
        if (res instanceof Server) {
            return (Server) res;
        } else if (res.hasProtegeType(getServerClass())) {
            return new DefaultServer(owlModel, res.getFrameID());
        }
        return null;
    }

    public Collection<Server> getAllServerInstances() {
        return getAllServerInstances(false);
    }

    public Collection<Server> getAllServerInstances(boolean transitive) {
        Collection<Server> result = new ArrayList<Server>();
        final RDFSNamedClass cls = getServerClass();
        RDFResource owlIndividual;
        for (Iterator it = cls.getInstances(transitive).iterator();it.hasNext();) {
            owlIndividual = (RDFResource) it.next();
            result.add(new DefaultServer(owlModel, owlIndividual.getFrameID()));
        }
        return result;
    }


    public RDFSNamedClass getActionClass() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#Action";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFSNamedClass(name);
    }

    public Action createAction(String name) {
        final RDFSNamedClass cls = getActionClass();
        if (name == null) {
            name = owlModel.getNextAnonymousResourceName();
        }
        return  new DefaultAction(owlModel, cls.createInstance(name).getFrameID());
    }

    public Action getAction(String name) {
        RDFResource res = owlModel.getRDFResource(name);
        if (res == null) {return null;}
        if (res instanceof Action) {
            return (Action) res;
        } else if (res.hasProtegeType(getActionClass())) {
            return new DefaultAction(owlModel, res.getFrameID());
        }
        return null;
    }

    public Collection<Action> getAllActionInstances() {
        return getAllActionInstances(false);
    }

    public Collection<Action> getAllActionInstances(boolean transitive) {
        Collection<Action> result = new ArrayList<Action>();
        final RDFSNamedClass cls = getActionClass();
        RDFResource owlIndividual;
        for (Iterator it = cls.getInstances(transitive).iterator();it.hasNext();) {
            owlIndividual = (RDFResource) it.next();
            result.add(new DefaultAction(owlModel, owlIndividual.getFrameID()));
        }
        return result;
    }


    public RDFProperty getHasRuleCategoryProperty() {
        final String uri = "http://swrl.stanford.edu/ontologies/3.3/swrla.owl#hasRuleCategory";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getHasBuiltInPhraseProperty() {
        final String uri = "http://swrl.stanford.edu/ontologies/3.3/swrla.owl#hasBuiltInPhrase";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getIsRuleEnabledProperty() {
        final String uri = "http://swrl.stanford.edu/ontologies/3.3/swrla.owl#isRuleEnabled";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getIsRuleGroupEnabledProperty() {
        final String uri = "http://swrl.stanford.edu/ontologies/3.3/swrla.owl#isRuleGroupEnabled";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getHasClassPhraseProperty() {
        final String uri = "http://swrl.stanford.edu/ontologies/3.3/swrla.owl#hasClassPhrase";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getHasPropertyPhraseProperty() {
        final String uri = "http://swrl.stanford.edu/ontologies/3.3/swrla.owl#hasPropertyPhrase";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getNameProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#name";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getRespectedProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#respected";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getUsedProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#used";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getTotalProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#total";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getWebServiceProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#webService";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getMemoryProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#memory";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getTaskPropertyProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#taskProperty";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getCoresProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#cores";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getStorageProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#storage";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getCpuProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#cpu";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getOptimumProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#optimum";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getHasRuleGroupProperty() {
        final String uri = "http://swrl.stanford.edu/ontologies/3.3/swrla.owl#hasRuleGroup";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getAssociatedInfoProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#associatedInfo";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getReferencesProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#references";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getAssociatedCoreProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#associatedCore";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }


    public RDFProperty getAssociatedComponentProperty() {
        final String uri = "http://www.owl-ontologies.com/Datacenter.owl#associatedComponent";
        final String name = owlModel.getResourceNameForURI(uri);
        return owlModel.getRDFProperty(name);
    }
}
