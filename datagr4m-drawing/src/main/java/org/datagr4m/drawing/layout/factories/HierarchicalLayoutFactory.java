package org.datagr4m.drawing.layout.factories;

import java.util.Map;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.IHierarchicalEdgeLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.SlotedTubeLayout;
import org.datagr4m.drawing.layout.hierarchical.matrix.HierarchicalColumnLayout;
import org.datagr4m.drawing.layout.hierarchical.matrix.HierarchicalMatrixLayout;
import org.datagr4m.drawing.layout.hierarchical.matrix.HierarchicalRowLayout;
import org.datagr4m.drawing.layout.hierarchical.pair.HierarchicalPairLayout;
import org.datagr4m.drawing.layout.hierarchical.stratum.HierarchicalStratumLayout;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
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
    public IHierarchicalLayout getLayout(IHierarchicalModel model){
        IHierarchicalLayout layout = getRootLayout(model, model.getEdgeModel());
        return layout;
    }

    @Override
    public IHierarchicalLayout getLayout(IHierarchicalModel model, final IHierarchicalEdgeModel tubeModel){
        IHierarchicalLayout layout = getRootLayout(model, tubeModel);
        return layout;
    }

    protected IHierarchicalLayout getRootLayout(IHierarchicalModel model, final IHierarchicalEdgeModel tubeModel) {
        IHierarchicalEdgeLayout tubeLayout = getHierarchicalEdgeLayout(model);
        IHierarchicalLayout layout = getHierarchicalNodeLayout(model);
        layout.setTubeModel(tubeModel);
        layout.setTubeLayout(tubeLayout);
        return layout;
    }
    
    @Override 
    public IHierarchicalLayout getHierarchicalNodeLayout(IHierarchicalModel model){
        IHierarchicalLayout layout = null;
        if(hasLayoutForModel(model.getLabel()))
            layout = getNodeLayoutByModelName(model);
        else
            layout = getNodeLayoutByModelType(model);
        attachChildren(layout, model);
        return layout;
    }
    
    /** recursively handle children models.*/
    protected void attachChildren(IHierarchicalLayout layout, IHierarchicalModel model){
        for(IBoundedItem child: model.getChildren()){
            if(child instanceof IHierarchicalModel){
                IHierarchicalModel submodel = (IHierarchicalModel)child;
                IHierarchicalLayout sublayout = getHierarchicalNodeLayout(submodel);
                if(sublayout!=null)
                    layout.addChild(sublayout);
            }
        }
    }
    
    @Override
    public IHierarchicalEdgeLayout getHierarchicalEdgeLayout(IHierarchicalModel model){
        return new SlotedTubeLayout(pathFactory);
    }
        
    /***********************/
    
    @Override
    public IHierarchicalLayout getNodeLayoutByModelType(IHierarchicalModel model){
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
    public IHierarchicalLayout getNodeLayoutByModelName(IHierarchicalModel model){
        if(modelNameToLayoutName!=null){
            String name = modelNameToLayoutName.get(model.getLabel());
            if(name!=null){
                IHierarchicalLayout layout = getNodeLayoutByName(name);
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
    
    protected String info(IHierarchicalModel model, IHierarchicalLayout layout){
    	return model.getClass().getSimpleName() + model.getLabel() + layout.getClass().getSimpleName();
    	
    }
    
    @Override
    public IHierarchicalLayout getNodeLayoutByName(String name) throws IllegalArgumentException{
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
