package contextawaremodel.worldInterface.dtos;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 8, 2010
 * Time: 10:52:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class StorageDto {
    public String name;
    public int size;
    public int freeSpace;

    public StorageDto(String name, int size, int freeSpace) {
        this.name = name;
        this.size = size;
        this.freeSpace = freeSpace;
    }

    public StorageDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFreeSpace() {
        return freeSpace;
    }

    public void setFreeSpace(int freeSpace) {
        this.freeSpace = freeSpace;
    }
}
