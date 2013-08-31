package org.datagr4m.drawing.layout.runner;

public class LayoutLevelSettings {
    public static double DEFAULT_REPULSION = 200d;//70000d;
    public static double DEFAULT_ATTRACTION = 10;//10;
    public static int DEFAULT_LOOPS = 10;
    
    public LayoutLevelSettings(){
        this(-1, DEFAULT_REPULSION, DEFAULT_ATTRACTION, DEFAULT_LOOPS);
    }
    
    public LayoutLevelSettings(int level, double repulsion, double attraction, int requiredLoops) {
        this.level = level;
        this.repulsion = repulsion;
        this.attraction = attraction;
        this.requiredLoops = requiredLoops;
    }
    public double getRepulsion() {
        return repulsion;
    }
    public void setRepulsion(double repulsion) {
        this.repulsion = repulsion;
    }
    public double getAttraction() {
        return attraction;
    }
    public void setAttraction(double attraction) {
        this.attraction = attraction;
    }
    /** Number of iteration for this level before running another different level in the hierarchical layout.*/
    public int getRequiredLoops() {
        return requiredLoops;
    }
    public void setRequiredLoops(int loops) {
        this.requiredLoops = loops;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }

    public double repulsion = 0.5d;
    public double attraction = 10;
    public int requiredLoops = 1;
    public int level = 0;
    
}
