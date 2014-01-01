package org.datagr4m.drawing.renderer.items.hierarchical.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.List;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.IHierarchicalGraphModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.infos.IEdgeInfo;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.LabelMode;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;
import org.datagr4m.drawing.renderer.bounds.BoundsRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.AbstractHierarchicalRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.local.IEdgeRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.local.IEdgeRendererSettings;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.local.LocalEdgeRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.labels.DifferedLabelHandler;
import org.datagr4m.drawing.renderer.items.shaped.MultiShapeItemIconRenderer;
import org.datagr4m.drawing.renderer.items.shaped.UnscaledTextDifferedRenderer;
import org.datagr4m.maths.geometry.PointUtils;
import org.datagr4m.maths.geometry.Pt;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.renderer.DifferedRenderer;
import org.datagr4m.viewer.renderer.TextUtils;


public class HierarchicalGraphRenderer extends AbstractHierarchicalRenderer implements IHierarchicalGraphRenderer {

    public HierarchicalGraphRenderer(IDisplay display, IHierarchicalGraphModel model) {
        this(display, model, new GraphRendererSettings());
    }

    public HierarchicalGraphRenderer(IDisplay display, IHierarchicalGraphModel model, IGraphRendererSettings settings) {
        super(display);
        setModel(model);
        this.configuration = new GraphRendererConfiguration();
        this.boundsRenderer = new BoundsRenderer();
        this.itemRenderer = new MultiShapeItemIconRenderer(model.getRoot(), display);
        this.edgeRenderer = new LocalEdgeRenderer(model.getRoot());
        this.settings = settings;
    }

    @Override
    public void setModel(IHierarchicalModel model) {
        this.model = (IHierarchicalGraphModel) model;
    }

    @Override
    public IHierarchicalModel getModel() {
        return model;
    }

    @Override
    public void render(Graphics2D graphic) {
        if (model.isCollapsed())
            return;
        
//        if (!configuration.isAllowHierarchicalEdgeManagement())
//            super.render(graphic);
        
        drawBounds(graphic);
        drawLocalEdges(graphic);
        drawNodes(graphic);
        drawDiffered(graphic);
//        TextUtils.changeFontSize(graphic, 12);
    }

    public void drawDiffered(Graphics2D graphic) {
        if (configuration.isAllowHierarchicalEdgeManagement()) {
            addDiffered(itemRenderer.getDiffered());
            itemRenderer.clearDiffered();

            // Draw all sub models on top
            // apres les differed sont disponibles
            super.render(graphic);

            for (IHierarchicalRenderer subr : getChildren()) {
                addDiffered(subr.getDiffered());
                subr.clearDiffered();
            }

            addRootTubeInfoDifferedLabels(differed);

            if (model.isRoot()) {
                renderDiffered(graphic);
                clearDiffered();
            }
        } else {
            super.render(graphic);
            List<DifferedRenderer> differed = itemRenderer.getDiffered();

            addRootTubeInfoDifferedLabels(differed);

            renderDiffered(graphic, differed);
            itemRenderer.clearDiffered();
        }
    }

    public void drawNodes(Graphics2D graphic) {
        itemRenderer.clearDiffered();
        for (IBoundedItem item : model.getChildren()) {
            // if(item.isDisplayed(display)){
            itemRenderer.render(graphic, item, settings.getNodeSettings());
            // }
        }
    }

    public void drawLocalEdges(Graphics2D graphic) {
        if (settings.isLocalEdgeDisplayed())
            for (Pair<IBoundedItem, IBoundedItem> e : model.getLocalEdges())
                edgeRenderer.render(graphic, e, settings.getEdgeSettings());
    }

    public void drawBounds(Graphics2D graphic) {
        if (settings.getBoundsSettings().isBoundDisplayed(model))
            boundsRenderer.render(graphic, model);
    }

    public void addRootTubeInfoDifferedLabels(List<DifferedRenderer> differed) {
        IHierarchicalEdgeModel m = model.getEdgeModel();
        if (m == null)
            return;
        if (!m.getLabelMode().equals(LabelMode.DYNAMIC))
            return;
        for (Tube tube : m.getRootTubes()) {
            int size = 8;
            int priority = 10;
            UnscaledTextDifferedRenderer r = getDifferedTubeInfoRenderer(tube, size, priority);
            if (r != null)
                differed.add(r);
        }
    }

    private UnscaledTextDifferedRenderer getDifferedTubeInfoRenderer(Tube tube, final int size, final int priority) {
        IEdgeInfo ei = tube.getEdgeInfo();
        if (ei != null) {
            Point2D pt1 = tube.getPathGeometry().getFirstPoint();
            Point2D pt2 = tube.getPathGeometry().getLastPoint();
            if (pt1 != null && pt2 != null) {
                Point2D txt = PointUtils.mean(pt1, pt2);
                return new UnscaledTextDifferedRenderer(ei.toString(), Pt.cloneAsCoord2d(txt), size, priority) {
                    @Override
                    public synchronized void renderText(Graphics2D graphic) {
                        if (size > 0)
                            drawUnscaledTextCell(graphic, label, position, Color.BLACK, Color.WHITE);
                    }
                };
            }
            return null;
        }
        return null;
    }

    protected void renderDiffered(Graphics2D graphic) {
        // Draw differed renderers (group labels)
        List<DifferedRenderer> list = getDiffered();

        renderDiffered(graphic, list);
    }

    protected void renderDiffered(Graphics2D graphic, List<DifferedRenderer> list) {
        DifferedLabelHandler handler = null;
        if (configuration.isApplySmartLabelScaling()) {
            handler = new DifferedLabelHandler(list, display.getView());
            if (oldhandler != null)
                handler.applyPrevious(oldhandler, true);
            // handler.debugHistory();

            // TicToc t = new TicToc();
            // t.tic();
            handler.process(configuration.isAllowHierarchicalEdgeManagement());
            // t.toc();
            // System.out.println(t.elapsedSecond() +
            // " ("+handler.getDiffered().size()+")");
        }
        for (DifferedRenderer renderer : list) {
            if (renderer != null)
                renderer.render(graphic);
        }

        if (configuration.isApplySmartLabelScaling()) {
            oldhandler = handler;
        }
    }

    DifferedLabelHandler oldhandler;

    /*********************/

    @Override
    public void setRendererSettings(IGraphRendererSettings settings) {
        this.settings = settings;
    }

    @Override
    public IGraphRendererSettings getRendererSettings() {
        return settings;
    }

    @Override
    public IEdgeRenderer getEdgeRenderer() {
        return edgeRenderer;
    }

    @Override
    public void setEdgeRenderer(IEdgeRenderer renderer) {
        this.edgeRenderer = renderer;
    }

    @Override
    public IEdgeRenderer getInterChildrenEdgeRenderer() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setInterChildrenEdgeRenderer(IEdgeRenderer renderer) {
        throw new RuntimeException("not implemented");
    }

    /************************/

    protected IHierarchicalGraphModel model;
    protected IEdgeRenderer edgeRenderer;
    protected IGraphRendererSettings settings;
    protected IEdgeRendererSettings edgeRendererSettings;
    protected GraphRendererConfiguration configuration;
}
