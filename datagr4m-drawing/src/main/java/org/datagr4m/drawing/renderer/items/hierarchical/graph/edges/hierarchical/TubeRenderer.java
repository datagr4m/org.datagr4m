package org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datagr4m.datastructures.pairs.CommutativePair;
import org.datagr4m.drawing.layout.hierarchical.tree.TreeFlowerLayout;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemState;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;
import org.datagr4m.drawing.model.items.hierarchical.tree.TreeModel;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.HierarchicalGraphRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical.buttons.TubeOpenCloseButton;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical.buttons.TubeSourceButton;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical.buttons.TubeTargetButton;
import org.datagr4m.drawing.renderer.items.hierarchical.tree.TreeRenderer;
import org.datagr4m.drawing.renderer.pathfinder.view.PathSegmentsRenderer;
import org.datagr4m.drawing.renderer.policy.DefaultStyleSheet;
import org.datagr4m.drawing.viewer.mouse.edges.ClickedEdge;
import org.datagr4m.maths.geometry.Pt;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.mouse.IClickableItem;
import org.datagr4m.viewer.renderer.IRenderer;
import org.datagr4m.viewer.renderer.TextUtils;


/**
 * For simplicity reason, dynamic tube labels are handled by {@link HierarchicalGraphRenderer}.
 * 
 * @author martin
 *
 */

public class TubeRenderer extends HierarchicalEdgeRenderer implements IRenderer {
    protected boolean enable = true;
    protected IHierarchicalRenderer parent;
    
    public TubeRenderer(IDisplay display, IHierarchicalEdgeModel model) {
        super(display);
        this.model = model;
        this.pathRenderer = new PathSegmentsRenderer();
        this.treeRenderer = new TreeRenderer(display);
        this.behaviour = new TubeRendererBehaviour(new TubeRendererConfiguration());
    }
    
    
    @Override
    public void render(Graphics2D graphic) {
        if(!isEnable()){
            return;
        }
            
    	//makeFirstRenderingActions();
        
        clearAnnotationSquatting();
        //applyAlpha(graphic, 0.5f);
        
        // render all root tubes, i.e. inter groups
        for (Tube tube : model.getRootTubes()) {
            boolean sourceD = true;//tube.getSourceItem().isDisplayed(display);
            boolean targetD = true;//tube.getTargetItem().isDisplayed(display);
            
            renderTubeHierarchy(graphic, tube, edgeRendererSettings.getEdgeColor(tube), sourceD, targetD);
        }
        
        // render all intermediate tubes, i.e. intra groups
        for (IEdge edge : model.getInternalTubesAndEdges()) {
            Color c = edgeRendererSettings.getEdgeColor(edge);
            
            if (edge instanceof Tube) {
                Tube tube = (Tube) edge;
                //edgeRendererSettings.setTubeSourceOpened(tube, true);
                //edgeRendererSettings.setTubeTargetOpened(tube, true);
 
                boolean sourceD = true;
                boolean targetD = true;
 
                if(DefaultStyleSheet.USE_CONTEXT_TREE){
                    sourceD = tube.getSourceItem().isDisplayed(display);
                    targetD = tube.getTargetItem().isDisplayed(display);
                }
                renderTubeHierarchy(graphic, tube, c, sourceD, targetD);
            }
            else{
                if(behaviour.allowDrawEdge(edge)){
                    Stroke s = edgeRendererSettings.getEdgeStroke(edge);
                    if(s==null)
                        renderEdge(graphic, edge, c);
                    else{
                        //setStroke(graphic, s);
                        renderEdge(graphic, edge, c, s);
                        //resetLineStroke(graphic);
                    }
                }
            }
        }
        //resetAlpha(graphic);
        
     // Tube buttons
        if(behaviour.allowButtons()){
            if(!didBuildButtons){
                buildButtons();
                didBuildButtons = false; // rebuild
            }
            Color c = edgeRendererSettings.getEdgeColor(null);
            for(TubeOpenCloseButton button: tubeButtons)
                button.render(graphic, c, Color.WHITE, c);
        }
    }
    
    
    

