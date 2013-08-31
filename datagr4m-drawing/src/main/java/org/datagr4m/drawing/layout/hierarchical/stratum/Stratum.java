package org.datagr4m.drawing.layout.hierarchical.stratum;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;


public class Stratum {
    public Stratum(IBoundedItem input, IBoundedItem output, List<IBoundedItem> intermediates) {
        this(input, output, intermediates, new ArrayList<IBoundedItem>());
    }
    
    public Stratum(IBoundedItem input, IBoundedItem output, List<IBoundedItem> intermediates, List<IBoundedItem> satellites) {
        this.input = input;
        this.output = output;
        this.intermediates = intermediates;
        this.satellites = satellites;
    }



    public IBoundedItem getInput() {
        return input;
    }
    public IBoundedItem getOutput() {
        return output;
    }
    public List<IBoundedItem> getIntermediates() {
        return intermediates;
    }
    public List<IBoundedItem> getSatellites() {
        return satellites;
    }
    public void addSatellite(IBoundedItem item){
        satellites.add(item);
    }
    
    @Override
	public String toString(){
        return "Stratum IN: "+input.getLabel() + " OUT: " + output.getLabel() + " CONTENT: " + intermediates;
    }


    protected IBoundedItem input;
    protected IBoundedItem output;
    protected List<IBoundedItem> intermediates;
    protected List<IBoundedItem> satellites;
}
