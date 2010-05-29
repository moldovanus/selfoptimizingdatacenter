/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negotiator;

import greenContextOntology.Server;
import greenContextOntology.Task;

import java.util.Map;

/**
 * @author Administrator
 */
public interface Negotiator {

    public static final String NEGOTIATED_CPU = "CPU";
    public static final String NEGOTIATED_MEMORY = "MEMORY";
    public static final String NEGOTIATED_STORAGE = "STORAGE";

    public Map<String,Double> negotiate(Server server, Task task);
}