	/*private void makeFirstRenderingActions() {
		if(isFirstTubeRendering){
    		
			for (Tube tube : model.getRootTubes()) {
				edgeRendererSettings.setTubeSourceOpened(tube, true);
				edgeRendererSettings.setTubeTargetOpened(tube, true);
	    		isFirstTubeRendering = false;
	        }
			for (IEdge edge : model.getInternalTubesAndEdges()) {
				if(edge instanceof Tube){
					Tube t = (Tube)edge;
					edgeRendererSettings.setTubeSourceOpened(t, true);
					edgeRendererSettings.setTubeTargetOpened(t, true);
		    		isFirstTubeRendering = false;
				}
	        }
    	}
	}*/
    
    /********* BUTTONS ***********/
    
    protected boolean didBuildButtons = false;
    
    protected void buildButtons(){
        if(tubeButtons != null)
            tubeButtons.clear();
        tubeButtons = null;
        if(tubeButtons == null){
            tubeButtons = new ArrayList<TubeOpenCloseButton>();
            buildButtons(model.getRootTubes());
            if(behaviour.allowButtonsInsideGroup())
                buildTubeButtons(model.getInternalTubesAndEdges());
            //buildTubeButtons(model.getInternalTubesAndEdges());
        }
    }
    
    protected void buildButtons(List<Tube> tubes){
        for(Tube tube: tubes){
            buildButtons(tube);
        }
    }
    
    protected void buildTubeButtons(List<IEdge> edges){
        for(IEdge edge: edges){
            if(edge instanceof Tube)
                buildButtons((Tube)edge);
        }
    }
    
    protected void buildButtons(Tube tube){
        
        float radius = Math.max(getConfiguration().getButtonMinRadius(), tubeWidth(tube)/2);//tube.getWidth()*TUBE_WIDTH_RATIO/2);

        synchronized(tube.getPathGeometry().getPoints()){
	        Point2D sourcePosition = tube.getPathGeometry().getPoint(0);
	        TubeOpenCloseButton sourceB = new TubeSourceButton(tube, sourcePosition, radius, "", edgeRendererSettings);
	        tubeButtons.add(sourceB);
	        
	        Point2D targetPosition = tube.getPathGeometry().getLastPoint();
	        TubeOpenCloseButton targetB = new TubeTargetButton(tube, targetPosition, radius, "", edgeRendererSettings);
	        tubeButtons.add(targetB);
        }
    }

    /******* PARTIAL & TOTAL HIERARCHICAL TUBE RENDERING ******************/
    
    public void renderTubeHierarchy(Graphics2D graphic, Tube tube, Color color, boolean visibleSource, boolean visibleTarget) {
        boolean doSource = edgeRendererSettings.isTubeSourceOpened(tube);
        boolean doTarget = edgeRendererSettings.isTubeTargetOpened(tube);
        renderTubeHierarchy(graphic, tube, color, doSource, doTarget, visibleSource, visibleTarget);
    }
    
    public void renderTubeHierarchy(Graphics2D graphic, Tube tube, Color color, boolean openSource, boolean openTarget, boolean visibleSource, boolean visibleTarget) {
        if(DefaultStyleSheet.USE_CONTEXT_TREE){
            if(visibleSource && visibleTarget){
                //TODO: warning: should better reuse parent color?
                renderTubeBody(graphic, tube, edgeRendererSettings.getEdgeColor(tube));
            }
        }
        else{
            renderTubeBody(graphic, tube, edgeRendererSettings.getEdgeColor(tube));
        }
        
        if(visibleSource){
            if(openSource){
                // montre l'interieur du groupe source
            	synchronized(tube.getPathGeometry().getPoints()){
	                Point2D point = tube.getPathGeometry().getFirstPoint(); // inside group, outside tube
	                if(point!=null){ // may have not been computed yet
	                    if(behaviour.allowDrawSourceSide(tube))
                            renderTubeHierarchyBefore(graphic, tube, point, Color.RED);
	                }
            	}
            }
        }
        else{
            if(DefaultStyleSheet.USE_CONTEXT_TREE)
                renderTubeSourceTree(graphic, tube);
        }
        
        if(visibleTarget){
            if(openTarget){
            	synchronized(tube.getPathGeometry().getPoints()){
	                Point2D point = tube.getPathGeometry().getLastPoint(); // inside group, outside tube
	                if(point!=null){ // may have not been computed yet
	                    if(behaviour.allowDrawTargetSide(tube))
	                        renderTubeHierarchyAfter(graphic, tube, point, Color.RED); // this tube should not appear
	                }
            	}
            }
        }
        else{
            if(DefaultStyleSheet.USE_CONTEXT_TREE)
                renderTubeTargetTree(graphic, tube);
        }
    }
    
    

