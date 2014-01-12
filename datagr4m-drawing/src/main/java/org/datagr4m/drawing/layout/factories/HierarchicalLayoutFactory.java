package org.datagr4m.drawing.layout.factories;

import java.util.Map;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.IHierarchicalEdgeLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.SlotedTubeLayout;
import org.datagr4m.drawing.layout.hierarchical.matrix.HierarchicalColumnLayout;
import org.datagr4m.drawing.layout.hierarchical.matrix.HierarchicalMatrixLayout;
import org.datagr4m.drawing.layout.hierarchical.matrix.HierarchicalRowLayout;
import org.datagr4m.drawing.layout.hierarchical.pair.HierarchicalPairLayout;
import org.datagr4m.drawing.layout.hierarchical.stratum.HierarchicalStratumLayout;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.IHierarchicalGraphModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.hierarchical.pair.IHierarchicalPairModel;
import org.datagr4m.drawing.model.pathfinder.path.IPathFactory;
import org.datagr4m.drawing.model.pathfinder.path.PathFactory;


public class HierarchicalLayoutFactory implements IHierarchicalLayoutFactory{
    protected IPathFactory pathFactory = new PathFactory();
    protected Map<String,String> modelNameToLayoutName;
    
    public static String LAYOUT_STRATUMS_NAME = "stratums";
    public static String LAYOUT_MATRIX_NAME = "matrix";
    public static String LAYOUT_COLUMN_NAME = "column";
    public static String LAYOUT_ROW_NAME = "row";
    
    public HierarchicalLayoutFactory(){}

    @Override
    public IHierarchicalNodeLayout getLayout(IHierarchicalNodeModel model){
        IHierarchicalNodeLayout layout = getRootLayout(model, model.getEdgeModel());
        return layout;
    }

    @Override
    public IHierarchicalNodeLayout getLayout(IHierarchicalNodeModel model, final IHierarchicalEdgeModel tubeModel){
        IHierarchicalNodeLayout layout = getRootLayout(model, tubeModel);
        return layout;
    }

    protected IHierarchicalNodeLayout getRootLayout(IHierarchicalNodeModel model, final IHierarchicalEdgeModel tubeModel) {
        IHierarchicalEdgeLayout tubeLayout = getHierarchicalEdgeLayout(model);
        IHierarchicalNodeLayout layout = getHierarchicalNodeLayout(model);
        layout.setTubeModel(tubeModel);
        layout.setTubeLayout(tubeLayout);
        return layout;
    }
    
    @Override 
    public IHierarchicalNodeLayout getHierarchicalNodeLayout(IHierarchicalNodeModel model){
        IHierarchicalNodeLayout layout = null;
        if(hasLayoutForModel(model.getLabel()))
            layout = getNodeLayoutByModelName(model);
        else
            layout = getNodeLayoutByModelType(model);
        attachChildren(layout, model);
        return layout;
    }
    
    /** recursively handle children models.*/
    protected void attachChildren(IHierarchicalNodeLayout layout, IHierarchicalNodeModel model){
        for(IBoundedItem child: model.getChildren()){
            if(child instanceof IHierarchicalNodeModel){
                IHierarchicalNodeModel submodel = (IHierarchicalNodeModel)child;
                IHierarchicalNodeLayout sublayout = getHierarchicalNodeLayout(submodel);
                if(sublayout!=null)
                    layout.addChild(sublayout);
            }
        }
    }
    
    @Override
    public IHierarchicalEdgeLayout getHierarchicalEdgeLayout(IHierarchicalNodeModel model){
        return new SlotedTubeLayout(pathFactory);
    }
        
    /***********************/
    
    @Override
    public IHierarchicalNodeLayout getNodeLayoutByModelType(IHierarchicalNodeModel model){
        if(model instanceof IHierarchicalPairModel){
            HierarchicalPairLayout layout = new HierarchicalPairLayout((IHierarchicalPairModel)model);
            return layout;
        }
        else if(model instanceof IHierarchicalGraphModel){
            HierarchicalGraphLayout layout = new HierarchicalGraphLayout((IHierarchicalGraphModel)model);
            return layout;
        }
        else 
            throw new RuntimeException("unknown layout for " + model);
    }
    
    @Override
    public IHierarchicalNodeLayout getNodeLayoutByModelName(IHierarchicalNodeModel model){
        if(modelNameToLayoutName!=null){
            String name = modelNameToLayoutName.get(model.getLabel());
            if(name!=null){
                IHierarchicalNodeLayout layout = getNodeLayoutByName(name);
                layout.setModel(model);
                
                String initInfo = info(model, layout);
                Logger.getLogger(HierarchicalLayoutFactory.class).info(initInfo);
                return layout;
            }
            else
                throw new RuntimeException("layout factory does not have a defined layout for model " + model.getLabel());
        }
        else
            throw new RuntimeException("layout factory does not have a model<->layout mapping by name");
    }
    
    protected String info(IHierarchicalNodeModel model, IHierarchicalNodeLayout layout){
    	return model.getClass().getSimpleName() + model.getLabel() + layout.getClass().getSimpleName();
    	
    }
    
    @Override
    public IHierarchicalNodeLayout getNodeLayoutByName(String name) throws IllegalArgumentException{
        if(LAYOUT_MATRIX_NAME.equals(name))
        	return new HierarchicalMatrixLayout();
        else if(LAYOUT_COLUMN_NAME.equals(name))
            return new HierarchicalColumnLayout();
        else if(LAYOUT_ROW_NAME.equals(name))
            return new HierarchicalRowLayout();
        else if(LAYOUT_STRATUMS_NAME.equals(name))
            return new HierarchicalStratumLayout();
        else 
            throw new IllegalArgumentException("unknown layout name: " + name);
    }
    
    protected boolean hasLayoutForModel(String name){
        if(modelNameToLayoutName!=null){
            if(modelNameToLayoutName.get(name)!=null){
                return true;
            }
            else
                return false;
        }
        return false;
    }
    
    @Override
    public void setModelLayoutMapping(Map<String,String> modelToLayout){
        modelNameToLayoutName = modelToLayout;
    }
    
    @Override
    public Map<String,String> getModelLayoutMapping(){
        return modelNameToLayoutName;
    }
}
