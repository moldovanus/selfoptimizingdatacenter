package actionselection.command.selfHealingCommand;

import greenContextOntology.ProtegeFactory;
import jade.core.Agent;
import selfHealingOntology.SelfHealingProtegeFactory;
import com.hp.hpl.jena.ontology.OntModel;
import actionselection.command.Command;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: Mar 16, 2010
 * Time: 10:09:56 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class SelfHealingCommand implements Command {
    protected transient SelfHealingProtegeFactory protegeFactory;
    protected int cost;

    public SelfHealingCommand(SelfHealingProtegeFactory protegeFactory) {
        this.protegeFactory = protegeFactory;
    }

    public final void setProtegeFactory(SelfHealingProtegeFactory protegeFactory) {
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

    //TODO: AM scris equals
    @Override
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }
}
