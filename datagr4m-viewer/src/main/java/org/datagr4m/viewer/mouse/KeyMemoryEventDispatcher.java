package org.datagr4m.viewer.mouse;

import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Ensure we have all events of a keyboard when connected to a Component.
 * In other word, the focus policy won't prevent us from memorizing key events.
 * 
 * Chain: component -> KeyboardFocusManager -> KeyEventDispatcher -> KeyEvent
 *                                             trigger event if has focus
 */
public class KeyMemoryEventDispatcher implements KeyEventDispatcher{
    KeyboardFocusManager manager;
    Component src;
    protected static boolean SHOULD_BREAK_EVENT_DISPATCHING = false;
    
    public KeyMemoryEventDispatcher(Component source){
        src = source;
        manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);        
        holdKeys = new ArrayList<Character>();
        listeners = new ArrayList<KeyListener>();
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        manager.redispatchEvent(src, e);
        handleEvent(e);
        //System.out.println( holdKeys.toString());
        return SHOULD_BREAK_EVENT_DISPATCHING;
    }
    
    protected void handleEvent(KeyEvent e){
        int id = e.getID();
        char c = e.getKeyChar();
        if(id==KeyEvent.KEY_TYPED){
            if(!holdKeys.contains(c)){
                //System.out.println("adding " + c);
                holdKeys.add(c);
            }
        }
        else if(id==KeyEvent.KEY_RELEASED){
            //System.out.println("removing " + c);
            holdKeys.remove((Object)c);
        }
        fireEvent(e);
    }
    
    public List<Character> getHoldKeys(){
        return holdKeys;
    }

    public boolean isHold(char c){
        return holdKeys.contains(c);
    }
    
    /*********/
    
    public void addKeyListener(KeyListener key){
        listeners.add(key);
    }

    public void removeKeyListener(KeyListener key){
        listeners.remove(key);
    }
    
    protected void fireEvent(KeyEvent event){
        for(KeyListener listener: listeners){
            if(event.getID()==KeyEvent.KEY_TYPED)
                listener.keyTyped(event);
            else if(event.getID()==KeyEvent.KEY_RELEASED)
                listener.keyReleased(event);
            else if(event.getID()==KeyEvent.KEY_PRESSED)
                listener.keyPressed(event);
        }
    }

    protected List<Character> holdKeys;    
    protected List<KeyListener> listeners;
}
