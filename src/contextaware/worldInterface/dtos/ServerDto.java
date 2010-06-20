package contextaware.worldInterface.dtos;

import ontologyRepresentations.greenContextOntology.CPU;
import ontologyRepresentations.greenContextOntology.Core;
import ontologyRepresentations.greenContextOntology.Server;
import ontologyRepresentations.greenContextOntology.Storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 8, 2010
 * Time: 10:51:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServerDto {
    public int coreCount;
    public int totalCPU;
    public List<Integer> freeCPU;
    public List<StorageDto> storage;
    public int totalMemory;
    public int freeMemory;
    private String serverName;

    @Override
    public boolean equals(Object sv) {
        if (sv instanceof Server) {
            Server server = (Server) sv;
            CPU cpu = server.getAssociatedCPU();
            Collection<Core> cores = cpu.getAssociatedCore();
            ArrayList<Integer> freeCpus = new ArrayList(cores.size());
            Storage s = server.getAssociatedStorage();
            int st = 0;
            for (int i = 0; i < storage.size(); i++) {
                st += storage.get(i).getFreeSpace();
            }
            if (st != (s.getTotal() - s.getUsed())) {
                return false;
            }

            int i = 0;
            for (Core core : cores) {
                if (freeCPU.get(i) != core.getTotal() - core.getUsed()) {
                    i++;
                    return false;
                }
                i++;
                if (core.getTotal() != totalCPU)
                    return false;
            }

            if (freeMemory != (server.getAssociatedMemory().getTotal() - server.getAssociatedMemory().getUsed()))
                return false;

            if (totalMemory != server.getAssociatedMemory().getTotal()) return false;

            if (coreCount != cores.size()) return false;
        }
        if (sv instanceof ServerDto) {
            ServerDto sDto = (ServerDto) sv;
            if (sDto.getCoreCount() != coreCount) return false;
            List<Integer> freeCpus = sDto.getFreeCPU();
            for (Integer freeCpu : freeCpus) {
                if (!freeCPU.contains(freeCpu)) return false;
            }
            List<StorageDto> storages = sDto.getStorage();
            for (StorageDto st : storages) {
                if (!storage.contains(st)) return false;
            }
            if (totalMemory != sDto.getTotalMemory()) return false;
            if (freeMemory != sDto.getFreeMemory()) return false;
            if (totalCPU != sDto.getTotalCPU()) return false;
        }

        return true;
    }

    public int getCoreCount() {
        return coreCount;
    }

    public void setCoreCount(int coreCount) {
        this.coreCount = coreCount;
    }

    public int getTotalCPU() {
        return totalCPU;
    }

    public void setTotalCPU(int totalCPU) {
        this.totalCPU = totalCPU;
    }

    public List<Integer> getFreeCPU() {
        return freeCPU;
    }

    public void setFreeCPU(List<Integer> freeCPU) {
        this.freeCPU = freeCPU;
    }

    public List<StorageDto> getStorage() {
        return storage;
    }

    public void setStorage(List<StorageDto> storage) {
        this.storage = storage;
    }

    public int getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(int totalMemory) {
        this.totalMemory = totalMemory;
    }

    public int getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(int freeMemory) {
        this.freeMemory = freeMemory;
    }


    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
