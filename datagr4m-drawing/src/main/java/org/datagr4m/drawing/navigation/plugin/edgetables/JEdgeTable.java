package org.datagr4m.drawing.navigation.plugin.edgetables;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;

public class JEdgeTable extends JPanel {
    private static final long serialVersionUID = 2611761508429773206L;

    public JEdgeTable(String title) {
        this(title, new JEdgeTableModel() );
    }
    
    public void setEdges(List<IEdge> edges){
        model.setEdges(edges);
        model.fireTableDataChanged();
    }

    /****************** LAYOUT ********************/
    
    public JEdgeTable(String title, JEdgeTableModel model) {
        this.model = model;        
        table = new JTable(model);
        registerRenderers();
        rowSorter = new TableRowSorter<JEdgeTableModel>(model);
        registerComparators();
        table.setRowSorter(rowSorter);
        
        if( title != null && "".compareTo(title)!=0)
            label = new JLabel(title); 

        JScrollPane scrollPane = new JScrollPane(table);
        this.setLayout(new BorderLayout());
        if(label != null)
            add(label, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    protected void registerRenderers(){
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
    }
    
    /************** SELECTION **************/

    protected void addListSelectionListener(ListSelectionListener listener){
        table.getSelectionModel().addListSelectionListener(listener);
    }

    /************** RANKING **************/

    protected void registerComparators(){
        /*for (int i = 0; i < JRouteTableModel.nColumn; i++) {
            rowSorter.setComparator(0, JRouteTableModel.getColumnComparator(0));            
        }*/
    }
    
    /******************************/
    
    protected TableRowSorter<JEdgeTableModel> rowSorter;
    protected JLabel label;
    protected JTable table;
    protected JEdgeTableModel model;
}