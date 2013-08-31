package org.datagr4m.drawing.model.items.zones;

import java.util.ArrayList;
import java.util.List;

public class ZoningModel {
    protected List<IZoneModel> zones = new ArrayList<IZoneModel>();
    
    public List<IZoneModel> getZones() {
        return zones;
    }

    public void setZones(List<IZoneModel> zones) {
        this.zones = zones;
    }
    
    public void addZone(IZoneModel zone){
        zones.add(zone);
    }
}