    public void renderTubeHierarchyComplete(Graphics2D graphic, Tube tube, Color color) {
        if(!edgeRendererSettings.isEdgeWithoutVisibleNodesVisible()){
            if(canRender(tube)){
                pathRenderer.render(graphic, tube.getPathGeometry(), color, tubeWidth(tube), false, false);
            }
        }
        else{
            if(endpointParentsNotCollapsed(tube))
                pathRenderer.render(graphic, tube.getPathGeometry(), color, tubeWidth(tube), false, false);
        }
        
        // hierarchy
        for (IEdge child : tube.getChildren()) {
            Color c = edgeRendererSettings.getEdgeColor(child);
            if (child instanceof Tube) {
                renderTubeHierarchyComplete(graphic, (Tube) child, c);
            } else
                renderEdge(graphic, child, c);
        }

        // back line width to default
        setLineWidth(graphic, 1);
    }
    
    public void renderTubeHierarchyBefore(Graphics2D graphic, Tube tube, Point2D pt, Color color) {
        if(!edgeRendererSettings.isEdgeWithoutVisibleNodesVisible()){
            if(canRender(tube)){
                pathRenderer.renderBefore(graphic, tube.getPathGeometry(), pt, INCLUDING, color, tubeWidth(tube), false, false, tube.getState().isSelected());
            }
        }
        else{
            if(endpointParentsNotCollapsed(tube))
                pathRenderer.renderBefore(graphic, tube.getPathGeometry(), pt, INCLUDING, color, tubeWidth(tube), false, false, tube.getState().isSelected());
        }
        
        // hierarchy
        for (IEdge child : tube.getChildren()) {
            Color c = edgeRendererSettings.getEdgeColor(child);

            if(child.isFlipped()){
                if (child instanceof Tube)
                    renderTubeHierarchyAfter(graphic, (Tube) child, pt, c);
                else{
                    //System.out.println("flip in after");
                    if(behaviour.allowDrawEdge(child))
                        renderEdgeAfter(graphic, child, pt, c);
                }
            }
            else{
                if (child instanceof Tube)
                    renderTubeHierarchyBefore(graphic, (Tube) child, pt, c);
                else{
                    if(behaviour.allowDrawEdge(child))
                        renderEdgeBefore(graphic, child, pt, c);
                }
            }
        }

        // back line width to default
        setLineWidth(graphic, 1);
    }
    
    public void renderTubeHierarchyAfter(Graphics2D graphic, Tube tube, Point2D pt, Color color) {
        if(!edgeRendererSettings.isEdgeWithoutVisibleNodesVisible()){
            if(canRender(tube)){
                pathRenderer.renderAfter(graphic, tube.getPathGeometry(), pt, INCLUDING, color, tubeWidth(tube), false, false, tube.getState().isSelected());
            }
        }
        else{
            if(endpointParentsNotCollapsed(tube))
                pathRenderer.renderAfter(graphic, tube.getPathGeometry(), pt, INCLUDING, color, tubeWidth(tube), false, false, tube.getState().isSelected());
        }
        // hierarchy
        for (IEdge child : tube.getChildren()) {
            Color c = edgeRendererSettings.getEdgeColor(child);
            
            if(child.isFlipped()){
                if (child instanceof Tube)
                    renderTubeHierarchyBefore(graphic, (Tube) child, pt, c);
                else{
                    if(behaviour.allowDrawEdge(child))
                        renderEdgeBefore(graphic, child, pt, c);
                }
            }
            else{
                if (child instanceof Tube)
                    renderTubeHierarchyAfter(graphic, (Tube) child, pt, c);
                else{
                    if(behaviour.allowDrawEdge(child))
                        renderEdgeAfter(graphic, child, pt, c);
                }
            }
        }

        // back line width to default
        setLineWidth(graphic, 1);
    }
    
