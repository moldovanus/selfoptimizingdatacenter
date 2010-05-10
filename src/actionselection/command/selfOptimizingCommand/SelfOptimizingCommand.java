/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package actionselection.command.selfOptimizingCommand;

import actionselection.command.Command;
import com.hp.hpl.jena.ontology.OntModel;
import greenContextOntology.ProtegeFactory;
import jade.core.Agent;

/**
 * @author Administrator
 */
public abstract class SelfOptimizingCommand implements Command {
    protected transient ProtegeFactory protegeFactory;
    protected int cost;

    public SelfOptimizingCommand(ProtegeFactory protegeFactory) {
        this.protegeFactory = protegeFactory;
    }

    public final void setProtegeFactory(ProtegeFactory protegeFactory) {
        this.protegeFactory = protegeFactory;
    }

    public final int getCost() {
        return cost;
    }

    public final void setCost(int cost) {
        this.cost = cost;
    }

    public abstract void execute(OntModel model);

    public abstract void rewind(OntModel model);

    @Override
    public abstract String toString();

    public abstract void executeOnWebService();

    public abstract String[] toStringArray();

    public abstract void executeOnX3D(Agent agent);

    public abstract void rewindOnX3D(Agent agent);

    @Override
    /**
     * This checks if two actions that led to a cycle are compared and returns true if that is the case
     * in order not to prune that branch and do not expand a scenario which contains cycles.
     */
    public final boolean equals(Object obj) {
        Command otherCommand = (Command) obj;
        Class thisClass = this.getClass();
        Class objectClass = otherCommand.getClass();

        if (thisClass.equals(objectClass)) {
            if (thisClass.equals(DeployTaskCommand.class)) {
                return false;
            }

            if (thisClass.equals(MoveTaskCommand.class)) {

                /*public String[] toStringArray() {
                    String[] array = new String[3];
                    array[0] = "Move";
                    array[1] = taskName.split("#")[1];
                    array[2] = oldServerName.split("#")[1];
                    array[3] = newServerName.split("#")[1];
                    return array;
                }*/
                String[] thisInfo = this.toStringArray();
                String[] objectInfo = otherCommand.toStringArray();
                if (thisInfo[1].equals(objectInfo[1])) {
                    return thisInfo[2].equals(objectInfo[3]);
                } else {
                    return false;
                }
            } else if (thisClass.equals(NegotiateResourcesCommand.class)) {
                return this.toString().equals(obj.toString());
            } else {
                return false;
            }
        } else if (thisClass.equals(DeployTaskCommand.class) && objectClass.equals(MoveTaskCommand.class)) {
            /*public String[] toStringArray() {
                String[] array = new String[3];
                array[0] = "Deploy";
                array[1] = taskName.split("#")[1];
                array[2] = serverName.split("#")[1];
                return array;
            }*/
            String[] thisInfo = this.toStringArray();
            String[] objectInfo = otherCommand.toStringArray();
            if (thisInfo[1].equals(objectInfo[1])) {
                return thisInfo[2].equals(objectInfo[3]);
            } else {
                return false;
            }
        } else if (thisClass.equals(DeployTaskCommand.class) && objectClass.equals(MoveTaskCommand.class)) {
            //only this case because only in this order deploy Task and then move same Task can appear
            /*public String[] toStringArray() {
                String[] array = new String[3];
                array[0] = "Deploy";
                array[1] = taskName.split("#")[1];
                array[2] = serverName.split("#")[1];
                return array;
            }*/
            String[] thisInfo = this.toStringArray();
            String[] objectInfo = otherCommand.toStringArray();
            if (thisInfo[1].equals(objectInfo[1])) {
                return thisInfo[2].equals(objectInfo[3]);
            } else {
                return false;
            }
        } else if ((thisClass.equals(SendServerToLowPowerStateCommand.class)
                && objectClass.equals(WakeUpServerCommand.class))
                || (thisClass.equals(WakeUpServerCommand.class)
                && objectClass.equals(SendServerToLowPowerStateCommand.class))) {
            /*
            public String[] toStringArray() {
                String[] array = new String[3];
                array[0] = "Send";
                array[1] = serverName.split("#")[1];
                array[2] = "to low power state";
                return array;
            }*/
            /*
            public String[] toStringArray() {
                    String[] array = new String[3];
                    array[0] = "Wake up";
                    array[1] = serverName.split("#")[1];
                    array[2] = "from low power state";
                    return array;
                }
            */
            String[] thisInfo = this.toStringArray();
            String[] objectInfo = otherCommand.toStringArray();
            if (thisInfo[1].equals(objectInfo[1])) {
                return true; // if one command sends it to low power and other wakes it up == cycle
            } else {
                return false;
            }

        } else {
            return false;
        }
        //return this.toString().equals(obj.toString());
    }

}
