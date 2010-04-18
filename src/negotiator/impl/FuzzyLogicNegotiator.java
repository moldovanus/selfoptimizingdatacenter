/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negotiator.impl;

import greenContextOntology.Core;
import greenContextOntology.Memory;
import greenContextOntology.RequestedTaskInfo;
import greenContextOntology.Server;
import greenContextOntology.Storage;
import greenContextOntology.Task;
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
        int minCPU = core.getMinAcceptableValue();
        int usedCPU = core.getUsed();
        int totalCPU = core.getTotal();


        if (usedCPU < minCPU) {

            cpuChanged = true;

            int[] values;
            int[] membership = new int[]{0, 1, 1, 0};
            int count = 0;
            Value[] serverValues;
            Value[] membershipValues;


            //convert available CPU ranges in Value type
            values = new int[]{0, minCPU - usedCPU, maxCPU - usedCPU, totalCPU - usedCPU};
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

            fis.getVariable("server_cpu").setValue(minCPU - usedCPU - 1);
            fis.getVariable("task_cpu").setValue(requestedTaskInfo.getCpuMinAcceptableValue() + 1);

            taskCPUMembershipFunction.setParameter(0, requestedTaskInfo.getCpuMinAcceptableValue());
            taskCPUMembershipFunction.setParameter(2, requestedTaskInfo.getCpuMaxAcceptableValue());

            negotiatedCPU.setUniverseMin(minCPU - usedCPU);
            negotiatedCPU.setUniverseMax(totalCPU - usedCPU);


        } else if (usedCPU > maxCPU) {

        }

        int maxMemory = memory.getMaxAcceptableValue();
        int minMemory = memory.getMinAcceptableValue();
        int usedMemory = memory.getUsed();
        int totalMemory = memory.getTotal();

        if (usedMemory < minMemory) {

            memoryChanged = true;

            int[] values;
            int[] membership = new int[]{ 1, 1, 0};
            int count = 0;
            Value[] serverValues;
            Value[] membershipValues;
            //convert available memory ranges in Value type
            values = new int[]{minMemory - usedMemory, maxMemory - usedMemory, totalMemory - usedMemory};
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

            fis.getVariable("server_memory").setValue(minMemory - usedMemory - 1);
            fis.getVariable("task_memory").setValue(requestedTaskInfo.getMemoryMinAcceptableValue() + 1);

            taskMemoryMembershipFunction.setParameter(0, requestedTaskInfo.getMemoryMinAcceptableValue());
            taskMemoryMembershipFunction.setParameter(2, requestedTaskInfo.getMemoryMaxAcceptableValue());

            negotiatedMemory.setUniverseMin(minMemory - usedMemory);
            negotiatedMemory.setUniverseMax(totalMemory - usedMemory);
            negotiatedMemoryMembershipFunction.setParameter(0, minMemory - usedMemory);
            negotiatedMemoryMembershipFunction.setParameter(2, totalMemory - usedMemory);


        } else if (usedMemory > maxMemory) {

        }

        int maxStorage = storage.getMaxAcceptableValue();
        int minStorage = storage.getMinAcceptableValue();
        int usedStorage = storage.getUsed();
        int totalStorage = storage.getTotal();

        if (usedStorage < minStorage) {

            storageChanged = true;

            int[] values;
            int[] membership = new int[]{0, 1, 1, 0};
            int count = 0;
            Value[] serverValues;
            Value[] membershipValues;
            //convert available Storage ranges in Value type
            values = new int[]{0, minStorage - usedStorage, maxStorage - usedStorage, totalStorage - usedStorage};
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

            fis.getVariable("server_storage").setValue(minStorage - usedStorage - 1);
            fis.getVariable("task_storage").setValue(requestedTaskInfo.getStorageMinAcceptableValue() + 1);

            taskStorageMembershipFunction.setParameter(0, requestedTaskInfo.getStorageMinAcceptableValue());
            taskStorageMembershipFunction.setParameter(2, requestedTaskInfo.getStorageMaxAcceptableValue());

            negotiatedStorage.setUniverseMin(minStorage - usedStorage);
            negotiatedStorage.setUniverseMax(totalStorage - usedStorage);


        } else if (usedStorage > maxStorage) {

        }


