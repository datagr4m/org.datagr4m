package org.datagr4m.drawing.renderer.items.hierarchical.graph.labels;

public class DifferedLabelConfiguration {
    
    
    public int getMinSize() {
        return minSize;
    }
    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }
    public int getMaxSize() {
        return maxSize;
    }
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
    public int getBaseStep() {
        return baseStep;
    }
    public void setBaseStep(int baseStep) {
        this.baseStep = baseStep;
    }
    public int getMaxHistory() {
        return maxHistory;
    }
    public void setMaxHistory(int maxHistory) {
        this.maxHistory = maxHistory;
    }
    protected int minSize = -1;
    protected int maxSize = 12;
    protected int baseStep = 1;
    protected int maxHistory = 3;

}
