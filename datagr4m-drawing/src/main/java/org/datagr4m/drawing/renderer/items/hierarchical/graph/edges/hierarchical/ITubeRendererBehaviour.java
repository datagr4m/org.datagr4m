package org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical;

import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;

public interface ITubeRendererBehaviour {
    public boolean allowDrawSourceSide(Tube tube);
    public boolean allowDrawTargetSide(Tube tube);
    public boolean allowDrawEdge(IEdge edge);

    public boolean allowButtons();
    public boolean allowButtonsInsideGroup();

    public TubeRendererConfiguration getConfiguration();
    public void setConfiguration(TubeRendererConfiguration configuration);
}