    public void renderTubeBody(Graphics2D graphic, Tube tube, Color color) {
        if(!edgeRendererSettings.isEdgeWithoutVisibleNodesVisible()){
            if(canRender(tube)){
                pathRenderer.render(graphic, tube.getPathGeometry(), color, tubeWidth(tube), false, false, tube.getState().isSelected());
                setLineWidth(graphic, 1);
                //renderTubeInfo(graphic, tube, color);
            }
        }
        else{
            pathRenderer.render(graphic, tube.getPathGeometry(), color, tubeWidth(tube), false, false, tube.getState().isSelected());
            setLineWidth(graphic, 1);
            //renderTubeInfo(graphic, tube, color);
        }
    }
    
    /*private void renderTubeInfo(Graphics2D graphic, Tube tube, Color color) {
        IEdgeInfo ei = tube.getEdgeInfo();
        if(ei!=null){
            Point2D pt1 = tube.getPathGeometry().getFirstPoint();
            Point2D pt2 = tube.getPathGeometry().getLastPoint();
            if(pt1!=null && pt2!=null){
                Point2D txt = PointUtils.mean(pt1, pt2);
                //pathRenderer.drawTextRotated(graphic, ei.toString(), txt, 0, Color.WHITE);
                
                
                UnscaledTextDifferedRenderer r = new UnscaledTextDifferedRenderer(ei.toString(), new Coord2d(txt), TextUtils.size-2, 10) {
                    @Override
                    public synchronized void renderText(Graphics2D graphic) {
                        if(size>0)
                            drawUnscaledTextCell(graphic, label, position, Color.BLACK, Color.WHITE);
                    }
                };
                if(getParent()!=null)
                    getParent().addDiffered(r);
                else
                    r.renderText(graphic);
            }
        }
    }*/
    
    

    protected float tubeWidth(Tube tube){
        float w = tube.getWidth() * TUBE_WIDTH_RATIO;
        return Math.min(w, 30);
    }

    /********** LOCAL TREE RENDERING ***********/
    
    protected Map<Tube,TreeModel> tubeSourceTree = new HashMap<Tube,TreeModel>();
    protected Map<Tube,TreeModel> tubeTargetTree = new HashMap<Tube,TreeModel>();
    protected Map<Tube,TreeFlowerLayout> tubeSourceLayout = new HashMap<Tube,TreeFlowerLayout>();
    protected Map<Tube,TreeFlowerLayout> tubeTargetLayout = new HashMap<Tube,TreeFlowerLayout>();
    
    protected static float LOCAL_TREE_DIST = 50;
    //protected static float LOCAL_TREE_NODE_WIDTH = 30;
    
    public void clearTreeModelCache(){
        tubeSourceTree.clear();
        tubeTargetTree.clear();
    }
    
    public void clearTreeLayoutCache(){
        tubeSourceLayout.clear();
        tubeTargetLayout.clear();
    }

    public void renderTubeSourceTree(Graphics2D graphic, Tube tube){
        TreeModel model = tubeSourceTree.get(tube);
        if(model==null){
            model = tube.getSourceItemHierarchy();
            tubeSourceTree.put(tube, model);
        }
        
        IBoundedItem container = tube.getTargetItem();
        float radius = container.getRadialBounds();
        Point2D pivot = Pt.cloneAsDoublePoint(container.getAbsolutePosition());
        
        Point2D p1 = null;
        Point2D p2 = null;

        TreeFlowerLayout layout = tubeSourceLayout.get(tube);
        if(layout==null){
            float nodeRadius = (float)Math.sqrt(Math.pow(TreeRenderer.NODE_WIDTH,2)*2)/2;
            synchronized(tube.getPathGeometry().getPoints()){
                if(tube.getPathGeometry().getPointNumber()>0){
                    p1 = tube.getPathGeometry().getPoint(1);
                    p2 = tube.getPathGeometry().getPoint(0);
                }
            }
            
            if(p1!=null && p2!=null){
                layout = new TreeFlowerLayout(model, radius, pivot, p1, p2, LOCAL_TREE_DIST, nodeRadius);
                tubeSourceLayout.put(tube, layout);
            }
        }
        renderTree(graphic, model, layout, tube);
    }
    
