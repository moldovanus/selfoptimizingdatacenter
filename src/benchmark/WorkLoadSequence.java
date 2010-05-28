package benchmark;

import greenContextOntology.ProtegeFactory;
import greenContextOntology.Task;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: May 27, 2010
 * Time: 11:10:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class WorkLoadSequence implements Serializable {
    private Map<String, Integer> sequence;

    public WorkLoadSequence(ProtegeFactory factory, int totaltaskCount) {
        this.sequence = new HashMap<String, Integer>();
        Collection<Task> tasks = factory.getAllTaskInstances();
        Random random = new Random();
        for (Task task : tasks) {
            int count = random.nextInt(totaltaskCount);
            totaltaskCount -= count;
            sequence.put(task.getLocalName(), count);
        }
    }

    public Map<String, Integer> getSequence() {
        return sequence;
    }

    /**
     * Assigns new Random values to the Integer element in the map
     * @param totalTaskNo - total number of tasks
     */
    public void regenerate(int totalTaskNo) {
        Random random = new Random();
        for (Map.Entry<String, Integer> entry : sequence.entrySet()) {
            int count = random.nextInt(totalTaskNo);
            totalTaskNo -= count;
            entry.setValue(count);
        }
    }
}
