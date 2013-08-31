package org.datagr4m.drawing.model.items.hierarchical.flower;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;


/** A base class usefull to implement various flower models.*/
public abstract class AbstractFlowerModel<E> extends HierarchicalGraphModel implements IFlowerModel<E>{
	private static final long serialVersionUID = -2589831537191505301L;

	/* (non-Javadoc)
	 * @see com.netlight.layout.model.items.hierarchical.flower.IFlowerModel#getCenter()
	 */
    @Override
	public IBoundedItem getCenter() {
        return center;
    }
    
    /* (non-Javadoc)
	 * @see com.netlight.layout.model.items.hierarchical.flower.IFlowerModel#getNeighbours()
	 */
    @Override
	public List<IBoundedItem> getNeighbours() {
        return neighbours;
    }

    /* (non-Javadoc)
	 * @see com.netlight.layout.model.items.hierarchical.flower.IFlowerModel#getHydres()
	 */
    @Override
	public List<HyperEdgeStructure> getNetworkItemsStructures() {
        return networkStructures;
    }
    
    
    public String getNetworkLabelPattern() {
        return networkLabelPattern;
    }

    public void setNetworkLabelPattern(String networkLabelPattern) {
        this.networkLabelPattern = networkLabelPattern;
    }

    public int getNetworkLabelFontSize() {
        return networkLabelFontSize;
    }

    public void setNetworkLabelFontSize(int networkLabelFontSize) {
        this.networkLabelFontSize = networkLabelFontSize;
    }

    public int getNetworkLabelFontWidth() {
        return networkLabelFontWidth;
    }

    public void setNetworkLabelFontWidth(int networkLabelFontWidth) {
        this.networkLabelFontWidth = networkLabelFontWidth;
    }

    public int getNetworkLabelFontHeight() {
        return networkLabelFontHeight;
    }

    public void setNetworkLabelFontHeight(int networkLabelFontHeight) {
        this.networkLabelFontHeight = networkLabelFontHeight;
    }

    /* (non-Javadoc)
	 * @see com.netlight.layout.model.items.hierarchical.flower.IFlowerModel#getDeviceLabelPattern()
	 */
    @Override
	public String getDeviceLabelPattern() {
        return deviceLabelPattern;
    }

    public void setDeviceLabelPattern(String deviceLabelPattern) {
        this.deviceLabelPattern = deviceLabelPattern;
    }

    /* (non-Javadoc)
	 * @see com.netlight.layout.model.items.hierarchical.flower.IFlowerModel#getDeviceLabelFontSize()
	 */
    @Override
	public int getDeviceLabelFontSize() {
        return deviceLabelFontSize;
    }

    public void setDeviceLabelFontSize(int deviceLabelFontSize) {
        this.deviceLabelFontSize = deviceLabelFontSize;
    }

    /* (non-Javadoc)
	 * @see com.netlight.layout.model.items.hierarchical.flower.IFlowerModel#getDeviceLabelFontWidth()
	 */
    @Override
	public int getDeviceLabelFontWidth() {
        return deviceLabelFontWidth;
    }

    public void setDeviceLabelFontWidth(int deviceLabelFontWidth) {
        this.deviceLabelFontWidth = deviceLabelFontWidth;
    }

    /* (non-Javadoc)
	 * @see com.netlight.layout.model.items.hierarchical.flower.IFlowerModel#getDeviceLabelFontHeight()
	 */
    @Override
	public int getDeviceLabelFontHeight() {
        return deviceLabelFontHeight;
    }

    public void setDeviceLabelFontHeight(int deviceLabelFontHeight) {
        this.deviceLabelFontHeight = deviceLabelFontHeight;
    }
    
    /* (non-Javadoc)
	 * @see com.netlight.layout.model.items.hierarchical.flower.IFlowerModel#getMinDist()
	 */
    @Override
	public double getMinDist() {
        return minDist;
    }

    /* (non-Javadoc)
	 * @see com.netlight.layout.model.items.hierarchical.flower.IFlowerModel#getMaxDist()
	 */
    @Override
	public double getMaxDist() {
        return maxDist;
    }
    
    /* EDGE MODEL */
    
    /*********** EDGES ***********/
    

    public void setEdge(FlowerEdge<E> edge){
        setEdge(edge.edge, edge.edgeInfo, edge.left, edge.leftInfo, edge.right, edge.rightInfo);
    }

    public void setEdge(E edge, IBoundedItem left, IBoundedItem right){
    	setEdge(edge, null, left, null, right, null);
    }
    
