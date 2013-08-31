package org.datagr4m.maths.geometry;

public class PolygonUtils {
    /**
     * @see http://fr.wikipedia.org/wiki/Polygone_r%C3%A9gulier
     */
    public static double getRegularPolygonBorderSize(double radius, int nborder){
        double apotheme = radius * Math.cos(Math.PI / nborder);
        double demicote = Math.sqrt( radius*radius - apotheme*apotheme );
        return demicote / 2;
    }
}