/*
negotiatedCPUMembershipFunction.setParameter(0, optimumCPU - usedCPU);
negotiatedCPUMembershipFunction.setParameter(2, totalCPU - usedCPU);*/
        /*negotiatedCPU.setUniverseMin(minCPU - usedCPU);
        negotiatedCPU.setUniverseMax(totalCPU - usedCPU);*/


        /*   negotiatedStorageMembershipFunction.setParameter(0, optimumStorage - usedStorage);
     negotiatedStorageMembershipFunction.setParameter(2, totalStorage - usedStorage);*/


        fis.evaluate();
        //System.out.println(fis);
        fis.chart();


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

    /**
     * Used for negotiating separated ranges.
     * Example : if I have a CPU which can give 1600-1800, one  2400-2600  we have :
     * - values     = [1599,1600,1800,1801,2399,2400,2600,2601]
     * - membership = [ 0  , 1  , 1  , 0  ,0   , 1  , 1  , 0  ]
     *
     * @param values     values for the cpu
     * @param membership 0 or 1 meaning 0 cannot give this amount 1 meaning can give
     * @param task
     * @return
     */
    public double negotiateCPU(int[] values, int[] membership, Task task) {

        int count = values.length;
        Value[] cpuValues = new Value[count];
        Value[] membershipValues = new Value[count];
        for (int i = 0; i < count; i++) {
            cpuValues[i] = new Value(values[i]);
            membershipValues[i] = new Value(membership[i]);
        }

        //create a new membership function for the CPU
        MembershipFunction membershipFunction = new MembershipFunctionPieceWiseLinear(cpuValues, membershipValues);

        //set the new membership function for the server_cpu variable
        functionBlock.getVariable("server_cpu").getLinguisticTerm("available_cpu").setMembershipFunction(membershipFunction);
        //do the same for negotiated field in order to limit the result to what the server is capable of giving
        functionBlock.getVariable("negotiated_cpu").getLinguisticTerm("negotiated_cpu_value").setMembershipFunction(membershipFunction);

        RequestedTaskInfo requestedTaskInfo = task.getRequestedInfo();
        taskCPUMembershipFunction.setParameter(0, requestedTaskInfo.getCpuMinAcceptableValue());
        taskCPUMembershipFunction.setParameter(2, requestedTaskInfo.getCpuMaxAcceptableValue());

        fis.evaluate();

        double result;

        result = fis.getVariable("negotiated_cpu").getValue();

        return result;
    }

    /**
     * Used for negotiating separated ranges.
     * Example : if I have a Memory which can give 1600-1800, one  2400-2600  we have :
     * - values     = [1599,1600,1800,1801,2399,2400,2600,2601]
     * - membership = [ 0  , 1  , 1  , 0  ,0   , 1  , 1  , 0  ]
     *
     * @param values     values for the memory
     * @param membership 0 or 1 meaning 0 cannot give this amount 1 meaning can give
     * @param task
     * @return
     */
    public double negotiateMemory(int[] values, int[] membership, Task task) {

        int count = values.length;
        Value[] cpuValues = new Value[count];
        Value[] membershipValues = new Value[count];
        for (int i = 0; i < count; i++) {
            cpuValues[i] = new Value(values[i]);
            membershipValues[i] = new Value(membership[i]);
        }

        //create a new membership function
        MembershipFunction membershipFunction = new MembershipFunctionPieceWiseLinear(cpuValues, membershipValues);

        //set the new membership function for the server_memory variable
        functionBlock.getVariable("server_memory").getLinguisticTerm("available_memory").setMembershipFunction(membershipFunction);
        //do the same for negotiated field in order to limit the result to what the server is capable of giving
        functionBlock.getVariable("negotiated_memory").getLinguisticTerm("negotiated_memory_value").setMembershipFunction(membershipFunction);

        RequestedTaskInfo requestedTaskInfo = task.getRequestedInfo();
        taskMemoryMembershipFunction.setParameter(0, requestedTaskInfo.getMemoryMinAcceptableValue());
        taskMemoryMembershipFunction.setParameter(2, requestedTaskInfo.getMemoryMaxAcceptableValue());

        fis.evaluate();

        double result;

        result = fis.getVariable("negotiated_memory").getValue();

        return result;
    }

    /**
     * Used for negotiating separated ranges.
     * Example : if I have a Storage which can give 1600-1800, one  2400-2600  we have :
     * - values     = [1599,1600,1800,1801,2399,2400,2600,2601]
     * - membership = [ 0  , 1  , 1  , 0  ,0   , 1  , 1  , 0  ]
     *
     * @param values     values for the storage
     * @param membership 0 or 1 meaning 0 cannot give this amount 1 meaning can give
     * @param task
     * @return
     */
    public double negotiateStorage(int[] values, int[] membership, Task task) {

        int count = values.length;
        Value[] cpuValues = new Value[count];
        Value[] membershipValues = new Value[count];
        for (int i = 0; i < count; i++) {
            cpuValues[i] = new Value(values[i]);
            membershipValues[i] = new Value(membership[i]);
        }

        //create a new membership function
        MembershipFunction membershipFunction = new MembershipFunctionPieceWiseLinear(cpuValues, membershipValues);

        //set the new membership function for the server_storage variable
        functionBlock.getVariable("server_storage").getLinguisticTerm("available_storage").setMembershipFunction(membershipFunction);
        //do the same for negotiated field in order to limit the result to what the server is capable of giving
        functionBlock.getVariable("negotiated_storage").getLinguisticTerm("negotiated_storage_value").setMembershipFunction(membershipFunction);

        RequestedTaskInfo requestedTaskInfo = task.getRequestedInfo();
        taskStorageMembershipFunction.setParameter(0, requestedTaskInfo.getStorageMinAcceptableValue());
        taskStorageMembershipFunction.setParameter(2, requestedTaskInfo.getStorageMaxAcceptableValue());

        fis.evaluate();

        double result;

        result = fis.getVariable("negotiated_storage").getValue();

        return result;
    }


}
