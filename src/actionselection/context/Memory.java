/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package actionselection.context;

import actionselection.command.Command;
import actionselection.command.selfOptimizingCommand.SelfOptimizingCommand;
import greenContextOntology.ProtegeFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 *
 * @author Me
 */
public class Memory implements Serializable {

    private Map<SensorValues, Queue<Command>> map;

    public Memory() {
        map = new HashMap<SensorValues, Queue<Command>>(10);
    }

    public void memorize(SensorValues values, Queue<Command> actions) {
        map.put(values, actions);
    }

    public Queue<Command> getActions(SensorValues values) {
        return map.get(values);
    }

    public void forgetAll() {
        map.clear();
    }

    //Todo : restore also SelfHealingProtegeFactory
    public void restoreProtegeFactory(ProtegeFactory protegeFactory) {
        for (Queue<Command> queue : map.values()) {
            for (Command command : queue) {
                if (queue instanceof SelfOptimizingCommand) {
                    ((SelfOptimizingCommand)command).setProtegeFactory(protegeFactory);
                }
            }
        }
    }
}
