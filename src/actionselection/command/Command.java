/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package actionselection.command;

import com.hp.hpl.jena.ontology.OntModel;
import greenContextOntology.ProtegeFactory;
import java.io.Serializable;
import java.text.NumberFormat;

/**
 *
 * @author Administrator
 */
public abstract class Command implements Serializable {

    protected String targetIndividualName;
    protected String targetPropertyName;
    protected String hasWebServicePropertyName;
    protected transient ProtegeFactory protegeFactory;
    protected NumberFormat integerNumberFormat = NumberFormat.getIntegerInstance();


    
    public Command(String targetIndividual, String targetProperty, String hasWebService,  ProtegeFactory protegeFactory) {
        this.targetIndividualName = targetIndividual;
        this.targetPropertyName = targetProperty;
        this.hasWebServicePropertyName = hasWebService;
        this.protegeFactory = protegeFactory;
    }

    public void setProtegeFactory(ProtegeFactory protegeFactory) {
        this.protegeFactory = protegeFactory;
    }

     

    public abstract void execute();

    public abstract void rewind();

    @Override
    public abstract String toString();

    public abstract void executeOnWebService();

    public abstract String[] toStringArray();
}
