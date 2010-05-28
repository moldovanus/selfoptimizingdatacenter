package benchmark;

import actionselection.command.selfOptimizingCommand.DeleteOWLIndividualCommand;
import actionselection.command.selfOptimizingCommand.RemoveTaskFromServerCommand;
import com.hp.hpl.jena.ontology.OntModel;
import greenContextOntology.ProtegeFactory;
import greenContextOntology.QoSPolicy;
import greenContextOntology.Task;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: May 26, 2010
 * Time: 10:51:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaskLifeManager {
    private static Map<Task, Timer> tasksLifeTimer = new HashMap<Task, Timer>();
    private static List<Task> toKill = new ArrayList<Task>();

    private TaskLifeManager() {

    }

    public static void addTask(final ProtegeFactory protegeFactory, final Task task, int lifeInMinutes, final OntModel model) {
        Timer timer = new Timer(lifeInMinutes * 1000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                toKill.add(task);
            }
        });
        timer.setRepeats(false);

        tasksLifeTimer.put(task, timer);

    }

    public static void kill(final ProtegeFactory protegeFactory, final OntModel model) {
        Task task;
        while (toKill.size() > 0) {
            task = toKill.remove(0);
            System.out.println(task.getLocalName() + " running = " + task.isRunning() + " has been killed by task life simulator");
            RemoveTaskFromServerCommand command = new RemoveTaskFromServerCommand(protegeFactory, task.getName(), task.getAssociatedServer().getLocalName());
            command.executeOnWebService();
            command.execute(model);
            tasksLifeTimer.remove(task);
            for (QoSPolicy policy : protegeFactory.getAllQoSPolicyInstances()) {
                if (policy.getReferenced() != null && policy.getReferenced().equals(task)) {
                    DeleteOWLIndividualCommand deleteTaskPolicyCommand = new DeleteOWLIndividualCommand(task);
                    deleteTaskPolicyCommand.execute(model);
                    break;
                }
            }
            DeleteOWLIndividualCommand deleteTaskCommand = new DeleteOWLIndividualCommand(task);
            deleteTaskCommand.execute(model);
        }
    }

    public static void startTaskTimer(Task task) {
//        try {
        tasksLifeTimer.get(task).start();
//        } catch (NullPointerException e) {
//            System.err.println(task.getLocalName());
//            System.err.println(e.getMessage());
//            e.printStackTrace();
//        }
    }

    public static void stopTaskTimer(Task task) {
        tasksLifeTimer.get(task).stop();
    }

}
