package org.datagr4m.drawing.renderer.items.hierarchical.graph.forceHighlight;

import java.awt.Color;

import org.datagr4m.drawing.layout.algorithms.forces.ForceIndex;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.renderer.items.IItemRendererSettings;
import org.datagr4m.drawing.renderer.items.ItemRendererSettings;


public class NodeForceRendererSettings extends ItemRendererSettings implements IItemRendererSettings{
    protected Color DEFAULT_COLOR = null;//Color.WHITE;
    
    public NodeForceRendererSettings(Selection selection, ForceIndex fi){
        this.selection = selection;
        this.fi = fi;
    }
    
    @Override
    public Color getNodeBodyColor(IBoundedItem item) {
        if(!selection.hasSelection())
            return DEFAULT_COLOR;
        else{
            if(selection.isSelected(item))
                return Color.CYAN;
            else{
                IBoundedItem selected = selection.getSelected();
                
                boolean isAttractor = fi.getAttractors(selected).contains(item);
                boolean isRepulsor = fi.getRepulsors(selected).contains(item);
                
                if(isAttractor && isRepulsor)
                    return Color.ORANGE;
                else if(isAttractor)
                    return Color.YELLOW;
                else if(isRepulsor)
                    return Color.RED;
                else
                    return DEFAULT_COLOR;
            }
        }
    }

    protected Selection selection;
    protected ForceIndex fi;
}
