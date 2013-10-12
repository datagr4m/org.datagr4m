package org.datagr4m.topology;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datagr4m.datastructures.pairs.CommutativePair;
import org.datagr4m.utils.BinaryFiles;
import org.datagr4m.utils.StringUtils;
import org.jzy3d.io.SimpleCsv;

import com.google.common.collect.ArrayListMultimap;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * A {@link Topology<V,E>} holds:
 * <ul>
 * <li>a global {@link Graph<V,E>} indicating all raw device interconnection
 * <li>a list of hierarchical {@link Group<V>}, with possible typing indicating
 * the nature of this device group (Security group, Man group, etc)
 * <li>a list of {@link NGroup<V>}, with specialization indicating the
 * configuration attributes the group relies on (HSRP, OSPF, etc)
 * </ul>
 * 
 * Where:
 * <ul>
 * <li>V is a java type for vertex
 * <li>E is a java type for edge
 * </ul>
 * 
 * Rules:
 * <ul>
 * <li>an item V must belong to exactly 1 Group<V>.
 * <li>an item V may belong zero or several NGroup<V> at the same time.
 * <li>a user defining its own NGroup<V> implementation can define more fine
 * grained rules for the number of NGroup<V> an item can belong to.
 * </ul>
 */
public class Topology<V, E> implements Serializable {
    private static final long serialVersionUID = -7538247590213085759L;

    @SuppressWarnings("unchecked")
    public Topology() {
        this((Graph<V, E>) newGraph(), new ArrayList<Group<V>>());
    }

    public Topology(Graph<V, E> graph) {
        this(graph, new ArrayList<Group<V>>());
    }

    public Topology(Graph<V, E> graph, Group<V> hierarchy) {
        this(graph, asList(hierarchy));
    }

    public Topology(Graph<V, E> graph, List<Group<V>> hierarchy) {
        this(graph, hierarchy, new ArrayList<Zone<V>>(0));
    }

    public Topology(Graph<V, E> graph, List<Group<V>> hierarchy, List<Zone<V>> zones) {
        this.graph = graph;
        this.hierarchy = hierarchy;
        this.zones = zones;
        // this.structures = new ArrayList<Structure<V>>();
        index();
    }

    /********* INDEXATION DES ELEMENTS DU MODELE ********/

    public void index() {
        buildItemGroupMap();
        buildGroupNeighbourhood();
    }

    protected static boolean HIERARCHICAL_GROUPS = true;

    /** build mapping item->group */
    protected void buildItemGroupMap() {
        if (itemGroup == null)
            itemGroup = new HashMap<V, Group<V>>();
        else
            itemGroup.clear();

        List<Group<V>> allGroups = getAllGroups(HIERARCHICAL_GROUPS);
        for (Group<V> group : allGroups) {
            for (V item : group) {
                itemGroup.put(item, group);
            }
        }
    }

    /** build mapping itemGroup->neighbourItemGroups */
    protected void buildGroupNeighbourhood() {
        if (groupNeighbours == null)
            groupNeighbours = ArrayListMultimap.create();
        else
            groupNeighbours.clear();

        List<Group<V>> allGroups = getAllGroups(HIERARCHICAL_GROUPS);

        for (Group<V> group : allGroups) {
            List<Group<V>> neighbours = new ArrayList<Group<V>>();
            for (V item : group) {
                List<V> externals = computeExternalNeighbors(item);
                for (V external : externals) {
                    Group<V> g = getGroup(external);
                    // if(g!=null)
                    if (!neighbours.contains(g)) {
                        neighbours.add(g);
                    }
                }
            }
            groupNeighbours.putAll(group, neighbours);
        }
    }

    protected List<Group<V>> getAllGroups(boolean isRecursive) {
        List<Group<V>> groups = new ArrayList<Group<V>>();

        for (Group<V> group : hierarchy) {
            groups.add(group);
            if (isRecursive)
                groups.addAll(group.flattenSubgroups());
        }
        return groups;
    }
    
    /****************************/

    public Collection<V> getItems() {
        return graph.getVertices();
    }

    public Graph<V, E> getGraph() {
        return graph;
    }

    public void delete(V item) {
        Group<V> group = deleteItem(item);

        // remove group neighbourhood if it is empty
        if (group != null && group.size() == 0) {
            deleteGroup(group);
        }
    }

    public List<Group<V>> deleteEmptyGroups() {
        List<Group<V>> deleted = new ArrayList<Group<V>>();
        // Iterator<Group<V>> it = getGroups().iterator();
        for (Group<V> g : getGroups()) {
            if (g.size() == 0 && g.getSubGroups().size()==0)
                deleted.add(g);
        }
        /*
         * while(it.hasNext()){ Group<V> g = it.next(); if(g.size()==0)
         * deleted.add(g); }
         */
        getGroups().removeAll(deleted);
        return deleted;
    }

