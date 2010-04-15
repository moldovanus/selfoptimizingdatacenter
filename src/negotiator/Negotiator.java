/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negotiator;

import greenContextOntology.Server;
import greenContextOntology.Task;

/**
 *
 * @author Administrator
 */
public interface Negotiator {
    
    public double[] negotiate(Server server, Task task);
    
}
