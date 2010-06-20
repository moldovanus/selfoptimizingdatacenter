package utils.workLoadGenerator;

import com.hp.hpl.jena.ontology.OntModel;
import ontologyRepresentations.greenContextOntology.DatacenterProtegeFactory;

import java.util.Date;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 28, 2010
 * Time: 2:00:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class RandomWorkLoadGenerator {
    private RandomWorkLoadGenerator() {
    }

    /**
     * @param workLoadSequenceNo maximum number of workload sequences
     * @param maxSequenceLength  maximum  number of tasks per workload sequence
     * @param maxSequenceDelayInMillis   maximum delay between workload sequences in milliseconds
     * @param protegeFactory
     * @return
     */
    public static WorkLoadLoader generateRandomWorkLoad(int workLoadSequenceNo, int maxSequenceLength, int maxSequenceDelayInMillis, DatacenterProtegeFactory protegeFactory, OntModel model) {
        WorkLoadLoader loader = new WorkLoadLoader();
        Random taskNoRandom = new Random();
        Random sequenceDelayRandom = new Random();
        for (int i = 0; i < workLoadSequenceNo; i++) {
            WorkLoadSequence sequence = new WorkLoadSequence(protegeFactory, taskNoRandom.nextInt(maxSequenceLength) + 2,model);
            Date scheduleDate = new java.util.Date();
            scheduleDate.setTime(scheduleDate.getTime() + sequenceDelayRandom.nextInt(maxSequenceDelayInMillis));
            System.out.println("scheduled for " + scheduleDate.toString());
            loader.scheduleSequence(sequence, protegeFactory, scheduleDate);
        }

        return loader;
    }
    
}