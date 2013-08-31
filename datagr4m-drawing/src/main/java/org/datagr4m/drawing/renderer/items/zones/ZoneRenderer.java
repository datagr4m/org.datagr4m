package org.datagr4m.drawing.renderer.items.zones;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.List;

import org.datagr4m.drawing.model.items.zones.IZoneModel;
import org.datagr4m.drawing.model.items.zones.ZoningModel;
import org.datagr4m.viewer.mouse.IClickableItem;
import org.datagr4m.viewer.renderer.IRenderer;
import org.datagr4m.viewer.renderer.hit.IHitProcessor;


public class ZoneRenderer implements IRenderer {
    ZoningModel zoning;
    
    public ZoneRenderer(ZoningModel zoning){
        this.zoning = zoning;
    }
    
    @Override
    public List<IClickableItem> hit(int x, int y) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> List<IClickableItem> hitOnly(int x, int y, Class<T> type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> List<IClickableItem> hitExcluding(int x, int y, Class<T> type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void render(Graphics2D graphic) {
        if(zoning!=null){
            graphic.setColor(new Color(1,0,0,0.5f));
            for(IZoneModel zone: zoning.getZones()){
                Polygon p = zone.getGeometry();
                //System.out.println(p.npoints + " points");
                graphic.fillPolygon(p);
            }
        }
    }

    @Override
    public void setHitProcessor(IHitProcessor hitProcessor) {
        // TODO Auto-generated method stub

    }

    @Override
    public IHitProcessor getHitProcessor() {
        // TODO Auto-generated method stub
        return null;
    }

}
