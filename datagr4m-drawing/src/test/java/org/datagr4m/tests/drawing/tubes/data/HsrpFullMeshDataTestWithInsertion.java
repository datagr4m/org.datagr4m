package org.datagr4m.tests.drawing.tubes.data;

import java.awt.geom.Point2D;

import org.datagr4m.drawing.layout.factories.HierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.IHierarchicalEdgeLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.SlotedTubeLayout;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;
import org.datagr4m.drawing.model.pathfinder.path.IPathFactory;
import org.datagr4m.drawing.model.pathfinder.path.PathFactory;
import org.datagr4m.tests.drawing.tubes.ITubeDataTest;


//construit model hierarchique
// root
//  group
//   pair1 i11 i12
//   pair2 i21 i22
public class HsrpFullMeshDataTestWithInsertion extends HsrpFullMeshDataTest implements ITubeDataTest{
    protected static IPathFactory pathFactory = new PathFactory();

    public HsrpFullMeshDataTestWithInsertion(){
         super();
    }
    
    public void hierarchicalInsertPointOnTube(int id, Point2D point) throws Exception{
        getFirstTube().insertPointHierarchicallyAfter(id, point);
    }
    
    public Tube getFirstTube(){
        for(IEdge t: edges.getInternalTubesAndEdges()){
            if(t instanceof Tube){
                return (Tube)t;
            }
        }
        return null;
    }
    
    @Override
	public IHierarchicalLayout makeTestLayout(){
        HierarchicalLayoutFactory layoutFactory = new HierarchicalLayoutFactory(){
            @Override
            public IHierarchicalEdgeLayout getHierarchicalEdgeLayout(IHierarchicalModel model){
                return new SlotedTubeLayout(pathFactory){
                    private static final long serialVersionUID = -7493879196180853695L;

					@Override
					protected void doPostProcessTubeAndEdgeLayout(IHierarchicalEdgeModel model){
                        try {
                            hierarchicalInsertPointOnTube(0, new Point2D.Double(0,0));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
            }
        };
        IHierarchicalLayout layout = layoutFactory.getLayout(items, edges);
        layout.initAlgo();
        
        items.changePosition(0,0);
        graph.changePosition(0,0);
        p1.changePosition(-100, 50);
        p2.changePosition(+100, 50);
        
        return layout;
        
        
        //testData.hierarchicalInsertPointOnTube(new Point2D.Double(0,0));
    }
}