    public void renderTubeTargetTree(Graphics2D graphic, Tube tube){
        TreeModel model = tubeTargetTree.get(tube);
        if(model==null){
            model = tube.getTargetItemHierarchy();
            tubeTargetTree.put(tube, model);
        }
        
        IBoundedItem container = tube.getSourceItem();
        float radius = container.getRadialBounds();
        Point2D pivot = Pt.cloneAsDoublePoint(container.getAbsolutePosition());
        
        Point2D p1 = null;
        Point2D p2 = null;

        TreeFlowerLayout layout = tubeTargetLayout.get(tube);
        if(layout==null){
            float nodeRadius = (float)Math.sqrt(Math.pow(TreeRenderer.NODE_WIDTH,2)*2)/2;
            synchronized(tube.getPathGeometry().getPoints()){
                if(tube.getPathGeometry().getPointNumber()>0){
                    p1 = tube.getPathGeometry().getPoint(0);
                    p2 = tube.getPathGeometry().getPoint(1);
                }
            }
            
            if(p1!=null && p2!=null){
                layout = new TreeFlowerLayout(model, radius, pivot, p1, p2, LOCAL_TREE_DIST, nodeRadius);
                tubeTargetLayout.put(tube, layout);
            }
        }
        //if(p1!=null)
        //    drawRectCentered(graphic, p1, 30, 30, Color.RED);
        renderTree(graphic, model, layout, tube);
    } 
        
    protected void renderTree(Graphics2D graphic, TreeModel model, TreeFlowerLayout layout, Tube tube){
        if(model!=null){
            // check if any node of the tree is visible
            if(layout!=null){
                boolean sourceDisplayed = display.getView().isDisplayed(layout.getSourcePoint());
                if(sourceDisplayed || model.isDisplayed(display)){
                    // render trunk
                    setLineStyleRound(graphic, tube.getWidth());
                    graphic.setColor(edgeRendererSettings.getEdgeColor(tube));
                    drawLine(graphic, layout.getSourcePoint(), layout.getRoot());
                    resetLineStroke(graphic);
                    
                    // render tree
                    treeRenderer.render(graphic, model);
                }
            }
        }
        
        // draw layout debug info
        if(layout!=null){
            Point2D debug = new Point2D.Double(layout.getRoot().getX(), layout.getRoot().getY()+TextUtils.size*1.5+2);
            drawText(graphic, "build:"+layout.getBuildLevel(), debug);

            /*graphic.setColor(Color.BLUE);
            drawRectCentered(graphic, layout.getSourcePoint(), 20, 20);
            graphic.setColor(Color.GREEN);
            drawRectCentered(graphic, layout.getRoot(), 20, 20);*/
        }
    }
    
    protected void drawPairPoint(Graphics2D graphic, Point2D p1, Point2D p2){
        drawRectCentered(graphic, p2, 30, 30, Color.RED);
        drawRectCentered(graphic, p1, 30, 30, Color.BLUE);        
    }
    
    /************** HITTING EDGES *****************/

    @Override
    public List<IClickableItem> hit(int x, int y) {
        boolean found = hitAnnotations(x, y);
        if(!found)
            return hitTubes(x,y);
        return null;
    }
    
    protected boolean hitAnnotations(int x, int y){
        if(tubeButtons!=null){
            //System.out.println("x="+x+", y="+y);
            for(TubeOpenCloseButton bt: tubeButtons){
                //System.out.print("trying to hit " + bt.getCircle().getCenterX() + "," + bt.getCircle().getCenterY()+"," + bt.getCircle().getHeight());
                List<IClickableItem> clicked = bt.hit(x, y);
                //System.out.println(clicked);
                if(clicked!=null)
                    return true;
            }
        }
        return false;
    }
    