    /**
     * Delete item from the item graph and from its parent group.
     * 
     * @return the group that was modified.
     */
    protected Group<V> deleteItem(V item) {
        graph.removeVertex(item);

        Group<V> group = getGroup(item);
        if (group != null) {
            group.remove(item);
            itemGroup.remove(item);
        }
        return group;
    }

    public int getItemDegree(V item) {
        return graph.degree(item);
    }

    /************ TOPOLOGY GROUPS *************/

    public Group<V> findGroupWithName(String name) {
        return findGroupWithName(hierarchy, name);
    }

    public Group<V> findGroupWithName(List<Group<V>> groups, String name) {
        for (Group<V> group : groups)
            if (group.getName() != null)
                if (group.getName().equals(name))
                    return group;
        for (Group<V> group : groups) {
            Group<V> res = findGroupWithName(group.getSubGroups(), name);
            if (res != null)
                return res;
        }
        return null;
    }

    public void addGroup(Group<V> group) {
        hierarchy.add(group);
    }

    public void addGroup(Group<V> group, boolean addItemsToGraph) {
        hierarchy.add(group);
        if (addItemsToGraph)
            addItemToGraph(group);
    }

    public void addGroups(List<Group<V>> groups) {
        addGroups(groups, false);
    }

    public void addGroups(List<Group<V>> groups, boolean addItemsToGraph) {
        hierarchy.addAll(groups);
        if (addItemsToGraph)
            addItemsToGraph(groups);
    }

    protected void addItemsToGraph(List<Group<V>> groups) {
        for (Group<V> group : groups) {
            for (V item : group)
                graph.addVertex(item);
            addItemsToGraph(group.getSubGroups());
        }
    }

    protected void addItemToGraph(Group<V> group) {
        for (V item : group)
            graph.addVertex(item);
        addItemsToGraph(group.getSubGroups());
    }

    public List<Group<V>> getGroups() {
        return hierarchy;
    }

    public Group<V> getGroup(String name) {
        if (name != null) {
            for (Group<V> g : getGroups())
                if (g.getName() != null && g.getName().equals(name))
                    return g;
        } else {
            for (Group<V> g : getGroups())
                if (g.getName() == null)
                    return g;
        }
        return null;
    }
    
    public boolean hasGroup(String name) {
        if(getGroup(name)!=null)
            return true;
        return false;
    }

    public Group<V> getGroup(V item) {
        return itemGroup.get(item);
    }

    public void delete(Group<V> group) {
        if (group != null) {
            for (V item : group) {
                graph.removeVertex(item);
                itemGroup.remove(item);
            }
            deleteGroup(group);
        }
    }

    protected void deleteGroup(Group<V> group) {
        // unreference this group has a neighbour
        for (Group<V> g : groupNeighbours.keySet()) {
            List<Group<V>> gNs = groupNeighbours.get(g);
            if (gNs.contains(group))
                gNs.remove(group);
        }
        // delete this group has neighbour holder
        groupNeighbours.removeAll(group);
        // delete this group
        hierarchy.remove(group);
    }

    /** Return an edge for each item having a neighbour in another group. */
    public List<CommutativePair<V>> getDetailedIntergroupEdges() {
        List<CommutativePair<V>> externalNeighbours = new ArrayList<CommutativePair<V>>();
        for (V item : getGraph().getVertices()) {
            List<V> neighbours = computeExternalNeighbors(item);
            for (V item2 : neighbours) {
                CommutativePair<V> pair = new CommutativePair<V>(item, item2);
                if (!externalNeighbours.contains(pair))
                    externalNeighbours.add(pair);
            }
        }
        return externalNeighbours;
    }

    /** Return an edge for each group neighbours */
    public List<CommutativePair<Group<V>>> getIntergroupEdges() {
        List<CommutativePair<Group<V>>> edges = new ArrayList<CommutativePair<Group<V>>>();

        for (Group<V> group : hierarchy) {
            Collection<Group<V>> neighbours = getGroupNeighbours(group);
            for (Group<V> group2 : neighbours) {
                CommutativePair<Group<V>> pair = new CommutativePair<Group<V>>(group, group2);
                if (!edges.contains(pair))
                    edges.add(pair);
            }
        }
        return edges;
    }

    public List<Group<V>> getGroupNeighbours(Group<V> group) {
        return groupNeighbours.get(group);
    }

