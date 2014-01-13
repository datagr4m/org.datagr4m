package org.datagr4m.drawing.tubes.data;

import java.awt.geom.Point2D;

import org.datagr4m.drawing.layout.factories.HierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.IHierarchicalEdgeLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.SlotedTubeLayout;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;
import org.datagr4m.drawing.model.pathfinder.path.IPathFactory;
import org.datagr4m.drawing.model.pathfinder.path.PathFactory;
import org.datagr4m.drawing.tubes.ITubeDataTest;


/** 
 * Contient des tubes/edge flipped
 * la methode makelayout effectue une insertion de point
 */
public class HierarchicalFlipDataTest4WithInsertion extends HierarchicalFlipDataTest4 implements ITubeDataTest {
    protected static IPathFactory pathFactory = new PathFactory();


    public HierarchicalFlipDataTest4WithInsertion() {
        super();
    }

    @Override
    public IHierarchicalNodeLayout makeTestLayout() {
        HierarchicalLayoutFactory layoutFactory = new HierarchicalLayoutFactory(){
            @Override
            public IHierarchicalEdgeLayout getHierarchicalEdgeLayout(IHierarchicalNodeModel model){
                return new SlotedTubeLayout(pathFactory){
                    @Override
					protected void doPostProcessTubeAndEdgeLayout(IHierarchicalEdgeModel model){
                        try {
                            hierarchicalInsertPointOnTube(0, new Point2D.Double(0,0));
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                };
            }
        };
        IHierarchicalNodeLayout layout = layoutFactory.getLayout(items, edges);
        layout.initAlgo();

        items.changePosition(0, 0);

        groupGroup1.changePosition(-800, +200);
        
        groupI.changePosition(-200, -150);
        pI1.changePosition(-100, 0);
        pI2.changePosition(+100, 0);

        groupJ.changePosition(+200, +150);
        pJ1.changePosition(-100, 0);
        pJ2.changePosition(+100, 0);
        
        groupGroup2.changePosition(+800, +200);
        pK1.changePosition(0, 0);


        layout.goAlgoEdge();

        return layout;
    }
    
    public void hierarchicalInsertPointOnTube(int id, Point2D point) throws Exception{
        getFirstTube().insertPointHierarchicallyAfter(id, point);
    }
    
    public Tube getFirstTube(){
        for(IEdge t: edges.getRootTubes()){
            if(t instanceof Tube){
                return (Tube)t;
            }
        }
        return null;
    }
}
