/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contextaware.agents.behaviours;

import actionEnforcement.command.selfOptimizingCommand.RemoveTaskFromServerCommand;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import contextaware.GlobalVars;
import contextaware.agents.ReinforcementLearningAgent;
import contextaware.worldInterface.datacenterInterface.proxies.ServerManagementProxyInterface;
import contextaware.worldInterface.datacenterInterface.proxies.impl.ProxyFactory;
import contextaware.worldInterface.dtos.TaskDto;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import ontologyRepresentations.greenContextOntology.*;

/**
 * @author Moldovanus
 */
public class ReceiveMessageRLBehaviour extends CyclicBehaviour {

    private ReinforcementLearningAgent agent;
    private OWLModel owlModel;
    private OntModel jenaModel;
    private SWRLFactory swrlFactory;
    private DatacenterProtegeFactory protegeFactory;
    private OntModel ontModelDataCenter;
    private OWLModel owlModelDataCenter;

    public ReceiveMessageRLBehaviour(Agent agent, OWLModel owlModel, OntModel jenaModel, OWLModel owlModelDataCenter,
                                     OntModel ontModelDataCenter) {
        super(agent);
        protegeFactory = new DatacenterProtegeFactory(owlModelDataCenter);
        this.owlModelDataCenter = owlModelDataCenter;
        this.agent = (ReinforcementLearningAgent) agent;
        this.owlModel = owlModel;
        this.jenaModel = jenaModel;
        this.ontModelDataCenter = ontModelDataCenter;
    }

    @Override
    public void action() {
        ACLMessage message = agent.receive();
        if (message == null) {
            return;
        }


        try {
            switch (message.getPerformative()) {
                case ACLMessage.INFORM_REF:
                    String individualName = (String) message.getContentObject();
                    final RDFResource individual = owlModel.getRDFResource(individualName);
                    if (!individual.getProtegeType().getNamedSuperclasses(true).contains(owlModel.getRDFSNamedClass("sensor"))) {
                        return;
                    }

                    RDFProperty urlProperty = owlModel.getRDFProperty("has-web-service-URI");
                    String url = individual.getPropertyValue(urlProperty).toString();

                    //register the web service URL read from the external file into the jena ont model
                    Individual sensor = jenaModel.getIndividual(GlobalVars.base + "#" + individualName);
                    Property urlJenaProperty = jenaModel.getDatatypeProperty(GlobalVars.base + "#has-web-service-URI");
                    sensor.setPropertyValue(urlJenaProperty, jenaModel.createLiteralStatement(
                            sensor, urlJenaProperty, url).getLiteral().as(RDFNode.class));
                    break;

                case ACLMessage.INFORM:
                    String content = (String) message.getContent();
                    if (content.equals("BROKEN")) {
                        agent.setContextIsOK(false);
                    } else if (content.equals("OK")) {
                        agent.setContextIsOK(true);
                    } else {
                        TaskDto taskDto = (TaskDto) message.getContentObject();
                        Task task = protegeFactory.createTask("http://www.owl-ontologies.com/Datacenter.owl#" + taskDto.getTaskName());
                        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa          " + taskDto.getTaskName());
                        QoSPolicy policy = protegeFactory.createQoSPolicy(taskDto.getTaskName() + "_QoSPolicy");

                        RequestedTaskInfo requestedInfo = protegeFactory.createRequestedTaskInfo(taskDto.getTaskName() + "_RequestedInfo");
                        ReceivedTaskInfo receivedInfo = protegeFactory.createReceivedTaskInfo(taskDto.getTaskName() + "_ReceivedInfo");
                        task.setTaskName(taskDto.getTaskName());

                        requestedInfo.setCores(taskDto.getRequestedCores(), ontModelDataCenter);
                        requestedInfo.setCpuMaxAcceptableValue(taskDto.getRequestedCPUMax(), ontModelDataCenter);
                        requestedInfo.setCpuMinAcceptableValue(taskDto.getRequestedCPUMin(), ontModelDataCenter);

                        requestedInfo.setMemoryMaxAcceptableValue(taskDto.getRequestedMemoryMax(), ontModelDataCenter);
                        requestedInfo.setMemoryMinAcceptableValue(taskDto.getRequestedMemoryMin(), ontModelDataCenter);

                        requestedInfo.setStorageMaxAcceptableValue(taskDto.getRequestedStorageMax());
                        requestedInfo.setStorageMinAcceptableValue(taskDto.getRequestedStorageMin());

                        receivedInfo.setCores(0, ontModelDataCenter);
                        receivedInfo.setCpuReceived(0, ontModelDataCenter);
                        receivedInfo.setMemoryReceived(0, ontModelDataCenter);
                        receivedInfo.setStorageReceived(0, ontModelDataCenter);

                        task.setReceivedInfo(receivedInfo);
                        task.setRequestedInfo(requestedInfo);
                        task.setCpuWeight(0.33f);
                        task.setStorageWeight(0.33f);
                        task.setMemoryWeight(0.33f);
                        task.setTaskName(taskDto.getTaskName());

                        policy.setReferenced(task);
                        policy.setRespected(false);
                        policy.setPriority(1);
                    }
                    break;
                case ACLMessage.INFORM_IF:
                    System.out.println("http://www.owl-ontologies.com/Datacenter.owl#" + message.getContent().split("-")[0]);

                    Task selectedTask = protegeFactory.getTask("http://www.owl-ontologies.com/Datacenter.owl#" + message.getContent().split("-")[0]);
                    if (selectedTask.getAssociatedServer() != null) {
                        RemoveTaskFromServerCommand command = new RemoveTaskFromServerCommand(protegeFactory, selectedTask.getName(), selectedTask.getAssociatedServer().getName());
                        command.execute(jenaModel);
                        command.executeOnX3D(agent);
                        command.executeOnWebService();
                        ServerManagementProxyInterface management;

                        if (selectedTask.getAssociatedServer() != null) {
                            management = ProxyFactory.createServerManagementProxy(selectedTask.getAssociatedServer().getServerIPAddress());
                            management.deleteVirtualMachine(selectedTask.getTaskName());
                        }

                        selectedTask.delete();


                        agent.sendAllTasksToClient();
                    } else {
                        agent.sendRefuseMessage();
                    }
                    break;
                case ACLMessage.SUBSCRIBE:
                    Boolean value = (Boolean) message.getContentObject();
                    agent.setContextDirty(value);
                    System.err.println("Context dirty received");
                    break;
                //http://www.owl-ontologies.com/Datacenter.owl#Task_1
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
}
