/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negotiator.impl;

import greenContextOntology.*;
import negotiator.Negotiator;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionPieceWiseLinear;
import net.sourceforge.jFuzzyLogic.membership.Value;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import org.antlr.runtime.RecognitionException;

import java.util.Collection;

/**
 * @author Administrator
 */
public class FuzzyLogicNegotiator implements Negotiator {

    private FIS fis;
    private FunctionBlock functionBlock;

    private MembershipFunction requestedRangeMembershipFunction;

    /*
  private MembershipFunction serverRangeMembershipFunction;
  private MembershipFunction negotiatedRangeMembershipFunction;*/

    private Variable serverRange;
    private Variable requestedRange;
    private Variable negotiatedRange;

    private Variable serverAndRequested;

    private LinguisticTerm serverRangeValue;
    private LinguisticTerm requestedRangeValue;
    private LinguisticTerm negotiatedRangeValue;

    private LinguisticTerm serverValue;
    private LinguisticTerm requestedValue;

    protected FuzzyLogicNegotiator(String fuzzyControlLanguageFile) {

        try {
            fis = FIS.load(fuzzyControlLanguageFile, true);
        } catch (RuntimeException e) {

            System.err.println("FuzzyLogicNegotiator creation failed : Can't load file: '"
                    + fuzzyControlLanguageFile + "'.");
            e.printStackTrace();
            return;
        }
        // Error while loading?
        if (fis == null) {
            System.err.println("FuzzyLogicNegotiator creation failed : Can't load file: '"
                    + fuzzyControlLanguageFile + "'.");
            return;
        }

        functionBlock = fis.getFunctionBlock("negotiator");


        requestedRangeMembershipFunction = functionBlock.getVariable("requested_range").getLinguisticTerm("requested_range_value").getMembershipFunction();
        // serverRangeMembershipFunction = functionBlock.getVariable("server_range").getLinguisticTerm("server_range_value").getMembershipFunction();
        // negotiatedRangeMembershipFunction = functionBlock.getVariable("negotiated_range").getLinguisticTerm("negotiated_range_value").getMembershipFunction();*/

        serverRange = functionBlock.getVariable("server_range");
        requestedRange = functionBlock.getVariable("requested_range");
        negotiatedRange = functionBlock.getVariable("negotiated_range");
        serverAndRequested = functionBlock.getVariable("server_and_requested");

        serverRangeValue = serverRange.getLinguisticTerm("server_range_value");
        requestedRangeValue = requestedRange.getLinguisticTerm("requested_range_value");
        negotiatedRangeValue = negotiatedRange.getLinguisticTerm("negotiated_range_value");
        serverValue = serverAndRequested.getLinguisticTerm("server_value");
        requestedValue = serverAndRequested.getLinguisticTerm("requested_value");

        requestedRangeMembershipFunction = requestedRangeValue.getMembershipFunction();

    }

    /**
     * @param server
     * @param task
     * @return [negotiated CPU, negotiated Memory, negotiated Storage]
     */

    public void negotiate(Server server, Task task) {

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
            if ((maxCPU - usedCPU < minRequestedCPU && minRequestedCPU <= totalCPU - usedCPU) && (maxCPU < totalCPU)) {

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
                    //System.out.println(serverValues[i] + ", " + membershipValues[i]);
                }

                //create a new membership function for the CPU
                MembershipFunction serverCpuMembershipFunction = new MembershipFunctionPieceWiseLinear(serverValues, membershipValues);

                serverRangeValue.setMembershipFunction(serverCpuMembershipFunction);
                negotiatedRangeValue.getMembershipFunction().setParameter(0, usedCPU + minRequestedCPU);
                negotiatedRangeValue.getMembershipFunction().setParameter(1, 0);
                negotiatedRangeValue.getMembershipFunction().setParameter(2, usedCPU + maxRequestedCPU);
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
                    return;
                }


