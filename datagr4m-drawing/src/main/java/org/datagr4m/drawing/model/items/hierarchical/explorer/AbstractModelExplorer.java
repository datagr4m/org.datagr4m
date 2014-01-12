package org.datagr4m.drawing.model.items.hierarchical.explorer;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.viewer.IDisplay;


public abstract class AbstractModelExplorer implements IModelExplorer{
    protected JPopupMenu menu;
    protected IHierarchicalNodeModel model;

    @Override
    public void showPopupMenuController(IDisplay display, int x, int y) {
        //System.out.println(display);
        if(menu==null)
            buildPopupMenuController(display, x, y);
        else
            updatePopupMenu(display, x, y); // update action listener
        menu.show((JComponent)display, x, y);
    }

    
    protected abstract void updatePopupMenu(IDisplay display, int x, int y);
	protected abstract void buildPopupMenuController(IDisplay display, int x, int y);


	/*class PopupPrintListener implements PopupMenuListener {
    	@Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            System.out.println("Popup menu will be visible!");
        }
    	@Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            System.out.println("Popup menu will be invisible!");
        }
        @Override
		public void popupMenuCanceled(PopupMenuEvent e) {
            System.out.println("Popup menu is hidden!");
        }
    }*/
}