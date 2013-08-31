package org.datagr4m.viewer.renderer.utils;

import java.awt.Color;

public class GridRendererSettings {
    public boolean isGridValuesDisplayed() {
        return gridValuesDisplayed;
    }
    public void setGridValuesDisplayed(boolean gridValuesDisplayed) {
        this.gridValuesDisplayed = gridValuesDisplayed;
    }
    public Color getGridColor() {
        return gridColor;
    }
    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
    }
    public Color getGridValuesColor() {
        return gridValuesColor;
    }
    public void setGridValuesColor(Color gridValuesColor) {
        this.gridValuesColor = gridValuesColor;
    }
    public int getMajorTicks() {
        return majorTicks;
    }
    public void setMajorTicks(int majorTicks) {
        this.majorTicks = majorTicks;
    }
    public int getSpacing() {
        return spacing;
    }
    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }
    public boolean isDisplayed() {
        return isDisplayed;
    }
    public void setDisplayed(boolean isDisplayed) {
        this.isDisplayed = isDisplayed;
    }

    protected boolean isDisplayed = true;

    protected int spacing = 100;
    protected int majorTicks = 10;
    protected boolean gridValuesDisplayed = false;
    protected Color gridColor = new Color(240, 240, 240);
    protected Color gridValuesColor = new Color(200,200,200);
}
