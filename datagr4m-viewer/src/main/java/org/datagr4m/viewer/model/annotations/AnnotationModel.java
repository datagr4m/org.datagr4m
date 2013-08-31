package org.datagr4m.viewer.model.annotations;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class AnnotationModel implements Serializable{
    private static final long serialVersionUID = 3840928516365225599L;

    public AnnotationModel(){
        
    }
    public void addAnnotation(Annotation annotation, Point2D pt){
        annotations.put(annotation, pt);
    }
    
    public void clear(){
        annotations.clear();
    }
    
    public Set<Annotation> getAnnotations(){
        return annotations.keySet();
    }
    
    public Point2D getPosition(Annotation s){
        return annotations.get(s);
    }
    
    protected BiMap<Annotation, Point2D> annotations = HashBiMap.create();
}
