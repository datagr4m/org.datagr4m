package org.datagr4m.application.gui.trees.topology;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import net.miginfocom.swing.MigLayout;

import org.datagr4m.application.gui.trees.listeners.ITreeNodeSelectedListener;
import org.datagr4m.application.gui.trees.listeners.TreeKeyListener;
import org.datagr4m.application.gui.trees.listeners.TreeMouseListener;
import org.datagr4m.application.gui.trees.node.NodeCellFilterList;
import org.datagr4m.application.neo4j.renderers.Neo4jRelationshipRendererSettings;
import org.datagr4m.topology.TopologyStatistics;

import com.datagr4m.neo4j.topology.Neo4jTopology;

public class TopologyTree extends JPanel{
    TopologyTreeModel topologyTreeModel;
    TopologyCellRenderer cellRenderer;
    public TopologyTree() {
        initGUI();
        setModel("", new Neo4jTopology());
    }

    public TopologyTree(String rootLabel, Neo4jTopology topology) {
        initGUI();
        setModel(rootLabel, topology);
    }

    public void setModel(String rootLabel, Neo4jTopology topology) {
        topologyTreeModel = new TopologyTreeModel(rootLabel, topology);
        topologyTreeModel.setTree(tree);
        //System.out.println(builder.n + " tree nodes");
        tree.setModel(topologyTreeModel);
        TopologyStatistics s = topology.getStatistics();
        statistics.setText(s.toCompactString());
    }
    
    public void setEdgeCellRenderer(Neo4jRelationshipRendererSettings s){
        cellRenderer.setEdgeRendererSettings(s);
    }

    protected void initGUI() {
        setLayout(new BorderLayout());
        
        cellRenderer = new TopologyCellRenderer();
        tree = new JTree();
        tree.setCellRenderer(cellRenderer);
        treePane = new JScrollPane(tree);
        statistics = new JLabel("");
        add(treePane);
        

        textFilter = new JTextField();
        textFilter.getDocument().addDocumentListener(new DocumentListener() {
            @Override
			public void changedUpdate(DocumentEvent e) {
                updateTreeFilters();
            }

            @Override
			public void insertUpdate(DocumentEvent e) {
                updateTreeFilters();
            }

            @Override
			public void removeUpdate(DocumentEvent e) {
                updateTreeFilters();
            }
        });

        toggle1 = new JToggleButton();
        toggle1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                expandAllInThread(true);
                //updateTreeFilters();
            }
        });
        toggle1.setMinimumSize(new Dimension(20, 20));
        toggle1.setToolTipText("Show interfaces");

        toggle2 = new JToggleButton();
        toggle2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                expandAllInThread(false);
                //updateTreeFilters();
            }
        });
        toggle2.setMinimumSize(new Dimension(20, 20));
        toggle2.setToolTipText("Show device without interface");

        // layout
        boolean showButtons = true;
        filtersPanel = new JPanel();
        if (showButtons) {
            filtersPanel.setLayout(new MigLayout("insets 0 0 0 0", "[280px][10px][10px]", "[20px]"));
            filtersPanel.add(textFilter, "growx");
            filtersPanel.add(toggle1, "growy");
            filtersPanel.add(toggle2, "growy");
        } else {
            filtersPanel.setLayout(new MigLayout("insets 0 0 0 0", "[300px]", "[20px]"));
            filtersPanel.add(textFilter, "growx");
        }

        if (filterIsDown) {
            setLayout(new MigLayout("insets 0 0 2 0", "[300px]", "[grow][20px][15px]"));
            add(treePane, "cell 0 0, grow");// ,height 400:400:600
            add(filtersPanel, "cell 0 1, height 20:20:20"); // , growy
            add(statistics, "cell 0 2, grow");
        } else {
            setLayout(new MigLayout("insets 0 0 2 0", "[grow]", "[20px][grow][15px]"));
            add(filtersPanel, "cell 0 0, height 25:25:25"); // , growy
            add(treePane, "cell 0 1, grow");// ,height 400:400:600
            add(statistics, "cell 0 2, grow");
        }
        // update filters
        // toggleHideEmptyDevices.setSelected(true);
        // toggleShowInterface.setSelected(true);
        updateTreeFilters();
    }
    
    /* */
    

    public void addItemSelectionListener(ITreeNodeSelectedListener listener) {
        tree.addMouseListener(new TreeMouseListener(listener));
        tree.addKeyListener(new TreeKeyListener(listener));
    }
    
    /* */
    
    
    public void expandAllInThread(final boolean position){
        //System.out.println("expand " + position);
        Runnable r = new Runnable(){
            @Override
            public void run() {
                expandAll(position);
            }
        };
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.start();
    }
    
    public void expandAll(boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        expandAll(tree, new TreePath(root), expand, 0);
    }

    protected void expandAll(final JTree tree, final TreePath parent, boolean expand, int depth) {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        
        if(expand || (!expand && depth <2))
        for (int i = 0; i < node.getChildCount(); i++) {
            TreeNode n = node.getChildAt(i);
            TreePath path = parent.pathByAddingChild(n);
            expandAll(tree, path, expand, depth+1);
        }

        // Expansion or collapse must be done bottom-up
        if (expand) {
            //System.out.println("open " + parent);
            tree.expandPath(parent);
        } else {
            //System.out.println("close " + parent);
            if(depth!=0)
                tree.collapsePath(parent);
        }
    }

    public void updateTreeFilters() {
        if (false) {
            filter.clear();

            // if text box provide a name or ip filter
            String input = textFilter.getText();
            if (input.length() > 0) {
                if (Character.isLetter(input.charAt(0))) {
                    // search by name
                    filter.addFilter(new TreeNodeFilterByLabel(input));
                }
            }
            if (topologyTreeModel != null)
                topologyTreeModel.applyFilter(filter);
        }
    }

    public NodeCellFilterList getFilters() {
        return filter;
    }

    /**
     * Set a filter that will be merged with built in ui filters (interface/void
     * device).
     */
    public void setFilter(NodeCellFilterList filter) {
        this.filter = filter;
    }


    /* */

    protected JTextField textFilter;
    protected NodeCellFilterList filter = new NodeCellFilterList();

    protected JToggleButton toggle1;
    protected JToggleButton toggle2;

    protected JPanel filtersPanel;

    protected boolean filterIsDown = false;

    protected JTree tree;
    protected JScrollPane treePane;
    
    protected JLabel statistics;

    private static final long serialVersionUID = -5302373110609849463L;

    
}
