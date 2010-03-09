/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package actionselection.command;

import greenContextOntology.ProtegeFactory;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public abstract class Command implements Serializable {
    protected transient ProtegeFactory protegeFactory;
    protected int cost;
    
    public Command(  ProtegeFactory protegeFactory) {
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

    public abstract void execute();

    public abstract void rewind();

    @Override
    public abstract String toString();

    public abstract void executeOnWebService();

    public abstract String[] toStringArray();

    //TODO: AM scris equals
    @Override
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }
}
