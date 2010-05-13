package contextawaremodel.gui;

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
    private int receivedCPUMax;
    private int receivedCPUMin;
    private int receivedMemoryMax;
    private int receivedMemoryMin;
    private int receivedStorageMax;
    private int receivedStorageMin;

    private boolean isRunning;

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

    public int getReceivedCPUMax() {
        return receivedCPUMax;
    }

    public void setReceivedCPUMax(int receivedCPUMax) {
        this.receivedCPUMax = receivedCPUMax;
    }

    public int getReceivedCPUMin() {
        return receivedCPUMin;
    }

    public void setReceivedCPUMin(int receivedCPUMin) {
        this.receivedCPUMin = receivedCPUMin;
    }

    public int getReceivedMemoryMax() {
        return receivedMemoryMax;
    }

    public void setReceivedMemoryMax(int receivedMemoryMax) {
        this.receivedMemoryMax = receivedMemoryMax;
    }

    public int getReceivedMemoryMin() {
        return receivedMemoryMin;
    }

    public void setReceivedMemoryMin(int receivedMemoryMin) {
        this.receivedMemoryMin = receivedMemoryMin;
    }

    public int getReceivedStorageMax() {
        return receivedStorageMax;
    }

    public void setReceivedStorageMax(int receivedStorageMax) {
        this.receivedStorageMax = receivedStorageMax;
    }

    public int getReceivedStorageMin() {
        return receivedStorageMin;
    }

    public void setReceivedStorageMin(int receivedStorageMin) {
        this.receivedStorageMin = receivedStorageMin;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
