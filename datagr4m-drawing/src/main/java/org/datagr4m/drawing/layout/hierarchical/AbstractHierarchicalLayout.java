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
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.hierarchical.visitor.ItemNameFinderVisitor;


public abstract class AbstractHierarchicalLayout implements IHierarchicalLayout{
    private static final long serialVersionUID = 4457921907995201196L;

    public AbstractHierarchicalLayout() {
        this(null);
    }
    
    public AbstractHierarchicalLayout(IHierarchicalLayout parent) {
        this.parent = parent;
        this.children = new ArrayList<IHierarchicalLayout>();
    }
    
    @Override
    public BoundedForceAtlasLayout getDelegate() {
        return null;
    }

    @Override
    public List<IHierarchicalLayout> getChildren() {
        return children;
    }

    @Override
    public List<IHierarchicalLayout> getNeighbours() {
        return neighbours;
    }

    @Override
    public IHierarchicalLayout getParent() {
        return parent;
    }
    
    @Override
    public boolean addChild(IHierarchicalLayout item){
        boolean s = children.add(item);
        ((AbstractHierarchicalLayout)item).setParent(this);
        return s;
    }
    
    public boolean addChildren(Collection<IHierarchicalLayout> items){
        boolean s = children.addAll(items);
        for(IHierarchicalLayout item: items)
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
    
    public void setParent(IHierarchicalLayout layout){
        parent = layout;
    }
    
    @Override
	public IHierarchicalLayout findLayoutHoldingModel(String model){
        ItemNameFinderVisitor v = new ItemNameFinderVisitor();
        List<IBoundedItem> list = v.find(model, getModel());
        
        if(list.size()==0)
            return null;
        else if(list.size()==1)
            return findLayoutHoldingModel((IHierarchicalModel)list.get(0));
        else
            throw new RuntimeException("found several result matching model name " + model + ": " + list);
    }
    
    @Override
	public IHierarchicalLayout findLayoutHoldingModel(IHierarchicalModel model){
        LayoutModelFinderVisitor finder = new LayoutModelFinderVisitor();
        return finder.findLayoutHolding(this, model);
    }
    
    /********************/

    @Override
    public IHierarchicalLayout getLayout(IHierarchicalModel model) {
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
        for(IHierarchicalLayout child: getChildren())
            child.resetPropertiesValues();
    } 
    
    @Override
    public void initAlgo() {
        for(IHierarchicalLayout child: getChildren())
            child.initAlgo();
    } 
    
    @Override
    public void goAlgo(){
        for(IHierarchicalLayout child: getChildren())
            child.goAlgo();
        getModel().refreshBounds(false);
    }

    @Override
    public void endAlgo() {
        for(IHierarchicalLayout child: getChildren())
            child.endAlgo();
		algoEnded = true;
    }
    
    @Override
    public boolean canAlgo() {
    	//if(algoEnded)
    	//	return false;
        for(IHierarchicalLayout child: getChildren())
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
    public IHierarchicalEdgeLayout getTubeLayout() {
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
    
    protected List<IHierarchicalLayout> children;
    protected List<IHierarchicalLayout> neighbours;
    protected IHierarchicalLayout parent;
    protected IBounds bounds;
    protected int counter = 0;
    protected boolean algoEnded = false;
}