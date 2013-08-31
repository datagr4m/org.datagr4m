package org.datagr4m.maths.geometry.functions;


public class UpDownInterpolation {
    public static float[] build(float from, float to, int steps){
        
        if(steps%2!=0)
            steps++;
        int half = steps/2;

        float step = 0.9f;
        
        float[] output = new float[steps];
        output[0] = from;
        
        for (int i = 1; i < steps; i++) {
            if(i<half)
                output[i] = output[i-1]-step;
            else if(i<=steps)
                output[i] = output[i-1]+step;
            
            if(output[i]==0)
                output[i] = 0.00000001f;
        }
        return output;
    }
    
}
