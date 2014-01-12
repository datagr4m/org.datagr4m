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
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;
import org.datagr4m.drawing.layout.hierarchical.pair.HierarchicalPairLayout;
import org.datagr4m.drawing.layout.hierarchical.visitor.AbstractLayoutParameterVisitor;
import org.datagr4m.viewer.Display;


public class ForceLayoutEditorUI extends JPanel{
    public ForceLayoutEditorUI(){
        this(null);
    }
    public ForceLayoutEditorUI(boolean showStartStop){
        this(null, showStartStop);
    }
    public ForceLayoutEditorUI(IHierarchicalNodeLayout root){
        this(root, true);
    }
    
    public ForceLayoutEditorUI(IHierarchicalNodeLayout root, boolean showStartStop){
        super();
        int n = 13;
        if(!showStartStop)
            n--;
        setLayout(new GridLayout(n,2));
        
        addPairMarginEdit();
        
        addAttractionEdit();
        addRepulsionEdit();
        addGravityEdit();
        addDistributeAttractionEdit();
        
        addStabilizeApplyEdit();
        addStabilizeInertiaEdit();
        addStabilizeStrengthEdit();
        
        addMaxDisplacementEdit();
        addSpeedEdit();
        addInertiaEdit();
        addCoolingEdit();

        if(showStartStop)
            addStartStop();

        if(root!=null)
            setRootLayout(root);
    }
    
    public IHierarchicalNodeLayout getRootLayout() {
        return root;
    }
    
    public void setRootLayout(IHierarchicalNodeLayout root) {
        this.root = root;
        mountAll(root);
    }
    
    /****************************/
    
    protected void mountAll(IHierarchicalNodeLayout root){
        AbstractLayoutParameterVisitor v = new AbstractLayoutParameterVisitor() {
            @Override
            public void editForceAtlas(ForceAtlasLayout layout) {
                mountForceAtlasParams(layout);
            }
            @Override
            public void editPairLayout(HierarchicalPairLayout layout) {
                mountPairParams(layout);
            }
        };
        v.visit(root);
    }
    
    protected void updateAll(IHierarchicalNodeLayout root){
        AbstractLayoutParameterVisitor v = new AbstractLayoutParameterVisitor() {
            @Override
            public void editForceAtlas(ForceAtlasLayout layout) {
                updateForceAtlasParams(layout);
            }
            @Override
            public void editPairLayout(HierarchicalPairLayout layout) {
                updatePairParams(layout);
            }
        };
        v.visit(root);
    }
    
    protected void mountForceAtlasParams(ForceAtlasLayout layout){
        attractionEdit.setText(layout.getAttractionStrength()+"");
        repulsionEdit.setText(layout.getRepulsionStrength()+"");
        gravityEdit.setText(layout.getGravity()+"");
        stabilizeApplyEdit.setSelected(layout.isFreezeBalance());
        stabilizeInertiaEdit.setText(layout.getFreezeInertia()+"");
        stabilizeStrengthEdit.setText(layout.getFreezeStrength()+"");
        distribAttractionEdit.setSelected(layout.isOutboundAttractionDistribution());
        maxDisplacementEdit.setText(layout.getMaxDisplacement()+"");
        speedEdit.setText(layout.getSpeed()+"");
        inertiaEdit.setText(layout.getInertia()+"");
        coolingEdit.setText(layout.getCooling()+"");
    }
    
    protected void updateForceAtlasParams(ForceAtlasLayout layout){
        layout.setAdjustSizes(true); /*layout.setAdjustSizes(adjustSizes);*/
        
        layout.setAttractionStrength(parse(attractionEdit.getText()));
        layout.setRepulsionStrength(parse(repulsionEdit.getText()));
        layout.setGravity(parse(gravityEdit.getText()));
        layout.setFreezeBalance(stabilizeApplyEdit.isSelected());
        layout.setFreezeInertia(parse(stabilizeInertiaEdit.getText()));
        layout.setFreezeStrength(parse(stabilizeStrengthEdit.getText()));
        layout.setOutboundAttractionDistribution(distribAttractionEdit.isSelected());
        layout.setCooling(parse(coolingEdit.getText()));
        layout.setInertia(parse(inertiaEdit.getText()));
        layout.setMaxDisplacement(parse(maxDisplacementEdit.getText()));
        layout.setSpeed(parse(speedEdit.getText()));
    }
    
