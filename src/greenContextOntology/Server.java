package greenContextOntology;

import edu.stanford.smi.protegex.owl.model.*;

import java.util.*;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Datacenter.owl#Server
 *
 * @version generated on Sun Mar 07 13:11:11 EET 2010
 */
public interface Server extends Resource {

    // Property http://www.owl-ontologies.com/Datacenter.owl#associatedComponent
    Collection getAssociatedComponent();

    RDFProperty getAssociatedComponentProperty();

    boolean hasAssociatedComponent();

    Iterator listAssociatedComponent();

    void addAssociatedComponent(Component newAssociatedComponent);

    void removeAssociatedComponent(Component oldAssociatedComponent);

    void setAssociatedComponent(Collection newAssociatedComponent);

    // Property http://www.owl-ontologies.com/Datacenter.owl#lowPowerState
    boolean getLowPowerState();

    RDFProperty getLowPowerStateProperty();

    boolean hasLowPowerState();

    void setLowPowerState(boolean newLowPowerState);

    // Property http://www.owl-ontologies.com/Datacenter.owl#name
    String getName();

    RDFProperty getNameProperty();

    boolean hasName();

    void setName(String newName);

    // Property http://www.owl-ontologies.com/Datacenter.owl#webService
    String getWebService();

    RDFProperty getWebServiceProperty();

    boolean hasWebService();

    void setWebService(String newWebService);

    // Property http://www.owl-ontologies.com/Datacenter.owl#runningTasks

    Collection getRunningTasks();

    RDFProperty getRunningTasksProperty();

    boolean hasRunningTasks();

    Iterator listRunningTasks();

    void addRunningTasks(greenContextOntology.Task newRunningTasks);

    void removeRunningTasks(greenContextOntology.Task oldRunningTasks);

    void setRunningTasks(Collection newRunningTasks);

}

