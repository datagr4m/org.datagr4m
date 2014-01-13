package org.datagr4m.application.gui.trees.topology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.datagr4m.application.gui.trees.node.NodeCellFilterList;
import org.datagr4m.application.gui.trees.nodes.builder.INodeVisitPredicate;
import org.datagr4m.application.gui.trees.nodes.builder.ShowContentOfFirstNodeOnly;
import org.datagr4m.neo4j.topology.Neo4jTopology;
import org.datagr4m.topology.Group;
import org.datagr4m.topology.graph.IPropertyNode;
import org.datagr4m.topology.graph.NodeType;
import org.neo4j.graphdb.RelationshipType;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;

/** A tree model that supports filter to dynamically add/remove tree nodes. */
public class TopologyTreeModel extends DefaultTreeModel {
    DefaultMutableTreeNode rootTreeNode;

    public TopologyTreeModel(DefaultMutableTreeNode root) {
        super(root);
    }

    public TopologyTreeModel(String name, Neo4jTopology topology) {
        super(null);
        n = 0;
        rootTreeNode = build(name, topology);
        System.out.println(n + " tree nodes");
        treeNodes = new ArrayList<DefaultMutableTreeNode>();
        scanNode(rootTreeNode);
        setRoot(rootTreeNode);
    }

    private void scanNode(DefaultMutableTreeNode m) {
        treeNodes.add(m);

        Enumeration<DefaultMutableTreeNode> e = m.children();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode child = e.nextElement();
            scanNode(child);
        }
    }

    public DefaultMutableTreeNode getRootNode() {
        return (DefaultMutableTreeNode) getRoot();
    }

    /** Use the filter to know wether a tree node should appear or not */
    public void applyFilter(NodeCellFilterList filter) {

        DefaultMutableTreeNode root = getRootNode();
        root.removeAllChildren();

        for (DefaultMutableTreeNode node : treeNodes)
            root.add(node);
        reload();
        expandAll(true);
    }

    public void clearFilter() {
        filter = null;

        DefaultMutableTreeNode root = getRootNode();
        root.removeAllChildren();

        for (DefaultMutableTreeNode node : treeNodes) {
            root.add(node);
        }
        reload();
    }

    public void expandAll(boolean expand) {
        TreeNode root = (TreeNode) getRoot();
        expandAll(tree, new TreePath(root), expand);
    }

    protected void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        for (int i = 0; i < node.getChildCount(); i++) {
            TreeNode n = node.getChildAt(i);
            TreePath path = parent.pathByAddingChild(n);
            expandAll(tree, path, expand);
        }

        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

    /* */
    protected boolean groupByType = true;
    
    public DefaultMutableTreeNode build(String rootLabel, Neo4jTopology topology) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootLabel);
        Set<Object> traversed = new HashSet<Object>();

        Collections.sort(topology.getGroups(), compareGroup());
        for (Group<IPropertyNode> group : topology.getGroups()) {
            DefaultMutableTreeNode groupTreeNode = buildGroupTreeNode(group, traversed);
            root.add(groupTreeNode);
        }

        // handle nodes not standing in any group
        List<IPropertyNode> remainingNodes = topology.getItemsWithoutGroup();
        
        // groupe remaining nodes by type
        if (groupByType) {
            ListMultimap<NodeType, IPropertyNode> remainingNodesByType = ArrayListMultimap.create();
            for (IPropertyNode node : remainingNodes) {
                remainingNodesByType.put(node.getType(), node);
            }
            List<NodeType> types = new ArrayList<NodeType>(remainingNodesByType.keySet());
            Collections.sort(types, compareType());
            for (NodeType type : types) {
                DefaultMutableTreeNode nt = new DefaultMutableTreeNode(type);
                root.add(nt);
                
                List<IPropertyNode> nodes = remainingNodesByType.get(type);
                Collections.sort(nodes, compareNode());
                
                for (IPropertyNode n : nodes) {
                    DefaultMutableTreeNode node = build(n, traversed, 0);
                    if (node != null && !root.isNodeAncestor(node)) {
                        nt.add(node);
                    }
                }
            }

        } 
        // or simply add nodes without pregroup
        else {

            List<IPropertyNode> rankedNodes = new ArrayList<IPropertyNode>(remainingNodes);
            Collections.sort(rankedNodes, compareNode());

            for (IPropertyNode n : rankedNodes) {
                DefaultMutableTreeNode node = build(n, traversed, 0);
                if (node != null && !root.isNodeAncestor(node)) {
                    // System.out.println(Utils.blanks(0) + node);

                    root.add(node);
                }
            }
        }
        return root;
    }

    protected DefaultMutableTreeNode buildGroupTreeNode(Group<IPropertyNode> group, Set<Object> traversed) {
        // Make output tree item
        DefaultMutableTreeNode groupTreeNode = new DefaultMutableTreeNode(group);

        // Update traversed items
        Set<Object> traversed2 = new HashSet<Object>(traversed);
        traversed2.add(group);

        // Add ordered subgroups
        Collections.sort(group.getSubGroups(), compareGroup());
        for (Group<IPropertyNode> subgroup : group.getSubGroups()) {
            DefaultMutableTreeNode subgroupTreeNode = buildGroupTreeNode(subgroup, traversed2);
            groupTreeNode.add(subgroupTreeNode);
        }

        // Add ordered nodes
        Collections.sort(group, compareNode());

        for (IPropertyNode node : group) {
            DefaultMutableTreeNode nodeTreeNode = build(node, traversed2, 1);
            groupTreeNode.add(nodeTreeNode);
        }

        return groupTreeNode;
    }

    /**
     * 
     * @param node
     *            the node for which we need a GUI tree node with descendants
     * @param traversed
     *            the set of node already in the hierarchy (i.e. path to root)
     * @param depth
     *            number of time the function recursed
     * @return
     */
    public DefaultMutableTreeNode build(IPropertyNode node, Set<Object> traversed, int depth) {
        /*
         * if(isInt(node.getLabel())){ return null; }
         */

        // Make output tree item
        DefaultMutableTreeNode nodeTreeNode = new DefaultMutableTreeNode(node);

        // Update traversed items
        Set<Object> traversed2 = new HashSet<Object>(traversed);
        traversed2.add(node);

        /** verify if can continue with this node */
        if (relationshipVisitor.continueWith(traversed2, node, depth)) {
            Multimap<String, IPropertyNode> allChildren = node.getInformationNodes();

            if (allChildren != null) {
                List<String> relationshipTypes = new ArrayList<String>(allChildren.keySet());
                Collections.sort(relationshipTypes);

                for (String type : relationshipTypes) {
                    boolean foundAnyRelevantChild = false; // don't want cycle,
                                                           // so avoid continue
                                                           // on traversed items

                    // Make relation type node
                    DefaultMutableTreeNode typeTreeNode = new DefaultMutableTreeNode(type);

                    // Add (sorted) each items involved in such type of
                    // relationshiip
                    List<IPropertyNode> children = new ArrayList<IPropertyNode>(allChildren.get(type));
                    Collections.sort(children, compareNode());

                    for (IPropertyNode child : children) {
                        if (!traversed2.contains(child)) {
                            DefaultMutableTreeNode childTreeNode = build(child, traversed2, depth + 1);
                            // DefaultMutableTreeNode childNode = build(child);
                            if (childTreeNode != null) {
                                typeTreeNode.add(childTreeNode);
                                foundAnyRelevantChild = true;
                            }
                        }
                    }

                    if (foundAnyRelevantChild)
                        nodeTreeNode.add(typeTreeNode);
                }
            }
        }

        n++;
        return nodeTreeNode;
    }

    public Comparator<RelationshipType> compareRelationshipType() {
        return new Comparator<RelationshipType>() {
            @Override
            public int compare(RelationshipType arg0, RelationshipType arg1) {
                return arg0.name().compareTo(arg1.name());
            }
        };
    }

    public Comparator<IPropertyNode> compareNode() {
        return new Comparator<IPropertyNode>() {
            @Override
            public int compare(IPropertyNode n1, IPropertyNode n2) {
                if (n1.getType().equals(n2.getType())) {
                    return n1.getLabel().compareTo(n2.getLabel());
                } else {
                    return n1.getType().getName().compareTo(n2.getType().getName());
                }
            }
        };
    }
    
    public Comparator<NodeType> compareType() {
        return new Comparator<NodeType>() {
            @Override
            public int compare(NodeType n1, NodeType n2) {
                return n1.getName().compareTo(n2.getName());
            }
        };
    }

    public Comparator<Group<?>> compareGroup() {
        return new Comparator<Group<?>>() {
            @Override
            public int compare(Group<?> n1, Group<?> n2) {
                if (n1.getName() != null && n2.getName() != null) {
                    return n1.getName().compareTo(n2.getName());
                } else
                    return 0;
            }
        };
    }

    private boolean isInt(String label) {
        try {
            Integer.parseInt(label);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* */

    public JTree getTree() {
        return tree;
    }

    public void setTree(JTree tree) {
        this.tree = tree;
    }

    protected TreeNodeFilterByLabel filter;
    protected List<DefaultMutableTreeNode> treeNodes;
    protected JTree tree;

    public int n = 0;
    public INodeVisitPredicate relationshipVisitor = new ShowContentOfFirstNodeOnly();// VisitAllWithMaxDepth(3);

    private static final long serialVersionUID = 2216742431040119558L;
}
