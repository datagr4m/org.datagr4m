package org.datagr4m.drawing.editors.tables;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class JItemTableModel<T> extends AbstractTableModel {
    private static final long serialVersionUID = -7751624539918393034L;
    protected List<JItemTableEntry<T>> entries;
    protected static String[] columnNames = { 
            "Item"/*, 
            "Front",
            "Back"*/ };
    /*private static Comparator<?> comparator[]= {
            new NetworkInterfaceComparator(), 
            new DeviceComparator()};*/

    public JItemTableModel(){
    }

    public JItemTableModel(List<T> items){
        setItems(items);
    }
    
    public List<JItemTableEntry<T>> getEntries(){
        return entries;
    }
    
    public JItemTableEntry<T> getEntry(Integer id){
        return entries.get(id);
    }

    public void setItems(List<T> items){
        entries = new ArrayList<JItemTableEntry<T>>();
        for(T device: items){
            entries.add( new JItemTableEntry<T>(device) );
        }
    }
    
    @Override
	public int getColumnCount() {
        return columnNames.length;
    }

    @Override
	public int getRowCount() {
        if( entries != null )
            return entries.size();
        return 0;
    }

    @Override
	public String getColumnName(int col) {
        return columnNames[col];
    }
    
/*    public static Comparator<?> getColumnComparator(int col){
        return comparator[col];
    }*/

    @Override
	public Object getValueAt(int row, int col) {
        if( entries != null ){
            JItemTableEntry<T> entry = entries.get(row);
            if(col == 0)
                return entry.getItem().toString();
        }
        return null;
    }
    
    @Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public Class getColumnClass(int c) {
        if(c==0)
            return String.class;
        return String.class;
    }

    @Override
	public boolean isCellEditable(int row, int col) {
        return false;
    }
}
