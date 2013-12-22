package com.netscope.neo4j.trials.trees;

import javax.swing.JFrame;
import javax.swing.tree.DefaultMutableTreeNode;

import org.datagr4m.apps.designer.LookAndFeel;
import org.datagr4m.topology.Group;
import org.datagr4m.topology.graph.IPropertyNode;

import com.datagr4m.neo4j.apps.gui.trees.listeners.ITreeNodeSelectedListener;
import com.datagr4m.neo4j.apps.gui.trees.topology.TopologyTree;
import com.datagr4m.neo4j.topology.Neo4jTopology;
import com.datagr4m.neo4j.workspace.Neo4jWorkspace;
import com.datagr4m.neo4j.workspace.Neo4jWorkspaceSettings;

public class TrialTopologyTree {

    public static void main(String[] args) throws Exception {
        LookAndFeel.apply();
        String workspace = "doctorwho-flatgraph";
        Neo4jWorkspaceSettings settings = new Neo4jWorkspaceSettings(workspace);
        Neo4jTopology topology = Neo4jWorkspace.loadTopology(settings);
        open(topology, workspace, 300, 700);
    }

    public static void open(Neo4jTopology topology, String title, int width, int height) {
        final TopologyTree jtree = new TopologyTree(title, topology);
        ITreeNodeSelectedListener listener = new ITreeNodeSelectedListener() {
            @Override
            public void nodeSelected(DefaultMutableTreeNode node) {
                Object o = node.getUserObject();
                if(o instanceof Group<?>){
                    Group<?> group = (Group<?>)o;
                }
                else if(o instanceof IPropertyNode){
                    IPropertyNode item = (IPropertyNode)o;
                }
                //System.out.println(.getClass());
            }
        };
        jtree.addItemSelectionListener(listener);
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(jtree);
        frame.pack();
        frame.setSize(width, height);
        frame.setVisible(true);
    }
}
