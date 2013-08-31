package org.datagr4m.maths.geometry.functions;

import org.jzy3d.maths.Coord2d;

public class LinearInterpolation {
    public static Coord2d[] build(Coord2d from, Coord2d to, int steps){
        if(steps<2)
            throw new RuntimeException("not an interpolation!");
        
        Coord2d[] output = new Coord2d[steps];
        for (int i = 0; i < steps; i++) {
            float w2 = i/((float)(steps-1));
            float w1 = 1-w2;
            float x = from.x*w1+to.x*w2;
            float y = from.y*w1+to.y*w2;
            output[i] = new Coord2d(x, y);
        }
        return output;
    }
    
    public static float[] build(float from, float to, int steps){
        if(steps<2)
            throw new RuntimeException("not an interpolation!");
        
        float[] output = new float[steps];
        for (int i = 0; i < steps; i++) {
            float w2 = i/((float)(steps-1));
            float w1 = 1-w2;
            //float x = from*w1+to*w2;
            output[i] = from*w1+to*w2;
        }
        return output;
    }
}
