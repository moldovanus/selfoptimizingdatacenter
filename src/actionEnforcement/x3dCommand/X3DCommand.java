/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package actionEnforcement.x3dCommand;

import org.web3d.x3d.sai.X3DScene;

import java.io.Serializable;

/**
 *
 * @author Me
 */
public abstract class X3DCommand implements Serializable {

    protected String nodeName;

    public final String getNodeName() {
        return nodeName;
    }
    
    public X3DCommand(String nodeName ) {
        this.nodeName = nodeName;

    }
    public abstract void execute(X3DScene mainScene);
}
