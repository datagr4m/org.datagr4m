package org.datagr4m.application.gui.toolbar;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.ILayoutRunnerListener;
import org.datagr4m.view2d.icons.IconLibrary;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.workspace.IWorkspace;


public class LayoutRunnerButton extends JButton{
    /**
     * 
     */
    private static final long serialVersionUID = -9014121460171483227L;

    public LayoutRunnerButton() {
        iconRunLayout = IconLibrary.createImageIconAsFile("data/images/icons/play.png");
        iconStopLayout = IconLibrary.createImageIconAsFile("data/images/icons/stop.png");

        setIcon(iconRunLayout);
        // btRunLayout.setPressedIcon(iconStopLayout);
        // btRunLayout.setDisabledIcon(iconStopLayout);
        /*
         * b.setPressedIcon(pressedIcon); b.setSelectedIcon(selectedIcon);
         * b.setRolloverIcon(rolloverIcon); b.setRolloverEnabled(true);
         */

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                //boolean isSelected = getModel().isSelected();
                // Logger.getLogger(this.getClass()).info("state:" +
                // isSelected);
                // updateBtFromModel();
                // System.out.println();
                if (runner != null) {
                    if (runner.isRunning()) {
                        runner.stop();
                        runner.oneEdgeStep();
                    } else
                        runner.start();
                }
            }
        });
        setMinimumSize(new Dimension(20, 20));
        setToolTipText("Start/stop layout");
    }

    public void plugWorkspace(IWorkspace workspace, IDisplay display) {
        if (runnerListener != null && runner != null) {
            runner.removeListener(runnerListener);
        }

        // the listener can change the button according
        // to the actual runner state (running or not)
        runnerListener = createRunnerListener();

        // create a runner and attach its listener
        runner = workspace.getRunner(display.getView());
        runner.addListener(runnerListener);
    }

    protected ILayoutRunnerListener createRunnerListener(){
        return new ILayoutRunnerListener() {
            @Override
            public void runnerStarted() {
                getModel().setSelected(true);
                updateBtFromModel();
            }

            @Override
            public void runnerStopped() {
                // System.out.println("runner stopped!");
                getModel().setSelected(false);
                updateBtFromModel();
            }

            @Override
            public void runnerFinished() {
                // System.out.println("runner stopped!");
                getModel().setSelected(false);
                updateBtFromModel();
            }
            @Override
            public void runnerFailed(String message, Exception e) {
                //System.err.println(message);
                Logger.getLogger(LayoutRunnerButton.class).info(message, e);
                getModel().setSelected(false);
                updateBtFromModel();
            }
        };
    }
    
    protected void updateBtFromModel() {
        if (getModel().isSelected())
            setIcon(iconStopLayout);
        else
            setIcon(iconRunLayout);
    }

    protected ILayoutRunner runner;
    protected ImageIcon iconRunLayout;
    protected ImageIcon iconStopLayout;

    protected ILayoutRunnerListener runnerListener;
}
