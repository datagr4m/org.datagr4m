package org.datagr4m.drawing.layout.slots;

public class SlotLayoutConfiguration {
    public boolean isCrashOnMissingTargetGroup() {
        return crashOnMissingTargetGroup;
    }
    public void setCrashOnMissingTargetGroup(boolean crashOnMissingTargetGroup) {
        this.crashOnMissingTargetGroup = crashOnMissingTargetGroup;
    }
    public boolean isCrashOnMissingPath() {
        return crashOnMissingPath;
    }
    public void setCrashOnMissingPath(boolean crashOnMissingPath) {
        this.crashOnMissingPath = crashOnMissingPath;
    }
    public boolean isCrashOnDupplicatePath() {
        return crashOnDupplicatePath;
    }
    public void setCrashOnDupplicatePath(boolean crashOnDupplicatePath) {
        this.crashOnDupplicatePath = crashOnDupplicatePath;
    }
    public boolean isConsiderInterfaceForPath() {
        return considerInterfaceForPath;
    }
    public void setConsiderInterfaceForPath(boolean considerInterfaceForPath) {
        this.considerInterfaceForPath = considerInterfaceForPath;
    }
    
    
    public boolean isAddUnusedInterface() {
        return addUnusedInterface;
    }
    public void setAddUnusedInterface(boolean addUnusedInterface) {
        this.addUnusedInterface = addUnusedInterface;
    }


    protected boolean crashOnMissingTargetGroup = true;// if false, ignore this path
    protected boolean crashOnMissingPath = true;
    protected boolean crashOnDupplicatePath = false;
    protected boolean considerInterfaceForPath = true; 
    protected boolean addUnusedInterface = false;

}
