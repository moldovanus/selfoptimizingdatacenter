/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contextawaremodel.agents.behaviours;

import actionselection.command.Command;
import actionselection.command.selfHealingCommand.SetCommand;
import actionselection.command.selfHealingCommand.DecrementCommand;
import actionselection.command.selfHealingCommand.IncrementCommand;
import actionselection.context.ContextSnapshot;
import actionselection.context.Memory;
import actionselection.context.SensorValues;
import actionselection.gui.ActionsOutputFrame;
import actionselection.utils.Pair;
import contextawaremodel.GlobalVars;
import contextawaremodel.agents.ReinforcementLearningAgent;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;


import selfHealingOntology.*;

import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;


/**
 * @author Administrator
 */
public class ReinforcementLearningBasicBehaviour extends TickerBehaviour {

    private Memory memory;
    private NumberFormat integerNumberFormat = NumberFormat.getIntegerInstance();
    private double smallestEntropy = 10000;
    private ActionsOutputFrame resultsFrame;
    private ReinforcementLearningAgent agent;
    private SelfHealingProtegeFactory protegeFactory;
    private boolean contextBroken = false;
    private JenaOWLModel model;
    private Property evaluatePolicyProperty;
    private Property hasAcceptedValueProperty;
    private OntModel policyConversionModel;

