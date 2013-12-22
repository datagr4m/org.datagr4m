package org.datagr4m.drawing.layout.pathfinder.view.debugger;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.layout.pathfinder.view.debugger.renderers.PathFinderDebuggerRenderer;
import org.datagr4m.drawing.model.pathfinder.obstacle.IPathObstacle;
import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.viewer.IDisplay;
import org.jzy3d.maths.Utils;


public class PathFinderDebugger {
    protected PathFinderDebuggerRenderer renderer;
    protected List<String> infos;
    
    public PathFinderDebugger(IDisplay display){
        infos = new ArrayList<String>();
        renderer = new PathFinderDebuggerRenderer();
        display.createViewFor(renderer);

        addKeyListenerToControlDebugger(display);
    }

	public void addKeyListenerToControlDebugger(IDisplay display) {
		((JComponent)display).addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				Logger.getLogger(PathFinderDebugger.class).info("debugger continue flow due to key typed on display");
				continueFlow();
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});
	}
    
    public void show(IPath path, List<IPathObstacle> obstacles){
        renderer.setContent(obstacles, path);
    }
    
    public void highlight(List<Point2D> points){
        renderer.setHighlightPoints(points);
    }
    
    public void error(List<Point2D> points){
        renderer.setErrorPoints(points);
    }
    
    public void setCurrentObstacle(IPathObstacle currentObstacle) {
        renderer.setCurrentObstacle(currentObstacle);
    }
    
    public void pause(int mili){
        try {
            Thread.sleep(mili);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void addInfo(String info){
        infos.add(info);
        fireInfoChangeEvent();
    }
    
    public void addInfo(int depth, String info){
        addInfo(Utils.blanks(depth)+info);
    }
    
    public List<String> getInfos(){
        return infos;
    }
    
    public PathFinderDebuggerRenderer getRenderer(){
        return renderer;
    }
    
    /*****************/
    
    public void addBreakListener(IDebuggerListener listener){
        listeners.add(listener);
    }
    
    public void fireBreakEvent(){
        for(IDebuggerListener listener: listeners)
            listener.breakReached();
    }
    
    public void fireInfoChangeEvent(){
        for(IDebuggerListener listener: listeners)
            listener.infoChanged();
    }
    
    public void breakFlow(){
        broke = true;
        fireBreakEvent();
        while(broke);
    }
    
    public void continueFlow(){
        broke = false;
    }
    
    protected boolean broke = false;    
    protected List<IDebuggerListener> listeners = new ArrayList<IDebuggerListener>();
}
