package org.datagr4m.drawing.model.items;

import java.io.Serializable;

/**
 * The state can both serve to indicate a shape status to the layout,
 * or a rendering status to the renderer.
 * 
 * The {@link equals()} method of a state supports string comparison, so it is
 * possible to compare a state with a string.
 */
public class ItemState implements Serializable{
    private static final long serialVersionUID = -8560998774831121415L;

    public static String NONE = "";
    public static String SELECTED = "selected";
    public static String MOUSE_OVER = "mouseover";

    public static ItemState STATE_NONE = new ItemState(NONE);
    public static ItemState STATE_SELECTED = new ItemState(SELECTED);
    public static ItemState STATE_MOUSE_OVER = new ItemState(MOUSE_OVER);
    
    public ItemState(){}
    
    public ItemState(String name){
        this.name = name;
    }
    
    public String get(){
        return name;
    }

    public void set(String name){
        this.name = name;
    }
    
    public boolean isNone(){
        return NONE.equals(name);
    }

    // TODO: c'est nul, stocker un booleen pour �viter de comparer avec une chaine de char!
    public boolean isSelected(){
        return SELECTED.equals(name);
    }

    public boolean isMouseOver(){
        return MOUSE_OVER.equals(name);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        
        if (obj == null)
            return false;
        
        if (obj.getClass() == ItemState.class){
            ItemState other = (ItemState) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
        }
        else if(obj.getClass() == String.class){
            String other = (String) obj;
            if (name == null) {
                if (other != null)
                    return false;
            } else if (!name.equals(other))
                return false;
        }
        else 
            return false;
        
        return true;
    }

    /*******************/
    
    public static ItemState none(){
        return new ItemState(NONE);
    }

    public static ItemState selected(){
        return new ItemState(SELECTED);
    }

    public static ItemState mouseover(){
        return new ItemState(MOUSE_OVER);
    }

    protected String name = "";
}
