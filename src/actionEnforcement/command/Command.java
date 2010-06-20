package actionEnforcement.command;

import com.hp.hpl.jena.ontology.OntModel;
import jade.core.Agent;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: Mar 16, 2010
 * Time: 10:13:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Command extends Serializable {
    public abstract void execute(OntModel model);

    public abstract void rewind(OntModel model);

    @Override
    public abstract String toString();

    public abstract void executeOnWebService();

    public abstract String[] toStringArray();

    public abstract void executeOnX3D(Agent agent);

    public abstract void rewindOnX3D(Agent agent);

    public  int getCost();

    public void setCost(int cost);
    

}
