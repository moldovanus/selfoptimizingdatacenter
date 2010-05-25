package actionselection.context;

import actionselection.command.Command;
import actionselection.command.selfHealingCommand.SelfHealingCommand;
import actionselection.command.selfOptimizingCommand.SelfOptimizingCommand;
import contextawaremodel.worldInterface.dtos.ServerDto;
import greenContextOntology.ProtegeFactory;
import greenContextOntology.Server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

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
}