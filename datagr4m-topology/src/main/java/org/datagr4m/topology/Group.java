package org.datagr4m.topology;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Group<K> extends ArrayList<K>{
    public Group() {
        super();
    }
    public Group(Collection<? extends K> c) {
        super(c);
    }
    public Group(String name, Collection<? extends K> c) {
        super(c);
        setName(name);
    }
    public Group(String name, String type, Collection<? extends K> c) {
        super(c);
        setName(name);
        setType(type);
    }
    public Group(int initialCapacity) {
        super(initialCapacity);
    }
    public Group(String name) {
        this();
        setName(name);
    }
    public Group(String name, String type) {
        this();
        setName(name);
        setType(type);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((subGroups == null) ? 0 : subGroups.hashCode());
        return result;
    }
    
    /**
     * Two groups are considered equals if they have the same name, the same subgroups.
     * 
     */
    @Override
    //TODO: consider list of items and group type 
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        @SuppressWarnings("rawtypes")
		Group other = (Group) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (subGroups == null) {
            if (other.subGroups != null)
                return false;
        } else if (!subGroups.equals(other.subGroups))
            return false;
        return true;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Group<K> getParent() {
        return parent;
    }
    public void setParent(Group<K> parent) {
        this.parent = parent;
    }
    public List<Group<K>> getSubGroups() {
        return subGroups;
    }
    public void setSubGroups(List<Group<K>> subGroups) {
        this.subGroups = subGroups;
    }
    public void addSubGroups(List<? extends Group<K>> subGroups) {
        this.subGroups.addAll(subGroups);
    }
    public void addSubGroup(Group<K> subGroup) {
        subGroup.setParent(this);
        this.subGroups.add(subGroup);
    }
    public Group<K> getSubGroup(String name) {
        if (name != null) {
            for (Group<K> g : getSubGroups())
                if (g.getName() != null && g.getName().equals(name))
                    return g;
        } else {
            for (Group<K> g : getSubGroups())
                if (g.getName() == null)
                    return g;
        }
        return null;
    }
    
    public boolean hasSubGroup(String name) {
        if(getSubGroup(name)!=null)
            return true;
        return false;
    }
    
    
    public int depth(){
        if(subGroups.size()==0)
            return 0;
        else{
            int subDepth = 0;
            for(Group<K> sg: subGroups){
                int sd = sg.depth();
                if(sd>subDepth)
                    subDepth = sd;
            }
            return subDepth+1;
        }   
    }
    public int width(){
        return subGroups.size() + size();
    }
    
    @Override
	public String toString(){
        if(type==null || type.equals(""))
            return "notype:" + this.name + super.toString();
        else
            return this.type + ":" + this.name + super.toString();
    }
    
    /** returns all V items and V items hold by all subgroups.*/
    public Group<K> flattenItems(){
        Group<K> group = new Group<K>();
        group.addAll(this);
        for(Group<K> sg: subGroups)
            group.addAll(sg.flattenItems());
        return group;
    }
    
    public List<Group<K>> flattenSubgroups(){
        List<Group<K>> group = new ArrayList<Group<K>>();
        for(Group<K> sg: subGroups){
            group.addAll(sg.flattenSubgroups());
            group.add(sg);
        }
        return group;
    }
    
    public boolean containsAtLeastOne(Collection<K> items){
        List<Group<K>> subGroups = flattenSubgroups();
        for(K item: items){
            if(contains(item))
                return true;
            for(Group<K> subGroup: subGroups){
                if(subGroup.contains(item))
                    return true;
            }
        }
        return false;
    }
    
    /**
     * Return a group containing all items given as input,
     * by filtering the hierarchy of sub group as well.
     * 
     * Output group is new structure with a copy of name, type, and list object,
     * but keeps a reference to same K instances held by the current group.
     * 
     * @param items
     * @return
     */
    // TODO: test me
    public Group<K> filter(Collection<K> items){
        Group<K> filtered = new Group<K>();
        filtered.setName(this.getName());
        filtered.setType(this.getType());
        
        // garde chaque item de items s'il est contenu par ce group
        for(K item: items){
            if(contains(item))
                filtered.add(item);
        }
        
        for(Group<K> subGroup: getSubGroups()){
            Group<K> subGroupFiltered = subGroup.filter(items);
            if(subGroupFiltered.size()>0 || subGroupFiltered.getSubGroups().size()>0){
                filtered.addSubGroup(subGroupFiltered);
            }
        }
        return filtered;
    }
    
    protected String name = "";
    protected String type = "";
    protected List<Group<K>> subGroups = new ArrayList<Group<K>>();
    protected Group<K> parent;
    private static final long serialVersionUID = -9124873363833593799L;
}
