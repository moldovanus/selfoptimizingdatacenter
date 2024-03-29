package ontologyRepresentations.selfHealingOntology;

import com.hp.hpl.jena.ontology.OntModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;

import java.util.Collection;
import java.util.Iterator;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/Ontology1230214892.owl#Sensor
 *
 * @version generated on Wed Mar 17 12:47:46 EET 2010
 */
public interface Sensor extends PhysicalResource {

     // Property http://www.owl-ontologies.com/Ontology1230214892.owl#acceptableSensorValue

     boolean getAcceptableSensorValue(OntModel ontModel);

    RDFProperty getAcceptableSensorValueProperty();

    boolean hasAcceptableSensorValue();

    void setAcceptableSensorValue(boolean newAcceptableSensorValue);


    //Property http://www.owl-ontologies.com/Ontology1230214892.owl#associatedActuators

    Collection getAssociatedActuators();

    RDFProperty getAssociatedActuatorsProperty();

    boolean hasAssociatedActuators();

    Iterator listAssociatedActuators();

    void addAssociatedActuators(Actuator newAssociatedActuators);

    void removeAssociatedActuators(Actuator oldAssociatedActuators);

    void setAssociatedActuators(Collection newAssociatedActuators);


    // Property http://www.owl-ontologies.com/Ontology1230214892.owl#valueOfService

    int getValueOfService();

    RDFProperty getValueOfServiceProperty();

    boolean hasValueOfService();

    void setValueOfService(int newValueOfService, OntModel model);


    // Property http://www.owl-ontologies.com/Ontology1230214892.owl#webServiceURI

    String getWebServiceURI();

    RDFProperty getWebServiceURIProperty();

    boolean hasWebServiceURI();

    void setWebServiceURI(String newWebServiceURI);
}
