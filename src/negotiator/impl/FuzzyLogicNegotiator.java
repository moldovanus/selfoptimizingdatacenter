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

/**
 *
 * @author Administrator
 */
public class FuzzyLogicNegotiator implements Negotiator {

    private FIS fis;
    private FunctionBlock functionBlock;
    private MembershipFunction serverCPUMembershipFunction;
    private MembershipFunction serverMemoryMembershipFunction;
    private MembershipFunction serverStorageMembershipFunction;
    private MembershipFunction taskCPUMembershipFunction;
    private MembershipFunction taskMemoryMembershipFunction;
    private MembershipFunction taskStorageMembershipFunction;
    private MembershipFunction negotiatedCPUMembershipFunction;
    private MembershipFunction negotiatedMemoryMembershipFunction;
    private MembershipFunction negotiatedStorageMembershipFunction;

    public FuzzyLogicNegotiator(String fuzzyControlLangageFile) {
        fis = FIS.load(fuzzyControlLangageFile, true);
        // Error while loading?
        if (fis == null) {
            System.err.println("Can't load file: '"
                    + fuzzyControlLangageFile + "'");
            return;
        }

        functionBlock = fis.getFunctionBlock("negotiator");

        serverCPUMembershipFunction = functionBlock.getVariable("server_cpu").getLinguisticTerm("available").getMembershipFunction();
        serverMemoryMembershipFunction = functionBlock.getVariable("server_memory").getLinguisticTerm("available").getMembershipFunction();
        serverStorageMembershipFunction = functionBlock.getVariable("server_storage").getLinguisticTerm("available").getMembershipFunction();

        taskCPUMembershipFunction = functionBlock.getVariable("task_cpu").getLinguisticTerm("available").getMembershipFunction();
        taskMemoryMembershipFunction = functionBlock.getVariable("task_memory").getLinguisticTerm("available").getMembershipFunction();
        taskStorageMembershipFunction = functionBlock.getVariable("task_storage").getLinguisticTerm("available").getMembershipFunction();

        negotiatedCPUMembershipFunction = functionBlock.getVariable("negotiated_cpu").getLinguisticTerm("available").getMembershipFunction();
        negotiatedMemoryMembershipFunction = functionBlock.getVariable("negotiated_memory").getLinguisticTerm("available").getMembershipFunction();
        negotiatedStorageMembershipFunction = functionBlock.getVariable("server_storage").getLinguisticTerm("available").getMembershipFunction();


    }
    public double[] negotiate(Server server, Task task) {

        Core core = (Core) server.getAssociatedCPU().getAssociatedCore().iterator().next();
        Memory memory = server.getAssociatedMemory();
        Storage storage = server.getAssociatedStorage();
        
        int optimumCPU = core.getMaxAcceptableValue();
        int usedCPU = core.getUsed();
        int totalCPU = core.getTotal();

        serverCPUMembershipFunction.setParameter(2, optimumCPU - usedCPU);
        serverCPUMembershipFunction.setParameter(3, totalCPU - usedCPU);

        int optimumMemory = memory.getMaxAcceptableValue();
        int usedMemory = memory.getUsed();
        int totalMemory = memory.getTotal();

        serverMemoryMembershipFunction.setParameter(2, optimumMemory - usedMemory);
        serverMemoryMembershipFunction.setParameter(3, totalMemory - usedMemory);

        int optimumStorage = storage.getMaxAcceptableValue();
        int usedStorage = storage.getUsed();
        int totalStorage = storage.getTotal();

        serverStorageMembershipFunction.setParameter(2, optimumStorage - usedStorage);
        serverStorageMembershipFunction.setParameter(3, totalStorage - usedStorage);

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

        throw new UnsupportedOperationException("Not supported yet.");
    }
}
