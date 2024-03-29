/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utils.negotiator;

import ontologyRepresentations.greenContextOntology.Server;
import ontologyRepresentations.greenContextOntology.Task;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Administrator
 */
public interface Negotiator extends Serializable {

    public static final String NEGOTIATED_CPU = "CPU";
    public static final String NEGOTIATED_MEMORY = "MEMORY";
    public static final String NEGOTIATED_STORAGE = "STORAGE";

    public Map<String,Double> negotiate(Server server, Task task);
}
