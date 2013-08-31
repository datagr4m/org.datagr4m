package org.datagr4m.drawing.model.items.hierarchical.flower;

import java.util.Collection;
import java.util.List;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.graph.IHierarchicalGraphModel;


public interface IFlowerModel<E> extends IHierarchicalGraphModel{
	public IBoundedItem getCenter();
	public List<HyperEdgeStructure> getNetworkItemsStructures();
	
	public List<IBoundedItem> getInternalCircleItems();
	public List<IBoundedItem> getExternalCircleItems();
	/** Return items from the external circle that are neighbours of the given internal circle item. */
	public List<IBoundedItem> getExternalCircleItems(IBoundedItem internalItem);

	public Collection<E> getEdges();
	public Pair<IBoundedItem, IBoundedItem> getEndpoints(E edge);
	public String getEdgeInfo(E edge);
	public Pair<String, String> getItemInfo(E edge);
	
	public void removeEdge(E edge);
	public List<E> getEdgesHolding(IBoundedItem item);
	
	public double getMinDist();
	public double getMaxDist();
	
	public String getDeviceLabelPattern();
	public int getDeviceLabelFontSize();
	public int getDeviceLabelFontWidth();
	public int getDeviceLabelFontHeight();
}