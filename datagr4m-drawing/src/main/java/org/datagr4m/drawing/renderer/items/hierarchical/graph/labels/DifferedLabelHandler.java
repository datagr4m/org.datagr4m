package org.datagr4m.drawing.renderer.items.hierarchical.graph.labels;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.renderer.items.shaped.UnscaledTextDifferedRenderer;
import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.renderer.DifferedRenderer;
import org.jzy3d.maths.TicToc;

import com.google.common.collect.ArrayListMultimap;

public class DifferedLabelHandler {
    protected DifferedLabelConfiguration configuration;
    protected static int LONGEST_HISTORY = 5;
    
    public DifferedLabelHandler(List<DifferedRenderer> differed, IView view) {
        this.configuration = new DifferedLabelConfiguration();
        this.differed = differed;
        this.view = view;
        this.bounds = new HashMap<UnscaledTextDifferedRenderer,Rectangle2D>();
        this.history = ArrayListMultimap.create();
        //this.priority = new HashMap<UnscaledTextDifferedRenderer,Integer>();
    }

    public List<DifferedRenderer> getDiffered() {
        return differed;
    }
    
    protected void computePriorityRanking(){
        this.priorityRanking= ArrayListMultimap.create();
        
        for(DifferedRenderer r: differed)
            if(r instanceof UnscaledTextDifferedRenderer){
                UnscaledTextDifferedRenderer utr = (UnscaledTextDifferedRenderer)r;
                priorityRanking.put(utr.getPriority(), utr);
            }
    }
    
    protected List<UnscaledTextDifferedRenderer> getPriorityEqualOrAbove(int p){
        List<UnscaledTextDifferedRenderer> output = new ArrayList<UnscaledTextDifferedRenderer>();
        
        for(int i=p; i>=0; i--){
            List<UnscaledTextDifferedRenderer> list = priorityRanking.get(i);
            if(list!=null)
                output.addAll(list);
        }
        return output;
    }

    /**
     * Calcule les collisions de label, et d�fini la liste de ceux qui doivent
     * �tre r�duit. 
     * 
     * Dans une seconde �tape, v�rifie si un ou des labels peuvent �tre aggrandis
     * sans intersecter d'autres labels.
     * 
     * Stocke un historique pour �viter les oscillations de taille de label.
     * 
     * Gere eventuellement la priorit� hierarchique si PRIORITY_SUPPORT=true
     */
    public void process(boolean hierarchical){
        TicToc t = new TicToc();
        //t.tic();
        
        
        
        computeLabelBounds(); // compute bounds of each item
        //t.toc(); Logger.getLogger(DifferedLabelHandler.class).info("bounds:"+t.elapsedSecond());
        
        List<UnscaledTextDifferedRenderer> reduce = new ArrayList<UnscaledTextDifferedRenderer>(bounds.size());
        boolean didReduce = true;
        
        int k = 0;
        t.tic();
        while(didReduce){
            reduce.clear();
            computeLabelOverlap(reduce, hierarchical); // d�fini les labels � r�duire            
            logNoResize(reduce); // historique � 0 pour les non reduce
            didReduce = applySizeReductionAndLog(reduce); // reduit et update les bounds
            k++;
        }
        //t.toc(); Logger.getLogger(DifferedLabelHandler.class).info("reduce:"+t.elapsedSecond() + "  iterations:"+k);
        
        // aumgente tout ceux qu'on peut si <police 12
        // augmente de 1 seulement: on augmentera peut �tre encore
        // au prochain appel
        //t.tic();
        enlargeIfNotMoreThan(reduce, configuration.getMaxSize());
        //t.toc(); Logger.getLogger(DifferedLabelHandler.class).info("enlarge:"+t.elapsedSecond());
    }

    protected void computeLabelBounds() {
        for(DifferedRenderer r: differed){
            if(r instanceof UnscaledTextDifferedRenderer){
                UnscaledTextDifferedRenderer textRenderer = (UnscaledTextDifferedRenderer)r;
                Rectangle2D bs = textRenderer.computeBounds(view);
                bounds.put(textRenderer, bs);
            }
        }
    }

