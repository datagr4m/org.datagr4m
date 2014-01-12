package org.datagr4m.drawing.model.items.hierarchical.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.FAAttraction;
import org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.FARepulsion;
import org.datagr4m.drawing.layout.algorithms.forces.IForce;
import org.datagr4m.drawing.model.factories.IHierarchicalModelFactory;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.AbstractHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public abstract class AbstractHierarchicalGraphModel extends
		AbstractHierarchicalModel implements IHierarchicalGraphModel {
	private static final long serialVersionUID = -7766840768858722210L;

	public AbstractHierarchicalGraphModel(IHierarchicalNodeModel parent,
			List<IBoundedItem> children, Collection<IBoundedItem> neighbours) {
		super(parent, children, neighbours);
		localEdges = new ArrayList<Pair<IBoundedItem, IBoundedItem>>();
		attractionEdges = new ArrayList<Pair<IBoundedItem, IBoundedItem>>();
		nodeDegree = new HashMap<IBoundedItem, Integer>();
		attractors = ArrayListMultimap.create();
		repulsors = ArrayListMultimap.create();
		attractionEdgeForces = new ArrayList<IForce>();
	}

	public AbstractHierarchicalGraphModel() {
		this(null, new ArrayList<IBoundedItem>(), new ArrayList<IBoundedItem>());
	}

	public AbstractHierarchicalGraphModel(IHierarchicalNodeModel parent,
			List<IBoundedItem> children) {
		super(parent, children);
	}

	public AbstractHierarchicalGraphModel(IHierarchicalNodeModel parent) {
		super(parent);
	}

	@Override
	public Map<IBoundedItem, IBoundedItem> changeChildrenLayoutModels(
			IHierarchicalModelFactory factory) {
		Map<IBoundedItem, IBoundedItem> changeMap = new HashMap<IBoundedItem, IBoundedItem>(
				children.size());
		for (IBoundedItem child : children) {
			IBoundedItem i = factory.getLayoutModel(child.getObject());
			if (i != null) {
				i.setParent(this);
				i.changePosition(child.getPosition());
				setNodeDegree(i, getNodeDegree(child));
				changeMap.put(child, i);
			}
		}
		for (IBoundedItem replaced : changeMap.keySet()) {
			IBoundedItem newItem = changeMap.get(replaced);
			children.remove(replaced);
			children.add(newItem);
			// children.c
			registerChild(newItem.getObject(), newItem);
		}
		if (changeMap.size() > 0) {
			fireBoundsDirty();
			// fireParentPositionChanged();
		}
		return changeMap;
	}

	/**
	 * Performs various verification to be done after construction:
	 * <ul>
	 * <li>Ensure all node have non negative degree.
	 * </ul>
	 */
	public void verify() throws Exception {
		for (IBoundedItem child : getChildren()) {
			if (getNodeDegree(child) == -1)
				throw new Exception(
						"at least one child has no degree information: "
								+ child);
			if (child.getParent() != this)
				throw new Exception("parent not set: " + child);
			/*
			 * if(child instanceof AbstractHierarchicalGraphModel){
			 * ((AbstractHierarchicalGraphModel)child).verify(); }
			 */
		}
	}

	/****** EDGES & NODE DEGREE ******/

	public void addLocalEdge(IBoundedItem source, IBoundedItem dest) {
		localEdges.add(new Pair<IBoundedItem, IBoundedItem>(source, dest));
	}

	public void addLocalEdge(Pair<IBoundedItem, IBoundedItem> edge) {
		localEdges.add(edge);
	}

	@Override
	public boolean hasLocalEdge(Pair<IBoundedItem, IBoundedItem> edge) {
		return localEdges.contains(edge);
	}

	@Override
	public Collection<Pair<IBoundedItem, IBoundedItem>> getLocalEdges() {
		return localEdges;
	}

	@Override
	public Collection<Pair<IBoundedItem, IBoundedItem>> getLocalEdges(
			IBoundedItem source, IBoundedItem destination) {
		Pair<IBoundedItem, IBoundedItem> key = new Pair<IBoundedItem, IBoundedItem>(
				source, destination);
		Collection<Pair<IBoundedItem, IBoundedItem>> output = new ArrayList<Pair<IBoundedItem, IBoundedItem>>();

		for (Pair<IBoundedItem, IBoundedItem> edge : localEdges)
			if (key.equals(edge))
				output.add(edge);
		return output;
	}

	public void setNodeDegree(IBoundedItem item, int degree) {
		nodeDegree.put(item, degree);
	}

	@Override
	public int getNodeDegree(IBoundedItem item) {
		if (nodeDegree.containsKey(item))
			return nodeDegree.get(item);
		else
			return -1;
	}

	/********** FORCES *******/

	@Override
	public Collection<IForce> getAttractors(IBoundedItem item) {
		return attractors.get(item);
	}

	@Override
	public Collection<IForce> getRepulsors(IBoundedItem item) {
		return repulsors.get(item);
	}

	@Override
	public Collection<IForce> getAttractorForces() {
		return attractionEdgeForces;
	}

	public void addAttractor(IBoundedItem target, IBoundedItem source) {
		addAttractor(target, new FAAttraction(target, source));
	}

	public void addRepulsor(IBoundedItem target, IBoundedItem source) {
		addRepulsor(target, new FARepulsion(target, source));
	}

	public void addAttractor(IBoundedItem target, IForce source) {
		attractors.put(target, source);
	}

	public void addRepulsor(IBoundedItem target, IForce source) {
		repulsors.put(target, source);
	}

	// public void hasAttractor()

	public void addAttractionEdgeForce(IBoundedItem source, IBoundedItem dest) {
		addAttractionEdgeForce(new Pair<IBoundedItem, IBoundedItem>(source,
				dest));
	}

	public void addAttractionEdgeForce(Pair<IBoundedItem, IBoundedItem> edge) {
		attractionEdges.add(edge);
		FAAttraction faa = new FAAttraction(edge.a, edge.b);

		// if(edge.a.getLabel().startsWith("dmz")||edge.b.getLabel().startsWith("dmz"))
		// faa.setWeight(100);

		// System.out.println(edge.a.getLabel() + ">" + edge.b.getLabel());
		attractionEdgeForces.add(faa);
	}

	@Override
	public boolean hasAttractionEdges(IBoundedItem source,
			IBoundedItem destination) {
		return attractionEdges.contains(new Pair<IBoundedItem, IBoundedItem>(
				source, destination));
	}

	@Override
	public boolean hasAttractionEdges(Pair<IBoundedItem, IBoundedItem> edge) {
		return attractionEdges.contains(edge);
	}

	@Override
	public List<IForce> getAttractionEdgeForces() {
		return attractionEdgeForces;
	}

	@Override
	public List<IForce> getAttractionEdgeForces(IBoundedItem item) {
		List<IForce> output = new ArrayList<IForce>();
		for (IForce f : attractionEdgeForces)
			if (f.getOwner().equals(item))
				output.add(f);
		return output;
	}

	@Override
	public Collection<Pair<IBoundedItem, IBoundedItem>> getAttractionEdges() {
		return attractionEdges;
	}

	@Override
	public Collection<Pair<IBoundedItem, IBoundedItem>> getAttractionEdges(
			IBoundedItem source) {
		Collection<Pair<IBoundedItem, IBoundedItem>> output = new ArrayList<Pair<IBoundedItem, IBoundedItem>>();

		for (Pair<IBoundedItem, IBoundedItem> edge : attractionEdges)
			if (source.equals(edge.a))
				output.add(edge);
		return output;
	}

	@Override
	public Collection<Pair<IBoundedItem, IBoundedItem>> getAttractionEdges(
			IBoundedItem source, IBoundedItem destination) {
		Pair<IBoundedItem, IBoundedItem> key = new Pair<IBoundedItem, IBoundedItem>(
				source, destination);
		Collection<Pair<IBoundedItem, IBoundedItem>> output = new ArrayList<Pair<IBoundedItem, IBoundedItem>>();

		for (Pair<IBoundedItem, IBoundedItem> edge : attractionEdges)
			if (key.equals(edge))
				output.add(edge);
		return output;
	}

	/* UTILS TO CREATE FORCES */

	/**
	 * Create (N*N-N) repulsors
	 */
	public void createAllMutualRepulsors() {
		for (IBoundedItem b1 : getChildren())
			for (IBoundedItem b2 : getChildren())
				if (b1 != b2)
					addRepulsor(b1, b2);
	}

	/**
	 * Create (N*N/2 - N) instead of N repulsors
	 */
	public void createAllMutualRepulsorsNoSymetry() {
		int n = getChildren().size();
		// int t = (n*n)/2;
		// int k = 0;

		for (int i = 0; i < n; i++) {
			// System.out.println((k++) + "/" + n);
			IBoundedItem b1 = children.get(i);
			for (int j = i + 1; j < n; j++) {
				IBoundedItem b2 = children.get(j);
				addRepulsor(b1, b2);
			}
		}
	}

	/**
	 * Create (N*N-N) attractors
	 */
	public void createAllMutualAttractors() {
		for (IBoundedItem b1 : getChildren())
			for (IBoundedItem b2 : getChildren())
				if (b1 != b2) {
					addAttractionEdgeForce(b1, b2);
				}
	}

	public void createMutualAttractors(IBoundedItem source,
			List<IBoundedItem> neighbours) {
		for (IBoundedItem neigbour : neighbours) {
			addAttractionEdgeForce(source, neigbour);
		}
	}

	/*****************/

	protected Collection<Pair<IBoundedItem, IBoundedItem>> localEdges;
	protected Collection<Pair<IBoundedItem, IBoundedItem>> attractionEdges;
	protected Map<IBoundedItem, Integer> nodeDegree;
	protected Multimap<IBoundedItem, IForce> attractors;
	protected Multimap<IBoundedItem, IForce> repulsors;
	protected List<IForce> attractionEdgeForces;
}
