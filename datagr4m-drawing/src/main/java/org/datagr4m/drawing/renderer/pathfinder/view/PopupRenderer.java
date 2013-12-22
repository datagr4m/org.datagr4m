package org.datagr4m.drawing.renderer.pathfinder.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.viewer.mouse.IClickableItem;
import org.datagr4m.viewer.renderer.AbstractRenderer;
import org.datagr4m.viewer.renderer.IRenderer;
import org.datagr4m.viewer.renderer.TextUtils;


public class PopupRenderer extends AbstractRenderer implements IRenderer{
    public void addPopup(Popup popup){
        popups.add(popup);
    }

    public void clearPopups(){
        popups.clear();
    }
    
    protected Color POPUP_BG = new Color(255, 150, 150);
    protected Color POPUP_BORDER = Color.BLACK;
    protected Color POPUP_TEXT = Color.RED;
    
    @Override
	public void render(Graphics2D graphic){
        for(Popup p: popups){
            float width = TextUtils.textWidth(p.getMessage())+6;
            float height = TextUtils.textHeight()+6;
            graphic.setColor(POPUP_BG);
            fillRectCentered(graphic, p.getPosition(), width, height);
            graphic.setColor(POPUP_BORDER);
            drawRectCentered(graphic, p.getPosition(), width, height);
            graphic.setColor(POPUP_TEXT);
            drawTextShifted(graphic, p.getMessage(), p.getPosition(), width/2);
        }
    }

    //@Override
    @Override
	public List<IClickableItem> hit(int x, int y) {
        throw new RuntimeException("not implemented");
    }
    
    protected List<Popup> popups = new ArrayList<Popup>();
}