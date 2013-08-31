package org.datagr4m.drawing.layout.algorithms.forces;


import org.datagr4m.drawing.model.items.IBoundedItem;

public class ForceVectorUtils {

    public static void fcBiRepulsor(IBoundedItem item1, IBoundedItem item2, double c, ItemForceVector N1L, ItemForceVector N2L) {
        double xDist = item1.getPosition().x - item2.getPosition().x;
        double yDist = item1.getPosition().y - item2.getPosition().y;
        double dist = Math.sqrt(xDist * xDist + yDist * yDist); 

        if (dist > 0) {
            double f = repulsion(c, dist);
            
            N1L.dx += xDist / dist * f; // go opposite to N2
            N1L.dy += yDist / dist * f;

            N2L.dx -= xDist / dist * f; // go opposite to N1
            N2L.dy -= yDist / dist * f;
        }
    }

    public static void fcBiRepulsor_y(IBoundedItem item1, IBoundedItem item2, double c, double verticalization, ItemForceVector N1L, ItemForceVector N2L) {
        double xDist = item1.getPosition().x - item2.getPosition().x;   // distance en x entre les deux noeuds
        double yDist = item1.getPosition().y - item2.getPosition().y;
        double dist = Math.sqrt(xDist * xDist + yDist * yDist);	// distance tout court

        if (dist > 0) {
            double f = repulsion(c, dist);

            N1L.dx += xDist / dist * f;
            N1L.dy += verticalization * yDist / dist * f;

            N2L.dx -= xDist / dist * f;
            N2L.dy -= verticalization * yDist / dist * f;
        }
    }

    public static void fcBiRepulsor_noCollide(IBoundedItem item1, IBoundedItem item2, double c, ItemForceVector N1L, ItemForceVector N2L) {
        double xDist = item1.getPosition().x - item2.getPosition().x;   // distance en x entre les deux noeuds
        double yDist = item1.getPosition().y - item2.getPosition().y;
        double dist = Math.sqrt(xDist * xDist + yDist * yDist) - item1.getRadialBounds()*2 - item2.getRadialBounds()*2;//N1.getSize() - N2.getSize();	// distance (from the border of each node)

        if (dist > 0) {
            double f = repulsion(c, dist);

            N1L.dx += xDist / dist * f;
            N1L.dy += yDist / dist * f;

            N2L.dx -= xDist / dist * f;
            N2L.dy -= yDist / dist * f;
        } else if (dist != 0) {
            double f = -c;	//flat repulsion

            //TODO: apply force on item at exactly same position
            if(xDist==0)
                xDist=Math.random();
            if(yDist==0)
                yDist=Math.random();
            
            N1L.dx += xDist / dist * f;
            N1L.dy += yDist / dist * f;

            N2L.dx -= xDist / dist * f;
            N2L.dy -= yDist / dist * f;
        }
    }

    public static void fcUniRepulsor(IBoundedItem item1, IBoundedItem item2, double c, ItemForceVector N2L) {
        double xDist = item1.getPosition().x - item2.getPosition().x;   // distance en x entre les deux noeuds
        double yDist = item1.getPosition().y - item2.getPosition().y;
        double dist = Math.sqrt(xDist * xDist + yDist * yDist);	// distance tout court

        if (dist > 0) {
            double f = repulsion(c, dist);

            N2L.dx -= xDist / dist * f;
            N2L.dy -= yDist / dist * f;
        }
    }

    public static void fcBiAttractor(IBoundedItem item1, IBoundedItem item2, double c, ItemForceVector N1L, ItemForceVector N2L) {
        double xDist = item1.getPosition().x - item2.getPosition().x;   // distance en x entre les deux noeuds
        double yDist = item1.getPosition().y - item2.getPosition().y;
        double dist = (float) Math.sqrt(xDist * xDist + yDist * yDist);	// distance tout court

        if (dist > 0) {
            double f = attraction(c, dist);

            N1L.dx += xDist / dist * f;
            N1L.dy += yDist / dist * f;

            N2L.dx -= xDist / dist * f;
            N2L.dy -= yDist / dist * f;
        }
    }

    public static void fcBiAttractor_noCollide(IBoundedItem item1, IBoundedItem item2, double c, ItemForceVector N1L, ItemForceVector N2L) {
        double xDist = item1.getPosition().x - item2.getPosition().x;   // distance en x entre les deux noeuds
        double yDist = item1.getPosition().y - item2.getPosition().y;
        double dist = (float) Math.sqrt(xDist * xDist + yDist * yDist) - item1.getRadialBounds()*2 - item2.getRadialBounds()*2;	// distance (from the border of each node)

        if (dist > 0) {
            double f = attraction(c, dist);

            N1L.dx += xDist / dist * f;
            N1L.dy += yDist / dist * f;

            N2L.dx -= xDist / dist * f;
            N2L.dy -= yDist / dist * f;
        }
    }

    public static void fcBiFlatAttractor(IBoundedItem item1, IBoundedItem item2, double c, ItemForceVector N1L, ItemForceVector N2L) {
        double xDist = item1.getPosition().x - item2.getPosition().x;   // distance en x entre les deux noeuds
        double yDist = item1.getPosition().y - item2.getPosition().y;
        double dist = (float) Math.sqrt(xDist * xDist + yDist * yDist);	// distance tout court

        if (dist > 0) {
            double f = -c;

            N1L.dx += xDist / dist * f;
            N1L.dy += yDist / dist * f;

            N2L.dx -= xDist / dist * f;
            N2L.dy -= yDist / dist * f;
        }
    }

    public static void fcUniAttractor(IBoundedItem item1, IBoundedItem item2, float c, ItemForceVector N2L) {
        double xDist = item1.getPosition().x - item2.getPosition().x;   // distance en x entre les deux noeuds
        double yDist = item1.getPosition().y - item2.getPosition().y;
        double dist = (float) Math.sqrt(xDist * xDist + yDist * yDist);	// distance tout court

        if (dist > 0) {
            double f = attraction(c, dist);

            N2L.dx -= xDist / dist * f;
            N2L.dy -= yDist / dist * f;
        }
    }

    public static double attraction(double c, double dist) {
        return 0.01 * -c * dist;
    }

    public static double repulsion(double c, double dist) {
        return 0.001 * c / dist;
    }
}
