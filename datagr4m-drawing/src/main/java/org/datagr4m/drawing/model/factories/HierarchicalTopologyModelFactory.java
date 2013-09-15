package org.datagr4m.drawing.model.factories;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import org.apache.log4j.Logger;
import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.factories.filters.GroupFilter;
import org.datagr4m.drawing.model.items.DefaultBoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemShape;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.explorer.CollapsedModelItem;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.IHierarchicalGraphModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.HierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.hierarchical.pair.HierarchicalPairModel;
import org.datagr4m.drawing.model.items.hierarchical.visitor.ItemVisitorAdapter;
import org.datagr4m.drawing.model.items.hierarchical.visitor.ModelEdgesVisitor;
import org.datagr4m.drawing.model.items.zones.IZoneModel;
import org.datagr4m.drawing.model.items.zones.ZoneModel;
import org.datagr4m.drawing.model.items.zones.ZoningModel;
import org.datagr4m.topology.Group;
import org.datagr4m.topology.Topology;
import org.datagr4m.topology.Zone;
import org.datagr4m.view2d.icons.IconLibrary;

import edu.uci.ics.jung.graph.Graph;

/**
 * Able to produce hierarchical models for a topology, meaning:
 * <ul>
 * <li>A complete hierarchy of hierarchical models is created using
 * <ul>
 * <li>pairs
 * <li>graphs
 * </ul>
 * <li>Some forces are added to each item held by a model according to the
 * topology structure
 * <li>A hierarchical edge model is created
 * </ul>
 * 
 * @author Martin Pernollet
 */
