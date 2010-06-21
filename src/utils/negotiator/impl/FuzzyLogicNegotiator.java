/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.negotiator.impl;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionPieceWiseLinear;
import net.sourceforge.jFuzzyLogic.membership.Value;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import ontologyRepresentations.greenContextOntology.*;
import org.antlr.runtime.RecognitionException;
import utils.negotiator.Negotiator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
public class FuzzyLogicNegotiator implements Negotiator {

    private String fuzzyControlLanguageFile;

    protected FuzzyLogicNegotiator(String fuzzyControlLanguageFile) {
        this.fuzzyControlLanguageFile = fuzzyControlLanguageFile;
    }

    /**
     * Uses JFuzzyLogic to compute the tradeoff between what a server is willing to offer and what a task needs
     *
     * @param server
     * @param task
     * @return [negotiated CPU, negotiated Memory, negotiated Storage]  -  map containing the negotiated values for the
     *         CPU, memory and storage
     */

    public synchronized Map<String, Double> negotiate(Server server, Task task) {

        FIS fis = null;
        try {
            fis = FIS.load(fuzzyControlLanguageFile, true);
        } catch (RuntimeException e) {

            System.err.println("FuzzyLogicNegotiator creation failed : Can't load file: '"
                    + fuzzyControlLanguageFile + "'.");
            e.printStackTrace();
        }
        // Error while loading?
        if (fis == null) {
            System.err.println("FuzzyLogicNegotiator creation failed : Can't load file: '"
                    + fuzzyControlLanguageFile + "'.");
        }

        FunctionBlock functionBlock = fis.getFunctionBlock("negotiator");


        MembershipFunction requestedRangeMembershipFunction = functionBlock.getVariable("requested_range").getLinguisticTerm("requested_range_value").getMembershipFunction();
        // serverRangeMembershipFunction = functionBlock.getVariable("server_range").getLinguisticTerm("server_range_value").getMembershipFunction();
        // negotiatedRangeMembershipFunction = functionBlock.getVariable("negotiated_range").getLinguisticTerm("negotiated_range_value").getMembershipFunction();*/

        Variable serverRange = functionBlock.getVariable("server_range");
        Variable requestedRange = functionBlock.getVariable("requested_range");
        Variable negotiatedRange = functionBlock.getVariable("negotiated_range");
        Variable serverAndRequested = functionBlock.getVariable("server_and_requested");

        LinguisticTerm serverRangeValue = serverRange.getLinguisticTerm("server_range_value");
        LinguisticTerm requestedRangeValue = requestedRange.getLinguisticTerm("requested_range_value");
        LinguisticTerm negotiatedRangeValue = negotiatedRange.getLinguisticTerm("negotiated_range_value");
        LinguisticTerm serverValue = serverAndRequested.getLinguisticTerm("server_value");
        LinguisticTerm requestedValue = serverAndRequested.getLinguisticTerm("requested_value");

        requestedRangeMembershipFunction = requestedRangeValue.getMembershipFunction();

        Map<String, Double> negotiatedValues = new HashMap<String, Double>();
        //TODO: eventually to reintroduce variables for each of the 3 elements in the file and evaluate all of them at the same time.
        Collection<Core> cores = server.getAssociatedCPU().getAssociatedCore();
        Memory memory = server.getAssociatedMemory();
        Storage storage = server.getAssociatedStorage();
        RequestedTaskInfo requestedTaskInfo = task.getRequestedInfo();

        int requestedCores = requestedTaskInfo.getCores();

        for (Core core : cores) {

            int maxCPU = core.getMaxAcceptableValue();
            int usedCPU = core.getUsed();
            int totalCPU = core.getTotal();
            int minRequestedCPU = requestedTaskInfo.getCpuMinAcceptableValue();
            int maxRequestedCPU = requestedTaskInfo.getCpuMaxAcceptableValue();

            //TODO: if needed on the else branch the requested values can be negotiated in order to give more resources than needed to the task
            //negotiate CPU max optimum value
            if ((maxCPU - usedCPU < maxRequestedCPU && minRequestedCPU <= totalCPU - usedCPU) && (maxCPU < totalCPU)) {

                int[] values;
                int[] membership = new int[]{1, 0};
                int count = 0;
                Value[] serverValues;
                Value[] membershipValues;

                //convert available CPU ranges in Value type
                values = new int[]{maxCPU, totalCPU};
                count = values.length;
                serverValues = new Value[count];
                membershipValues = new Value[count];
                for (int i = 0; i < count; i++) {
                    serverValues[i] = new Value(values[i]);
                    membershipValues[i] = new Value(membership[i]);
                }

                //create a new membership function for the CPU
                MembershipFunction serverCpuMembershipFunction = new MembershipFunctionPieceWiseLinear(serverValues, membershipValues);

                serverRangeValue.setMembershipFunction(serverCpuMembershipFunction);


                if (totalCPU > usedCPU + maxRequestedCPU) {
                    negotiatedRangeValue.getMembershipFunction().setParameter(2, usedCPU + maxRequestedCPU);
                } else {
                    negotiatedRangeValue.getMembershipFunction().setParameter(2, totalCPU);
                }

                negotiatedRangeValue.getMembershipFunction().setParameter(0, usedCPU + minRequestedCPU);
                negotiatedRangeValue.getMembershipFunction().setParameter(1, 0);

                negotiatedRangeValue.getMembershipFunction().setParameter(3, 1);

                requestedRangeMembershipFunction.setParameter(0, usedCPU + minRequestedCPU);
                requestedRangeMembershipFunction.setParameter(2, usedCPU + maxRequestedCPU);

                serverValue.setMembershipFunction(serverCpuMembershipFunction);
                requestedValue.setMembershipFunction(requestedRangeMembershipFunction);

                //serverAndRequested.setValue();
                serverAndRequested.setUniverseMin(maxCPU);
                serverAndRequested.setUniverseMax(totalCPU);

                negotiatedRange.setUniverseMin(maxCPU);
                negotiatedRange.setUniverseMax(totalCPU);

                //After modifying the output variable membership function a new FIS has to be created in order to evaluate the rules properly
                //Otherwise the value from the FIS creation time is used for deffuzification of the output variable
                FIS finalFuzzyInferenceSystem = null;
                try {
                    finalFuzzyInferenceSystem = FIS.createFromString(fis.toString(), true);
                } catch (RecognitionException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    return null;
                }


                finalFuzzyInferenceSystem.setVariable("server_range", usedCPU + minRequestedCPU + 1);
                finalFuzzyInferenceSystem.setVariable("requested_range", usedCPU + minRequestedCPU + 1);

                finalFuzzyInferenceSystem.evaluate();

                finalFuzzyInferenceSystem.setVariable("server_and_requested", usedCPU + maxRequestedCPU - 1);
                finalFuzzyInferenceSystem.evaluate();

                double negotiatedCPU = finalFuzzyInferenceSystem.getFunctionBlock("negotiator").getVariable("negotiated_range").getValue();
                negotiatedValues.put(NEGOTIATED_CPU, negotiatedCPU - core.getUsed());
                System.out.println("Negotiated for " + core.getLocalName() + " from " + core.getMaxAcceptableValue() +
                        " to " + negotiatedCPU);

                core.setMaxAcceptableValue((int) finalFuzzyInferenceSystem.getFunctionBlock("negotiator").getVariable("negotiated_range").getValue());

                //only negotiate for requested number of cores
                requestedCores--;
                if (requestedCores == 0) {
                    break;
                }
            }
        }


        int maxMemory = memory.getMaxAcceptableValue();
        int usedMemory = memory.getUsed();
        int totalMemory = memory.getTotal();
        int maxRequestedMemory = requestedTaskInfo.getMemoryMaxAcceptableValue();
        int minRequestedMemory = requestedTaskInfo.getMemoryMinAcceptableValue();

        //negotiate Memory max optimum value
        if ((maxMemory - usedMemory < maxRequestedMemory && minRequestedMemory <= totalMemory - usedMemory) && (maxMemory < totalMemory)) {

            int[] values;
            int[] membership = new int[]{1, 0};
            int count = 0;
            Value[] serverValues;
            Value[] membershipValues;

            int negotiatedMemoryMaxRange = (totalMemory > usedMemory + maxRequestedMemory) ? usedMemory + maxRequestedMemory : totalMemory;

            //convert available memory ranges in Value type
            values = new int[]{maxMemory, negotiatedMemoryMaxRange};
            count = values.length;
            serverValues = new Value[count];
            membershipValues = new Value[count];
            for (int i = 0; i < count; i++) {
                serverValues[i] = new Value(values[i]);
                membershipValues[i] = new Value(membership[i]);
            }

            //create a new membership function
            MembershipFunction serverMemoryMembershipFunction = new MembershipFunctionPieceWiseLinear(serverValues, membershipValues);

            //set the new membership function for the server_memory variable
            serverRangeValue.setMembershipFunction(serverMemoryMembershipFunction);
            negotiatedRangeValue.setMembershipFunction(serverMemoryMembershipFunction);

            serverRange.setValue(maxMemory + 1);
            requestedRange.setValue(usedMemory + minRequestedMemory + 1);

            requestedRangeMembershipFunction.setParameter(0, usedMemory + minRequestedMemory);
            requestedRangeMembershipFunction.setParameter(2, usedMemory + maxRequestedMemory);

            negotiatedRange.setUniverseMin(maxMemory);
            negotiatedRange.setUniverseMax(totalMemory);
            serverAndRequested.setUniverseMin(maxMemory);
            serverAndRequested.setUniverseMax(totalMemory);

            //After modifying the output variable membership function a new FIS has to be created in order to evaluate the rules properly
            //Otherwise the value from the FIS creation time is used for deffuzification of the output variable
            FIS finalFuzzyInferenceSystem = null;
            try {
                finalFuzzyInferenceSystem = FIS.createFromString(fis.toString(), true);
            } catch (RecognitionException e) {
                for (int i = 0; i < count; i++) {
                    System.out.println("Server " + serverValues[i] + ", Membership " + membershipValues[i]);
                }
                System.err.println(e.getMessage());
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return null;
            }


            finalFuzzyInferenceSystem.setVariable("server_range", usedMemory + 1);
            finalFuzzyInferenceSystem.setVariable("requested_range", usedMemory + minRequestedMemory + 1);


            finalFuzzyInferenceSystem.evaluate();

            double negotiatedMemory = finalFuzzyInferenceSystem.getFunctionBlock("negotiator").getVariable("negotiated_range").getValue();
            negotiatedValues.put(NEGOTIATED_MEMORY, negotiatedMemory - memory.getUsed());
            System.out.println("Negotiated for " + memory.getLocalName() + " from " + memory.getMaxAcceptableValue() +
                    " to " + negotiatedMemory);

            memory.setMaxAcceptableValue((int) finalFuzzyInferenceSystem.getFunctionBlock("negotiator").getVariable("negotiated_range").getValue());

        }

        int maxStorage = storage.getMaxAcceptableValue();
        int usedStorage = storage.getUsed();
        int totalStorage = storage.getTotal();
        int minRequestedStorage = requestedTaskInfo.getStorageMinAcceptableValue();
        int maxRequestedStorage = requestedTaskInfo.getStorageMaxAcceptableValue();

        //negotiate Storage max optimum value

        if ((maxStorage - usedStorage < maxRequestedStorage && minRequestedStorage <= totalStorage - usedStorage) && (maxStorage < totalStorage)) {

            int[] values;
            int[] membership = new int[]{1, 0};
            int count = 0;
            Value[] serverValues;
            Value[] membershipValues;

            int negotiatedStorageMaxRange = (totalStorage > usedStorage + maxRequestedStorage) ? usedStorage + maxRequestedStorage : totalStorage;

            //convert available Storage ranges in Value type
            values = new int[]{maxStorage, negotiatedStorageMaxRange};
            count = values.length;
            serverValues = new Value[count];
            membershipValues = new Value[count];
            for (int i = 0; i < count; i++) {
                serverValues[i] = new Value(values[i]);
                membershipValues[i] = new Value(membership[i]);
            }

            //create a new membership function

            MembershipFunction serverStorageMembershipFunction = new MembershipFunctionPieceWiseLinear(serverValues, membershipValues);

            //set the new membership function for the server_storage variable
            serverRangeValue.setMembershipFunction(serverStorageMembershipFunction);
            negotiatedRangeValue.setMembershipFunction(serverStorageMembershipFunction);

            requestedRangeMembershipFunction.setParameter(0, usedStorage + minRequestedStorage);
            requestedRangeMembershipFunction.setParameter(2, usedStorage + maxRequestedStorage);

            negotiatedRange.setUniverseMin(maxStorage);
            negotiatedRange.setUniverseMax(totalStorage);

            serverAndRequested.setUniverseMin(maxStorage);
            serverAndRequested.setUniverseMax(totalStorage);

            //After modifying the output variable's membership function a new FIS has to be created in order to evaluate the rules properly
            //Otherwise the value from the FIS creation time is used for deffuzification of the output variable
            FIS finalFuzzyInferenceSystem = null;
            try {
                finalFuzzyInferenceSystem = FIS.createFromString(fis.toString(), true);
            } catch (RecognitionException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return null;
            }


            finalFuzzyInferenceSystem.setVariable("server_range", usedStorage + 1);
            finalFuzzyInferenceSystem.setVariable("requested_range", usedStorage + minRequestedStorage + 1);

            finalFuzzyInferenceSystem.evaluate();

            double negotiatedStorage = finalFuzzyInferenceSystem.getFunctionBlock("negotiator").getVariable("negotiated_range").getValue();
            negotiatedValues.put(NEGOTIATED_STORAGE, negotiatedStorage - storage.getUsed());
            System.out.println("Negotiated for " + storage.getLocalName() + " from " + storage.getMaxAcceptableValue() +
                    " to " + negotiatedStorage);
            storage.setMaxAcceptableValue((int) finalFuzzyInferenceSystem.getFunctionBlock("negotiator").getVariable("negotiated_range").getValue());
        }

        return negotiatedValues;
    }
}
