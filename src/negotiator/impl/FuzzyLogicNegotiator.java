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
import net.sourceforge.jFuzzyLogic.rule.Variable;

/**
 * @author Administrator
 */
public class FuzzyLogicNegotiator implements Negotiator {

    //TODO : Maybe remove variables in order to evaluate only what is needed. Currently FIS evaluates all three fields : storage memory cpu

    private FIS fis;
    private FunctionBlock functionBlock;
    private MembershipFunction taskCPUMembershipFunction;
    private MembershipFunction taskMemoryMembershipFunction;
    private MembershipFunction taskStorageMembershipFunction;

    private MembershipFunction negotiatedCPUMembershipFunction;
    private MembershipFunction negotiatedMemoryMembershipFunction;
    private MembershipFunction negotiatedStorageMembershipFunction;

    private Variable negotiatedCPU;
    private Variable negotiatedMemory;
    private Variable negotiatedStorage;


    protected FuzzyLogicNegotiator(String fuzzyControlLangageFile) {
        fis = FIS.load(fuzzyControlLangageFile, true);
        // Error while loading?
        if (fis == null) {
            System.err.println("FuzzyLogicNegotiator creation failed : Can't load file: '"
                    + fuzzyControlLangageFile + "'.");
            return;
        }

        functionBlock = fis.getFunctionBlock("negotiator");

        taskCPUMembershipFunction = functionBlock.getVariable("task_cpu").getLinguisticTerm("requested_cpu").getMembershipFunction();
        taskMemoryMembershipFunction = functionBlock.getVariable("task_memory").getLinguisticTerm("requested_memory").getMembershipFunction();
        taskStorageMembershipFunction = functionBlock.getVariable("task_storage").getLinguisticTerm("requested_storage").getMembershipFunction();

        negotiatedCPU = functionBlock.getVariable("negotiated_cpu");
        negotiatedMemory = functionBlock.getVariable("negotiated_memory");
        negotiatedStorage = functionBlock.getVariable("negotiated_storage");

        negotiatedCPUMembershipFunction = negotiatedCPU.getLinguisticTerm("negotiated_cpu_value").getMembershipFunction();
        negotiatedMemoryMembershipFunction = negotiatedMemory.getLinguisticTerm("negotiated_memory_value").getMembershipFunction();
        negotiatedStorageMembershipFunction = negotiatedStorage.getLinguisticTerm("negotiated_storage_value").getMembershipFunction();

    }

