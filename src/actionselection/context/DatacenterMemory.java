package actionselection.context;

import actionselection.command.Command;
import actionselection.command.selfHealingCommand.SelfHealingCommand;
import actionselection.command.selfOptimizingCommand.*;
import contextawaremodel.worldInterface.dtos.ServerDto;
import contextawaremodel.worldInterface.dtos.TaskDto;
import greenContextOntology.ProtegeFactory;
import greenContextOntology.Server;
import greenContextOntology.Task;

import java.io.Serializable;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: GEORGY
 * Date: May 24, 2010
 * Time: 10:17:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatacenterMemory implements Serializable {

    private Map<DatacenterMockupContext, Queue<Command>> map;

    public DatacenterMemory() {
        map = new HashMap<DatacenterMockupContext, Queue<Command>>(10);
    }

    public void memorize(DatacenterMockupContext context, Queue<Command> actions) {
        map.put(context, actions);
    }

    public Queue<Command> getActions(Queue<ServerDto> values) {
        return map.get(values);
    }

    public void forgetAll() {
        map.clear();
    }


    public void restoreProtegeFactory(ProtegeFactory protegeFactory) {

        for (Queue<Command> queue : map.values()) {
            for (Command command : queue) {
                if (command instanceof SelfOptimizingCommand) {
                    ((SelfOptimizingCommand) command).setProtegeFactory(protegeFactory);
                }
            }
        }
    }

    public Queue<Command> getActionsForTasks(ProtegeFactory protegeFactory) {
        Queue<Command> commands = null;
        DatacenterMockupContext context = new DatacenterMockupContext();
        context.createMockupContextFromOntology(protegeFactory);

        Collection<Task> tasks = protegeFactory.getAllTaskInstances();
        if (map.containsKey(context)) {
            System.out.println("Found tasks! existing learned things");
            Map<ServerDto, Server> serverAssoc = new HashMap<ServerDto, Server>();
            Queue<Command> actions = map.get(context);     ///Return commands for  current tasksssssssssssssssssssssssssssssss!!!!
            List<ServerDto> serverDtos = context.getServers();
            for (ServerDto serverDto : serverDtos) {
                Collection<Server> servers = protegeFactory.getAllServerInstances();
                for (Server server : servers) {
                    if (serverDto.equals(server))
                        serverAssoc.put(serverDto, server);
                }
            }
            for (Command command : actions) {
                if ((command instanceof DeployTaskCommand)) {
                    DeployTaskCommand dtc = (DeployTaskCommand) command;
                    String taskName = dtc.getTaskName();
                    String serverName = dtc.getServerName();
                    TaskDto taskDto = context.getTask(dtc.getTaskName());
                    for (Task task : tasks) {
                        if (taskDto.equals(task)) {
                            ((DeployTaskCommand) command).setTaskName(taskName);
                        }
                    }
                    dtc.setServerName(serverAssoc.get(context.getServer(serverName)).getServerName());

                }
                if (command instanceof MoveTaskCommand) {
                    MoveTaskCommand dtc = (MoveTaskCommand) command;
                    String taskName = dtc.getTaskName();

                    TaskDto taskDto = context.getTask(dtc.getTaskName());
                    for (Task task : tasks) {
                        if (taskDto.equals(task)) {
                            ((DeployTaskCommand) command).setTaskName(taskName);
                        }
                    }

                }
                if (command instanceof RemoveTaskFromServerCommand) {
                    RemoveTaskFromServerCommand dtc = (RemoveTaskFromServerCommand) command;
                    String taskName = dtc.getTaskName();
                    String serverName = dtc.getServerName();
                    TaskDto taskDto = context.getTask(dtc.getTaskName());
                    for (Task task : tasks) {
                        if (taskDto.equals(task)) {
                            dtc.setTaskName(taskName);
                        }
                    }
                    dtc.setServerName(serverAssoc.get(context.getServer(serverName)).getServerName());
                }
                if (command instanceof SendServerToLowPowerStateCommand) {
                    RemoveTaskFromServerCommand dtc = (RemoveTaskFromServerCommand) command;
                    String serverName = dtc.getServerName();
                    dtc.setServerName(serverAssoc.get(context.getServer(serverName)).getServerName());

                }
                if (command instanceof WakeUpServerCommand) {
                    RemoveTaskFromServerCommand dtc = (RemoveTaskFromServerCommand) command;
                    String serverName = dtc.getServerName();
                    dtc.setServerName(serverAssoc.get(context.getServer(serverName)).getServerName());
                }
                commands.add(command);
            }
        }


        return commands;
    }
}