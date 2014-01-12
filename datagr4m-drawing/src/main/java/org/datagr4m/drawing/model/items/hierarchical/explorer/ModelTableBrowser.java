package org.datagr4m.drawing.model.items.hierarchical.explorer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.datagr4m.drawing.editors.tables.JItemTable;
import org.datagr4m.drawing.editors.tables.JItemTableEntry;
import org.datagr4m.drawing.editors.tables.JItemTableModel;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.layered.LayeredDisplay;


public class ModelTableBrowser extends AbstractModelExplorer implements IModelExplorer {
    protected JMenuItem exploreMenuItem;
    protected ActionListener exploreActionListener;
    
    public ModelTableBrowser(IHierarchicalNodeModel model){
        this.model = model;
    }

    @Override
    protected void buildPopupMenuController(IDisplay display, int x, int y) {
        LayeredDisplay layered = display.getLayeredDisplay();
        
        menu = new JPopupMenu();
        exploreActionListener = createExploreAction(layered, x, y);
        
        // item 1
        exploreMenuItem = new JMenuItem("Explore", null);// new ImageIcon("1.gif")
        exploreMenuItem.setHorizontalTextPosition(SwingConstants.RIGHT);
        exploreMenuItem.addActionListener(exploreActionListener);
        
        // menu
        menu.add(exploreMenuItem);
        menu.setLabel("Actions");
        menu.setBorder(new BevelBorder(BevelBorder.RAISED));
        //menu.addPopupMenuListener(new PopupPrintListener());
    }
    
    @Override
    protected void updatePopupMenu(IDisplay display, int x, int y){
        if(exploreActionListener!=null)
            exploreMenuItem.removeActionListener(exploreActionListener);
        LayeredDisplay layered = display.getLayeredDisplay();
        exploreActionListener = createExploreAction(layered, x, y);
        exploreMenuItem.addActionListener(exploreActionListener);
    }
    
    /*****************/
    
    protected ActionListener createExploreAction(final LayeredDisplay layered, final int x, final int y){
        return new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent event) {
                List<IBoundedItem> content = model.getChildren();
                
                JPanel component = getGenericTable(content);
                layered.addFlatLayer(component, "", x, y, 200, 200);
                //System.out.println("added a new table: " + component);
            }
            
            public JPanel getGenericTable(List<IBoundedItem> content) {
                return new JItemTable<IBoundedItem>(content, new JItemTableModel<IBoundedItem>(){
					private static final long serialVersionUID = -4606488872259553919L;

					@Override
                    public Object getValueAt(int row, int col) {
                        if( getEntries() != null ){
                            JItemTableEntry<IBoundedItem> entry = getEntries().get(row);
                            if(col == 0)
                                return entry.getItem().getLabel();
                        }
                        return null;
                    }
                });
            }
        };
    }
}
