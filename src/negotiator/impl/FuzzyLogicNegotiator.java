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

/**
 * @author Administrator
 */
public class FuzzyLogicNegotiator implements Negotiator {

    //TODO : Maybe remove variables in order to evaluate only what is needed. Currently FIS evaluates all three fields : storage memory cpu

    private FIS fis;
    private FunctionBlock functionBlock;
    private MembershipFunction serverCPUMembershipFunction;
    private MembershipFunction taskCPUMembershipFunction;
    private MembershipFunction taskMemoryMembershipFunction;
    private MembershipFunction taskStorageMembershipFunction;

    protected FuzzyLogicNegotiator(String fuzzyControlLangageFile) {
        fis = FIS.load(fuzzyControlLangageFile, true);
        // Error while loading?
        if (fis == null) {
            System.err.println("FuzzyLogicNegotiator creation failed : Can't load file: '"
                    + fuzzyControlLangageFile + "'.");
            return;
        }

        functionBlock = fis.getFunctionBlock("negotiator");

        serverCPUMembershipFunction = functionBlock.getVariable("server_cpu").getLinguisticTerm("available").getMembershipFunction();

        taskCPUMembershipFunction = functionBlock.getVariable("task_cpu").getLinguisticTerm("requested").getMembershipFunction();
        taskMemoryMembershipFunction = functionBlock.getVariable("task_memory").getLinguisticTerm("requested").getMembershipFunction();
        taskStorageMembershipFunction = functionBlock.getVariable("task_storage").getLinguisticTerm("requested").getMembershipFunction();
    }

    /**
     *
     * @param server
     * @param task
     * @return  [negotiated CPU, negotiated Memory, negotiated Storage]
     */
    public double[] negotiate(Server server, Task task) {

        Core core = (Core) server.getAssociatedCPU().getAssociatedCore().iterator().next();
        Memory memory = server.getAssociatedMemory();
        Storage storage = server.getAssociatedStorage();

        int[] values;
        int[] membership = new int[]{0, 1, 0};
        int count = 0;
        Value[] serverValues;
        Value[] membershipValues;

        int optimumCPU = core.getMaxAcceptableValue();
        int usedCPU = core.getUsed();
        int totalCPU = core.getTotal();

        serverCPUMembershipFunction.setParameter(2, optimumCPU - usedCPU);
        serverCPUMembershipFunction.setParameter(3, totalCPU - usedCPU);

        //convert available CPU ranges in Value type
        values = new int[]{0, optimumCPU - usedCPU, totalCPU - usedCPU};
        count = values.length;
        serverValues = new Value[count];
        membershipValues = new Value[count];
        for (int i = 0; i < count; i++) {
            serverValues[i] = new Value(values[i]);
            membershipValues[i] = new Value(membership[i]);
        }
        //create a new membership function for the CPU
        MembershipFunction serverCpuMembershipFunction = new MembershipFunctionPieceWiseLinear(serverValues, membershipValues);

        //set the new membership function for the server_cpu variable
        functionBlock.getVariable("server_cpu").getLinguisticTerm("available").setMembershipFunction(serverCpuMembershipFunction);
        //do the same for negotiated field in order to limit the result to what the server is capable of giving
        functionBlock.getVariable("negotiated_cpu").getLinguisticTerm("negotiated").setMembershipFunction(serverCpuMembershipFunction);

        int optimumMemory = memory.getMaxAcceptableValue();
        int usedMemory = memory.getUsed();
        int totalMemory = memory.getTotal();

        //convert available memory ranges in Value type
        values = new int[]{0, optimumMemory - usedMemory, totalMemory - usedMemory};
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
        functionBlock.getVariable("server_memory").getLinguisticTerm("available").setMembershipFunction(serverMemoryMembershipFunction);
         //do the same for negotiated field in order to limit the result to what the server is capable of giving
        functionBlock.getVariable("negotiated_memory").getLinguisticTerm("negotiated").setMembershipFunction(serverCpuMembershipFunction);

        int optimumStorage = storage.getMaxAcceptableValue();
        int usedStorage = storage.getUsed();
        int totalStorage = storage.getTotal();

        //convert available Storage ranges in Value type
        values = new int[]{0, optimumStorage - usedStorage, totalStorage - usedStorage};
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
        functionBlock.getVariable("server_storage").getLinguisticTerm("available").setMembershipFunction(serverMemoryMembershipFunction);
         //do the same for negotiated field in order to limit the result to what the server is capable of giving
        functionBlock.getVariable("negotiated_storage").getLinguisticTerm("negotiated").setMembershipFunction(serverCpuMembershipFunction);

        RequestedTaskInfo requestedTaskInfo = task.getRequestedInfo();
        taskCPUMembershipFunction.setParameter(0, requestedTaskInfo.getCpuMinAcceptableValue());
        taskCPUMembershipFunction.setParameter(2, requestedTaskInfo.getCpuMaxAcceptableValue());

        taskMemoryMembershipFunction.setParameter(0, requestedTaskInfo.getMemoryMinAcceptableValue());
        taskMemoryMembershipFunction.setParameter(2, requestedTaskInfo.getMemoryMaxAcceptableValue());

        taskStorageMembershipFunction.setParameter(0, requestedTaskInfo.getStorageMinAcceptableValue());
        taskStorageMembershipFunction.setParameter(2, requestedTaskInfo.getStorageMaxAcceptableValue());

        fis.evaluate();

        double[] result = new double[3];

        result[0] = fis.getVariable("negotiated_cpu").getValue();
        result[1] = fis.getVariable("negotiated_memory").getValue();
        result[2] = fis.getVariable("negotiated_storage").getValue();


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
        functionBlock.getVariable("server_cpu").getLinguisticTerm("available").setMembershipFunction(membershipFunction);
         //do the same for negotiated field in order to limit the result to what the server is capable of giving
        functionBlock.getVariable("negotiated_cpu").getLinguisticTerm("negotiated").setMembershipFunction(membershipFunction);

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
        functionBlock.getVariable("server_memory").getLinguisticTerm("available").setMembershipFunction(membershipFunction);
          //do the same for negotiated field in order to limit the result to what the server is capable of giving
        functionBlock.getVariable("negotiated_memory").getLinguisticTerm("negotiated").setMembershipFunction(membershipFunction);

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
        functionBlock.getVariable("server_storage").getLinguisticTerm("available").setMembershipFunction(membershipFunction);
         //do the same for negotiated field in order to limit the result to what the server is capable of giving
        functionBlock.getVariable("negotiated_storage").getLinguisticTerm("negotiated").setMembershipFunction(membershipFunction);

        RequestedTaskInfo requestedTaskInfo = task.getRequestedInfo();
        taskStorageMembershipFunction.setParameter(0, requestedTaskInfo.getStorageMinAcceptableValue());
        taskStorageMembershipFunction.setParameter(2, requestedTaskInfo.getStorageMaxAcceptableValue());

        fis.evaluate();

        double result;

        result = fis.getVariable("negotiated_storage").getValue();

        return result;
    }


}
