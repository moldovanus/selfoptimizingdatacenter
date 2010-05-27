package benchmark;

import actionselection.command.selfOptimizingCommand.DeleteOWLIndividualCommand;
import actionselection.command.selfOptimizingCommand.RemoveTaskFromServerCommand;
import com.hp.hpl.jena.ontology.OntModel;
import greenContextOntology.ProtegeFactory;
import greenContextOntology.QoSPolicy;
import greenContextOntology.Task;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: May 26, 2010
 * Time: 10:51:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaskLifeManager {
    private static Map<Task, Timer> tasksLifeTimer = new HashMap<Task, Timer>();

    private TaskLifeManager() {

    }

    public static void addTask(final ProtegeFactory protegeFactory, final Task task, int lifeInMinutes, final OntModel model) {
        Timer timer = new Timer(lifeInMinutes * 60, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                RemoveTaskFromServerCommand command = new RemoveTaskFromServerCommand(protegeFactory, task.getTaskName(), task.getAssociatedServer().getLocalName());
                command.executeOnWebService();
                command.execute(model);
                tasksLifeTimer.remove(task);
                for (QoSPolicy policy : protegeFactory.getAllQoSPolicyInstances()) {
                    if (policy.getReferenced().equals(task)) {
                        DeleteOWLIndividualCommand deleteTaskPolicyCommand = new DeleteOWLIndividualCommand(task);
                        deleteTaskPolicyCommand.execute(model);
                        break;
                    }
                }
                DeleteOWLIndividualCommand deleteTaskCommand = new DeleteOWLIndividualCommand(task);
                deleteTaskCommand.execute(model);
                System.out.println(task.getLocalName() + " has been killed by task life simulator");
            }
        });
        timer.setRepeats(false);

        tasksLifeTimer.put(task, timer);

    }

    public static void startTaskTimer(Task task) {
        tasksLifeTimer.get(task).start();
    }

    public static void stopTaskTimer(Task task) {
        tasksLifeTimer.get(task).stop();
    }

}
