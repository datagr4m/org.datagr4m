package org.datagr4m.application.gui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import org.datagr4m.application.Datagr4mWorkspaceController;
import org.datagr4m.application.designer.IDesigner;
import org.datagr4m.application.designer.IDesktopDesigner;
import org.datagr4m.application.designer.PopupHelper;
import org.datagr4m.workspace.IWorkspace;
import org.datagr4m.workspace.IWorkspaceController;

/**
 * Simply add menu to a {@link IDesigner} by creating an instance of builder.
 * 
 * @author martin
 */
public class MenuBuilder {
    public MenuBuilder(IDesktopDesigner designer) {
        this.designer = designer;
        this.frame = (JFrame) designer; // before building
        //this.wizardMenuProvider = new WorkspaceWizardMenuProvider(designer);
        this.menuBar = buildMenuBar();
        this.frame.setJMenuBar(menuBar);

    }

    protected JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(buildFileMenu());
        // wizardMenuProvider.addWorkspaceWizardMenu(menuBar);
        // menuBar.add(buildToolsMenu());
        // menuBar.add(buildHelpMenu());
        // menuBar.add(buildSampleMenu1());
        return menuBar;
    }

    /******* FILE MENU ********/

    public JMenu buildFileMenu() {
        JMenu menu = new JMenu("File");

        menu.setMnemonic(KeyEvent.VK_F);
        menuOpenWorkspace(menu);
        menuSaveWorkspaceMap(menu);
        menuQuit(menu);
        return menu;
    }

    protected void menuNewWorkspace(JMenu menu) {
        JMenuItem newWorkspaceItem = new JMenuItem("New Workspace...");
        newWorkspaceItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                throw new RuntimeException("not implemented");
            	//((MultiWizardPopupController) designer.getDataController()).showWorkspaceCreateQuestion();
            }
        });
        // newWorkspaceItem.setMnemonic(KeyEvent.VK_A);
        newWorkspaceItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        // menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");

        menu.add(newWorkspaceItem);
    }

    protected void menuOpenWorkspace(JMenu menu) {
        JMenuItem openAnalysisItem = new JMenuItem("Open Workspace...");
        openAnalysisItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Thread t = new Thread(new Runnable() {
                    @Override
					public void run() {
                    	IWorkspaceController wc = designer.getDataController();
                        IWorkspace w = wc.getCurrentWorkspace();
                        if (w != null)
                            w.shutdown();
                        
                        if(wc instanceof Datagr4mWorkspaceController){
                        	Datagr4mWorkspaceController nwc = (Datagr4mWorkspaceController)wc;
                        	nwc.showWorkspaceSelectGUI();
                        }
                    	
                        //designer.getDataController().
                        //designer.openLoadWorkspaceWizard();
                    }
                });
                t.start();
            }
        });
        openAnalysisItem.setMnemonic(KeyEvent.VK_A);
        openAnalysisItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        // menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");

        menu.add(openAnalysisItem);
    }

    protected void menuSaveWorkspaceMap(JMenu menu) {
        JMenuItem saveMap = new JMenuItem("Save map");
        saveMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Thread d = new Thread(new Runnable() { // separate thread to
                                                       // avoid GUI deadlock
                            @Override
                            public void run() {
                                actionSaveWorkspaceMap();
                            }
                        });
                d.start();
            }
        });
        // newWorkspaceItem.setMnemonic(KeyEvent.VK_A);
        saveMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        // menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");

        menu.add(saveMap);
    }

    protected void actionSaveWorkspaceMap() {
    	throw new RuntimeException("not implemented");
    	/*
        designer.getWizardController().showPleaseWait("Please wait while saving workspace map");

        designer.toJFrame().repaint();

        Workspace w = designer.getDataController().getCurrentWorkspace();
        BinaryWorkspaceIO bio = new BinaryWorkspaceIO();
        String file = designer.getDataController().getSettings().getWorkspaceFolder() + "/map.bin";
        try {
            bio.saveMap(file, w);
            // Thread.sleep(5000);
        } catch (Exception e) {
            designer.getWizardController().hidePleaseWait();
            designer.popup("Error while saving the workspace map", e);
        }
        designer.getWizardController().hidePleaseWait();*/
    }

    protected void menuQuit(JMenu menu) {
        JMenuItem quitItem = new JMenuItem("Quit", KeyEvent.VK_Q);
        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                designer.getDataController().getCurrentWorkspace().shutdown();
                System.exit(0);
            }
        });
        menu.add(quitItem);
    }

    /********* HELP MENU ********/

    public JMenu buildHelpMenu() {
        JMenu menu = new JMenu("Help");
        // menu.setMnemonic(KeyEvent.VK_F1);
        menuOpenDocumentation(menu);
        // menuQueryLicense(menu);
        return menu;
    }

    protected void menuOpenDocumentation(JMenu menu) {
        JMenuItem openDocumentation = new JMenuItem("Documentation...");
        openDocumentation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    Desktop.getDesktop().open(new File("data/doc/documentation.html"));
                } catch (IOException e) {
                    PopupHelper.popup(frame, e);
                }
            }
        });
        openDocumentation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        menu.add(openDocumentation);
    }

    protected void encryptionError(Exception e) {
        PopupHelper.popup(frame, e);
    }

    protected void ioError(Exception e) {
        PopupHelper.popup(frame, e);
    }

    public JMenu buildSampleMenu1() {
        // Build the first menu.
        JMenu menu = new JMenu("A Menu");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        // menuBar.add(menu);

        // a group of JMenuItems
        JMenuItem menuItem = new JMenuItem("A text-only menu item", KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
        menu.add(menuItem);

        menuItem = new JMenuItem("Both text and icon", new ImageIcon("images/middle.gif"));
        menuItem.setMnemonic(KeyEvent.VK_B);
        menu.add(menuItem);

        menuItem = new JMenuItem(new ImageIcon("images/middle.gif"));
        menuItem.setMnemonic(KeyEvent.VK_D);
        menu.add(menuItem);

        // a group of radio button menu items
        menu.addSeparator();
        ButtonGroup group = new ButtonGroup();
        rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
        rbMenuItem.setSelected(true);
        rbMenuItem.setMnemonic(KeyEvent.VK_R);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Another one");
        rbMenuItem.setMnemonic(KeyEvent.VK_O);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        // a group of check box menu items
        menu.addSeparator();
        cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
        cbMenuItem.setMnemonic(KeyEvent.VK_C);
        menu.add(cbMenuItem);

        cbMenuItem = new JCheckBoxMenuItem("Another one");
        cbMenuItem.setMnemonic(KeyEvent.VK_H);
        menu.add(cbMenuItem);

        // a submenu
        menu.addSeparator();
        submenu = new JMenu("A submenu");
        submenu.setMnemonic(KeyEvent.VK_S);

        menuItem = new JMenuItem("An item in the submenu");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
        submenu.add(menuItem);

        menuItem = new JMenuItem("Another item");
        submenu.add(menuItem);
        menu.add(submenu);

        return menu;
    }

    JMenuBar menuBar;
    JMenu menu, submenu;
    JMenuItem menuItem;
    JRadioButtonMenuItem rbMenuItem;
    JCheckBoxMenuItem cbMenuItem;

    JFrame frame;

    protected IDesktopDesigner designer;
}
