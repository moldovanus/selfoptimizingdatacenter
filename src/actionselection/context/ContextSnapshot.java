/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package actionselection.context;

import actionselection.command.Command;
import com.hp.hpl.jena.ontology.OntModel;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;

import java.util.Queue;

/**
 * @author Administrator
 */
public class ContextSnapshot implements Comparable {
    private Queue<Command> actions;
    private double contextEntropy = 0;
    private double rewardFunction = 0;
    public final static double gamma = 0.6d;

    public ContextSnapshot(final Queue<Command> actions) {
        this.actions = actions;
    }

    public double getContextEntropy() {
        return contextEntropy;
    }

    public void setContextEntropy(double contextEntropy) {
        this.contextEntropy = contextEntropy;
    }

    public void addActions(Queue<Command> commands) {
        for (Command command : commands) {
            actions.add(command);
        }
    }

    public void executeActions() {
        for (Command command : actions) {
            command.execute();
            System.out.println("Executing " + command.toString());
        }
    }

    public void executeActionsOnOWL() {
        for (Command command : actions) {
            command.executeOnWebService();
        }
    }

    /**
     * Rewinds in the inverse order
     */
    public void rewind() {

        Object[] commands = actions.toArray();

        for (int i = commands.length-1; i >=0 ; i--) {
            Command command = (Command) commands[i];
            command.rewind();
           // System.out.println("Rewinding  " + command.toString());
        }
    }

    public Queue<Command> getActions() {
        return actions;
    }

    public void setActions(final Queue<Command> actions) {
        this.actions = actions;
    }

    /**
     * @return the rewardFunction
     */
    public double getRewardFunction() {
        return rewardFunction;
    }

    /**
     * @param rewardFunction the rewardFunction to set
     */
    public void setRewardFunction(double rewardFunction) {
        this.rewardFunction = rewardFunction;
    }

    /* public int compareTo(Object o) {

  if (o.getClass() != this.getClass())
      return 0;
  ContextSnapshot cs = (ContextSnapshot) o;
  if (cs.contextEntropy < contextEntropy)
      return 1;
  else if (cs.contextEntropy == contextEntropy) {
      if (cs.rewardFunction < rewardFunction) {
          return 1;
      } else if (cs.rewardFunction == rewardFunction) {
          return 0;
      } else {
          return -1;
      }
  } else return -1;
}      */

    public int compareTo(Object o) {
        if (o.getClass() != this.getClass())
            return 0;
        ContextSnapshot cs = (ContextSnapshot) o;
        if ( cs.rewardFunction < rewardFunction){
            return -1;
        }else if ( cs.rewardFunction == rewardFunction){
            return 0;
        }else{
            return 1;
        }
    }

    public String toString() {
        String description;
        description = "Entropy: " + this.contextEntropy + " Reward: " + this.rewardFunction;
        return description;
    }
}