    protected List<IClickableItem> hitTubes(int x, int y){
        List<IClickableItem> clicked = new ArrayList<IClickableItem>();

        // reset state
        //for (IEdge edge : model.getFlattenList())
        //    edge.setState(ItemState.none());
        
        for (IEdge edge : model.getFlattenList()) {
         // don't want to add a child of an existing selected hierarchy
            if(isDescendantOfAny(clicked, edge))
                continue; 
            
            float width = 1;
            if (edge instanceof Tube)
                width *= (((Tube) edge).getWidth() * TUBE_WIDTH_RATIO);
            else {
                width += EDGE_CLICK_TOLERANCE;
            }

            // hit and retrive segment
            
            synchronized(edge.getPathGeometry().getPoints()){
	            CommutativePair<Point2D> segment = hitEdgePath(x, y, edge, width);
	            
	            if (segment != null) {
	                // search highest parent holding the same segment
	                if (edge.getParent() != null) {
	                    IEdge child;
	                    do {
	                        child = edge;
	                        edge = edge.getParent();
	                    } while (edge.getParent() != null 
	                          && edge.getParent().getPathGeometry().hasSegment(segment));
	
	                    // make the selection
	                    if(edge.getPathGeometry().hasSegment(segment)){ // take the most root tube
	                        inverseSelection(edge); 
	                        clicked.add(new ClickedEdge(edge));
	                    }
	                    else{
	                        // or the previous one if this tube did not contain the segment
	                        // meaning it's a tube that do not contains the current child for
	                        // the clicked segment
	                        inverseSelection(child); 
	                        clicked.add(new ClickedEdge(child));
	                    }
	
	                } else {
	                    inverseSelection(edge); 
	                    clicked.add(new ClickedEdge(edge));
	                }
	            } else{
	                // TODO: use static instance instead of new instance
	                //edge.setState(ItemState.none());
	                
	            }
            }
        }
        
        //for(IClickableItem i: clicked)
        //    System.out.println(i);
        
        return clicked;
    }
    
    protected boolean isDescendantOfAny(List<IClickableItem> items, IEdge e){
        for(IClickableItem clicked: items)
            if(clicked instanceof ClickedEdge){
                IEdge edg = ((ClickedEdge)clicked).getEdge();
                if(edg instanceof Tube)
                    if(((Tube)edg).hasDescendant(e))
                        return true;
            }
        return false;
    }
    
    // TODO: use static instance instead of new instance
    /*protected void makeSelected(IEdge edge){
        if(edge instanceof Tube){
            ((Tube)edge).setHierarchyState(ItemState.selected());
        }
        else
            edge.setState(ItemState.selected());
    }*/
    
    protected void inverseSelection(IEdge edge){
        if(edge instanceof Tube){
            if(edge.getState().isSelected())
                ((Tube)edge).setHierarchyState(ItemState.none());
            else
                ((Tube)edge).setHierarchyState(ItemState.selected());
        }
        else{
            if(edge.getState().isSelected())
                edge.setState(ItemState.none());
            else
                edge.setState(ItemState.selected());
        }
    }

    /*****************/


    public IHierarchicalRenderer getParent() {
        return parent;
    }

    public void setParent(IHierarchicalRenderer parent) {
        this.parent = parent;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
    
    public TubeRendererConfiguration getConfiguration() {
        return behaviour.getConfiguration();
    }

    public void setConfiguration(TubeRendererConfiguration configuration) {
        behaviour.setConfiguration(configuration);
    }

    public TreeRenderer getTreeRenderer() {
        return treeRenderer;
    }

    public IHierarchicalEdgeModel getModel() {
        return model;
    }
    
    protected static float TUBE_WIDTH_RATIO = 2; // FOR RENDERING PURPOSE
    

    protected static float EDGE_CLICK_TOLERANCE = 4; // FOR RAW EDGE HAVING
                                                     // WIDTH=1

    protected IHierarchicalEdgeModel model;
    protected PathSegmentsRenderer pathRenderer;
    protected TreeRenderer treeRenderer;
    protected List<TubeOpenCloseButton> tubeButtons;
    
    protected boolean isFirstTubeRendering = true;
    
    protected ITubeRendererBehaviour behaviour;
    //protected TubeRendererConfiguration configuration;
}
