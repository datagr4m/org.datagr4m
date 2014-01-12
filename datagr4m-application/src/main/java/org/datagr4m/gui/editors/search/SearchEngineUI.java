package org.datagr4m.gui.editors.search;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.visitor.ItemLabelFinder;


public class SearchEngineUI extends JPanel{
    public static int DEFAULT_HEIGHT = 20;
    
    public SearchEngineUI(){
        search = new JTextField();
        search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
			public void changedUpdate(DocumentEvent e) {
                find(search.getText());
            }
            @Override
			public void insertUpdate(DocumentEvent e) {
                find(search.getText());
            }
            @Override
			public void removeUpdate(DocumentEvent e) {
                find(search.getText());
            }
        });
        
        setLayout(new BorderLayout());
        add(search); // col row width height
        wire();
    }
    
    protected void wire(){
        visitor = new ItemLabelFinder();
        listeners = new ArrayList<ISearchEngineListener>();        
    }
    
    protected void find(String txt){
        visitor.setSearchString(txt);
        visitor.visit(model);
        fireResults(new ArrayList<IBoundedItem>(visitor.getResults())); // widepread a copy of results
    }
    
    protected void fireResults(List<IBoundedItem> items){
        for(ISearchEngineListener listener: listeners)
            listener.searchFinished(items);
    }
    
    public void addSearchListener(ISearchEngineListener listener){
        listeners.add(listener);
    }

    public void removeSearchListener(ISearchEngineListener listener){
        listeners.remove(listener);
    }
    
    public IHierarchicalNodeModel getModel() {
        return model;
    }

    public void setModel(IHierarchicalNodeModel model) {
        this.model = model;
    }

    /************/

    protected JTextField search;
    protected JButton button;

    protected IHierarchicalNodeModel model;
    protected ItemLabelFinder visitor;
    
    protected List<ISearchEngineListener> listeners;
    
    private static final long serialVersionUID = -4655142592700918481L;
}
