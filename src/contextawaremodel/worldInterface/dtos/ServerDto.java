package contextawaremodel.worldInterface.dtos;

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
}
