package org.datagr4m.gui.editors.runners;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunner;


public class LayoutRunnerUI extends JPanel{
    public static int DEFAULT_HEIGHT = 80;

    public LayoutRunnerUI(){
        super();
        setLayout(new GridLayout(2,2));
        
        addStartStop();
        addDoRunWithEdge();
        setBorder(BorderFactory.createBevelBorder(1));
    }
        
    public void setRunner(LayoutRunner runner){
        this.runner = runner;
    }
    
    public LayoutRunner getRunner() {
        return runner;
    }

    public IHierarchicalLayout getRootLayout() {
        return root;
    }
    
    public void setRootLayout(IHierarchicalLayout root) {
        this.root = root;
        
        if(root instanceof HierarchicalGraphLayout)
            doRunWithEdge.setEnabled(true);
        else
            doRunWithEdge.setEnabled(false);
    }
    
    /**************/
    
    protected void addStartStop(){
        startButton = new JButton("Start layout");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                runner.start();
            }
        });
        add(startButton);
        
        stopButton = new JButton("Stop layout");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                runner.stop();
            }
        });
        add(stopButton);
    }
    
    protected void addDoRunWithEdge(){
        doRunWithEdge = new JCheckBox("Run with edge");
        doRunWithEdge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                runner.setDoRunEdge(doRunWithEdge.isSelected());
            }
        });
        doRunWithEdge.setSelected(false);
        add(doRunWithEdge);
        
        startEdgeButton = new JButton("One edge step");
        startEdgeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                runner.oneEdgeStep();
            }
        });
        add(startEdgeButton);
    }
            
    
    /*************/
    
    protected JButton startButton;
    protected JButton stopButton;

    protected JButton startEdgeButton;
    protected JButton stopEdgeButton;
    
    protected JCheckBox doRunWithEdge;
    
    protected LayoutRunner runner;
    
    protected IHierarchicalLayout root;
    
    private static final long serialVersionUID = -130800220679157938L;
}
