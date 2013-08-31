package org.datagr4m.drawing.renderer.items.hierarchical.tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItemIcon;
import org.datagr4m.drawing.model.items.ItemState;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.tree.TreeModel;
import org.datagr4m.drawing.renderer.items.IItemRenderer;
import org.datagr4m.drawing.renderer.items.IItemRendererSettings;
import org.datagr4m.drawing.renderer.pathfinder.view.slots.SlotableItemRenderer;
import org.datagr4m.drawing.viewer.renderer.AbstractExtendedRenderer;
import org.datagr4m.maths.geometry.functions.LinearFunction;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.renderer.DifferedRenderer;
import org.datagr4m.viewer.renderer.TextUtils;
import org.jzy3d.maths.Coord2d;


/** The item must be a DeviceKey.*/
public class TreeIconRenderer extends AbstractExtendedRenderer implements IItemRenderer{
    public TreeIconRenderer(IHierarchicalModel root, IDisplay display){
        if(root!=null)
            setModel((TreeModel)root);
        this.display = display;
        this.slotableItemRenderer = new SlotableItemRenderer();
    }
    
    @Override
    public void render(Graphics2D graphic, IBoundedItem item, IItemRendererSettings settings) {
        Coord2d c = item.getPosition();
        float radius = item.getRadialBounds();
        //String label = item.getLabel();
        
        if(item.getState().equals(ItemState.MOUSE_OVER)){
            RectangleBounds r = item.getRawRectangleBounds().shiftCenterTo(c);
            fillRect(graphic, r, settings.getNodeStatusBackgroundColor(item, item.getState()));
        }
        
        // Show bullet
        if(settings.isNodeBodyDisplayed(item)){
            Color nodeColor = settings.getNodeBodyColor(item);
            drawCircle(graphic, c, radius, 0, 0, null, nodeColor);
        }
        if(settings.isNodeBorderDisplayed(item)){
            Color borderColor = settings.getNodeBorderColor(item);
            drawCircle(graphic, c, radius, 0, 0, borderColor, null);
        }
        
        // Show icon
        if(settings.isNodeDisplayed(item)){
            if(item instanceof IBoundedItemIcon){
                IBoundedItemIcon iNode = (IBoundedItemIcon)item;
                Icon icon = iNode.getIcon();
                
                if(display!=null && icon!=null){
                    Color color = settings.getIconFilterColor(item);
                    if(color==null)
                        icon.paintIcon((Component)display, graphic, (int)c.x-icon.getIconWidth()/2, (int)c.y-icon.getIconHeight());
                    else{
                        Icon icon2 = settings.getFilteredIcon((ImageIcon)icon, color, display);
                        icon2.paintIcon((Component)display, graphic, (int)c.x-icon2.getIconWidth()/2, (int)c.y-icon2.getIconHeight());
                    }
                    
                    //icon.paintIcon((Component)display, graphic, (int)c.x-icon.getIconWidth()/2, (int)c.y-icon.getIconHeight()/2);
                }
                
                //setLineWidth(graphic, 5);
                //graphic.setColor(Color.RED);
                
                // compute direction of the text
                Coord2d nodeCenter = iNode.getPosition(); // point du centre de l'icone du node
                Coord2d pivotCenter = null; // point de pivot
                IHierarchicalModel root = iNode.getRoot();
                if(root instanceof TreeModel){
                    TreeModel model = (TreeModel)root;
                    if(model.getPivotPoint()!=null)
                        pivotCenter = model.getPivotPoint();
                }
                if(pivotCenter==null)
                    pivotCenter = iNode.getRoot().getPosition();
                
                float txtWidth = TextUtils.textWidth(item.getLabel());
                float iconRadius = (float)Math.hypot(icon.getIconWidth(),icon.getIconHeight())/2;
                
                LinearFunction lf = new LinearFunction(pivotCenter, nodeCenter);
                
                int f = 1;
                if(nodeCenter.x<pivotCenter.x)
                    f = -1;
                
                Point2D iconBorder = lf.at(nodeCenter, f*(iconRadius)); // point de bordure de l'icone
                //Point2D textStart = lf.at(nodeCenter, f*(iconRadius+txtWidth)); // point de d�part du texte
                //Point2D textStop = lf.at(nodeCenter, f*(iconRadius+txtWidth*2)); // point de fuite du texte
                
                //drawLine(graphic, p4, p3);
                //resetLineWidth(graphic);
                graphic.setColor(settings.getNodeLabelColor(item));
                if(nodeCenter.x<pivotCenter.x){
                    Point2D textStart = lf.at(nodeCenter, f*(iconRadius+txtWidth)); // point de d�part du texte
                    Point2D textStop = iconBorder;//lf.at(nodeCenter, f*(iconRadius)); // point de fuite du texte
                    drawTextOnSegment(graphic, item.getLabel(), textStart, textStop);
                }
                else{
                    Point2D textStart = lf.at(nodeCenter, f*(iconRadius)); // point de d�part du texte
                    Point2D textStop = lf.at(nodeCenter, f*(iconRadius+txtWidth)); // point de fuite du texte
                    drawTextOnSegment(graphic, item.getLabel(), textStart, textStop);
                }
            }
        }
    }
    
    public void setModel(TreeModel model){
        if(model!=null)
            root = model;
    }
    

    @Override
    public boolean hit(IBoundedItem item, int x, int y) {
        throw new RuntimeException("not implemented");
    }
    
    @Override
    public void clearDiffered() {
        differed.clear();
    }

    @Override
    public List<DifferedRenderer> getDiffered() {
        return differed;
    }
    
    @Override
    public void addDiffered(List<DifferedRenderer> differed){
        this.differed.addAll(differed);
    }
    
    @Override
    public void addDiffered(DifferedRenderer differed){
        this.differed.add(differed);
    }
    
    protected List<DifferedRenderer> differed = new ArrayList<DifferedRenderer>(0);

    /*********************/
    
    protected IDisplay display;
    protected TreeModel root;
    
    protected SlotableItemRenderer slotableItemRenderer;

}
