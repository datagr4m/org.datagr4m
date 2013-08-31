package org.datagr4m.drawing.navigation.plugin.bringandgo.flower;

public class FlowerBuilderConfiguration {
    protected boolean farmGroupAsSingleItem = true;
    protected double minRadius = 300;
    protected double maxRadius = 500;
    protected boolean autoGroupWhenCommonNetwork = true;

    public boolean isFarmGroupAsSingleItem() {
        return farmGroupAsSingleItem;
    }
    public void setFarmGroupAsSingleItem(boolean farmGroup) {
        this.farmGroupAsSingleItem = farmGroup;
    }
    public double getMinRadius() {
        return minRadius;
    }
    public void setMinRadius(double minRadius) {
        this.minRadius = minRadius;
    }
    public double getMaxRadius() {
        return maxRadius;
    }
    public void setMaxRadius(double maxRadius) {
        this.maxRadius = maxRadius;
    }
    public boolean isAutoGroupWhenCommonNetwork() {
        return autoGroupWhenCommonNetwork;
    }
    public void setAutoGroupWhenCommonNetwork(boolean autoGroupWhenCommonNetwork) {
        this.autoGroupWhenCommonNetwork = autoGroupWhenCommonNetwork;
    }
}
