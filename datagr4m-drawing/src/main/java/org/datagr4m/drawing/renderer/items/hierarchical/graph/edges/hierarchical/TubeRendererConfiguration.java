package org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical;

public class TubeRendererConfiguration {
    
    public float getButtonMinRadius() {
        return buttonMinRadius;
    }
    public void setButtonMinRadius(float buttonMinRadius) {
        this.buttonMinRadius = buttonMinRadius;
    }
    public boolean isAllowButtonRendering() {
        return allowButtonRendering;
    }
    public void setAllowButtonRendering(boolean allowButtonRendering) {
        this.allowButtonRendering = allowButtonRendering;
    }
    public boolean isAllowInternalButton() {
        return allowInternalButton;
    }
    public void setAllowInternalButton(boolean allowInternalButton) {
        this.allowInternalButton = allowInternalButton;
    }
    public boolean isRenderEdgeOnlyOnMouseOver() {
        return renderEdgeOnlyOnMouseOver;
    }
    public void setRenderEdgeOnlyOnMouseOver(boolean renderEdgeOnlyOnMouseOver) {
        this.renderEdgeOnlyOnMouseOver = renderEdgeOnlyOnMouseOver;
    }
    
    protected float buttonMinRadius = 10;
    protected boolean allowButtonRendering = false;
    protected boolean allowInternalButton = true; // only if buttons are allowed

    
    protected boolean renderEdgeOnlyOnMouseOver = false;
}
