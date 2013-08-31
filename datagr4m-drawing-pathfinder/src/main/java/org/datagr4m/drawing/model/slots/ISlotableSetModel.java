package org.datagr4m.drawing.model.slots;

import java.util.List;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.links.DirectedLink;
import org.datagr4m.drawing.model.links.ILink;
import org.datagr4m.drawing.model.links.ILinkFactory;
import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.drawing.model.pathfinder.path.LockablePath;


/** 
 * A group of {@link ISlotableItem} that have {@link DirectedLink}s between each other, 
 * that are represented by {@link LockablePath}es, pluged into slots.
 */
public interface ISlotableSetModel {
    public void addItem(ISlotableItem o);
    public List<? extends ISlotableItem> getItems();
    public boolean has(ISlotableItem o);
    
    /** 
     * Register a path between the two given extremities. After such a call, 
     * the user can retrieve:
     * <ul>
     * <li>the extremities, i.e. the source and target of the path, by calling {@link getExtremities(IPath p)}
     * <li>all existing pathes for a pair of extremities, by calling {@link getPathes(ISlotableItem source, ISlotableItem target)}
     * <li>all the registered pathes
     * <li>all the registered links
     * </ul>
     * 
     * Note that the implementation is supposed to consider the path as directed,
     * meaning that adding path for (A,B) is not the same as calling a path for (B,A).
     * 
     * It is allowed to add several pathes for the same pair source/target, therefore
     * calling {@link getPathes()} returns a list.
     * 
     * Creating a path implies creating a link that describes the pair of object.
     * This link is instanciated by the {@link ILinkFactory} and can be retrieved at 
     * path addition.
     */
    public ILink<ISlotableItem> addPath(ISlotableItem source, IPath p, ISlotableItem target);
    public ILink<Pair<ISlotableItem,Object>> addPath(ISlotableItem source, Object srcInterface, IPath p, ISlotableItem target, Object trgInterface);

    /** Return all pathes that exists between the given source and target.*/
    public List<IPath> getPathes(ISlotableItem source, ISlotableItem target);
    public List<IPath> getPathes(ISlotableItem source, Object srcInterface, ISlotableItem target, Object trgInterface);

    
    /** Return all extremities of a path that have been registered by addPath(...).*/
    public ILink<ISlotableItem> getExtremities(IPath p);
    
    /** Return all paths that have been registered by addPath(...).*/
    public List<IPath> getPathes();    
    /** Return all links that have been registered by addPath(...).*/
    public List<ILink<ISlotableItem>> getLinks();
    
    /** Return true if the path is hold by this slotable group, according to
     * {@link IPath.equals(Object)}.*/
    public boolean has(IPath p);
    
    /** Erase all pathes and associated data (links, and map to these links.*/
    public void clearPathRegistry();
    
    /** 
     * A utility method that fills the group with any ISlotableItem that does 
     * not stand in the group, but as a source or target of the link.
     */
    public void fillMissingItems(List<ILink<ISlotableItem>> links);
    
    public ILinkFactory getLinkFactory();
    
    public void logLinks();
}