    /**
     * Compute degree by considering all neighbour nodes that are not in this
     * group.
     */
    public int getGroupDegree(Group<V> group) {
        int degree = 0;
        for (V v : group) {
            Collection<V> neighbours = graph.getNeighbors(v);
            if (neighbours != null) {
                for (V neighbour : neighbours)
                    if (!group.contains(neighbour)) {
                        degree++;
                    }
            }
        }
        return degree;
    }

    /**
     * Return the maximum depth of the groups, i.e. the maximum number of
     * subgroups levels it can have in its hierarchy
     * 
     * @return
     */
    public int getDepth() {
        int depth = 0;
        for (Group<V> g : getGroups()) {
            if (g.depth() > depth)
                depth = g.depth();
        }
        if (getGroups().size() > 0)
            depth++;
        return depth;
    }

    /**
     * Requires a fresh index().
     * 
     * @return
     */
    public Group<V> getItemsWithoutGroup() {
        Group<V> lost = new Group<V>();
        for (V v : graph.getVertices()) {
            Group<V> group = getGroup(v);
            if (group == null)
                lost.add(v);
        }
        return lost;
    }

    /**
     * Assuming all devices have been added to the graph, one may call this
     * method to gather all items into a root group which name is given by this
     * method.
     * 
     * 
     * One is supposed to call index(); after.
     */
    public void createDefaultRootGroup(String name, String type) {
        Group<V> perimeter = new Group<V>(name, type);
        for (V d : getGraph().getVertices())
            perimeter.add(d);
        getGroups().add(perimeter);
    }

    /* */

    public List<Zone<V>> getZones() {
        return zones;
    }

    /************ ITEM NEIGHBOURHOOD *************/

    public Collection<V> getNeighbors(V item) {
        return graph.getNeighbors(item);
    }

    public boolean areNeighbors(V item1, V item2) {
        return graph.getNeighbors(item1).contains(item2);
    }

    public boolean areInSameLocalGraph(V item1, V item2) {
        return getGroup(item1).contains(item2);
    }

    /**
     * Returns the list of graph neighbours of input item that stand in its
     * group.
     * 
     * If the item has no parent group, then any neighbour with no parent group
     * will be considered internal.
     */
    public List<V> computeInternalNeighbors(V item) {
        ArrayList<V> attractingInternal = new ArrayList<V>();
        Collection<V> neighbours = getNeighbors(item);
        if (neighbours == null)
            return attractingInternal;
        Group<V> group = getGroup(item);

        if (group != null) {
            for (V neighbour : neighbours)
                if (group.contains(neighbour))
                    attractingInternal.add(neighbour);
        } else {
            for (V neighbour : neighbours)
                if (getGroup(neighbour) == null)
                    attractingInternal.add(neighbour);
        }
        return attractingInternal;
    }

    /**
     * Returns the list of graph neighbours of input item that stand out its
     * group.
     * 
     * If the item has no parent group, then external neighbourhood has no sense
     * and the list is returned empty
     */
    public List<V> computeExternalNeighbors(V item) {
        ArrayList<V> attractingExternal = new ArrayList<V>();
        Collection<V> neighbours = getNeighbors(item);
        if (neighbours == null)
            return attractingExternal;
        Group<V> group = getGroup(item);

        // case we have a parent group for input item
        if (group != null) {
            for (V neighbour : neighbours)
                if (!group.contains(neighbour))
                    attractingExternal.add(neighbour);
        }

        return attractingExternal;
    }

    /************ SUBSTRUCTURES EXTRACTION AND MERGING *************/

    public void mergeInto(List<Group<V>> groups, Group<V> target) {
        for (Group<V> group : groups)
            // verify that input group is hold by topo
            if (!hierarchy.contains(group))
                throw new IllegalArgumentException("An input group does not belong to the topology yet: " + group);
            else {
                target.addAll(group);

                /*
                 * TODO: cleanup deleteGroup(group); List<Group<V>> neighbours =
                 * groupNeighbours.get(group);
                 * 
                 * for(Group<V> neighbour: neighbours){
                 * groupNeighbours.put(neighbour, target);
                 * groupNeighbours.put(target, neighbour); }
                 */
            }

        for (Group<V> g : groups) {
            hierarchy.remove(g);
        }

        hierarchy.add(target);
        for (V item : target)
            itemGroup.put(item, target);
        buildGroupNeighbourhood();
    }

