package contextawaremodel.worldInterface.dtos;

import greenContextOntology.ReceivedTaskInfo;
import greenContextOntology.RequestedTaskInfo;
import greenContextOntology.Task;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: GEORGY
 * Date: May 12, 2010
 * Time: 11:44:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class TaskDto implements Serializable {
    private String taskName;
    private int requestedCores;
    private int requestedCPUMax;
    private int requestedCPUMin;
    private int requestedMemoryMax;
    private int requestedMemoryMin;
    private int requestedStorageMax;
    private int requestedStorageMin;

    private int receivedCores;
    private int receivedCPU;
    private int receivedMemory;
    private int receivedStorage;

    private boolean isRunning;

    @Override
    public boolean equals(Object task) {
        if (this == task) return true;
        if (task instanceof Task) {
            Task t = (Task) task;
            RequestedTaskInfo rti = t.getRequestedInfo();
            if (requestedCores != rti.getCores()) return false;
            if (requestedCPUMax != rti.getCpuMaxAcceptableValue()) return false;
            if (requestedCPUMin != rti.getCpuMinAcceptableValue()) return false;
            if (requestedMemoryMax != rti.getMemoryMaxAcceptableValue()) return false;
            if (requestedMemoryMin != rti.getMemoryMinAcceptableValue()) return false;
            if (requestedStorageMax != rti.getStorageMaxAcceptableValue()) return false;
            if (requestedStorageMin != rti.getStorageMinAcceptableValue()) return false;

            ReceivedTaskInfo rci = t.getReceivedInfo();
            if (receivedCores != rci.getCores()) return false;
            if (receivedCPU != rci.getCpuReceived()) return false;
            if (receivedMemory != rci.getMemoryReceived()) return false;
            if (receivedStorage != rci.getStorageReceived()) return false;
            if (t.isRunning() == isRunning()) return false;
        }
        if (task instanceof TaskDto) {
            TaskDto taskDto = (TaskDto) task;
            if (requestedCores != taskDto.getRequestedCores()) return false;
            if (requestedCPUMax != taskDto.getRequestedCPUMax()) return false;
            if (requestedCPUMin != taskDto.getRequestedCPUMin()) return false;
            if (requestedMemoryMax != taskDto.getRequestedMemoryMax()) return false;
            if (requestedMemoryMin != taskDto.getRequestedMemoryMin()) return false;
            if (requestedStorageMax != taskDto.getRequestedStorageMax()) return false;
            if (requestedStorageMin != taskDto.getRequestedMemoryMin()) return false;


            if (receivedCores != taskDto.getReceivedCores()) return false;
            if (receivedCPU != taskDto.getReceivedCPU()) return false;
            if (receivedMemory != taskDto.getReceivedMemory()) return false;
            if (receivedStorage != taskDto.getReceivedStorage()) return false;
            if (taskDto.isRunning() == isRunning()) return false;
        }

        return true;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getRequestedCores() {
        return requestedCores;
    }

    public void setRequestedCores(int requestedCores) {
        this.requestedCores = requestedCores;
    }

    public int getRequestedCPUMax() {
        return requestedCPUMax;
    }

    public void setRequestedCPUMax(int requestedCPUMax) {
        this.requestedCPUMax = requestedCPUMax;
    }

    public int getRequestedCPUMin() {
        return requestedCPUMin;
    }

    public void setRequestedCPUMin(int requestedCPUMin) {
        this.requestedCPUMin = requestedCPUMin;
    }

    public int getRequestedMemoryMax() {
        return requestedMemoryMax;
    }

    public void setRequestedMemoryMax(int requestedMemoryMax) {
        this.requestedMemoryMax = requestedMemoryMax;
    }

    public int getRequestedMemoryMin() {
        return requestedMemoryMin;
    }

    public void setRequestedMemoryMin(int requestedMemoryMin) {
        this.requestedMemoryMin = requestedMemoryMin;
    }

    public int getRequestedStorageMax() {
        return requestedStorageMax;
    }

    public void setRequestedStorageMax(int requestedStorageMax) {
        this.requestedStorageMax = requestedStorageMax;
    }

    public int getRequestedStorageMin() {
        return requestedStorageMin;
    }

    public void setRequestedStorageMin(int requestedStorageMin) {
        this.requestedStorageMin = requestedStorageMin;
    }

    public int getReceivedCores() {
        return receivedCores;
    }

    public void setReceivedCores(int receivedCores) {
        this.receivedCores = receivedCores;
    }

    public int getReceivedCPU() {
        return receivedCPU;
    }

    public void setReceivedCPU(int receivedCPU) {
        this.receivedCPU = receivedCPU;
    }

    public int getReceivedMemory() {
        return receivedMemory;
    }

    public void setReceivedMemory(int receivedMemory) {
        this.receivedMemory = receivedMemory;
    }

    public int getReceivedStorage() {
        return receivedStorage;
    }

    public void setReceivedStorage(int receivedStorage) {
        this.receivedStorage = receivedStorage;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
