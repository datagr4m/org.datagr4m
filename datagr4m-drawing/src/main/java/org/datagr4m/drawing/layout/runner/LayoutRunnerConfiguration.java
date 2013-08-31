package org.datagr4m.drawing.layout.runner;

import org.datagr4m.drawing.layout.runner.sequence.ILayoutRunnerSequence;
import org.datagr4m.drawing.layout.runner.sequence.LayoutRunnerSequenceSinglePhase;

/**
 * 
 * @author Martin
 *
 */
public class LayoutRunnerConfiguration {
    public static class LayoutConfigurationCommandLine{
        public LayoutConfigurationCommandLine(double attract, double repulse) {
            this.attract = attract;
            this.repulse = repulse;
        }
        
        @Override
		public String toString(){
            return "attract:"+attract +" repulse:"+repulse;
        }
        
        public double attract = -1;
        public double repulse = -1;
    }
    
    public static LayoutConfigurationCommandLine command = null;
    
    public boolean isAllowAutoFitAtStepEnd() {
        return allowAutoFitAtStepEnd;
    }
    public void setAllowAutoFitAtStepEnd(boolean allowAutoFitAtStepEnd) {
        this.allowAutoFitAtStepEnd = allowAutoFitAtStepEnd;
    }
    public boolean isDoRunEdge() {
        return doRunEdge;
    }
    public void setDoRunEdge(boolean doRunEdge) {
        this.doRunEdge = doRunEdge;
    }
    public ILayoutRunnerSequence getSequence() {
        return sequence;
    }
    public void setSequence(ILayoutRunnerSequence sequence) {
        this.sequence = sequence;
    }
    @Override
	public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()+"\n");
        sb.append("allowAutoFitAtStepEnd:" + allowAutoFitAtStepEnd + "\n");
        
        /*for (int i = 0; i < levelSettings.length; i++) {
            sb.append("level[" + i + "]\n");
            
            LayoutLevelSettings level = levelSettings[i];
            if(level!=null){
                sb.append(" repulsion: " + level.getRepulsion() + "\n");
                sb.append(" attraction: " + level.getAttraction() + "\n");
                sb.append(" requiredLoops: " + level.getRequiredLoops() + "\n");
            }
            else
                sb.append(" null");
        }*/
        
        return sb.toString();
    }
    
    /*public void multiplyRepulsion(double i) {
        for(LayoutLevelSettings level: levelSettings){
            level.setRepulsion(level.getRepulsion()*i);
        }
    }*/

    
    //protected LayoutLevelSettings[] levelSettings;
    protected boolean allowAutoFitAtStepEnd;
    
    protected boolean doRunEdge = false;
    
    protected ILayoutRunnerSequence sequence = new LayoutRunnerSequenceSinglePhase();
        
    /* DEFAULT */
    
    public static LayoutRunnerConfiguration getDefault(){
        LayoutRunnerConfiguration settings = new LayoutRunnerConfiguration();
        //settings.setLevelSettings(getDefaultMultiLevelSettings(MAX_LEVELS));
        
        return settings;
    }
    
    /*public static LayoutLevelSettings[] getDefaultMultiLevelSettings(int maxLevels){
        LayoutLevelSettings[] levels = new LayoutLevelSettings[maxLevels];
        levels[0] = new LayoutLevelSettings(0, LEVEL0_REPULSION, LEVEL0_ATTRACTION, LEVEL0_LOOPS);
        for (int i = 1; i < maxLevels; i++) {
            levels[i] = new LayoutLevelSettings(i, LEVEL1_REPULSION, LEVEL1_ATTRACTION, LEVEL1_LOOPS);            
        }
        return levels;
    }*/
    
    /* DEFAULT VALUES */
    
    

    public static boolean ALLOW_AUTO_FIT = false;
}
