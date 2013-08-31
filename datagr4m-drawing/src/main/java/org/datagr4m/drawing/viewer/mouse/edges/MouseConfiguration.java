package org.datagr4m.drawing.viewer.mouse.edges;

public class MouseConfiguration {
    public boolean isApplyMouseDragTrick() {
        return applyMouseDragTrick;
    }
    public void setApplyMouseDragTrick(boolean applyMouseDragTrick) {
        this.applyMouseDragTrick = applyMouseDragTrick;
    }
    public boolean isDragWithBarycentreUpdate() {
        return dragWithBarycentreUpdate;
    }
    public void setDragWithBarycentreUpdate(boolean dragWithBarycentreUpdate) {
        this.dragWithBarycentreUpdate = dragWithBarycentreUpdate;
    }
    public boolean isEdgeRefreshAtDragEvent() {
        return edgeRefreshAtDragEvent;
    }
    public void setEdgeRefreshAtDragEvent(boolean edgeRefreshAtDragEvent) {
        this.edgeRefreshAtDragEvent = edgeRefreshAtDragEvent;
    }
    public boolean isMouseOverOn() {
        return mouseOverOn;
    }
    public void setMouseOverOn(boolean mouseOverOn) {
        this.mouseOverOn = mouseOverOn;
    }
    /** Determine an extra radius to be added to any pointed item radius
     * to check if mouse pointer is in their circle mouse sensor.
     */
    public float getMouseMovedAroundDistanceSensitivity() {
        return mouseMovedAroundDistanceSensitivity;
    }
    public void setMouseMovedAroundDistanceSensitivity(float mouseMovedAroundDistanceSensitivity) {
        this.mouseMovedAroundDistanceSensitivity = mouseMovedAroundDistanceSensitivity;
    }
    
    /** Determine a distance out of which a slot group is not analysed
     * for a distance-to-mouse comparison for each slot.
     * 
     * This distance ratio refers to distance between the current mouse pointer and
     * any slot group center.
     * 
     * This value should thus be large enough to capture slot groups when they're large
     * 
     * |             |
     * |             |
     * |------|------|<-- mouse on right/left most slot should be near enough to slot group center
     * 
     * utilis� �galement par distance souris<->centre objet
     * 
     */
    public float getMouseMovedAroundManhattanDistance() {
        return mouseMovedAroundManhattanDistance;
    }
    public void setMouseMovedAroundManhattanDistance(float mouseMovedAroundManhattanDistance) {
        this.mouseMovedAroundManhattanDistance = mouseMovedAroundManhattanDistance;
    }
    
    protected boolean applyMouseDragTrick = false; // FOIRE SUR BRING AND GO
    protected boolean dragWithBarycentreUpdate = false;
    protected boolean edgeRefreshAtDragEvent = true;
    protected boolean mouseOverOn = true;
    
    protected float mouseMovedAroundDistanceSensitivity = 0;    
    protected float mouseMovedAroundManhattanDistance = 100;
}
