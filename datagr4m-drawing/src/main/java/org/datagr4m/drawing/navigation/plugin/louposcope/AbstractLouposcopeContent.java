package org.datagr4m.drawing.navigation.plugin.louposcope;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.maths.geometry.Pt;
import org.datagr4m.viewer.renderer.AbstractRenderer;
import org.datagr4m.viewer.renderer.CellAnchor;
import org.datagr4m.viewer.renderer.TextUtils;
import org.datagr4m.viewer.renderer.annotations.items.ClickableRectangleAnnotation;
import org.jzy3d.maths.Coord2d;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * A geometry object describing lists of {@link ClickableRectangleAnnotation}s
 * with general layout settings (width, height, margin, etc)
 * 
 * @author Martin Pernollet
 *
 * @param <T> the type of information data displayed by each {@link ClickableRectangleAnnotation}
 */
public abstract class AbstractLouposcopeContent<T> extends AbstractRenderer implements ILouposcopeContent<T>{
    public AbstractLouposcopeContent(IBoundedItem item, Collection<T> details, int width, int height, float margin, int boxMargin){
        this.item = item;
        this.details = details;
        this.width = width;
        this.height = height;
        this.margin = margin;
        this.boxMargin = boxMargin;
        this.interboxMargin = boxMargin;
        this.leftList = new ArrayList<ClickableRectangleAnnotation>();
        this.rightList = new ArrayList<ClickableRectangleAnnotation>();
        this.detailAnnotation = HashBiMap.create();
        update();
    }
    
    @Override
    public abstract void build(Collection<T> details);

    @Override
    public void update(){
        if(!built)
            build(details);

        if(item==null)
            return;
        
        Coord2d center = item.getAbsolutePosition();
        float radius = item.getRadialBounds();
        
        // left and right label trees
        leftRoot  = center.add(-radius, 0);
        rightRoot = center.add(+radius, 0);
        leftMarginPosition  = center.add(-(radius+margin), 0);
        rightMarginPosition = center.add(+(radius+margin), 0);
        
        cellHeight  = boxMargin*2+TextUtils.textHeight();
        leftHeight  = (cellHeight+interboxMargin)*leftList.size()-1;
        rightHeight = (cellHeight+interboxMargin)*rightList.size()-1;
        
        leftMarginTopPosition     = leftMarginPosition.add(0, -leftHeight/2);
        leftMarginBottomPosition  = leftMarginTopPosition.add(0, leftHeight);
        rightMarginTopPosition    = rightMarginPosition.add(0, -rightHeight/2);
        rightMarginBottomPosition = rightMarginTopPosition.add(0, rightHeight);
        
        int kLeft = 0;
        for(ClickableRectangleAnnotation a: leftList){
            Point2D boxPoint = Pt.cloneAsDoublePoint(leftMarginTopPosition.add(-boxOffset, cellHeight/2+kLeft*(cellHeight+interboxMargin)));
            a.setPosition(boxPoint);
            a.setAnchor(CellAnchor.MIDDLE_RIGHT);
            kLeft++;
        }

        int kRight = 0;
        for(ClickableRectangleAnnotation a: rightList){
            Point2D boxPoint = Pt.cloneAsDoublePoint(rightMarginTopPosition.add(+boxOffset, cellHeight/2+kRight*(cellHeight+interboxMargin)));
            a.setPosition(boxPoint);
            a.setAnchor(CellAnchor.MIDDLE_LEFT);
            kRight++;
        }

    }
    
    @Override
    public void render(Graphics2D graphic){
        Coord2d center = item.getAbsolutePosition();
        float radius = item.getRadialBounds();

        renderCircle(graphic, center, radius);
        renderLeftList(graphic);
        renderRightList(graphic);
    }

    protected void renderCircle(Graphics2D graphic, Coord2d center, float radius) {
        setLineWidth(graphic, 2);
        drawCircle(graphic, center, radius, Color.BLACK, null);
    }

    protected void renderRightList(Graphics2D graphic) {
        if(rightList.size()>0){
            setLineWidth(graphic, 2);
            drawLine(graphic, rightRoot, rightMarginPosition);
            drawLine(graphic, rightMarginTopPosition, rightMarginBottomPosition);
            setLineWidth(graphic, 1);
            for(ClickableRectangleAnnotation a: rightList)
                a.render(graphic);
        }
    }

    protected void renderLeftList(Graphics2D graphic) {
        if(leftList.size()>0){
            setLineWidth(graphic, 2);
            drawLine(graphic, leftRoot, leftMarginPosition);
            drawLine(graphic, leftMarginTopPosition, leftMarginBottomPosition);
            setLineWidth(graphic, 1);
            for(ClickableRectangleAnnotation a: leftList)
                a.render(graphic);
        }
    }
    
    /**********************/
    
    protected IBoundedItem item;
    protected Collection<T> details;
    //protected List<ClickableRectangleAnnotation> annotation;

    protected int width;
    protected int height;
    protected float margin;
    protected int boxMargin;
    protected int boxOffset = 5;
    protected int interboxMargin;
    protected float leftHeight;
    protected float rightHeight;
    protected float cellHeight;
    
    protected List<ClickableRectangleAnnotation> leftList;
    protected List<ClickableRectangleAnnotation> rightList;
    protected BiMap<T,ClickableRectangleAnnotation> detailAnnotation;
    protected boolean built = false;
    
    protected Coord2d leftRoot;
    protected Coord2d leftMarginPosition;
    protected Coord2d leftMarginTopPosition;
    protected Coord2d leftMarginBottomPosition;

    protected Coord2d rightRoot;
    protected Coord2d rightMarginPosition;
    protected Coord2d rightMarginTopPosition;
    protected Coord2d rightMarginBottomPosition;
    
}
