package org.datagr4m.drawing.layout.geometrical.flower;

import java.util.ArrayList;
import java.util.List;

import org.jzy3d.maths.Coord2d;

/**
 * R1 est le radius du coeur de la fleur. R2 est le radius de chaque p�tale de
 * la fleur, sachant un certain nombre de p�tales, et un angle pour ces p�tales.
 */
public class FlowerGeometry {
    /** Angle total occup� par les p�tales. */
    double alpha;
    /** Angle au sommet du triangle isocele (centre du coeur) */
    double alpha2;
    /** Angle � la base sommet du triangle isocele (centre du p�tale) */
    double alpha3;
    /** Nombre de p�tales. */
    int nChildren;
    /** Radius du body. */
    double r1;
    /** Radius du p�tale. */
    double r2;

    public FlowerGeometry(double alpha, double alpha2, double alpha3, int nChildren, double r1, double r2) {
        this.alpha = alpha;
        this.alpha2 = alpha2;
        this.alpha3 = alpha3;
        this.nChildren = nChildren;
        this.r1 = r1;
        this.r2 = r2;
    }

    public double getTotalAngle() {
        return alpha;
    }

    public double getPetalSliceAngle() {
        return alpha2;
    }

    public double getAlpha3() {
        return alpha3;
    }

    public int getNPetal() {
        return nChildren;
    }

    public double getBodyRadius() {
        return r1;
    }

    public double getPetalRadius() {
        return r2;
    }
    
    /** Returns all the center points of each petal. */
    public List<Coord2d> getIteration(Coord2d center){
        double angle = 0;
        double step = alpha2;
        double dist = r1+r2;
        
        List<Coord2d> coords = new ArrayList<Coord2d>();
        for (int i = 0; i < nChildren; i++) {
            coords.add(new Coord2d(angle, dist).cartesian().add(center));
            angle+=step;
        }
        return coords;
    }

    /****************/

    public static FlowerGeometry fromR1(double r1, int nChildren) {
        return fromR1(r1, nChildren, Math.PI * 2);
    }

    public static FlowerGeometry fromR1(double r1, int nChildren, double alpha) {
        double alpha2 = alpha / nChildren; // angle au sommet du triangle
                                           // isocele
        double alpha3 = (Math.PI - alpha2) / 2; // angle � la base du triangle
                                                // isocele

        // r2, radius du cercle enfant est la base du triangle isocele
        // soit r2 = cos(alpha3)*(r1+r2)
        // soit r2 = (cos(alpha3)*r1) / (1-cos(alpha3));
        double cosA3 = Math.cos(alpha3);
        double r2 = (cosA3 * r1) / (1 - cosA3);
        return new FlowerGeometry(alpha, alpha2, alpha3, nChildren, r1, r2);
    }

    public static FlowerGeometry fromR2(double r2, int nChildren) {
        return fromR2(r2, nChildren, Math.PI * 2);
    }

    public static FlowerGeometry fromR2(double r2, int nChildren, double alpha) {
        double alpha2 = alpha / nChildren; // angle au sommet du triangle isocele
        double alpha3 = (Math.PI - alpha2) / 2; // angle � la base du triangle isocele
        double cosA3 = Math.cos(alpha3);
        
        double r1;
        if(nChildren==1)
        	r1 = r2/2;
        else
        	r1 = (r2 - (cosA3 * r2)) / cosA3;
        
        return new FlowerGeometry(alpha, alpha2, alpha3, nChildren, r1, r2);
    }

    public static FlowerGeometry fromR1AndR2(double r1, double r2, int nChildren) {
        double cosAlpha3 = r2/(r1+r2);
        double alpha3 = Math.acos(cosAlpha3);
        double alpha2 = Math.PI - alpha3*2;
        double alpha1 = alpha2*nChildren;
        
        /*double step = Math.PI*2/((float)TRY);
        double angle = 0;
        double score = 0;
        double bestAngle = 0;
        double bestScore = Double.MAX_VALUE;
        
        // TODO WARNING CA PUE CE TRUC!!!
        for (int i = 0; i < TRY; i++) {
            score = Math.abs(evalRadiusValue(angle, nChildren)-cosAlpha3);
            if(score<bestScore){
                bestScore = score;
                bestAngle = angle;
            } 
            angle+=step;
        }
        
        double alpha2 = bestAngle / nChildren; 
        double alpha3 = (Math.PI - alpha2) / 2; */
        return new FlowerGeometry(alpha1, alpha2, alpha3, nChildren, r1, r2);
    }
    protected static int TRY = 100;

    protected static double evalRadiusValue(double alpha, int nChildren) {
        double alpha2 = alpha / nChildren; 
        double alpha3 = (Math.PI - alpha2) / 2; 
        double cosA3 = Math.cos(alpha3);
        return cosA3;
    }
}
