package org.datagr4m.drawing.layout.hierarchical;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.datagr4m.drawing.layout.algorithms.forceAtlas.BoundedForceAtlasLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.IHierarchicalEdgeLayout;
import org.datagr4m.drawing.layout.hierarchical.visitor.LayoutModelFinderVisitor;
import org.datagr4m.drawing.model.bounds.IBounds;
import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.hierarchical.visitor.ItemLabelFinder;
import org.datagr4m.monitors.TimeMonitor;


public abstract class AbstractHierarchicalLayout implements IHierarchicalNodeLayout{
    private static final long serialVersionUID = 4457921907995201196L;

    public AbstractHierarchicalLayout() {
        this(null);
    }
    
    public AbstractHierarchicalLayout(IHierarchicalNodeLayout parent) {
        this.parent = parent;
        this.children = new ArrayList<IHierarchicalNodeLayout>();
    }
    
    @Override
    public BoundedForceAtlasLayout getDelegate() {
        return null;
    }

    @Override
    public List<IHierarchicalNodeLayout> getChildren() {
        return children;
    }

    @Override
    public List<IHierarchicalNodeLayout> getNeighbours() {
        return neighbours;
    }

    @Override
    public IHierarchicalNodeLayout getParent() {
        return parent;
    }
    
    @Override
    public boolean addChild(IHierarchicalNodeLayout item){
        boolean s = children.add(item);
        ((AbstractHierarchicalLayout)item).setParent(this);
        return s;
    }
    
    public boolean addChildren(Collection<IHierarchicalNodeLayout> items){
        boolean s = children.addAll(items);
        for(IHierarchicalNodeLayout item: items)
            ((AbstractHierarchicalLayout)item).setParent(this);
        return s;
    }
    
    @Override
	public boolean isLeaf(){
        return (children==null||children.size()==0);
    }
    
    @Override
	public int getDepth(){
        if(parent==null)
            return 0;
        else
            return 1+parent.getDepth();
    }
    
    public void setParent(IHierarchicalNodeLayout layout){
        parent = layout;
    }
    
    @Override
	public IHierarchicalNodeLayout findLayoutHoldingModel(String model){
        ItemLabelFinder v = new ItemLabelFinder();
        List<IBoundedItem> list = v.find(model, getModel());
        
        if(list.size()==0)
            return null;
        else if(list.size()==1)
            return findLayoutHoldingModel((IHierarchicalNodeModel)list.get(0));
        else
            throw new RuntimeException("found several result matching model name " + model + ": " + list);
    }
    
    @Override
	public IHierarchicalNodeLayout findLayoutHoldingModel(IHierarchicalNodeModel model){
        LayoutModelFinderVisitor finder = new LayoutModelFinderVisitor();
        return finder.findLayoutHolding(this, model);
    }
    
    /********************/

    @Override
    public IHierarchicalNodeLayout getLayout(IHierarchicalNodeModel model) {
        throw new RuntimeException("not implemented");
    }    

    @Override
    public IBounds getBounds() {
        return bounds;
    }

    @Override
    public void setBounds(IBounds bounds) {
        this.bounds = bounds;
    }
    
    @Override
    public RectangleBounds getActualBounds() {
        return getModel().getRawRectangleBounds();
    }
    
    /***********************/

    @Override
    public void resetPropertiesValues() {
        for(IHierarchicalNodeLayout child: getChildren())
            child.resetPropertiesValues();
    } 
    
    @Override
    public void initAlgo() {
        for(IHierarchicalNodeLayout child: getChildren())
            child.initAlgo();
    } 
    
    @Override
    public void goAlgo(){
        for(IHierarchicalNodeLayout child: getChildren())
            child.goAlgo();
        getModel().refreshBounds(false);
    }

    @Override
    public void endAlgo() {
        for(IHierarchicalNodeLayout child: getChildren())
            child.endAlgo();
		algoEnded = true;
    }
    
    @Override
    public boolean canAlgo() {
    	//if(algoEnded)
    	//	return false;
        for(IHierarchicalNodeLayout child: getChildren())
            if(child.canAlgo())
                return true;
        		
        return false;
    }
    
    @Override
    public void onDoneAlgo(){
        if(doRunEdgeLayout)
            goAlgoEdge();
    }
    
    public void pause(long mili){
        try {
            Thread.sleep(mili);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public int getCounter() {
        return counter;
    }

    public void clearCounter(){
        counter = 0;
    }

    /***********************/
    
    @Override
    public void goAlgoEdge(){
        if(tubeLayout!=null && tubeModel!=null)
            tubeLayout.build(tubeModel);
    }
    
    @Override
    public boolean isDoRunEdgeLayout() {
        return doRunEdgeLayout;
    }

    @Override
    public void setDoRunEdgeLayout(boolean doRunEdgeLayout) {
        this.doRunEdgeLayout = doRunEdgeLayout;
    }
    
    @Override
    public IHierarchicalEdgeModel getTubeModel() {
        return tubeModel;
    }

    @Override
    public IHierarchicalEdgeLayout getEdgeLayout() {
        return tubeLayout;
    }
    
    @Override
    public void setTubeModel(IHierarchicalEdgeModel model) {
        this.tubeModel= model ;
    }

    @Override
    public void setTubeLayout(IHierarchicalEdgeLayout layout) {
        this.tubeLayout = layout ;
    }
    
    protected IHierarchicalEdgeModel tubeModel;
    protected IHierarchicalEdgeLayout tubeLayout;
    protected boolean doRunEdgeLayout = false;

    
    /*******************/
    
    protected List<IHierarchicalNodeLayout> children;
    protected List<IHierarchicalNodeLayout> neighbours;
    protected IHierarchicalNodeLayout parent;
    protected IBounds bounds;
    protected int counter = 0;
    protected boolean algoEnded = false;
}