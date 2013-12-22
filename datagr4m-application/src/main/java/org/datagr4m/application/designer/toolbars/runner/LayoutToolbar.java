package org.datagr4m.application.designer.toolbars.runner;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.datagr4m.application.designer.LookAndFeel;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.ILayoutRunnerListener;
import org.datagr4m.view2d.icons.IconLibrary;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.workspace.IWorkspace;


public class LayoutToolbar extends JPanel{
	private static final long serialVersionUID = 2877155169815259266L;
	public static final int BT_WIDTH = 20;
	public static final int BT_HEIGHT = 20;
	public static final int DEFAULT_WIDTH = BT_WIDTH*1;
	public static final int DEFAULT_HEIGHT = BT_HEIGHT*1;
	
	public LayoutToolbar(){
		LookAndFeel.apply();
		
		//RUN_LAYOUT_ICON_PATH = this.getClass().getPackage().getName().replace(".", "/") + "/run.png";
	    //RUN_LAYOUT_ICON = IconLibrary.createImageIconAsResource(RUN_LAYOUT_ICON_PATH);
		//iconRunLayout = new ImageIcon(getClass().getResource("play.png"));
		//iconStopLayout = new ImageIcon(getClass().getResource("stop.png"));
		
		iconRunLayout = IconLibrary.createImageIconAsFile("data/images/icons/play.png");
        iconStopLayout = IconLibrary.createImageIconAsFile("data/images/icons/stop.png");
		
		btRunLayout = new JButton();
		btRunLayout.setIcon(iconRunLayout);
        //btRunLayout.setPressedIcon(iconStopLayout);
		//btRunLayout.setDisabledIcon(iconStopLayout);
		
		/*b.setPressedIcon(pressedIcon);
	    b.setSelectedIcon(selectedIcon);
	    b.setRolloverIcon(rolloverIcon);
	    b.setRolloverEnabled(true);*/
		
		btRunLayout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                boolean isSelected = btRunLayout.getModel().isSelected();
                Logger.getLogger(this.getClass()).info("state:" + isSelected);
                
                //updateBtFromModel();
                
                //System.out.println();
            	
                
                if(runner!=null){
            		if(runner.isRunning()){
            			runner.stop();
            			runner.oneEdgeStep();
            		}
            		else
            			runner.start();
            	}
            }
        });
        btRunLayout.setMinimumSize(new Dimension(20, 20));
        btRunLayout.setToolTipText("Start/stop layout");
        
        setLayout(new MigLayout("insets 0 0 0 0", "["+BT_WIDTH+"px]", "["+BT_HEIGHT+"20px]"));
        add(btRunLayout, "growy");
	}
	
	public void plugWorkspace(IWorkspace workspace, IDisplay display){
	    if(runnerListener!=null && runner!=null){
	        runner.removeListener(runnerListener);	        
	    }
	    
	    // the listener can change the button according
	    // to the actual runner state (running or not)
	    runnerListener = new ILayoutRunnerListener() {
	        @Override
            public void runnerStarted() {
                btRunLayout.getModel().setSelected(true);
                updateBtFromModel();
            }
            @Override
            public void runnerStopped() {
                btRunLayout.getModel().setSelected(false);
                updateBtFromModel();
            }
            @Override
            public void runnerFinished() {
                btRunLayout.getModel().setSelected(false);
                updateBtFromModel();
            }
            @Override
            public void runnerFailed(String message, Exception e) {
                btRunLayout.getModel().setSelected(false);
                updateBtFromModel();
            }
        };
	    
	    // create a runner and attach its listener
		runner = workspace.getRunner(display.getView());
		runner.addListener(runnerListener);
    }
	
	protected void updateBtFromModel(){
	    if(btRunLayout.getModel().isSelected())
	        btRunLayout.setIcon(iconStopLayout);
        else
            btRunLayout.setIcon(iconRunLayout);
	}
	
	protected ILayoutRunner runner;
    protected JButton btRunLayout;
    protected ImageIcon iconRunLayout;
    protected ImageIcon iconStopLayout;
    
    protected ILayoutRunnerListener runnerListener;
}
