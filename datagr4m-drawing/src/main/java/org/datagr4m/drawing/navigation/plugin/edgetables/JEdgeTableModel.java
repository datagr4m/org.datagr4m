package org.datagr4m.drawing.navigation.plugin.edgetables;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.infos.IEdgeInfo;

public class JEdgeTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -7751624539918393034L;

    private List<JEdgeTableEntry> entries;

    private static String[] columnNames = { "info", "source", "target" };

    /*
     * private static Comparator<?> comparator[]= { new NetworkInterfaceComparator(), new DeviceComparator()};
     */

    public JEdgeTableModel() {
    }

    public JEdgeTableModel(List<IEdge> edges) {
        setEdges(edges);
    }

    public JEdgeTableEntry getEntry(Integer id) {
        return entries.get(id);
    }

    /*******************/

    // to be overriden
    public void setEdges(List<IEdge> edges) {
        entries = new ArrayList<JEdgeTableEntry>();
        for (IEdge edge : edges) {
            entries.add(new JEdgeTableEntry(edge));
        }
    }

    /*******************/

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        if (entries != null)
            return entries.size();
        return 0;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

/*
 * public static Comparator<?> getColumnComparator(int col){ return comparator[col]; }
 */

    public Object getValueAt(int row, int col) {
        if (entries != null) {
            JEdgeTableEntry entry = entries.get(row);
            if (col == 0)
                return first(entry.getEdge().getEdgeInfo());
            else if (col == 1)
                return entry.getEdge().getSourceItem().getLabel();
            else if (col == 2)
                return entry.getEdge().getTargetItem().getLabel();
        }
        return null;
    }

    public static String first(List<IEdgeInfo> info) {
        if (info.size() > 0) {
            return first(info.get(0));
        }
        return "n/a";
    }

    public static String first(IEdgeInfo info) {
        if (info == null)
            return "null";
        return info.toString();
    }

    public static List<String> flattenInfoAsString(IEdge edge, List<IEdgeInfo> info) {
        List<String> infos = new ArrayList<String>();
        for (IEdgeInfo i : info) {
            infos.add(i.toString());
        }
        return infos;
    }

    public static List<String> flattenInfoAsString(IEdgeInfo info) {
        List<String> infos = new ArrayList<String>();
        infos.add(info.toString());
        return infos;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Class getColumnClass(int c) {
        if (c == 0)
            return String.class;
        else if (c == 1)
            return Integer.class;
        else if (c == 2)
            return String.class;
        return String.class;
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }
}