    public void setEdge(E edge, String edgeInfo, IBoundedItem left, String leftInfo, IBoundedItem right, String rightInfo){
        Pair<IBoundedItem,E> leftInfoKey = new Pair<IBoundedItem,E>(left, edge);
        Pair<IBoundedItem,E> rightInfoKey = new Pair<IBoundedItem,E>(right, edge);
        Pair<IBoundedItem,IBoundedItem> endPointsPair = new Pair<IBoundedItem,IBoundedItem>(left, right);
        itemEdgeInfos.put(leftInfoKey, leftInfo);
        itemEdgeInfos.put(rightInfoKey, rightInfo);
        edgeInfos.put(edge, edgeInfo);
        
        
        edges.add(edge);
        endPoints.put(edge, endPointsPair);
    }
    
    @Override
	public void removeEdge(E edge){
    	Pair<IBoundedItem,IBoundedItem> endPointsPair = getEndpoints(edge);
    	Pair<IBoundedItem,E> leftInfoKey = new Pair<IBoundedItem,E>(endPointsPair.a, edge);
        Pair<IBoundedItem,E> rightInfoKey = new Pair<IBoundedItem,E>(endPointsPair.b, edge);
        
        edgeInfos.remove(edge);
        edges.remove(edge);
        itemEdgeInfos.remove(leftInfoKey);
        itemEdgeInfos.remove(rightInfoKey);
        endPoints.remove(edge);
    }
    
    @Override
	public List<E> getEdgesHolding(IBoundedItem item){
    	List<E> out = new ArrayList<E>();
    	for(E e: endPoints.keySet()){
    		Pair<IBoundedItem, IBoundedItem> endpoints = getEndpoints(e);
    		
    		if(endpoints.a == item){
    			out.add(e);
    		}
    		else if(endpoints.b == item){
    			out.add(e);
    		}
    	}
    	return out;
    }
    
    /* (non-Javadoc)
	 * @see com.netlight.layout.model.items.hierarchical.flower.IFlowerModel#getEdges()
	 */
    @Override
	public Collection<E> getEdges(){
        return edges;
    }

    /* (non-Javadoc)
	 * @see com.netlight.layout.model.items.hierarchical.flower.IFlowerModel#getEndpoints(E)
	 */
    @Override
	public Pair<IBoundedItem,IBoundedItem> getEndpoints(E edge){
        return endPoints.get(edge);
    }
    
    /* (non-Javadoc)
	 * @see com.netlight.layout.model.items.hierarchical.flower.IFlowerModel#getEdgeInfo(E)
	 */
    @Override
	public String getEdgeInfo(E edge){
        return edgeInfos.get(edge);
    }

    /* (non-Javadoc)
	 * @see com.netlight.layout.model.items.hierarchical.flower.IFlowerModel#getItemInfo(E)
	 */
    @Override
	public Pair<String, String> getItemInfo(E edge){
        Pair<IBoundedItem,IBoundedItem> points = endPoints.get(edge);
        Pair<IBoundedItem,E> leftInfoKey = new Pair<IBoundedItem,E>(points.a, edge);
        Pair<IBoundedItem,E> rightInfoKey = new Pair<IBoundedItem,E>(points.b, edge);
        String left = itemEdgeInfos.get(leftInfoKey);
        String right = itemEdgeInfos.get(rightInfoKey);
        return new Pair<String,String>(left, right);
    } 
    
    /* */

    protected IBoundedItem center;
    protected List<IBoundedItem> neighbours;
    protected List<HyperEdgeStructure> networkStructures;
    
    protected List<E> edges = new ArrayList<E>();
    protected Map<Pair<IBoundedItem,E>,String> itemEdgeInfos = new HashMap<Pair<IBoundedItem,E>,String>();
    protected Map<E,String> edgeInfos = new HashMap<E, String>();
    protected Map<E,Pair<IBoundedItem,IBoundedItem>> endPoints = new HashMap<E,Pair<IBoundedItem,IBoundedItem>>();


    protected double minDist = 300;
    protected double maxDist = 1000;
    protected double minInterNodeDist = 5;
    
    protected String networkLabelPattern = "XXX.XXX.XXX.XXX/XX";
    protected int networkLabelFontSize = 24;
    protected int networkLabelFontWidth = 0;
    protected int networkLabelFontHeight = 0;

    protected String deviceLabelPattern = ".XXX";
    protected int deviceLabelFontSize = 12;
    protected int deviceLabelFontWidth = 0;
    protected int deviceLabelFontHeight = 0;
    
    /* UTILS */
    
    public static boolean inStructure(List<HyperEdgeStructure> structure, IBoundedItem item){
        for(HyperEdgeStructure hydre: structure){
            if(hydre.containsNeighbour(item))
                return true;
        }
        return false;
    }
    
    public static float getMaxRadius(List<IBoundedItem> items){
        float max = 0;
        for(IBoundedItem item: items){
            float r = item.getRadialBounds();
            if(r>max)
                max = r;
        }
        return max;
    }
}
