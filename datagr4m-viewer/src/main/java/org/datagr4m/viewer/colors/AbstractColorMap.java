package org.datagr4m.viewer.colors;

import java.awt.Color;

public abstract class AbstractColorMap {
    public void setDirection(boolean isStandard) {
    	direction = isStandard;
    }

    public boolean getDirection() {
    	return direction;
    }
    
    public Color negative(Color c){
        return new Color(255-c.getRed(), 255-c.getGreen(), 255-c.getBlue());
    }

    public Color getColor(float z, float zMin, float zMax) {
        float rel_value = 0;
        
        if( z < zMin )
            rel_value = 0;
        else if( z > zMax )
            rel_value = 1;
        else{
        	if(direction)
        		rel_value = ( z - zMin ) / ( zMax - zMin );
        	else
        		rel_value = ( zMax - z ) / ( zMax - zMin );
        }
        
        float b = colorComponentRelative( rel_value, 0.25f, 0.25f, 0.75f );
        float v = colorComponentRelative( rel_value, 0.50f, 0.25f, 0.75f );
        float r = colorComponentRelative( rel_value, 0.75f, 0.25f, 0.75f );
        
        return new Color( r, v, b );
    }
   
    protected abstract float colorComponentRelative(float value, float center, float topwidth, float bottomwidth);
        
    protected boolean direction;

}