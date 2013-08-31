package org.datagr4m.drawing.layout.geometrical.flower;

public class FlowerSolver {
    public static FlowerGeometry getGeometry(int n, float minR1, float minR2, float minM1){
        // cherche R1 � partir de R2 et A=2PI
        FlowerGeometry gfm = FlowerGeometry.fromR2(minR2, n);
        if(gfm.getBodyRadius()>(minR1+minM1)){
            return gfm;
        }
        else{
            // cherche A � partir de R1 et R2
            gfm = FlowerGeometry.fromR1AndR2(minR1+minM1, minR2, n);
            if(gfm.getTotalAngle()<=Math.PI*2)
                return gfm;
            else{
                throw new RuntimeException();
            }
        }
    }
    
}
