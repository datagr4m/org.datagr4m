package org.datagr4m.drawing.layout.hierarchical.stratum;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections15.ListUtils;
import org.apache.log4j.Logger;
import org.datagr4m.drawing.layout.hierarchical.matrix.HierarchicalMatrixLayout;
import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;


/**
 * Build a stratum layout based on a sequence of stratum inputs.
 * From this sequence, the algorithm seeks all items that are
 * between sequence item i and i+1, which defines the content of a stratum
 * 
 * @author Martin Pernollet
 */
public class HierarchicalStratumLayout extends HierarchicalMatrixLayout{
    private static final long serialVersionUID = 8509524795602744235L;
    
    public HierarchicalStratumLayout() {
        sequence = null;
    }
    
    public HierarchicalStratumLayout(IHierarchicalModel model, List<IBoundedItem> sequence) {
        this.model = model;
        this.sequence = sequence;
    }
   
    public List<IBoundedItem> getSequence() {
        return sequence;
    }
    
    public void setSequence(List<IBoundedItem> sequence) {
        this.sequence = sequence;
    }
    
    /*******************/

    @Override
    public void initAlgo() {
        //super.initAlgo();
        computeStratums(); // on a besoin d'avoir les edges initialis�
        //endAlgo();
    }
    
    /*@Override
    public void goAlgo() {
        computeStratums();
        endAlgo();
        //super.goAlgo();
    }*/
    
    protected void computeStratums(){
        if(sequence==null)
            throw new RuntimeException("the item sequence required to build stratums is missing!");
        if(model==null)
            throw new RuntimeException("the model is missing!");
        
        // Compute stratums with element that stand between items of the sequence
        List<Stratum> stratums = new ArrayList<Stratum>();
        IHierarchicalEdgeModel edges = model.getRoot().getEdgeModel();
        
        //Logger.getLogger(HierarchicalStratumLayout.class).info(sequence);
        
        List<IBoundedItem> allItemsInStratum = new ArrayList<IBoundedItem>();
        for (int i = 0; i < sequence.size()-1; i++) {
            IBoundedItem in = sequence.get(i);
            IBoundedItem out = sequence.get(i+1);
            List<IBoundedItem> betweens = edges.findItemsBetween(in, out);
            
            Stratum stratum = new Stratum(in, out, betweens);
            stratums.add(stratum);
            
            allItemsInStratum.add(in);
            //allItemsInStratum.addAll(betweens);
            for(IBoundedItem item: betweens){
                if(item instanceof IHierarchicalModel){
                    allItemsInStratum.addAll(((IHierarchicalModel) item).getDescendants(true));
                }
            }
        }
        allItemsInStratum.add(last(stratums).getOutput());
        
        // Try to connect models connected to only one side of the stratum (IN)
        List<IHierarchicalModel> descendants = model.getDescendantModels();
        List<IBoundedItem> unhandledModels = ListUtils.subtract(descendants, allItemsInStratum);
        Logger.getLogger(HierarchicalStratumLayout.class).info("Unhandled: "+unhandledModels);
        
        List<IBoundedItem> stillUnhandledModels = new ArrayList<IBoundedItem>();
        for(IBoundedItem unhandled: unhandledModels) {
            for (int i = 0; i < sequence.size()-1; i++) {
                IBoundedItem in = sequence.get(i);
                IBoundedItem out = sequence.get(i+1);
                
                boolean touchIn = edges.hasLink(unhandled, in);
                boolean touchOut = edges.hasLink(unhandled, out);
                if(touchIn && !touchOut){
                    Logger.getLogger(HierarchicalStratumLayout.class).info("SATTELITE!! " + unhandled);
                    stratums.get(i).addSatellite(unhandled);
                }
                else
                    stillUnhandledModels.add(unhandled);
            }
        }
        
        // Debug still unhandled models
        for (IBoundedItem un: stillUnhandledModels) {
            System.err.println("HierarchicalStratumLayout: Unhandled model: " + un);
        }
        
        setSize(stratums.size()*2+1, Math.max(maxStratumWidth(stratums), 1));
        
        
        int i = 0;
        for(Stratum stratum: stratums){
            Logger.getLogger(HierarchicalStratumLayout.class).info(stratum);
            setItemCell(stratum.getInput(), i, 0);
            int s= 1;
            for(IBoundedItem sat: stratum.getSatellites()){
                setItemCell(sat, i, s);
                s++; //next cell
            }
            i++; // new row
            
            int j = 0;
            for(IBoundedItem intermediate: stratum.getIntermediates()){
                setItemCell(intermediate, i, j); 
                j++; //next cell
            }
            i++; // new row
        }
        
        setItemCell(last(stratums).getOutput(), i, 0);
        
        /*if(mapIndex.size()==0){
            autoColumnGrid();
        }*/
        autoRowSize();
        autoColSize();
        super.computeLayout();
    }
    
    protected Stratum last(List<Stratum> stratums){
        if(stratums.size()==0)
            return null;
        else
            return stratums.get(stratums.size()-1);
    }
    
    protected int maxStratumWidth(List<Stratum> stratums){
        int max = 0;
        for(Stratum s: stratums){
            if(s.getIntermediates().size()>max)
                max = s.getIntermediates().size();
        }
        return max;
    }
    
    @Override
	protected void autoColumnGrid(){
        int k = 0;
        for(IBoundedItem i: getModel().getChildren()){
            setItemCell(i, k, 0);
            k++;
        }
    }
    
    @Override
	protected void autoRowSize(){
        float max = 0;
        for(IBoundedItem i: getModel().getChildren()){
            RectangleBounds b = i.getRawRectangleBounds();            
            float h = b.getHeight();
            if(h>max)
                max = h;
        }
        allLineHeight = max;
    }


    /********************/

    protected List<IBoundedItem> sequence;
    protected List<Stratum> stratums;
    protected IHierarchicalEdgeModel edgeModel;
}