    protected void mountPairParams(HierarchicalPairLayout layout){
        pairMarginEdit.setText(layout.getInterItemMargin()+"");
    }
    
    protected void updatePairParams(HierarchicalPairLayout layout){
        layout.setInterItemMargin((int)parse(pairMarginEdit.getText()));
        layout.goAlgo();
    }

    
    /**************************/
    
    protected void addPairMarginEdit(){
        pairMarginEdit = new JTextField();
        pairMarginEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                updateAll(root);
            }
        });
        add(new JLabel("Pair margin"));
        add(pairMarginEdit);        
    }
    
    protected void addAttractionEdit(){
        attractionEdit = new JTextField();
        attractionEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                updateAll(root);
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
                updateAll(root);
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
                updateAll(root);
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
                updateAll(root);
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
                updateAll(root);
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
                updateAll(root);
            }
        });
        add(new JLabel("Stabilize strength"));
        add(stabilizeStrengthEdit);        
    }
    
    protected void addDistributeAttractionEdit(){
        distribAttractionEdit = new JCheckBox();
        distribAttractionEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                updateAll(root);
            }
        });
        add(new JLabel("Distribute attraction"));
        add(distribAttractionEdit);       
    }
    
    protected void addMaxDisplacementEdit(){
        maxDisplacementEdit = new JTextField();
        maxDisplacementEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                updateAll(root);
            }
        });
        add(new JLabel("Max. displacement"));
        add(maxDisplacementEdit);       
    }
    
    protected void addSpeedEdit(){
        speedEdit = new JTextField();
        speedEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                updateAll(root);
            }
        });
        add(new JLabel("Speed"));
        add(speedEdit);       
    }
    
    protected void addInertiaEdit(){
        inertiaEdit = new JTextField();
        inertiaEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                updateAll(root);
            }
        });
        add(new JLabel("Inertia"));
        add(inertiaEdit);       
    }
    
    protected void addCoolingEdit(){
        coolingEdit = new JTextField();
        coolingEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                updateAll(root);
            }
        });
        add(new JLabel("Cooling"));
        add(coolingEdit);       
    }
    
    /**********/
    
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
    
    /**********/
    
    protected double parse(String value){
        return Double.parseDouble(value);
    }
    
    protected Thread getRunner(){
        return new Thread(new Runnable(){
            @Override
			public void run(){
                if(root!=null)
                    while (doRun) {
                        root.goAlgo();
                    }
            }
        });
    }
    
    /**************************/

    protected JTextField pairMarginEdit;

    protected JTextField attractionEdit;
    protected JTextField repulsionEdit;
    protected JTextField gravityEdit;
    protected JCheckBox  stabilizeApplyEdit;
    protected JTextField stabilizeInertiaEdit;
    protected JTextField stabilizeStrengthEdit;
    protected JCheckBox  distribAttractionEdit;
    protected JTextField inertiaEdit;
    protected JTextField coolingEdit;
    protected JTextField maxDisplacementEdit;
    protected JTextField speedEdit;
    protected JButton startButton;
    protected JButton stopButton;
    protected Thread runner;
    protected boolean doRun = false;
    
    protected IHierarchicalNodeLayout root;
    
    /**************************/
    
    public static void main(String[] args){
        Display.show(new ForceLayoutEditorUI(), new Dimension(300,200));
    }
    
    private static final long serialVersionUID = 2689470123482571210L;

}