    protected void enlargeIfNotMoreThan(List<UnscaledTextDifferedRenderer> reduced, int maxSize) {
        List<UnscaledTextDifferedRenderer> done = new ArrayList<UnscaledTextDifferedRenderer>();
        while(done.size()<bounds.keySet().size()){
            for(UnscaledTextDifferedRenderer t: bounds.keySet()){
                if(reduced.contains(t))
                    done.add(t);
                else{
                    int newSize = t.getSize()+configuration.getBaseStep();
                    if(newSize<maxSize && newSize<t.getInitialSize() && (!oscillation(t, +configuration.getBaseStep()))){
                        t.setSize(newSize);
                        bounds.put(t, t.computeBounds(view));
                        if(hitAnybody(t, bounds.keySet())){
                            t.setSize(t.getSize()-configuration.getBaseStep());
                            bounds.put(t, t.computeBounds(view)); 
                            //log(t, 0);
                        }
                        else
                            log(t, +configuration.getBaseStep());
                        done.add(t);
                    }
                    else
                        done.add(t); 
                }
            }
        }
    }

    protected boolean applySizeReductionAndLog(List<UnscaledTextDifferedRenderer> reduce) {
        boolean didReduce = false;
        for(UnscaledTextDifferedRenderer t: reduce){
            if(t.getSize()>=configuration.getMinSize()+configuration.getBaseStep()){
                if(!oscillation(t, -configuration.getBaseStep())){
                    t.setSize(t.getSize()-configuration.getBaseStep());
                    log(t, -configuration.getBaseStep());
                    
                    Rectangle2D bs = t.computeBounds(view);
                    
                    //view.getViewTransformer().inverseTransform(bs);
                    bounds.put(t, bs);
                    didReduce = true;
                }
            }
        }
        return didReduce;
    }

    protected void logNoResize(List<UnscaledTextDifferedRenderer> reduce) {
        for(UnscaledTextDifferedRenderer t1: bounds.keySet()){
            if(!reduce.contains(t1))
                log(t1, 0);
        }
    }

    protected void computeLabelOverlap(List<UnscaledTextDifferedRenderer> reduce, boolean hierarchical) {
        for(UnscaledTextDifferedRenderer t1: bounds.keySet()){
            if(reduce.contains(t1))
                continue;
            
            Collection<UnscaledTextDifferedRenderer> alter;
            if(hierarchical){
                if(priorityRanking==null)
                    computePriorityRanking();
                alter = getPriorityEqualOrAbove(t1.getPriority());
            }
            else
                alter = bounds.keySet();
            //Logger.getLogger(DifferedLabelHandler.class).info("npriority:" +alter.size());
            for(UnscaledTextDifferedRenderer t2: alter){
                if(reduce.contains(t2))
                    continue;
                if(!t1.equals(t2)){
                    boolean overlap = bounds.get(t1).intersects(bounds.get(t2));
                    if(overlap){
                        if(!hierarchical){
                            if(t1.getSize()>configuration.getMinSize())
                                reduce.add(t1);
                            if(t2.getSize()>configuration.getMinSize())
                                reduce.add(t2);
                        }
                        // supporte la priorit� des items
                        else{
                            // priorit� similaire
                            if(t1.getPriority()==t2.getPriority()){
                                if(t1.getSize()>configuration.getMinSize())
                                    reduce.add(t1);
                                if(t2.getSize()>configuration.getMinSize())
                                    reduce.add(t2);
                            }
                            // t1 est prioritaire, on r�duit t2 d'abord
                            else if(t1.getPriority()<t2.getPriority()){
                                if(t2.getSize()>configuration.getMinSize())
                                    reduce.add(t2);
                            }
                            // t1 est prioritaire, on r�duit t2 d'abord
                            else if(t2.getPriority()<t1.getPriority()){
                                if(t1.getSize()>configuration.getMinSize())
                                    reduce.add(t1);
                            } 
                        }
                    }
                }
                
            }
        }
    }
    
    protected boolean hitAnybody(UnscaledTextDifferedRenderer t1, Collection<UnscaledTextDifferedRenderer> all){
        for(UnscaledTextDifferedRenderer t2: all){
            if(t1!=t2){
                if(bounds.get(t1).intersects(bounds.get(t2))){
                    return true;
                }
            }
        }
        return false;
    }
    
    /******************/
    
    public void resize(String who, int size){
        for(DifferedRenderer r: differed){
            if(r instanceof UnscaledTextDifferedRenderer){
                UnscaledTextDifferedRenderer ur = (UnscaledTextDifferedRenderer)r;
                if(ur.getLabel().equals(who)){
                    ur.setSize(size);
                    return;
                }
            }
        }
    }
    
    protected void log(UnscaledTextDifferedRenderer ur, int step){
        history.put(ur, +configuration.getBaseStep());
        if(history.get(ur).size()>configuration.getMaxHistory())// remove first element
            history.get(ur).remove(history.get(ur).get(0));
    }
    
    /******************/
    