    public ReinforcementLearningBasicBehaviour(Agent a, int interval, OntModel policyConversionModel, JenaOWLModel jenaOWLModel, Memory memory) {
        super(a, interval);
        this.policyConversionModel = policyConversionModel;
        agent = (ReinforcementLearningAgent) a;
        this.memory = memory;
        resultsFrame = new ActionsOutputFrame("Enviroment");
        evaluatePolicyProperty = policyConversionModel.getDatatypeProperty(GlobalVars.base + "#EvaluatePolicyP");
        hasAcceptedValueProperty = policyConversionModel.getDatatypeProperty(GlobalVars.base + "#acceptableSensorValue");
        model = jenaOWLModel;
        protegeFactory = new SelfHealingProtegeFactory(jenaOWLModel);

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                System.out.println("Synchronized Temperature and Humidity ");
                //synchronize X3D display values with ontology values
                Collection<Sensor> sensors = protegeFactory.getAllSensorInstances();
                for (Sensor sensor : sensors) {
                    SetCommand command = new SetCommand(protegeFactory, sensor.getName(), sensor.getValueOfService());
                    command.executeOnX3D(agent);
                    System.out.println(command);
                }
                resultsFrame.setVisible(true);
            }
        });
    }

    /**
     * Rescris compute entropy sa foloseasca protege factory
     *
     * @param
     * @return
     */

    private Pair<Double, Policy> computeEntropy(ContextSnapshot contextSnapshot) {


        Policy brokenPolicy = null;
        double entropy = 0.0;
        Collection<Policy> policies = protegeFactory.getAllPolicyInstances();

        for (Policy policy : policies) {
            if (!policy.getEvaluatePolicyP(policyConversionModel)) {
                if (brokenPolicy == null) {
                    brokenPolicy = policy;
                }
                entropy++;
            }
        }

        return new Pair<Double, Policy>(entropy, brokenPolicy);
    }

    public Boolean hasCycles(HashMap<SensorValues, SensorValues> contexts, SensorValues myContext) {
        if (contexts.get(myContext) != null) {
            return true;
        } else {
            return false;
        }
    }

    public ContextSnapshot reinforcementLearning(Queue<ContextSnapshot> queue, HashMap<SensorValues, SensorValues> contexts) throws Exception {

        ContextSnapshot context = queue.remove();

        SensorValues values = new SensorValues(protegeFactory);
        Queue<Command> actions = memory.getActions(values);

        //exists
        if (actions != null) {
            context.addActions(actions);
            System.out.println("Remembered");
            return context;
        }

        context.executeActions(policyConversionModel);
        values = new SensorValues(protegeFactory);
        Pair<Double, Policy> contextEvaluationResult = computeEntropy(context);

        //TODO: modify ==0 ( 0 not equals 0.00001    ( Double )
        if (contextEvaluationResult.getFirst() == 0) {
            context.rewind(policyConversionModel);
            return context;
        }
        SensorValues newContext = new SensorValues(protegeFactory);

        if (!hasCycles(contexts, newContext)) {

            HashMap<SensorValues, SensorValues> myContexts = new HashMap<SensorValues, SensorValues>(contexts);
            myContexts.put(newContext, newContext);

            if (contextEvaluationResult.getFirst() > 0) {

                Policy brokenPolicy = contextEvaluationResult.getSecond();
                Collection<Sensor> associatedResources = brokenPolicy.getAssociatedResources();

                for (Sensor sensor : associatedResources) {
                    //skip sensor if its value respects the Policy
                    if (sensor.getAcceptableSensorValue(policyConversionModel)) {
                        continue;
                    }

                    Collection<Actuator> associatedActuators = sensor.getAssociatedActuators();

                    for (Actuator actuator : associatedActuators) {

                        Collection<Action> associatedActions = actuator.getAssociatedActions();
                        //get all actions possible on this

                        for (Action action : associatedActions) {

                            String effect = action.getEffect();
                            char firstChar = effect.trim().charAt(0);

                            if (firstChar == '+') {

                                String numberString = effect.substring(1, effect.length());
                                int value = integerNumberFormat.parse(numberString).intValue();
                                //TODO: constructor cu value
                                IncrementCommand incrementCommand = new IncrementCommand(protegeFactory, sensor.getName(), value);
                                Queue<Command> incrementQueue = new LinkedList(context.getActions());

                                incrementCommand.execute(policyConversionModel);
                                incrementQueue.add(incrementCommand);

                                ContextSnapshot afterIncrement = new ContextSnapshot(incrementQueue);
                                queue.add(afterIncrement);
                                incrementCommand.rewind(policyConversionModel);


                            } else if (firstChar == '-') {

                                String numberString = effect.substring(1, effect.length());
                                int value = integerNumberFormat.parse(numberString).intValue();
                                Queue<Command> decrementQueue = new LinkedList(context.getActions());
                                DecrementCommand decrementCommand = new DecrementCommand(protegeFactory, sensor.getName(), value);

                                decrementCommand.execute(policyConversionModel);
                                decrementQueue.add(decrementCommand);

                                ContextSnapshot afterDecrement = new ContextSnapshot(decrementQueue);
                                queue.add(afterDecrement);
                                decrementCommand.rewind(policyConversionModel);

                            } else if (firstChar >= '0' && firstChar <= '9') {

                                String numberString = effect.substring(0, effect.length());
                                int value = integerNumberFormat.parse(numberString).intValue();

                                SetCommand setCommand = new SetCommand(protegeFactory, sensor.getName(), value);

                                int before = sensor.getValueOfService();
                                setCommand.execute(policyConversionModel);
                                int after = sensor.getValueOfService();

                                if (after != before) {
                                    Queue<Command> setCommandQueue = new LinkedList(context.getActions());
                                    setCommandQueue.add(setCommand);
                                    ContextSnapshot afterSet = new ContextSnapshot(setCommandQueue);
                                    queue.add(afterSet);
                                }
                                setCommand.rewind(policyConversionModel);

                            } else {
                                throw new Exception("Unsupported effect exception:" + firstChar);
                            }
                        }
                    }
                }

                if (queue.peek() == null) {
                    System.err.println("EMPTY QUEUE");
                    context.rewind(policyConversionModel);
                    return context;
                } else {
                    context.rewind(policyConversionModel);
                    return reinforcementLearning(queue, new HashMap<SensorValues, SensorValues>(myContexts));
                }
            }
        } else {
            if (queue.peek() == null) {
                System.err.println("EMPTY QUEUE");
                context.rewind(policyConversionModel);
                return context;
            } else {
                context.rewind(policyConversionModel);
                return reinforcementLearning(queue, new HashMap<SensorValues, SensorValues>(contexts));
            }
        }
        context.rewind(policyConversionModel);
        return context;
    }

    public double evaluateResourceValue(double currentValue, double wantedValue) {
        if (currentValue - wantedValue < 0) {
            return (wantedValue - currentValue);
        }
        return (currentValue - wantedValue);
    }

    private void setBrokenResources(ContextSnapshot contextSnapshot) {

        Map<String, String> brokenResources = GlobalVars.brokenResources;
        //Map<String, String> validResources = GlobalVars.validResources;
        brokenResources.clear();
        //validResources.clear();

        //set for printing
        ArrayList<String> brokenPoliciesNames = new ArrayList<String>();
        Collection<Policy> policies = protegeFactory.getAllPolicyInstances();

        for (Policy policy : policies) {
            Collection<Sensor> sensors = policy.getAssociatedResources();

            if (!policy.getEvaluatePolicyP(policyConversionModel)) {

                brokenPoliciesNames.add(policy.getName());

                for (Sensor sensor : sensors) {
                    if (!sensor.getAcceptableSensorValue(policyConversionModel)) {
                        brokenResources.put(sensor.getLocalName(), sensor.getLocalName());
                    } else {
                        //validResources.put(sensor.getLocalName(), sensor.getLocalName());
                    }
                }

            } //else {
            //for (Sensor sensor : sensors) {
            //skip sensor if its value respects the Policy
            //validResources.put(sensor.getName(), sensor.getName());
            //}
            // }
        }

        resultsFrame.setBrokenPoliciesList(brokenPoliciesNames);

    }

    @Override
    protected void onTick() {

        System.out.println("On tick");

        Queue<ContextSnapshot> queue = new LinkedList<ContextSnapshot>();
        ContextSnapshot initialContext = new ContextSnapshot(new LinkedList<Command>());
        SensorValues currentValues = new SensorValues(protegeFactory);
        queue.add(initialContext);

        resultsFrame.setActionsList(null);
        resultsFrame.setBrokenStatesList(currentValues.toArrayList());

        setBrokenResources(initialContext);

        smallestEntropy = 10000;
        ContextSnapshot contextSnapshot;
        Pair<Double, Policy> entropyState = computeEntropy(initialContext);
        if (entropyState.getFirst() != 0) {
            contextBroken = true;
            ArrayList<String> list = new ArrayList<String>();
            String policyName = entropyState.getSecond().getName().split("#")[1];
            list.add(policyName);
            agent.getSelfHealingLogger().log(Color.ORANGE, "Broken policy", list);

            agent.getSelfHealingLogger().log(Color.red, "Current state", currentValues.toLogMessage());

            Policy brokenPolicy = entropyState.getSecond();
            Object[] associatedResources = brokenPolicy.getAssociatedResources().toArray();

            ArrayList<String> brokenSensorsList = new ArrayList<String>();
            int associatedResourcesSize = associatedResources.length;
            for (int i = 0; i < associatedResourcesSize; i++) {


                //get the resource as individual from the global model such that getPropertyValue can be called on it
                Sensor sensor = (Sensor) associatedResources[i];

                //skip sensor if its value respects the Policy
                if (!sensor.getAcceptableSensorValue(policyConversionModel)) {
                    brokenSensorsList.add(sensor.getName().split("#")[1]);
                }
            }
            agent.getSelfHealingLogger().log(Color.ORANGE, "Sensors that break the policies", brokenSensorsList);


            try {
                long startMinutes = new java.util.Date().getTime();

                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setLanguage("JavaSerialization");

                msg.setContent("BROKEN");
                msg.addReceiver(new AID(GlobalVars.RLAGENT_NAME + "@" + agent.getContainerController().getPlatformName()));
                agent.send(msg);
                agent.setContextDirty(false);
                contextSnapshot = reinforcementLearning(queue, new HashMap<SensorValues, SensorValues>());

                msg.setContent("OK");
                msg.addReceiver(new AID(GlobalVars.RLAGENT_NAME + "@" + agent.getContainerController().getPlatformName()));
                agent.send(msg);

                long endMinutes = new java.util.Date().getTime();

                int value = (int) ((endMinutes - startMinutes) / 1000);

                agent.setRlTime(value);

                System.err.println("Reinforcement alg running time: " + value + " seconds");

            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }

            //do nothing if
            if (agent.isContextDirty()) {
                return;
            }
            setBrokenResources(contextSnapshot);

            Queue<Command> bestActionsList = contextSnapshot.getActions();

            ArrayList<String[]> actions = new ArrayList<String[]>(bestActionsList.size());


            System.err.println();
            System.err.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.err.println("for " + currentValues);


            System.err.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.err.println("===============================================================");

            ArrayList<String> message = new ArrayList<String>();
            for (Command command : bestActionsList) {
                actions.add(command.toStringArray());
                System.err.println(command);
                message.add(command.toString());

            }

            agent.getSelfHealingLogger().log(Color.BLUE, "Corrective actions", message);

            System.err.println("===============================================================");
            System.err.println();

            resultsFrame.setActionsList(actions);

            memory.memorize(currentValues, bestActionsList);
            contextSnapshot.executeActionsOnOWL();
            contextSnapshot.executeActions(policyConversionModel);
            contextSnapshot.executeActionsOnX3D(agent);


        } else {
            if (contextBroken) {
                contextBroken = false;
                agent.getSelfHealingLogger().log(Color.green, "Current state", currentValues.toLogMessage());
            }
        }

    }

    public boolean getEvaluateProp(Individual policy) {
        //System.out.println(base + "#EvaluatePolicyP");
        Statement property = policy.getProperty(evaluatePolicyProperty);

        if (property == null) {
            return false;
        }

        return property.getBoolean();
    }


}