                finalFuzzyInferenceSystem.setVariable("server_range", usedCPU + minRequestedCPU + 1);
                finalFuzzyInferenceSystem.setVariable("requested_range", usedCPU + minRequestedCPU + 1);
                //JDialogFis jDialogFis = new JDialogFis(finalFuzzyInferenceSystem);
                //jDialogFis.setSize(800, 600);

                finalFuzzyInferenceSystem.evaluate();

                finalFuzzyInferenceSystem.setVariable("server_and_requested", usedCPU + maxRequestedCPU - 1);
                finalFuzzyInferenceSystem.evaluate();
                //jDialogFis.repaint();

                //System.out.println(finalFuzzyInferenceSystem);
                //finalFuzzyInferenceSystem.chart();
                //jDialogFis.repaint();

                System.out.println("Negotiated for " + core.getLocalName() + " from " + core.getMaxAcceptableValue() +
                        " to " + finalFuzzyInferenceSystem.getFunctionBlock("negotiator").getVariable("negotiated_range").getValue());

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
        if ((maxMemory - usedMemory < minRequestedMemory && minRequestedMemory <= totalMemory - usedMemory) && (maxMemory < totalMemory)) {

            int[] values;
            int[] membership = new int[]{1, 0};
            int count = 0;
            Value[] serverValues;
            Value[] membershipValues;
            //convert available memory ranges in Value type
            values = new int[]{maxMemory, totalMemory};
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
                return;
            }


            finalFuzzyInferenceSystem.setVariable("server_range", usedMemory + 1);
            finalFuzzyInferenceSystem.setVariable("requested_range", usedMemory + minRequestedMemory + 1);


            finalFuzzyInferenceSystem.evaluate();

            //System.out.println(finalFuzzyInferenceSystem);
            //finalFuzzyInferenceSystem.chart();


            System.out.println("Negotiated for " + memory.getLocalName() + " from " + memory.getMaxAcceptableValue() +
                    " to " + finalFuzzyInferenceSystem.getFunctionBlock("negotiator").getVariable("negotiated_range").getValue());

            memory.setMaxAcceptableValue((int) finalFuzzyInferenceSystem.getFunctionBlock("negotiator").getVariable("negotiated_range").getValue());

        }

        int maxStorage = storage.getMaxAcceptableValue();
        int usedStorage = storage.getUsed();
        int totalStorage = storage.getTotal();
        int minRequestedStorage = requestedTaskInfo.getStorageMinAcceptableValue();
        int maxRequestedStorage = requestedTaskInfo.getStorageMaxAcceptableValue();

        //negotiate Storage max optimum value

        if ((maxStorage - usedStorage < minRequestedStorage && minRequestedStorage <= totalStorage - usedStorage) && (maxStorage < totalStorage)) {

            int[] values;
            int[] membership = new int[]{1, 0};
            int count = 0;
            Value[] serverValues;
            Value[] membershipValues;
            //convert available Storage ranges in Value type
            values = new int[]{maxStorage, totalStorage};
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

            //After modifying the output variable's membership function a new FIS has to be created in order to evaluate the rules properly
            //Otherwise the value from the FIS creation time is used for deffuzification of the output variable
            FIS finalFuzzyInferenceSystem = null;
            try {
                finalFuzzyInferenceSystem = FIS.createFromString(fis.toString(), true);
            } catch (RecognitionException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return;
            }


            finalFuzzyInferenceSystem.setVariable("server_range", usedStorage + 1);
            finalFuzzyInferenceSystem.setVariable("requested_range", usedStorage + minRequestedStorage + 1);


            finalFuzzyInferenceSystem.evaluate();
            //System.out.println(finalFuzzyInferenceSystem);
            //finalFuzzyInferenceSystem.chart();


            System.out.println("Negotiated for " + storage.getLocalName() + " from " + storage.getMaxAcceptableValue() +
                    " to " + finalFuzzyInferenceSystem.getFunctionBlock("negotiator").getVariable("negotiated_range").getValue());
            storage.setMaxAcceptableValue((int) finalFuzzyInferenceSystem.getFunctionBlock("negotiator").getVariable("negotiated_range").getValue());

        }

    }

}
