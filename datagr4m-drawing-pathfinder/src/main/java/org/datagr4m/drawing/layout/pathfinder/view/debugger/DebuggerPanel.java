package org.datagr4m.drawing.layout.pathfinder.view.debugger;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DebuggerPanel extends JPanel implements IDebuggerListener{
    protected JButton btContinue;
    protected JTextArea txInfo;
    
    protected PathFinderDebugger debugger;

    public DebuggerPanel(final PathFinderDebugger debugger){
        debugger.addBreakListener(this);
        this.debugger = debugger;
        
        
        setLayout(new BorderLayout());

        btContinue = new JButton("OOOOOOOOOOOOOO");
        btContinue.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                debugger.continueFlow();
                btContinue.setText("running...");
            }
        });
        btContinue.setSize(200, 40);
        add(btContinue, BorderLayout.NORTH);
        
        txInfo = new JTextArea();
        txInfo.setFont(new Font("Courier New", Font.PLAIN, 12));
        add(new JScrollPane(txInfo), BorderLayout.CENTER);
    }

    @Override
    public void breakReached() {
        btContinue.setText("continue");        
    }
    
    @Override
    public void infoChanged(){
        List<String> infos = debugger.getInfos();
        String m = "";        
        for(String i: infos)
            m+=(i+"\n");
        txInfo.setText(m);        
    }
}
