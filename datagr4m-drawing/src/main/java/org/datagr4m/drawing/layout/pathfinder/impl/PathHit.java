package org.datagr4m.drawing.layout.pathfinder.impl;

import org.datagr4m.drawing.model.pathfinder.obstacle.IPathObstacle;

public class PathHit {
    protected IPathObstacle obstacle;
    protected HitType type;
    
    public PathHit(IPathObstacle obstacle, HitType type){
        this.obstacle = obstacle;
        this.type = type;
    }
    
    public IPathObstacle getObstacle() {
        return obstacle;
    }

    public HitType getType() {
        return type;
    }
    
    public boolean isBothInside(){
        return HitType.BOTH_INSIDE.equals(type);
    }

    public boolean isBothOutside(){
        return HitType.BOTH_OUTSIDE.equals(type);
    }

    public boolean isP1Inside(){
        return HitType.P1_INSIDE.equals(type);
    }

    public boolean isP2Inside(){
        return HitType.P2_INSIDE.equals(type);
    }

    /*****************/
    
    public static enum HitType{
        BOTH_OUTSIDE,
        BOTH_INSIDE,
        P1_INSIDE,
        P2_INSIDE,
        UNDEFINED
    }
}
