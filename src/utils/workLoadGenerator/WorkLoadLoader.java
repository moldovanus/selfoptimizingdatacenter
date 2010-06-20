package utils.workLoadGenerator;

import contextaware.GlobalVars;
import ontologyRepresentations.greenContextOntology.DatacenterProtegeFactory;
import ontologyRepresentations.greenContextOntology.QoSPolicy;
import ontologyRepresentations.greenContextOntology.Task;

import java.io.Serializable;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: May 27, 2010
 * Time: 11:03:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class WorkLoadLoader implements Serializable {
    private Map<WorkLoadSequence, Long> workLoadSequenceMap;
    private Date currentDate;

    public WorkLoadLoader() {
        this.currentDate = new java.util.Date();
        workLoadSequenceMap = new HashMap<WorkLoadSequence, Long>();
    }

    public void loadWorkLoad(WorkLoadSequence benchmarkSequence, DatacenterProtegeFactory factory) {
        Map<String, Integer> workload = benchmarkSequence.getSequence();
        Collection<Task> tasks = factory.getAllTaskInstances();
        for (Map.Entry<String, Integer> entry : workload.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                for (Task task : tasks) {
                    if (task.getLocalName().equals(entry.getKey())) {
                        Task clone = (Task) task.clone(factory);

                        TaskLifeManager.addTask(factory, clone, new Random().nextInt(GlobalVars.MAX_TASK_LIFE_IN_MINUTES),null);

                        QoSPolicy policy = factory.createQoSPolicy("QoSPolicy_" + task.getLocalName() + "_" + UUID.randomUUID());
                        policy.setReferenced(clone);
                        policy.setRespected(false);
                        policy.setPriority(1);
                        break;
                    }
                }
            }
        }
    }

    public void removeSequence(WorkLoadSequence sequence){
        workLoadSequenceMap.remove(sequence);
    }

    
    public void scheduleSequence(final WorkLoadSequence sequence, final DatacenterProtegeFactory protegeFactory, final Date scheduleTime) {
        Timer timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    public void run() {
                        System.err.println("Workload laoded");
                        loadWorkLoad(sequence, protegeFactory);
                    }
                }
                , scheduleTime);
        workLoadSequenceMap.put(sequence, scheduleTime.getTime() );

    }

    /**
     * @param newReferenceDate the new date from which the schedule is generated
     * @param protegeFactory
     */
    public void reschedule(Date newReferenceDate, final DatacenterProtegeFactory protegeFactory) {
        long newReferenceTime = newReferenceDate.getTime();
        for (final Map.Entry<WorkLoadSequence, Long> entry : workLoadSequenceMap.entrySet()) {
            Date scheduleTime = new Date(newReferenceTime + entry.getValue());
            Timer timer = new Timer();
            timer.schedule(
                    new TimerTask() {
                        public void run() {
                            loadWorkLoad(entry.getKey(), protegeFactory);
                        }
                    }
                    , scheduleTime);
        }
    }
}