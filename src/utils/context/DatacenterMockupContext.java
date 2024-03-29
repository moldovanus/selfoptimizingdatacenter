package utils.context;

import contextaware.worldInterface.dtos.ServerDto;
import contextaware.worldInterface.dtos.StorageDto;
import contextaware.worldInterface.dtos.TaskDto;
import ontologyRepresentations.greenContextOntology.*;

import java.io.Serializable;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: GEORGY
 * Date: May 24, 2010
 * Time: 10:45:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatacenterMockupContext implements Serializable {
    private List<ServerTaskList> context;

    public DatacenterMockupContext() {
        context = new ArrayList<ServerTaskList>();
    }

    public List<TaskDto> getTasks() {
        List<TaskDto> myList = new ArrayList<TaskDto>();
        int i = 0;
        for (ServerTaskList st : context) {
            List<TaskDto> list = st.getTasks();
            for (TaskDto td : list) {
                if (!myList.contains(td)) {
                    myList.add(i, td);
                    i++;
                }
            }

        }
        return myList;
    }

    public TaskDto getTask(String taskName) {
        List<TaskDto> tasksList = getTasks();
        for (TaskDto taskDto : tasksList) {
            if (taskDto.getTaskName().equalsIgnoreCase(taskName)) return taskDto;
        }
        return null;
    }

    public List<ServerDto> getServers() {
        List<ServerDto> myList = new ArrayList<ServerDto>();
        int i = 0;
        for (ServerTaskList st : context) {
            myList.add(i, st.getServer());
            i++;
        }
        return myList;
    }

    public ServerDto getServer(String serverName) {
        List<ServerDto> myList = getServers();
        for (ServerDto serverDto : myList)
            if (serverDto.getServerName().equalsIgnoreCase(serverName)) return serverDto;
        return null;
    }

    public void createMockupContextFromOntology(DatacenterProtegeFactory protegeFactory) {
        System.out.println("Creating mockup context.....");
        Collection<Server> servers = protegeFactory.getAllServerInstances();
        for (Server server : servers) {
            ServerDto serverDto = new ServerDto();

            Collection<Task> tasks = server.getRunningTasks();
            List<TaskDto> taskDtos = new ArrayList(tasks.size());
            int i = 0;
            for (Task task : tasks) {
                TaskDto taskDto = new TaskDto();
                RequestedTaskInfo rti = task.getRequestedInfo();
                taskDto.setRequestedCores(rti.getCores());
                taskDto.setRequestedCPUMax(rti.getCpuMaxAcceptableValue());
                taskDto.setRequestedCPUMin(rti.getCpuMinAcceptableValue());
                taskDto.setRequestedMemoryMax(rti.getMemoryMaxAcceptableValue());
                taskDto.setRequestedMemoryMin(rti.getMemoryMinAcceptableValue());
                taskDto.setRequestedStorageMax(rti.getStorageMaxAcceptableValue());
                taskDto.setRequestedStorageMin(rti.getStorageMinAcceptableValue());
                taskDto.setTaskName(task.getTaskName());
                taskDto.setReceivedCores(0);
                taskDto.setReceivedCPU(0);
                taskDto.setReceivedMemory(0);
                taskDto.setReceivedStorage(0);
                taskDto.setTaskName(task.getTaskName());
                taskDtos.add(i, taskDto);
                i++;
            }
            CPU cpu = server.getAssociatedCPU();

            Collection<Core> cores = cpu.getAssociatedCore();
            ArrayList<Integer> freeCpus = new ArrayList(cores.size());
            Storage storage = server.getAssociatedStorage();
            StorageDto storageDto = new StorageDto();
            storageDto.setFreeSpace(storage.getTotal() - storage.getUsed());
            ArrayList<StorageDto> storages = new ArrayList(1);
            storages.add(0, storageDto);
            serverDto.setStorage(storages);
            i = 0;
            for (Core core : cores) {
                freeCpus.add(i, core.getTotal() - core.getUsed());
                i++;
                serverDto.setTotalCPU(core.getTotal());
            }

            serverDto.setFreeMemory(server.getAssociatedMemory().getTotal() - server.getAssociatedMemory().getUsed());
            serverDto.setTotalMemory(server.getAssociatedMemory().getTotal());
            serverDto.setCoreCount(cores.size());
            ServerTaskList st = new ServerTaskList();
            st.setServer(serverDto);
            st.setTasks(taskDtos);
            context.add(st);
        }

    }

    public boolean equals(Object object) {
        boolean isEqual = true;
        if (object == this) return true;
        if (object instanceof DatacenterMockupContext) {
            DatacenterMockupContext newContext = (DatacenterMockupContext) object;
            for (ServerTaskList st : context) {
                if (!newContext.getContext().contains(st)) return false;
            }
        }
        if (object instanceof DatacenterProtegeFactory) {
            DatacenterProtegeFactory protegeFactory = (DatacenterProtegeFactory) object;
            Collection<Server> servers = protegeFactory.getAllServerInstances();

            for (Server server : servers) {
                ServerDto serverDto = new ServerDto();
                CPU cpu = server.getAssociatedCPU();
                Collection<Core> cores = cpu.getAssociatedCore();
                ArrayList<Integer> freeCpus = new ArrayList(cores.size());
                Storage storage = server.getAssociatedStorage();
                StorageDto storageDto = new StorageDto();
                storageDto.setFreeSpace(storage.getTotal() - storage.getUsed());
                ArrayList<StorageDto> storages = new ArrayList(1);
                storages.add(0, storageDto);
                serverDto.setStorage(storages);
                int i = 0;
                for (Core core : cores) {
                    freeCpus.add(i, core.getTotal() - core.getUsed());
                    i++;
                    serverDto.setTotalCPU(core.getTotal());
                }

                serverDto.setFreeMemory(server.getAssociatedMemory().getTotal() - server.getAssociatedMemory().getUsed());
                serverDto.setTotalMemory(server.getAssociatedMemory().getTotal());
                serverDto.setCoreCount(cores.size());


                for (ServerTaskList st : context)
                    if (st.getServer().equals(serverDto)) {
                        List<TaskDto> tasksList = st.getTasks();
                        Collection<Task> tasks = server.getRunningTasks();

                        for (Task task : tasks) {
                            TaskDto taskDto = new TaskDto();
                            RequestedTaskInfo rti = task.getRequestedInfo();
                            taskDto.setRequestedCores(rti.getCores());
                            taskDto.setRequestedCPUMax(rti.getCpuMaxAcceptableValue());
                            taskDto.setRequestedCPUMin(rti.getCpuMinAcceptableValue());
                            taskDto.setRequestedMemoryMax(rti.getMemoryMaxAcceptableValue());
                            taskDto.setRequestedMemoryMin(rti.getMemoryMinAcceptableValue());
                            taskDto.setRequestedStorageMax(rti.getStorageMaxAcceptableValue());
                            taskDto.setRequestedStorageMin(rti.getStorageMinAcceptableValue());
                            taskDto.setReceivedCores(0);
                            taskDto.setReceivedCPU(0);
                            taskDto.setReceivedMemory(0);
                            taskDto.setReceivedStorage(0);
                            taskDto.setTaskName(task.getTaskName());
                            if (!tasksList.contains(taskDto))
                                isEqual = false;
                        }
                    }
            }
        }

        return isEqual;
    }

    public void restoreContext(DatacenterProtegeFactory factory) {
        //TODO: restore context
    }

    public List<ServerTaskList> getContext() {
        return context;
    }

    public void setContext(List<ServerTaskList> context) {
        this.context = context;
    }
}