    /**
     * @param server
     * @param task
     * @return [negotiated CPU, negotiated Memory, negotiated Storage]
     */
    public double[] negotiate(Server server, Task task) {

        //TODO : de facut ceva cu asta k i garbadge : si de facut cumva sa stim k am indesat task ( negociat) k la move-deploy sa punem cum era sau nush. ceva de genu

        double[] result = new double[]{0, 0, 0};
        boolean cpuChanged = false;
        boolean memoryChanged = false;
        boolean storageChanged = false;


        Core core = (Core) server.getAssociatedCPU().getAssociatedCore().iterator().next();
        Memory memory = server.getAssociatedMemory();
        Storage storage = server.getAssociatedStorage();
        RequestedTaskInfo requestedTaskInfo = task.getRequestedInfo();

        int maxCPU = core.getMaxAcceptableValue();
        int usedCPU = core.getUsed();
        int totalCPU = core.getTotal();
        int minRequestedCPU = requestedTaskInfo.getCpuMinAcceptableValue();
        int maxRequestedCPU = requestedTaskInfo.getCpuMaxAcceptableValue();


        //negotiate CPU max optimum value
        if (maxCPU - usedCPU < minRequestedCPU && minRequestedCPU <= totalCPU - usedCPU) {
            cpuChanged = true;
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

            //set the new membership function for the server_cpu variable
            functionBlock.getVariable("server_cpu").getLinguisticTerm("available_cpu").setMembershipFunction(serverCpuMembershipFunction);
            negotiatedCPU.getLinguisticTerm("negotiated_cpu_value").setMembershipFunction(serverCpuMembershipFunction);

            taskCPUMembershipFunction.setParameter(0, usedCPU + minRequestedCPU);
            taskCPUMembershipFunction.setParameter(2, usedCPU + maxRequestedCPU);

            negotiatedCPU.setUniverseMin(maxCPU);
            negotiatedCPU.setUniverseMax(totalCPU);

            fis.getVariable("server_cpu").setValue(maxCPU + 1);
            fis.getVariable("task_cpu").setValue(usedCPU + minRequestedCPU + 1);


        }

        int maxMemory = memory.getMaxAcceptableValue();
        int minMemory = memory.getMinAcceptableValue();
        int usedMemory = memory.getUsed();
        int totalMemory = memory.getTotal();
        int maxRequestedMemory = requestedTaskInfo.getMemoryMaxAcceptableValue();
        int minRequestedMemory = requestedTaskInfo.getMemoryMinAcceptableValue();


        //negotiate Memory max optimum value
        if (maxMemory - usedMemory < minRequestedMemory && minRequestedMemory <= totalMemory - usedMemory) {

            memoryChanged = true;

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
            functionBlock.getVariable("server_memory").getLinguisticTerm("available_memory").setMembershipFunction(serverMemoryMembershipFunction);
            // negotiatedMemory.getLinguisticTerm("negotiated_memory_value").setMembershipFunction(serverMemoryMembershipFunction);

            fis.getVariable("server_memory").setValue(maxMemory + 1);
            fis.getVariable("task_memory").setValue(usedMemory + minRequestedMemory + 1);

            taskMemoryMembershipFunction.setParameter(0, usedMemory + minRequestedMemory);
            taskMemoryMembershipFunction.setParameter(2, usedMemory + maxRequestedMemory);

            negotiatedMemory.setUniverseMin(maxMemory);
            negotiatedMemory.setUniverseMax(totalMemory);

            negotiatedMemoryMembershipFunction.setParameter(0, minMemory + 1);
            negotiatedMemoryMembershipFunction.setParameter(2, totalMemory - 1);


        }

        int maxStorage = storage.getMaxAcceptableValue();
        int minStorage = storage.getMinAcceptableValue();
        int usedStorage = storage.getUsed();
        int totalStorage = storage.getTotal();
        int minRequestedStorage = requestedTaskInfo.getStorageMinAcceptableValue();
        int maxRequestedStorage = requestedTaskInfo.getStorageMaxAcceptableValue();

        //negotiate Storage max optimum value
        if (maxStorage - usedStorage < minRequestedStorage && minRequestedStorage <= totalStorage - usedStorage) {

            storageChanged = true;

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
            functionBlock.getVariable("server_storage").getLinguisticTerm("available_storage").setMembershipFunction(serverStorageMembershipFunction);
            negotiatedStorage.getLinguisticTerm("negotiated_storage_value").setMembershipFunction(serverStorageMembershipFunction);

            fis.getVariable("server_storage").setValue(maxStorage + 1);
            fis.getVariable("task_storage").setValue(usedStorage + minRequestedStorage + 1);

            taskStorageMembershipFunction.setParameter(0, usedStorage + minRequestedStorage);
            taskStorageMembershipFunction.setParameter(2, usedStorage + maxRequestedStorage);

            negotiatedStorage.setUniverseMin(maxStorage);
            negotiatedStorage.setUniverseMax(totalStorage);

            negotiatedStorageMembershipFunction.setParameter(0, maxStorage + 1);
            negotiatedStorageMembershipFunction.setParameter(2, totalStorage - 1);

        }


        fis.evaluate();
        //System.out.println(fis);
        // fis.chart();


        if (cpuChanged) {
            result[0] = fis.getVariable("negotiated_cpu").getValue();
        }
        if (memoryChanged) {
            result[1] = fis.getVariable("negotiated_memory").getValue();
        }
        if (storageChanged) {
            result[2] = fis.getVariable("negotiated_storage").getValue();
        }


        //throw new UnsupportedOperationException("Not supported yet.");

        return result;
    }


}
