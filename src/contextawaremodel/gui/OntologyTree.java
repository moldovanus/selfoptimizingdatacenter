package contextawaremodel.gui;

import greenContextOntology.ProtegeFactory;

import javax.swing.*;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Mar 12, 2010
 * Time: 10:05:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class OntologyTree extends JFrame {
    private ProtegeFactory protegeFactory;
    private JTree myTree;

    public OntologyTree(){

        JTree tree = new JTree();
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        // Find node to which new node is to be added
         int startRow = 0; String prefix = "J";
        TreePath path = tree.getNextMatch(prefix, startRow, Position.Bias.Forward);
        MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();
        // Create new node
        MutableTreeNode newNode = new DefaultMutableTreeNode("green");
        // Insert new node as last child of node
        model.insertNodeInto(newNode, node, node.getChildCount());
    }
}
