package org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemState;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;

/**
 * A {@link TubeRendererBehaviour} is able to state wether an item of a tube
 * hierarchy should be displayed or not.
 * 
 * @author Martin Pernollet
 */
public class TubeRendererBehaviour implements ITubeRendererBehaviour {
	protected TubeRendererConfiguration configuration;

	public TubeRendererBehaviour(TubeRendererConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public TubeRendererConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	public void setConfiguration(TubeRendererConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public boolean allowButtons() {
		return configuration.isAllowButtonRendering();
	}

	@Override
	public boolean allowButtonsInsideGroup() {
		return configuration.isAllowInternalButton();
	}

	@Override
	public boolean allowDrawSourceSide(Tube tube) {
		if (configuration.isRenderEdgeOnlyOnMouseOver()) {
			if (tubeSourceIsMouseOver(tube) || tubeTargetIsMouseOver(tube))
				return true;
			else
				return false;
		} else
			// otherwise always accepts to render edge
			return true;
	}

	@Override
	public boolean allowDrawTargetSide(Tube tube) {
		if (configuration.isRenderEdgeOnlyOnMouseOver()) {
			if (tubeSourceIsMouseOver(tube) || tubeTargetIsMouseOver(tube))
				return true;
			else
				return false;
		} else
			// otherwise always accepts to render edge
			return true;
	}

	@Override
	public boolean allowDrawEdge(IEdge edge) {
		if (edgeHasMouseOver(edge))
			return true;
		else {
			if (configuration.isRenderEdgeOnlyOnMouseOver()) {
				if (sourceParentIsMouseOver(edge))
					return true;
				if (targetParentIsMouseOver(edge))
					return true;
				return false;
			} else { // always render
				return true;
			}
		}
	}

	/* UTILS */

	protected boolean sourceParentIsMouseOver(IEdge edge) {
		IBoundedItem sourceParent = edge.getSourceItem().getParent();
		return sourceParent.getState().equals(ItemState.MOUSE_OVER);
	}

	protected boolean targetParentIsMouseOver(IEdge edge) {
		IBoundedItem targetParent = edge.getTargetItem().getParent();
		return targetParent.getState().equals(ItemState.MOUSE_OVER);
	}

	protected boolean edgeHasMouseOver(IEdge edge) {
		return edge.getSourceItem().getState().equals(ItemState.MOUSE_OVER)
				|| edge.getTargetItem().getState().equals(ItemState.MOUSE_OVER);
	}

	protected boolean tubeSourceIsMouseOver(Tube tube) {
		return tube.getSourceItem().getState().equals(ItemState.MOUSE_OVER);
	}

	protected boolean tubeTargetIsMouseOver(Tube tube) {
		return tube.getTargetItem().getState().equals(ItemState.MOUSE_OVER);
	}
}