    /**
     * Returns a new topology made of a subgraph of the original graph and
     * groups of input nodes given as a filter.
     * 
     * @param nodes
     *            to be kept in new topology
     * @return
     */
    public Topology<V, E> filter(List<V> nodes) {
        Graph<V, E> subGraph = getSubGraph(new Group<V>(nodes));

        List<Group<V>> filteredGroups = new ArrayList<Group<V>>();

        for (Group<V> group : getGroups()) {
            if (group.containsAtLeastOne(nodes)) {
                Group<V> filteredGroup = group.filter(nodes);
                if (filteredGroup.size() > 0 || filteredGroup.getSubGroups().size() > 0)
                    filteredGroups.add(filteredGroup);
            }
        }
        Topology<V, E> t = new Topology<V, E>(subGraph, filteredGroups);
        return t;
    }

    /**
     * A graph representation of the items stored in the group. The graph is
     * generated at each call, thus the caller has the ownership of the returned
     * graph.
     * 
     * However, the items in the graph are items from the internal global graph,
     * thus the caller has no ownership on the graph nodes and edges.
     */
    public Graph<V, E> getSubGraph(Group<V> group) {
        Graph<V, E> subGraph = newGraph();

        for (V item : graph.getVertices())
            if (group.contains(item)) {
                subGraph.addVertex(item);
            }

        for (E edge : graph.getEdges()) {
            Pair<V> endpoints = graph.getEndpoints(edge);
            if (group.contains(endpoints.getFirst()) && group.contains(endpoints.getSecond())) {
                subGraph.addEdge(edge, endpoints.getFirst(), endpoints.getSecond());
            }
        }
        return subGraph;
    }

    public List<E> getEdgesDontCareDirection(V source, V dest) {
        Collection<E> edges = graph.getIncidentEdges(source);
        List<E> output = new ArrayList<E>();

        for (E edge : edges) {
            Pair<V> pair = graph.getEndpoints(edge);
            if (pair.getFirst().equals(dest) || pair.getSecond().equals(dest)) {
                output.add(edge);
            }
        }
        return output;
    }

    /* */

    protected static <V> List<Group<V>> asList(Group<V> group) {
        List<Group<V>> list = new ArrayList<Group<V>>();
        list.add(group);
        return list;
    }

    public TopologyStatistics getStatistics() {
        return new TopologyStatistics(this);
    }

    public void toConsole() {
        toConsole(new ToString<V>() {
            @Override
            public String toString(V item) {
                return item.toString();
            }
        });
    }

    public void toConsole(ToString<V> stringIt) {
        for (Group<V> group : hierarchy) {
            toConsole(group, stringIt, 0);
        }
        System.out.println(getGraph().getVertexCount() + " vertices");
        System.out.println(getGraph().getEdgeCount() + " edges");
    }

    public void toConsole(Group<V> group, ToString<V> stringIt, int depth) {
        System.out.println(StringUtils.blanks(depth) + "[" + group.getClass().getSimpleName() + "] " + group.getName() + " (" + group.size() + " items, " + group.getSubGroups().size() + " subgroups)");
        for (V item : group)
            System.out.println(StringUtils.blanks(depth) + " " + stringIt.toString(item));
        for (Group<V> sg : group.getSubGroups())
            toConsole(sg, stringIt, depth + 1);
    }

    public void toCsv(String file, char separator) throws IOException {
        List<String[]> lines = new ArrayList<String[]>();
        for (Group<V> group : hierarchy) {
            String[] line = new String[2];
            line[0] = "[" + group.getClass().getSimpleName() + "] '" + group.getName() + "'";
            line[1] = group.size() + "";
            lines.add(line);
        }
        SimpleCsv.write(lines, file, separator);
    }

    /*
     * public void toGroupGraphML(String file) throws IOException{
     * Graph<Group<V>, String> groupGraph = new DirectedSparseGraph<Group<V>,
     * String>();
     * 
     * for(Group<V> g: localGraphs) groupGraph.addVertex(g);
     * 
     * int e=0; for(Group<V> g: localGraphs){ List<Group<V>> ng =
     * getGroupNeighbours(g);
     * 
     * for(Group<V> g2: ng){ groupGraph.addEdge(""+(e++), g, g2); } }
     * 
     * GraphIO.saveML(file, groupGraph); }
     */

    public void save(String filename) throws IOException {
        BinaryFiles.save(filename, this);
    }

    public static <V, E> Topology<V, E> load(String filename) throws IOException, ClassNotFoundException {
        return BinaryFiles.load(filename);
    }
    
    protected static <V, E> Graph<V, E> newGraph() {
        return new DirectedSparseMultigraph<V, E>();
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }


    /**************************/

    protected Graph<V, E> graph;
    protected List<Group<V>> hierarchy;
    protected List<Zone<V>> zones;

    // index
    protected Map<V, Group<V>> itemGroup;
    protected ArrayListMultimap<Group<V>, Group<V>> groupNeighbours;

    // protected List<Structure<V>> structures;

    /**************************/
}
