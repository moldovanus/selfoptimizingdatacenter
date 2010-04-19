/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negotiator;

import greenContextOntology.Server;
import greenContextOntology.Task;

/**
 * @author Administrator
 */
public interface Negotiator {

    public double[] negotiate(Server server, Task task);

    public class IntervalEntry {
        private double value;
        private double membership;

        public IntervalEntry(double membership, double value) {
            this.membership = membership;
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        public double getMembership() {
            return membership;
        }
    }
}
