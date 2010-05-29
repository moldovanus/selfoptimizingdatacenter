package actionselection.context;

import contextawaremodel.worldInterface.dtos.ServerDto;
import contextawaremodel.worldInterface.dtos.TaskDto;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: GEORGY
 * Date: 29.05.2010
 * Time: 11:16:47
 * To change this template use File | Settings | File Templates.
 */
public class ServerTask {
    private ServerDto server;
    private List<TaskDto> tasks;

    public ServerDto getServer() {
        return server;
    }

    public void setServer(ServerDto sv) {
        this.server = sv;
    }

    public List<TaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDto> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object instanceof ServerTask) {
            ServerTask serverTask = (ServerTask) object;
            if (!server.equals(serverTask.getServer())) return false;
            List<TaskDto> tasksList = serverTask.getTasks();
            for (TaskDto tdto : tasksList) {
                if (!tasks.contains(tdto)) return false;
            }
        }
        return true;
    }
}