    public boolean oscillation(UnscaledTextDifferedRenderer ur){
        /*List<Integer> his = new ArrayList<Integer>(history.get(ur));
        if(his.size()>=MAX_HISTORY){
            int total = 0;
            for (int i = 0; i < his.size(); i++) {
                total+=his.get(0);
            }
            
            if(Math.abs(total)<MAX_HISTORY/2){
                return true;
            }
            else
                return false;
        }
        return false;*/
        List<Integer> his = new ArrayList<Integer>(history.get(ur));
        if(his.size()>=configuration.getMaxHistory()){
            return bothDirection(his);
        }
        return false;
    }
    
    public boolean oscillation(UnscaledTextDifferedRenderer ur, int next){
        List<Integer> his = new ArrayList<Integer>(history.get(ur));
        if(his.size()>=configuration.getMaxHistory()){
            return bothDirection(his);
        }
        return false;
    }
    
    public boolean bothDirection(List<Integer> list){
        boolean didUp = false;
        boolean didDown = false;
        
        for(int i = 1; i < list.size(); i++){
            if(i>0)
                didUp = true;
            if(i<0)
                didDown = true;
            if(didUp && didDown)
                return true;
        }
        return false;
    }
    
    protected boolean isUp(Integer i ){
        return i>0;
    }
    
    public boolean oscillationOld(UnscaledTextDifferedRenderer ur){
        List<Integer> his = new ArrayList<Integer>(history.get(ur));
        if(his.size()>=configuration.getMaxHistory()){
            boolean wasUp = isUp(his.get(0));
            for (int i = 1; i < configuration.getMaxHistory(); i++) {
                if(isUp(his.get(0))==wasUp)
                    return false;
                else
                    wasUp=!wasUp;
            }
            return true;
        }
        return false;
    }
    
    public boolean oscillationOld2(UnscaledTextDifferedRenderer ur, int next){
        List<Integer> his = new ArrayList<Integer>(history.get(ur));
        if(his.size()>=configuration.getMaxHistory()){
            boolean wasUp = isUp(his.get(0));
            for (int i = 1; i < configuration.getMaxHistory(); i++) {
                if(isUp(his.get(0))==wasUp)
                    return false;
                else
                    wasUp=!wasUp;
            }
            if(isUp(next)==wasUp)
                return false;
            else
                return true;
        }
        return false;
    }
    
    /********************/
    
    public void applyPrevious(DifferedLabelHandler old){
        applyPrevious(old, true);
    }
    
    public void applyPrevious(DifferedLabelHandler old, boolean withHistory){
        for(DifferedRenderer r: old.getDiffered()){
            if(r instanceof UnscaledTextDifferedRenderer){
                UnscaledTextDifferedRenderer ur = (UnscaledTextDifferedRenderer)r;
                resize(ur.getLabel(), ur.getSize());
                if(withHistory){
                    history.putAll(ur, removeExceeding(old.history.get(ur), LONGEST_HISTORY));
                }
            }
        }
    }
    
    private List<Integer> removeExceeding(List<Integer> list, int max) {
        if(max<0)
            return list;
        else if(list.size()<max)
            return list;
        else{
            List<Integer> ints = new ArrayList<Integer>();
            for (int i = list.size()-max; i < list.size(); i++) {
                ints.add(list.get(i));
            }
            return ints;
        }
    }

    public void clearHistory(){
        history.clear();
    }
    
    public ArrayListMultimap<UnscaledTextDifferedRenderer,Integer> getHistory(){
        return history;
    }
    
    /*public void clearPriority(){
        priority.clear();
    }
    
    public Map<UnscaledTextDifferedRenderer,Integer> getPriority(){
        return priority;
    }*/
    
    
    public void debugHistory() {
        for(DifferedRenderer r: getDiffered()){
            if(r instanceof UnscaledTextDifferedRenderer){
                UnscaledTextDifferedRenderer utr = (UnscaledTextDifferedRenderer)r;
                
                //if(utr.getLabel().equals("lan_8")){
                    System.out.print(utr.getLabel()+": (" +  getHistory().get(utr).size()+")");
                    List<Integer> li = getHistory().get(utr);
                    for(Integer i: li)
                        System.out.print(i+" ");
                    Logger.getLogger(DifferedLabelHandler.class).info("oscillation:" + oscillation(utr));
                //}
            }
        }
    }
    
    
    /****************/
    
    protected List<DifferedRenderer> differed;
    protected IView view;
    protected Map<UnscaledTextDifferedRenderer,Rectangle2D> bounds;
    protected ArrayListMultimap<UnscaledTextDifferedRenderer,Integer> history;
    
    protected ArrayListMultimap<Integer,UnscaledTextDifferedRenderer> priorityRanking;
}
