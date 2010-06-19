package benchmark;

import com.hp.hpl.jena.ontology.OntModel;
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

    public WorkLoadSequence(ProtegeFactory factory, int totalTaskCount, OntModel model) {
        this.sequence = new HashMap<String, Integer>();
        Collection<Task> tasks = factory.getAllTaskInstances();
        Random random = new Random();
        for (Task task : tasks) {
            int count = random.nextInt(totalTaskCount);
            totalTaskCount -= count;
            sequence.put(task.getLocalName(), count);
        }
    }

    public WorkLoadSequence() {
        this.sequence = new HashMap<String, Integer>();
    }

    public Map<String, Integer> getSequence() {
        return sequence;
    }

    public void addTask(String name) {
        if (sequence.containsKey(name)) {
            sequence.put(name, sequence.get(name) + 1);
        } else {
            sequence.put(name, 1);
        }
    }

    public void removeTask(String name) {
        sequence.remove(name);
    }

    /**
     * Assigns new Random values to the Integer element in the map
     *
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
