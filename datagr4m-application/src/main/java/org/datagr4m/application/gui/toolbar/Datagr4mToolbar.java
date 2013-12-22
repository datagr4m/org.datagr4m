package org.datagr4m.application.gui.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.datagr4m.application.designer.IDesigner;
import org.datagr4m.gui.editors.toolbars.ModalListSelector;


public class Datagr4mToolbar extends JToolBar{
	private static final long serialVersionUID = 3810671040350396253L;

    protected IDesigner designer;
    
    public Datagr4mToolbar(IDesigner designer, String string) {
        super(string);
        this.designer = designer;
    }
    public Datagr4mToolbar() {
        super();
    }
    public Datagr4mToolbar(IDesigner designer) {
        super();
        this.designer = designer;
    }

    public static Datagr4mToolbar newToolbar(IDesigner designer, ToolbarConfiguration conf) {
        Datagr4mToolbar toolBar = new Datagr4mToolbar(designer);
        toolBar.configure(designer, conf);
        return toolBar;
    }

    protected void configure(IDesigner designer, ToolbarConfiguration conf) {
        //addButtonNew(designer);
        addButtonOpen(designer);
        addButtonSave(designer);
        
        /*addSeparator();
        if(conf.isButtonImport())
            addButtonImport();
        if(conf.isButtonParse())
            addButtonParse();
        if(conf.isButtonTopology())
            addButtonTopology();
        if(conf.isButtonLayout())
            addButtonLayout();
        if(conf.isSearch())
            addButtonSearch();*/
        
        addSeparator();
        addButtonRun();
        addSeparator();
        
        setFloatable(false);//conf.isFloatable());
        setRollover(false);
    }
    
    public LayoutRunnerButton getButtonRun() {
        return buttonRun;
    }



    LayoutRunnerButton buttonRun = new LayoutRunnerButton();
    
    protected void addButtonRun() {
        add(buttonRun);
    }
    
    protected void addButtonSearch() {
        add(new JTextField("search..."));
    }

    protected void addButtonNew(final IDesigner designer2) {
        add(makeNavigationButton(imageRoot + "out-22.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //designer.getWizardController().
                //designer.openLoadWorkspaceWizard();
            }
        }));
    }
    
    protected void addButtonOpen(final IDesigner designer2) {
        add(makeNavigationButton(imageRoot + "open-22.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	List<String> workspaces;
                try {
                    workspaces = designer2.getDataController().getWorkspaceNames();
                    Collections.sort(workspaces);
                    Object[] choice = workspaces.toArray(new Object[workspaces.size()]);// {"default","conf.and.db"};
                    String answer = ModalListSelector.ask("Open Workspace", "Please select a workspace", choice, "");

                    // if did not cancel
                    if (answer != null) {
                    	designer2.getDataController().getSettings().setName(answer);
                    	designer2.getDataController().loadWorkpaceFiles();
                        //loadWorkpaceFiles();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }));
    }
    
    protected void addButtonSave(final IDesigner designer2) {
        add(makeNavigationButton(imageRoot + "save-32.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //designer.openLoadWorkspaceWizard();
            }
        }));
    }
    
    protected void addButtonLayout() {
        add(makeNavigationButton(imageRoot + "layout.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new RuntimeException("ploum");
            }
        }));
    }

    protected void addButtonTopology() {
        add(makeNavigationButton(imageRoot + "topology.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new RuntimeException("ploum");
            }
        }));
    }

    protected void addButtonParse() {
        add(makeNavigationButton(imageRoot + "parse.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new RuntimeException("ploum");
            }
        }));
    }

    protected void addButtonImport() {
        add(makeNavigationButton(imageRoot + "import.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new RuntimeException("ploum");
            }
        }));
    }
    
    public static final int BT_WIDTH = 20;
    public static final int BT_HEIGHT = 20;
    public static final int DEFAULT_WIDTH = BT_WIDTH*1;
    public static final int DEFAULT_HEIGHT = BT_HEIGHT*1;
    
    
    

    protected JButton makeNavigationButton(String imageName, ActionListener listener) {
        return makeNavigationButton(imageName, imageName, null, listener);
    }

    protected JButton makeNavigationButton(String imageName, String altText, String toolTipText, ActionListener listener) {
        // Look for the image.
        URL imageURL = Datagr4mToolbar.class.getResource(imageName);

        // Create and initialize the button.
        JButton button = new JButton();

        if (toolTipText != null)
            button.setToolTipText(toolTipText);
        if (listener != null) {
            button.addActionListener(listener);
        }

        if (imageURL != null) { // image found
            button.setIcon(new ImageIcon(imageURL, altText));
        } else { // no image found
            button.setText(altText);
            System.err.println("Resource not found: " + imageName);
        }

        return button;
    }
    
    public static String imageRoot = "";
}
