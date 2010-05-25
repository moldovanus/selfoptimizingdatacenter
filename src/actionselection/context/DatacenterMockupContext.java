package actionselection.context;

import contextawaremodel.worldInterface.dtos.ServerDto;
import contextawaremodel.worldInterface.dtos.StorageDto;
import contextawaremodel.worldInterface.dtos.TaskDto;
import greenContextOntology.*;

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
    private Map<ServerDto, List<TaskDto>> context;

    public DatacenterMockupContext() {
        context = new HashMap<ServerDto, List<TaskDto>>();
    }

    public void createMockupContextFromOntology(ProtegeFactory protegeFactory) {
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
                taskDto.setReceivedCPUMax(0);
                taskDto.setReceivedCPUMin(0);
                taskDto.setReceivedMemoryMax(0);
                taskDto.setReceivedMemoryMin(0);
                taskDto.setReceivedStorageMin(0);
                taskDto.setReceivedStorageMax(0);
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
            getContext().put(serverDto, taskDtos);
        }

    }

    public void restoreContext(ProtegeFactory factory) {
        //TODO: restore context
    }

    public Map<ServerDto, List<TaskDto>> getContext() {
        return context;
    }

    public void setContext(Map<ServerDto, List<TaskDto>> context) {
        this.context = context;
    }
}
