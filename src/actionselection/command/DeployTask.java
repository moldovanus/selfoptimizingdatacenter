/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package actionselection.command;

import com.hp.hpl.jena.ontology.OntModel;
import greenContextOntology.ProtegeFactory;

/**
 *
 * @author Me
 */
public class DeployTask extends Command{

    public DeployTask(String targetIndividual, String targetProperty, String hasWebService, ProtegeFactory protegeFactory) {
        super(targetIndividual, targetProperty, hasWebService, protegeFactory);
    }

    
    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void rewind() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void executeOnWebService() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] toStringArray() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
