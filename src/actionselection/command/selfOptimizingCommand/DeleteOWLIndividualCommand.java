package actionselection.command.selfOptimizingCommand;

import actionselection.command.Command;
import com.hp.hpl.jena.ontology.OntModel;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import jade.core.Agent;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: Apr 4, 2010
 * Time: 12:07:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeleteOWLIndividualCommand implements Command {

    OWLIndividual individual;
    public DeleteOWLIndividualCommand(OWLIndividual individual) {
        this.individual = individual;
    }

    public void execute(OntModel model) {
        individual.delete();
    }

    public void rewind(OntModel model) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void executeOnWebService() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String[] toStringArray() {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void executeOnX3D(Agent agent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void rewindOnX3D(Agent agent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getCost() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setCost(int cost) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String toString(){
        String description="Deleted " + individual.getName();
        return description;
    }
}
