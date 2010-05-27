package benchmark;

import greenContextOntology.ProtegeFactory;
import greenContextOntology.QoSPolicy;
import greenContextOntology.Task;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: May 27, 2010
 * Time: 11:03:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class WorkLoadGenerator {


    private WorkLoadGenerator() {

    }

    public static void generateWorkload(WorkLoadSequence benchmarkSequence, ProtegeFactory factory) {
        Map<String, Integer> workload = benchmarkSequence.getSequence();
        Collection<Task> tasks = factory.getAllTaskInstances();
        for (Map.Entry<String, Integer> entry : workload.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                for (Task task : tasks) {
                    if (task.getLocalName().equals(entry.getKey())) {
                        task.clone(factory);
                        QoSPolicy policy = factory.createQoSPolicy(task.getLocalName() + "_" + UUID.randomUUID());
                        policy.setReferenced(task);
                        break;
                    }
                }
            }
        }
    }

    public static void scheduleSequence(final WorkLoadSequence sequence, final ProtegeFactory protegeFactory, final Date scheduleTime) {
        Timer timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    public void run() {
                        generateWorkload(sequence, protegeFactory);
                    }
                }
                , scheduleTime);
    }
}
