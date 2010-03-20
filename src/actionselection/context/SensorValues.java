/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package actionselection.context;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import contextawaremodel.GlobalVars;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import selfHealingOntology.SelfHealingProtegeFactory;
import selfHealingOntology.Sensor;

/**
 *
 * @author Administrator
 */
public class SensorValues implements Serializable {

    private Map<String, Integer> myMap;

    public SensorValues() {
        myMap = new HashMap<String, Integer>();
    }

    //TODO : modified to use protege factory
    /**
     *
     * @param protegeFactory
     */
    public SensorValues(SelfHealingProtegeFactory protegeFactory) {
        myMap = new HashMap<String, Integer>();
        Collection<Sensor> sensors= protegeFactory.getAllSensorInstances();
        for ( Sensor sensor : sensors){
             myMap.put(sensor.getName().split("#")[1],sensor.getValueOfService());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SensorValues other = (SensorValues) obj;
        return this.equals(other);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.myMap != null ? this.myMap.hashCode() : 0);
        return hash;
    }

    public Integer getValue(String sensorName) {
        return myMap.get(sensorName);
    }

    public void setValue(String sensorName, Integer value) {
        myMap.put(sensorName, value);
    }

    public Map<String, Integer> getMyMap() {
        return myMap;
    }

    public void setMyMap(Map<String, Integer> myMap) {
        this.myMap = myMap;
    }

    public boolean equals(SensorValues values) {
        return myMap.equals(values.getMyMap());
    }

    @Override
    public String toString() {
        String stringValue = "";
        for (String key : myMap.keySet()) {
            stringValue += "[ " + key + ": " + myMap.get(key) + "]  ";
        }
        return stringValue;
    }


    public ArrayList<String> toMessage() {
        ArrayList<String> list = new ArrayList<String>();
        String stringValue = "";
        Map<String,Map<String,String>> map = GlobalVars.valueMapping;
        for (String key : myMap.keySet()) {
            list.add("[ " + key + ": " + myMap.get(key) + "]  ");
        }
        return list;
    }

    public ArrayList<String[]> toArrayList() {
        ArrayList<String[]> list = new ArrayList<String[]>();
        Map<String, Map<String, String>> mapping = GlobalVars.valueMapping;

        for (String key : myMap.keySet()) {
            String[] entry = new String[2];
            entry[0] = key;
            Map<String, String> valueMapping = mapping.get(key);
            if (valueMapping == null) {
                entry[1] = "" + myMap.get(key);
            } else {
                entry[1] = valueMapping.get("" + myMap.get(key));
            }
            list.add(entry);
        }
        return list;
    }
}
