package greenContextOntology;

import edu.stanford.smi.protegex.owl.model.*;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Datacenter.owl#Component
 *
 * @version generated on Sat Mar 06 16:14:39 EET 2010
 */
public interface Component extends Resource {

    // Property http://www.owl-ontologies.com/Datacenter.owl#optimum

    int getOptimum();

    RDFProperty getOptimumProperty();

    boolean hasOptimum();

    void setOptimum(int newOptimum);


    // Property http://www.owl-ontologies.com/Datacenter.owl#total

    int getTotal();

    RDFProperty getTotalProperty();

    boolean hasTotal();

    void setTotal(int newTotal);


    // Property http://www.owl-ontologies.com/Datacenter.owl#used

    int getUsed();

    RDFProperty getUsedProperty();

    boolean hasUsed();

    void setUsed(int newUsed);
}
