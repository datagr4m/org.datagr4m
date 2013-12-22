package org.datagr4m.trials.drawing.demo07.pathfinder;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.datagr4m.drawing.model.pathfinder.obstacle.ObstacleGroupModel;
import org.datagr4m.drawing.model.pathfinder.obstacle.ObstacleStructure;
import org.datagr4m.drawing.renderer.pathfinder.view.ObstacleGroupRenderer;
import org.datagr4m.drawing.renderer.pathfinder.view.struct.StructRenderer;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.View;
import org.datagr4m.viewer.mouse.factory.DefaultMouseControllerFactory;
import org.datagr4m.viewer.mouse.factory.IMouseControllerFactory;
import org.datagr4m.viewer.renderer.IRenderer;


public class AbstractDemo {
    public static void show(ObstacleGroupModel group){
        show(new ObstacleGroupRenderer(group));
    }
    
    public static void show(ObstacleGroupModel group, IMouseControllerFactory factory){
        show(new ObstacleGroupRenderer(group), factory);
    }
    
    public static void show(ObstacleStructure struct){
        show(new StructRenderer(struct));
    }

    public static void show(IRenderer renderer){
        show(renderer, new DefaultMouseControllerFactory());
    }
        
    public static void show(IRenderer renderer, IMouseControllerFactory factory){
        final Display display = new Display(true, factory);
        display.setView(new View(renderer, display));
        display.openFrame();
        
        if(renderer instanceof ObstacleGroupRenderer){
            ((ObstacleGroupRenderer)renderer).setMouseController(display.getMouse());
        }
        
        try {
            ImageIO.write(display.screenshot(), "png", new File("screenshot.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
