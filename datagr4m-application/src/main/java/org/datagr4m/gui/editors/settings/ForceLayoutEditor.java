package org.datagr4m.gui.editors.settings;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.datagr4m.drawing.layout.algorithms.forceAtlas.ForceAtlasLayout;
import org.datagr4m.viewer.Display;


public class ForceLayoutEditor extends JPanel{
    public ForceLayoutEditor(){
        this(null);
    }
    public ForceLayoutEditor(ForceAtlasLayout layout){
        super();
        setLayout(new GridLayout(7,2));
        
        addAttractionEdit();
        addRepulsionEdit();
        addGravityEdit();
        
        addStabilizeApplyEdit();
        addStabilizeInertiaEdit();
        addStabilizeStrengthEdit();

        addStartStop();

        this.layout = layout;
        mountParams(layout);
    }
    
    protected void mountParams(ForceAtlasLayout layout){
        attractionEdit.setText(layout.getAttractionStrength()+"");
        repulsionEdit.setText(layout.getRepulsionStrength()+"");
        gravityEdit.setText(layout.getGravity()+"");
        stabilizeApplyEdit.setSelected(layout.isFreezeBalance());
        stabilizeInertiaEdit.setText(layout.getFreezeInertia()+"");
        stabilizeStrengthEdit.setText(layout.getFreezeStrength()+"");
    }
    
    /**************************/
    
    protected void addAttractionEdit(){
        attractionEdit = new JTextField();
        attractionEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                double value = parse(attractionEdit.getText());
                System.out.println("edit attraction: " + value);
                layout.setAttractionStrength(value);
            }
        });
        add(new JLabel("Attraction"));
        add(attractionEdit);        
    }
    
    protected void addRepulsionEdit(){
        repulsionEdit = new JTextField();
        repulsionEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                double value = parse(repulsionEdit.getText());
                System.out.println("edit repulsion: " + value);
                layout.setRepulsionStrength(value);
            }
        });
        add(new JLabel("Repulsion"));
        add(repulsionEdit);        
    }
    
    protected void addGravityEdit(){
        gravityEdit = new JTextField();
        gravityEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                double value = parse(gravityEdit.getText());
                System.out.println("edit gravity: " + value);
                layout.setGravity(value);
            }
        });
        add(new JLabel("Gravity"));
        add(gravityEdit);        
    }
    
    protected void addStabilizeApplyEdit(){
        stabilizeApplyEdit = new JCheckBox();
        stabilizeApplyEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("edit stabilize apply: " + stabilizeApplyEdit.isSelected());
                layout.setFreezeBalance(stabilizeApplyEdit.isSelected());
            }
        });
        add(new JLabel("Apply stabilization"));
        add(stabilizeApplyEdit);       
    }
    
    protected void addStabilizeInertiaEdit(){
        stabilizeInertiaEdit = new JTextField();
        stabilizeInertiaEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                double value = parse(stabilizeInertiaEdit.getText());
                System.out.println("edit stabilize inertia: " + value);
                layout.setFreezeInertia(value);
            }
        });
        add(new JLabel("Stabilize intertia"));
        add(stabilizeInertiaEdit);        
    }
    
    protected void addStabilizeStrengthEdit(){
        stabilizeStrengthEdit = new JTextField();
        stabilizeStrengthEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                double value = parse(stabilizeStrengthEdit.getText());
                System.out.println("edit stabilize strength: " + value);
                layout.setFreezeStrength(value);
            }
        });
        add(new JLabel("Stabilize strength"));
        add(stabilizeStrengthEdit);        
    }
    
    protected void addStartStop(){
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                doRun = true;
                if(runner==null){
                    runner = getRunner();
                    runner.start();
                }
            }
        });
        add(startButton);
        
        stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                doRun = false;
                runner = null;
            }
        });
        add(stopButton);
        
    }
    
    protected double parse(String value){
        return Double.parseDouble(value);
    }
    
    protected Thread getRunner(){
        return new Thread(new Runnable(){
            @Override
			public void run(){
                if(layout!=null)
                    while (doRun) {
                        layout.goAlgo();
                    }
            }
        });
    }
    
    /**************************/
    
    protected JTextField attractionEdit;
    protected JTextField repulsionEdit;
    protected JTextField gravityEdit;
    protected JCheckBox  stabilizeApplyEdit;
    protected JTextField stabilizeInertiaEdit;
    protected JTextField stabilizeStrengthEdit;
    protected JButton startButton;
    protected JButton stopButton;
    protected Thread runner;
    protected boolean doRun = false;
    
    protected ForceAtlasLayout layout;
    
    /**************************/
    
    public static void main(String[] args){
        Display.show(new ForceLayoutEditor(), new Dimension(300,200));
    }
    
    private static final long serialVersionUID = 2689470123482571210L;

}
