package org.datagr4m.drawing.editors.tables;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

public class JItemTable<T> extends JPanel {
    private static final long serialVersionUID = 2611761508429773206L;

    public JItemTable(String title) {
        this(title, new JItemTableModel<T>());
    }
    
    public JItemTable(List<T> items) {
        this("", new JItemTableModel<T>());
        setItems(items);
    }

    public JItemTable(List<T> items, JItemTableModel<T> model) {
        this("", model);
        setItems(items);
    }

    
    public JItemTable(String title, List<T> items) {
        this(title, new JItemTableModel<T>());
        setItems(items);
    }

    public void setItems(List<T> items) {
        model.setItems(items);
        model.fireTableDataChanged();
    }

    /****************** LAYOUT ********************/

    public JItemTable(String title, JItemTableModel<T> model) {
        this.model = model;
        table = new JTable(model);
        registerRenderers();
        rowSorter = new TableRowSorter<JItemTableModel<T>>(model);
        registerComparators();
        table.setRowSorter(rowSorter);

        if (title != null && "".compareTo(title) != 0)
            label = new JLabel(title);

        JScrollPane scrollPane = new JScrollPane(table);
        this.setLayout(new BorderLayout());
        if (label != null)
            add(label, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    protected void registerRenderers() {
        //table.setDefaultRenderer(INetworkInterface.class, new NetworkInterfaceRenderer());
        //table.setDefaultRenderer(IDevice.class, new DeviceRenderer());
    }

    /************** SELECTION **************/

    protected void addListSelectionListener(ListSelectionListener listener) {
        table.getSelectionModel().addListSelectionListener(listener);
    }

    /************** RANKING **************/

    protected void registerComparators() {
        /*
         * for (int i = 0; i < JRouteTableModel.nColumn; i++) {
         * rowSorter.setComparator(0, JRouteTableModel.getColumnComparator(0));
         * }
         */
    }

    /******************************/

    protected TableRowSorter<JItemTableModel<T>> rowSorter;
    protected JLabel label;
    protected JTable table;
    protected JItemTableModel<T> model;
}