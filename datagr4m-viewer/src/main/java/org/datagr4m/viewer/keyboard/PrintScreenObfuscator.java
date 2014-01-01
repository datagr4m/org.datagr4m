package org.datagr4m.viewer.keyboard;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.datagr4m.viewer.Display;


public class PrintScreenObfuscator extends KeyAdapter{
    public PrintScreenObfuscator(Display display){
        display.addKeyListener(this);
    }
    
    @Override
	public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_PRINTSCREEN)
            System.out.println("print pressed");
        else
            System.out.println("another pressed");
    }
    
    @Override
	public void keyReleased(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_PRINTSCREEN)
            System.out.println("print release");
        else
            System.out.println("another release");
    }
    
    @Override
	public void keyTyped(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_PRINTSCREEN)
            System.out.println("print typed");
        else
            System.out.println("another typed");
    }
}
