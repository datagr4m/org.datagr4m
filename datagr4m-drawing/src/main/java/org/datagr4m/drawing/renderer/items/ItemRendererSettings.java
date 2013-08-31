package org.datagr4m.drawing.renderer.items;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.BoundsType;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemState;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.pair.IHierarchicalPairModel;
import org.datagr4m.viewer.IDisplay;


public class ItemRendererSettings implements IItemRendererSettings {
    /*** BODY COLOR ***/
    
    @Override
    public Color getNodeBodyColor(IBoundedItem item) {
        if(item instanceof IHierarchicalPairModel){
            return null;
        }
        else{
            return nodeBodyColorMap.get(item);            
        }
    }

    @Override
    public void setNodeBodyColor(IBoundedItem item, Color color) {
        nodeBodyColorMap.put(item, color);
    }
    
    /*** BORDER COLOR ***/

    @Override
    public Color getNodeBorderColor(IBoundedItem item) {
        Color color = nodeBorderColorMap.get(item);
        if (color == null) {
            if (item instanceof IHierarchicalModel){
                // pair only have border on roll over
                if(item instanceof IHierarchicalPairModel)
                 // direct return: do not store a contextual color (depend on mouse over)
                    return onlyIfRollOver(item, Color.BLACK); 
                // other models have black border
                else
                    color = Color.BLACK;
            }
            // items have gray border, like tubes
            else{
                color = Color.DARK_GRAY;
            }
            nodeBorderColorMap.put(item, color);
        }
        return color;
    }
    
    protected Color onlyIfRollOver(IBoundedItem item, Color c){
        if(item.getState().isMouseOver())
            return c;
        else
            return null;
    }

    @Override
    public void setNodeBorderColor(IBoundedItem item, Color color) {
        nodeBorderColorMap.put(item, color);
    }
    
    /*** LABEL COLOR ***/

    @Override
    public Color getNodeLabelColor(IBoundedItem item) {
        Color color = nodeLabelColorMap.get(item);
        if (color == null) {
            color = Color.BLACK;
            nodeLabelColorMap.put(item, color);
        }
        return color;
    }

    @Override
    public void setNodeLabelColor(IBoundedItem item, Color color) {
        nodeLabelColorMap.put(item, color);
    }
    
    /**
     * A non scaled label will never scale with zoom level, and will remain
     * readable. Consequently, if lot of label are unscaled, they slow down any
     * possible label algorithm.
     */
    @Override
    public boolean isNodeLabelScaled(IBoundedItem item) {
        if(item instanceof IHierarchicalModel){
            /*Group<?> group = (Group<?>)item.getObject();
            if(group.depth()>0)
                return false;
            else
                return true;*/
            return false;
        }
        else
            return true;
    }

    @Override
    public boolean setNodeLabelScaled(IBoundedItem item, boolean scaled) {
        throw new RuntimeException("not implemented");
    }

    /*** ICON FILTER COLOR ***/
    
    @Override
    public ImageIcon getFilteredIcon(ImageIcon icon, Color filter, IDisplay display){
        ImageIcon filtered = filteredIconCache.get(key(icon,filter));
        if(filtered==null){
            Image img = icon.getImage();
            ImageProducer producer = new FilteredImageSource(img.getSource(), new RGBGrayFilter());
            filtered = new ImageIcon(((Component)display).createImage(producer));
            filteredIconCache.put(key(icon,filter), filtered);
        }
        return filtered;
    }
    
    @Override
    public Color getIconFilterColor(IBoundedItem item) {
        return iconFilterColor.get(item);
    }
    
    @Override
    public void setNodeIconFilterColor(IBoundedItem item, Color color) {
        iconFilterColor.put(item, color);
    }


    protected Pair<ImageIcon, Color> key(ImageIcon icon, Color color) {
        return new Pair<ImageIcon, Color>(icon, color);
    }

    protected Map<IBoundedItem, Color> iconFilterColor = new HashMap<IBoundedItem, Color>();
    protected Map<Pair<ImageIcon, Color>, ImageIcon> filteredIconCache = new HashMap<Pair<ImageIcon, Color>, ImageIcon>();

    /*** DISPLAYED STATUS ***/
    
    @Override
    public boolean isNodeLabelDisplayed(IBoundedItem item) {
        return true;
    }

    @Override
    public boolean isNodeBodyDisplayed(IBoundedItem item) {
        return true;
    }

    @Override
    public boolean isNodeBorderDisplayed(IBoundedItem item) {
        /*String label = item.getLabel();
        if(label!=null)
            if(label.contains("module")||label.contains("stratum"))
                return false;*/
        return nodeBorderDisplayed;
    }

    @Override
    public void setNodeBorderDisplayed(IBoundedItem item, boolean status) {
        nodeBorderDisplayed = status;
    }

    @Override
    public boolean isNodeDisplayed(IBoundedItem item) {
        Boolean i = nodeDisplayedMap.get(item);
        if (i == null)
            return true;
        else
            return i;
    }

    @Override
    public void setNodeDisplayed(IBoundedItem item, boolean displayed) {
        nodeDisplayedMap.put(item, displayed);
    }

    @Override
    public boolean isNodeCenterDisplayed(IBoundedItem item) {
        Boolean i = nodeCenterDisplayedMap.get(item);
        if (i == null)
            return false;
        else
            return i;
    }

    @Override
    public void setNodeCenterDisplayed(IBoundedItem item, boolean displayed) {
        nodeCenterDisplayedMap.put(item, displayed);
    }

    @Override
    public Color getNodeStatusBackgroundColor(IBoundedItem item, ItemState state) {
        return Color.PINK;
    }
    
    
    @Override
    public boolean isNodeBoundsDisplayed(IBoundedItem item) {
        return nodeBoundsDisplayed;
    }

    @Override
    public void setNodeBoundsDisplayed(IBoundedItem item, boolean status) {
        this.nodeBoundsDisplayed = status;
    }
    
    @Override
    public void setNodeBoundsTypeColor(BoundsType type, Color color){
        boundsTypeColor.put(type, color);
    }
    
    @Override 
    public Color getNodeBoundsTypeColor(BoundsType type){
        Color c = boundsTypeColor.get(type);
        if(c==null){
            c = DEFAULT_BOUNDS_COLOR;
            boundsTypeColor.put(type, c);
        }
        return c;
    }


    protected Map<IBoundedItem, Boolean> nodeDisplayedMap = new HashMap<IBoundedItem, Boolean>();
    protected Map<IBoundedItem, Boolean> nodeCenterDisplayedMap = new HashMap<IBoundedItem, Boolean>();
    protected Map<IBoundedItem, Color> nodeBodyColorMap = new HashMap<IBoundedItem, Color>();
    protected Map<IBoundedItem, Color> nodeBorderColorMap = new HashMap<IBoundedItem, Color>();
    protected Map<IBoundedItem, Color> nodeLabelColorMap = new HashMap<IBoundedItem, Color>();
    protected boolean nodeBorderDisplayed = true;
    protected boolean nodeBoundsDisplayed = false;

    protected Map<BoundsType, Color> boundsTypeColor = new HashMap<BoundsType, Color>();
    protected static Color DEFAULT_BOUNDS_COLOR = Color.GRAY;
}