public class HierarchicalTopologyModelFactory<V, E> implements
		IHierarchicalModelFactory {
	protected GroupFilter<V> filter;
	protected boolean rectangularGroups = false;
	protected boolean crashOnDuplicateItemLocation = false; // if false, will
															// ignore

	public HierarchicalTopologyModelFactory() {
		this(new GroupFilter<V>() {
			@Override
			public boolean accepts(Group<V> group) {
				return true;
			}
		});
	}

	public HierarchicalTopologyModelFactory(GroupFilter<V> filter) {
		this.filter = filter;
	}

	@Override
	public IBoundedItem getLayoutModel(Object data) {
		if (data instanceof Topology<?, ?>) {
			@SuppressWarnings("unchecked")
			IHierarchicalGraphModel model = getTopologyLayoutModel((Topology<V, E>) data);

			if (rectangularGroups) {
				ItemVisitorAdapter visitor = new ItemVisitorAdapter() {
					@Override
					public void doVisitElement(IHierarchicalModel parent,
							IBoundedItem element, int depth) {
						if (element instanceof IHierarchicalModel) {
							IHierarchicalModel model = ((IHierarchicalModel) element);
							int level = model.getMaxLeafLevel();
							if (level >= 2)
								model.setShape(ItemShape.RECTANGLE);
						}
					}
				};
				visitor.visit(model);
			}
			return model;
		} else
			throw new IllegalArgumentException("object not supported: " + data);
	}

	public IHierarchicalGraphModel getTopologyLayoutModel(
			final Topology<V, E> topology) {
		// build complete model recursively
		HierarchicalGraphModel model = new HierarchicalGraphModel();
		model.setObject(topology);
		model.setLabel("root");

		createHierarchy(topology, model);
		createForces(topology, model);
		createEdgeModel(topology, model);
		createZoningModel(topology, model);
		// model.toConsole();
		return model;
	}

	public void createHierarchy(final Topology<V, E> topology,
			HierarchicalGraphModel model) {
		if (topology.getGroups() != null && topology.getGroups().size() > 0) {
			for (Group<V> group : topology.getGroups()) {
				if (filter.accepts(group)) {
					createChildrenGroup(topology, model, group, 0);
				}
			}
		}
		// also add all item that do not belong to a group
		Group<V> ungrouped = topology.getItemsWithoutGroup();
		for (V item : ungrouped) {
			createChildrenItem(topology, model, item);
		}
	}

	public void createZoningModel(final Topology<V, E> topology,
			HierarchicalGraphModel model) {
		ZoningModel zoningModel = new ZoningModel();
		List<Zone<V>> zones = topology.getZones();
		for (Zone<V> zone : zones) {
			IZoneModel zoneModel = new ZoneModel(zone.getName(), zone);
			for (V item : zone) {
				IBoundedItem itemModel = model.getItem(item);
				zoneModel.addChild(itemModel, false);
			}
			zoneModel.updateGeometry();
		}
		model.setZoningModel(zoningModel);
	}

	@SuppressWarnings("unchecked")
	protected void createEdgeModel(Topology<V, E> topology,
			HierarchicalGraphModel model) {
		ModelEdgesVisitor edgeFinder = new ModelEdgesVisitor();
		edgeFinder.visit(model);
		IHierarchicalEdgeModel tubeModel = getHierarchicalEdgeModel();
		tubeModel.build(topology, model);
		// tubeModel.toConsole();
		model.setEdgeModel(tubeModel);
	}

	/*************/

	public interface AsCollapsedPredicate<V> {
		public boolean shouldCollapse(Group<V> group);
	}

	public class FarmCollapsedPredicate implements AsCollapsedPredicate<V> {
		@Override
		public boolean shouldCollapse(Group<V> group) {
			String type = group.getType();
			return type != null && type.contains("farm")
					&& !type.contains("megafarm");
		}
	}

	public class SinglesGroupCollapsedPredicate implements
			AsCollapsedPredicate<V> {
		@Override
		public boolean shouldCollapse(Group<V> group) {
			String type = group.getType();
			return type != null && type.contains("Singles");
		}
	}

	public List<AsCollapsedPredicate<V>> getCollapsedPredicates() {
		return collapsedPredicates;
	}

	public void setCollapsedPredicates(
			List<AsCollapsedPredicate<V>> collapsedPredicates) {
		this.collapsedPredicates = collapsedPredicates;
	}

	protected List<AsCollapsedPredicate<V>> collapsedPredicates = new ArrayList<AsCollapsedPredicate<V>>();
	{
		collapsedPredicates.add(new FarmCollapsedPredicate());
		collapsedPredicates.add(new SinglesGroupCollapsedPredicate());
	}

	public boolean matchCollapsedPredicate(Group<V> group) {
		for (AsCollapsedPredicate<V> p : collapsedPredicates) {
			if (p.shouldCollapse(group))
				return true;
		}
		return false;
	}

	protected void createChildrenGroup(Topology<V, E> topology,
			HierarchicalGraphModel parent, Group<V> group, int depth)
			throws RuntimeException {
		// a standard group
		if (!isPair(group)) {
			HierarchicalGraphModel groupModel = getGroupLayoutModel(group);

			// COLLAPSED FARM
			if (group.getType() == null)
				Logger.getLogger(this.getClass()).warn(
						"no group type for group '" + group + "'");
			if (matchCollapsedPredicate(group)) {
				Icon i;
				// if(type.equals("farm"))
				i = getCollapsableIcon(group, groupModel);
				// else
				// i = IconLibrary.getIcon("data/images/" + type);
				CollapsedModelItem farm = new CollapsedModelItem(
						group.getName(), i, groupModel);// new
														// DefaultBoundedItemIcon(group.getName(),
														// i);
				groupModel.setCollapsedModel(farm);
				groupModel.setCanCollapse(true);
				groupModel.setCollapsed(true);
				groupModel.setCanExpand(false);
			}

			int degree = topology.getGroupDegree(group);
			parent.registerChild(group, groupModel);
			parent.setNodeDegree(groupModel, degree);

			createChildrenItems(topology, groupModel, group);

			if (group.getSubGroups() != null)
				for (Group<V> subGroup : group.getSubGroups()) {
					createChildrenGroup(topology, groupModel, subGroup,
							depth + 1);
				}

			// setup layout
			// createGroupForces(topology, group, groupModel, depth);
			// System.out.println("done! " + group.getName());
		}
		// a pair
		else {
			HierarchicalPairModel pairModel = getPairLayoutModel(group);
			int degree = topology.getGroupDegree(group);
			parent.registerChild(group, pairModel);
			parent.setNodeDegree(pairModel, degree);

			createChildrenItem(topology, pairModel, group.get(0));
			createChildrenItem(topology, pairModel, group.get(1));
		}
	}

	protected Icon getCollapsableIcon(Group<V> group, HierarchicalGraphModel groupModel) {
		Icon i;
		i = IconLibrary.getIcon("data/images/icons/symbolic/n3.png");
		return i;
	}

	protected void createChildrenItems(Topology<V, E> topology,
			HierarchicalGraphModel parent, Group<V> group/*
														 * ,
														 * HierarchicalGraphModel
														 * groupModel
														 */)
			throws RuntimeException {
		for (V item : group) {
			if (item == null)
				throw new RuntimeException("Item " + item + "");
			createChildrenItem(topology, parent, item);
		}
	}

	/**
	 * Add the item as a child of the given parent, only if it does not exist
	 * yet in the hierarchy. If the item already exists, factory will either
	 * ignore addition, or crash, depending on value of
	 * {@link crashOnDuplicateItemLocation}.
	 */
	protected void createChildrenItem(Topology<V, E> topology,
			HierarchicalGraphModel parent, V item) {
		if (verifyNoExistYet(parent, item)) {
			IBoundedItem itemModel = getItemLayoutModel(item);
			if (!topology.getGraph().containsVertex(item)) {
				Logger.getLogger(this.getClass()).error(
						"a topology group contains " + item
								+ " but it is not referenced in the graph");
			} else {
				int degree = topology.getItemDegree(item);
				parent.registerChild(item, itemModel);
				parent.setNodeDegree(itemModel, degree);
			}
		} else
			Logger.getLogger(this.getClass()).info(
					"item '" + item + "' already exists in "
							+ parent.getLabel());
	}

	/**
	 * Add the item as a child of the given parent, only if it does not exist
	 * yet in the hierarchy. If the item already exists, factory will either
	 * ignore addition, or crash, depending on value of
	 * {@link crashOnDuplicateItemLocation}
	 */
	protected void createChildrenItem(Topology<V, E> topology,
			HierarchicalPairModel parent, V item) {
		if (verifyNoExistYet(parent, item)) {
			IBoundedItem itemModel = getItemLayoutModel(item);
			parent.registerChild(item, itemModel);
		} else
			Logger.getLogger(this.getClass()).info(
					"item '" + item + "' already exists in "
							+ parent.getLabel());
	}

	/**
	 * Returns true if item does not exist in the hierarchy.
	 * 
	 * If item exist, either throw exception or return false, according to value
	 * crashOnDuplicateItemLocation.
	 */
	protected boolean verifyNoExistYet(IHierarchicalModel parent, V item) {
		IBoundedItem p = parent.getRoot().getItem(item);
		if (p != null) {
			if (crashOnDuplicateItemLocation) {
				String m = "Item " + item
						+ " is already defined in the hierarchy in "
						+ parent.getRoot().getItem(item).getParent().getLabel()
						+ ", can't be added to " + parent.getLabel();
				throw new RuntimeException(m);
			} else
				return false;
		}
		return true;
	}

	/******* CREATION DES FORCES *******/

	protected boolean LOG_FORCE = true;

	protected void createForces(Topology<V, E> topology,
			HierarchicalGraphModel topologyModel) {
		createRootItemsForce(topology, topologyModel);
		createGroupForces(topology, topology.getGroups(), topologyModel);
	}

	/**
	 * Etablie attraction item->item pour tous les items n'appartenant � aucun
	 * groupe, donc situ�s � la racine du mod�le
	 */
	protected void createRootItemsForce(Topology<V, E> topology,
			HierarchicalGraphModel parentModel) {
		// parentModel.createAllMutualRepulsorsNoSymetry();
		Group<V> nogroup = topology.getItemsWithoutGroup();
		Graph<V, E> subGraph = topology.getSubGraph(nogroup);

		for (E edge : subGraph.getEdges()) {
			V v1 = subGraph.getEndpoints(edge).getFirst();
			V v2 = subGraph.getEndpoints(edge).getSecond();

			IBoundedItem item1 = parentModel.getItem(v1);
			IBoundedItem item2 = parentModel.getItem(v2);

			if (item1 != null && item2 != null && item1 != item2) {
				HierarchicalGraphModel parent = (HierarchicalGraphModel) item1
						.getParent();

				Pair<IBoundedItem, IBoundedItem> e = new Pair<IBoundedItem, IBoundedItem>(
						item1, item2);
				if (!parent.hasAttractionEdges(e)) {
					parent.addAttractionEdgeForce(e);
				}
			} else
				throw new RuntimeException("did not find either item 1 or 2: "
						+ v1 + ", " + v2);
		}
	}

	/**
	 * Etablie attraction item->item / group->group en fonction du graphe global
	 * machine to machine port� par la topologie.
	 * 
	 * Si deux items ont un arc les reliant, mais ne sont pas dans le m�me
	 * groupe, on cherche les groupes parents contenu. par un ancetre commun, et
	 * on ajoute une force entre ces deux groupes.
	 * 
	 * La proc�dure est r�cursive.
	 */
	protected void createGroupForces(Topology<V, E> topology,
			List<Group<V>> groups, HierarchicalGraphModel parentModel) {
		// System.out.println(parentModel.getLabel() + " can expand:" +
		// parentModel.canExpand());

		boolean ALLOW_ANCESTORS = false;

		// ----------------------------------------------------------------
		// 1) Analyse l'item parent pour voir si on doit se passer de force
		boolean shouldHaveForce = true;

		// a) Si item non expandable, pas de force
		if (!parentModel.canExpand())
			shouldHaveForce = false;

		// b) Si un seul item, aucune force dans ce groupe
		if (parentModel.getChildren().size() == 1)
			shouldHaveForce = false;

		// ------------------------------------------------------
		// 2) Cr�� une r�pulsion entre tous les item de ce niveau
		if (shouldHaveForce)
			parentModel.createAllMutualRepulsorsNoSymetry();

		// ------------------------------------------------------
		// 3) Pour chaque groupe:
		// a) traite ses sous groupes descendants de mani�re r�cursive
		// b) �tablie une force attractive avec ses voisins
		for (Group<V> g : groups) {
			// ---------------------------------------------
			// a) Descend dans la hierarchie: gere les forces au sein des
			// groupes enfants
			IBoundedItem item = parentModel.getItem(g);
			if (item == null)
				throw new RuntimeException("ne trouve pas le modele de '"
						+ g.getName() + "' (parent: '" + g.getParent()
						+ "') dans " + parentModel.getLabel());
			else {
				// System.out.println("group:" + g +
				// " a pour item:"+item.getLabel());
				if (item instanceof HierarchicalGraphModel)
					createGroupForces(topology, g.getSubGroups(),
							(HierarchicalGraphModel) item);
			}

			// ----------------------------------------
			// b) Etablie des attractions entre tous les GROUPES connect�s
			// DANS ce niveau
			if (shouldHaveForce) {
				// ATTENTION CA DOIT SE MULTIPLIER PAR LE NOMBRE DE GROUPE!!
				interGroupAttractionInGroup(topology, parentModel, g,
						ALLOW_ANCESTORS);
			}

			// ----------------------------------------
			// c) Etablie des attraction entre tous les ITEMS connect�s DANS
			// ce niveau
			if (true/* shouldHaveForce */) {
				interItemAttractionInGroup(topology, parentModel, g,
						ALLOW_ANCESTORS);
			}
		}
	}

	/**
	 * Etablie des attraction entre groupes voisins ou entre leurs parents.
	 * <ul>
	 * <li>Si deux groupes sont voisins et ont le m�me parent, alors on cr�e
	 * une force d'attraction
	 * <li>Si deux groupes sont voisins et ont un ancetre commun, alors les deux
	 * groupes enfants de cet ancetre auront une force d'attraction (sauf si ces
	 * deux enfants sont une m�me entit�)
	 * </ul>
	 * 
	 * @see {@link Topology.getGroupNeighbours()}
	 */
	protected void interGroupAttractionInGroup(Topology<V, E> topology,
			HierarchicalGraphModel parentModel, Group<V> g,
			boolean allowAncestorForces) throws RuntimeException {
		List<Group<V>> neighbours = topology.getGroupNeighbours(g);

		IBoundedItem item1 = parentModel.getRoot().getItem(g);
		if (item1 == null)
			throw new RuntimeException("internal error: no model for group A "
					+ g + " in " + parentModel.getLabel());

		for (Group<V> neighbour : neighbours) {
			if (neighbour != null) {
				IBoundedItem item2 = parentModel.getRoot().getItem(neighbour);

				if (item2 == null) {
					// A) le groupe voisin est dans une autre branche de la
					// hierarchie
					throw new RuntimeException("no model for group B "
							+ neighbour);
				} else {

					// B) si le groupe parent imm�diat est commun, alors on
					// cr�e une attraction
					if (item1.getParent() == item2.getParent()) {
						HierarchicalGraphModel parent = (HierarchicalGraphModel) item1
								.getParent();
						parent.addAttractionEdgeForce(new Pair<IBoundedItem, IBoundedItem>(
								item1, item2));
						// parent.addAttractor(item2, item1);
						// System.out.println("ATTRACTION in " +
						// parent.getLabel() + ": " + item1.getLabel() + ">" +
						// item2.getLabel());
					}

					// C) le groupe voisin est dans une autre branche de la
					// hierarchie, mais ils ont un
					// ancetre commun par exemple:
					// ANCESTOR-->parentg1-->group1
					// ANCESTOR-->parentg2-->group2
					else {
						if (allowAncestorForces) {
							HierarchicalGraphModel ancestor = (HierarchicalGraphModel) item1
									.getFirstCommonAncestor(item2);
							if (ancestor != null) {
								IHierarchicalModel p1 = item1
										.getChildrenOfAncestor(ancestor);
								IHierarchicalModel p2 = item2
										.getChildrenOfAncestor(ancestor);

								// accepte les mecs qui sont descendant direct
								// du point commun
								if (p1 == null && ancestor.hasChild(item1))
									p1 = (IHierarchicalModel) item1;
								if (p2 == null && ancestor.hasChild(item2))
									p2 = (IHierarchicalModel) item2;

								if (p1 != null && p2 != null) {
									if (p1 != p2) { // should never happen
										Logger.getLogger(this.getClass())
												.error("This attractor is not used!!");
										if (!ancestor
												.hasAttractionEdges(p1, p2))
											ancestor.addAttractionEdgeForce(p1,
													p2);
									}
								} else {
									String out = ("les ancetres sont nuls! "
											+ p1 + " et " + p2 + "  ancestor:" + ancestor);
									out += ("\n" + item1 + " et " + item2);
									throw new RuntimeException(out);
								}
							} else {
								throw new RuntimeException("pas d'ancetre! "
										+ item1.getLabel() + " et "
										+ item2.getLabel());
							}
						}
						// System.err.println("parents diff�rents: " +
						// item1.getLabel() + " et " + item2.getLabel());
					}
				}
			}
		}
	}

	/**
	 * Cr�� une force d'attraction entre deux �l�ments s'ils sont dans
	 * le m�me groupe. Sinon g�re avec les ancetres.
	 */
	protected void interItemAttractionInGroup(Topology<V, E> topology,
			HierarchicalGraphModel parentModel, Group<V> g,
			boolean allowAncestorForce) {
		Graph<V, E> subGraph = topology.getSubGraph(g);
		for (E edge : subGraph.getEdges()) {
			V v1 = subGraph.getEndpoints(edge).getFirst();
			V v2 = subGraph.getEndpoints(edge).getSecond();
			IBoundedItem item1 = parentModel.getItem(v1);
			IBoundedItem item2 = parentModel.getItem(v2);

			if (item1 != null && item2 != null && item1 != item2) {
				// s'ils sont dans le m�me groupe et que ce parent est un
				// groupe supportant des force
				if (item1.getParent() == item2.getParent()
						&& (item2.getParent() instanceof HierarchicalGraphModel)) {
					HierarchicalGraphModel parent = (HierarchicalGraphModel) item1
							.getParent();

					// parent.addAttractor(item2, item1);

					Pair<IBoundedItem, IBoundedItem> e = new Pair<IBoundedItem, IBoundedItem>(
							item1, item2);
					if (!parent.hasAttractionEdges(e)) {
						parent.addAttractionEdgeForce(e);
						// System.out.println("ATTRACTION in " +
						// parent.getLabel() + ": " + item1.getLabel() + ">" +
						// item2.getLabel());
					}
				}
				// sinon, on va placer une attraction sur les parents
				else {
					if (allowAncestorForce) {
						IHierarchicalModel firstAncestor = item1
								.getFirstCommonAncestor(item2);
						if (firstAncestor instanceof HierarchicalGraphModel) {
							HierarchicalGraphModel ancestor = (HierarchicalGraphModel) firstAncestor;
							if (ancestor != null) {
								IHierarchicalModel p1 = item1
										.getChildrenOfAncestor(ancestor);
								IHierarchicalModel p2 = item2
										.getChildrenOfAncestor(ancestor);

								// accepte les mecs qui sont descendant direct
								// du point commun
								if (p1 == null && ancestor.hasChild(item1))
									p1 = (IHierarchicalModel) item1;
								if (p2 == null && ancestor.hasChild(item2))
									p2 = (IHierarchicalModel) item2;

								if (p1 != null && p2 != null) {
									if (!ancestor.hasAttractionEdges(p1, p2)) {
										ancestor.addAttractionEdgeForce(p1, p2);
									}
								} else {
									System.err
											.println("les ancetres sont nuls! "
													+ p1 + " et " + p2);
									System.err.println(item1 + " et " + item2);
								}
							} else {
								System.err.println("pas d'ancetre! "
										+ item1.getLabel() + " et "
										+ item2.getLabel());
							}
						}
					}
				}
			}
		}
	}

	/* */

	public HierarchicalGraphModel getGroupLayoutModel(Group<V> group) {
		HierarchicalGraphModel groupModel = new HierarchicalGraphModel();
		groupModel.setLabel(group.getName());
		groupModel.setObject(group);
		return groupModel;
	}

	public HierarchicalPairModel getPairLayoutModel(Group<V> group) {
		HierarchicalPairModel pairModel = new HierarchicalPairModel();
		// if(group.getName().startsWith("base_p"))
		// System.out.println("gotcha");
		pairModel.setLabel(group.getName());
		// if(group==null)
		// System.out.println("ploum");
		pairModel.setObject(group);
		return pairModel;
	}

	public IBoundedItem getItemLayoutModel(V item) {
		return new DefaultBoundedItem(item);
	}

	protected IHierarchicalEdgeModel getHierarchicalEdgeModel() {
		IHierarchicalEdgeModel tubeModel = new HierarchicalEdgeModel();
		return tubeModel;
	}

	protected boolean isPair(Group<V> group) {
		if (group.size() == 2) {
			return true;
		}
		return false;
	}
}
