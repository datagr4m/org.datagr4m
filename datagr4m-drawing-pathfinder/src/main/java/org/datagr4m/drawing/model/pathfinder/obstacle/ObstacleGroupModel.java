package org.datagr4m.drawing.model.pathfinder.obstacle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.layout.pathfinder.IPathFinder;
import org.datagr4m.drawing.layout.pathfinder.PathFinderException;
import org.datagr4m.drawing.model.links.ILink;
import org.datagr4m.drawing.model.links.ILinkFactory;
import org.datagr4m.drawing.model.links.LinkFactory;
import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.drawing.model.slots.ISlotableItem;
import org.datagr4m.drawing.model.slots.ISlotableSetModel;

import com.google.common.collect.ArrayListMultimap;

public class ObstacleGroupModel implements ISlotableSetModel, Serializable{
    /** Run a path optimization for each held path */
    public void run(IPathFinder finder) /*throws PathFinderException*/{
        for(ISlotableItem o: getItems()){
            ((IPathObstacle)o).addBypass();
        }
        
        for(IPath p: getPathes()){
            ILink<ISlotableItem> ext = getExtremities(p);
            try {
                finder.find((IPathObstacle)ext.getSource(), (IPathObstacle)ext.getDestination(), p, asObstacle(getItems()), true);
            } catch (PathFinderException e) {
                e.printStackTrace();
                System.err.println("For " + ext.getSource() + " to " + ext.getDestination());
                //throw e;
            }
        }
    }
    
    public static List<IPathObstacle> asObstacle(List<? extends ISlotableItem> items){
        List<IPathObstacle> obs = new ArrayList<IPathObstacle>();
        //obs.addAll(items);
        for(ISlotableItem item: items)
            obs.add((IPathObstacle)item);
        return obs;
    }
    
    /**************/
    
    @Override
    public void addItem(ISlotableItem o){
        slotables.add(o);
    }
    
    @Override
    public boolean has(ISlotableItem o){
        return slotables.contains(o);
    }
    
    @Override
    public List<ISlotableItem> getItems(){
        return slotables;
    }
    
    @Override
    public ILink<Pair<ISlotableItem,Object>> addPath(ISlotableItem source, Object srcInterface, IPath p, ISlotableItem target, Object trgInterface) {
    	throw new RuntimeException("ObstacleGroup.addPath with interface: not implemented");
        //return null;
    }

    @Override
    public List<IPath> getPathes(ISlotableItem source, Object srcInterface, ISlotableItem target, Object trgInterface) {
        throw new RuntimeException("not implemented");
    }
    
    @Override
    public ILink<ISlotableItem> addPath(ISlotableItem source, IPath p, ISlotableItem target){
    	ILink<ISlotableItem> link = getLinkFactory().getLink(source, target);
        pathExtremities.put(p, link); //1->1
        itemsPathes.put(link, p); //1->N
        links.add(link);
        pathes.add(p);
        return link;
    }
    
    @Override
    public List<IPath> getPathes(ISlotableItem source, ISlotableItem target){
        return itemsPathes.get(getLinkFactory().getLink(source,target));
    }

    @Override    
    public ILink<ISlotableItem> getExtremities(IPath p){
        return pathExtremities.get(p);
    }
        
    @Override
    public List<IPath> getPathes(){
        return pathes;
    }
    
    @Override
    public List<ILink<ISlotableItem>> getLinks(){
        return links;
    }
    
    @Override
    public boolean has(IPath p){
        return pathes.contains(p);
    }
    
    @Override
    public void clearPathRegistry(){
        links.clear();
        pathes.clear();
        pathExtremities.clear();
        itemsPathes.clear();
    }
    
    @Override
    public void fillMissingItems(List<ILink<ISlotableItem>> links){
        for(ILink<ISlotableItem> link: links){
            if(!has(link.getSource()))
                addItem(link.getSource());
            if(!has(link.getDestination()))
                addItem(link.getDestination());
        }
    }
    
    @Override 
    public ILinkFactory getLinkFactory(){
        return linkFactory;
    }
    
    
    
    /**********************/
    
    protected List<ILink<ISlotableItem>> links = new ArrayList<ILink<ISlotableItem>>();
    protected List<ISlotableItem> slotables = new ArrayList<ISlotableItem>();
    protected List<IPath> pathes = new ArrayList<IPath>();
    protected Map<IPath, ILink<ISlotableItem>> pathExtremities = new HashMap<IPath, ILink<ISlotableItem>>();
    protected ArrayListMultimap<ILink<ISlotableItem>,IPath> itemsPathes = ArrayListMultimap.create();
    private static final long serialVersionUID = -6121038667185125508L;
    
    protected ILinkFactory linkFactory = new LinkFactory();

    @Override
    public void logLinks() {
        throw new RuntimeException("not implemented");
    }
}
