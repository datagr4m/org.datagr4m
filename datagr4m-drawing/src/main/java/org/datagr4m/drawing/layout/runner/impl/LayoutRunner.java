package org.datagr4m.drawing.layout.runner.impl;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.layout.algorithms.forceAtlas.BoundedForceAtlasLayout;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.layout.hierarchical.matrix.HierarchicalMatrixLayout;
import org.datagr4m.drawing.layout.hierarchical.visitor.AbstractLayoutVisitor;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.ILayoutRunnerListener;
import org.datagr4m.drawing.layout.runner.LayoutLevelSettings;
import org.datagr4m.drawing.layout.runner.LayoutRunnerConfiguration;
import org.datagr4m.drawing.layout.runner.LayoutRunnerListenerAdapter;
import org.datagr4m.drawing.layout.runner.sequence.ILayoutRunnerSequence;
import org.datagr4m.drawing.layout.runner.stop.IBreakCriteria;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.viewer.mouse.items.MouseItemViewController;
import org.datagr4m.io.xml.generated.layout.Attract;
import org.datagr4m.io.xml.generated.layout.Forces;
import org.datagr4m.io.xml.generated.layout.Group;
import org.datagr4m.io.xml.generated.layout.Groups;
import org.datagr4m.io.xml.generated.layout.Layout;
import org.datagr4m.io.xml.generated.layout.Repulse;
import org.datagr4m.io.xml.layout.LMLEditor;
import org.datagr4m.monitors.ITimeMonitor;
import org.datagr4m.monitors.TimeMonitor;
import org.datagr4m.viewer.IView;
import org.openide.util.lookup.ServiceProvider;

/**
 * Layout runner is able to compute {@link IHierarchicalNodeLayout}s of
 * {@link IHierarchicalNodeModel}. It can be finely tuned to enable or disable part
 * of the general layout, by using a {@link LayoutRunnerConfiguration}. For
 * example, one can use the configuration to indicate:
 * <ul>
 * <li>if the edge layout should be updated at each iteration, or at the end
 * only, which can be helpfull with long lasting edge layout computation (e.g.
 * if the layout involve edge bundling)</li>
 * <li>if the view should automatically fit the last graph bounds or remain as
 * is</li>
 * </ul>
 * This settings are used when someone call <code>runner.start()</code>. However
 * one can perform "manual" operations one after one without actually running
 * the layout:
 * <ul>
 * <li>runner.oneEdgeStep() (doing only one step)
 * </ul>
 * 
 * Hierarchy of the layout is traversed. Each level is asked to run a required
 * number of steps according to configuration.getRequiredLoops()
 * 
 * Note that the runner can be assigned a {@link IBreakCriteria} that defines
 * when to stop.
 * 
 * Appart of defining various layout settings, {@link LayoutRunnerConfiguration}
 * also defines a sequence of operations to do while computing a layout (e.g.
 * run with high repulsion for 1 minute, and then reduce this parameter to
 * compact the graph).
 * 
 * The sequence returned by the configuration is a Runnable that will be the
 * thread initialized by this LayoutRunner.
 * 
 * @see MouseItemViewController.setRunner as mouse may interrupt running or
 *      simply update edge layout after dragging an item
 * @see com.netlight.apps.designer.toolbars.runner.LayoutToolbar as it use the
 *      runner to start/stop computation
 * 
 * @author Martin Pernollet
 */
@ServiceProvider(service=ILayoutRunner.class)
public class LayoutRunner extends AbstractLayoutVisitor implements ILayoutRunner {
	// http://netbeans.dzone.com/news/mini-tutorial-netbeans-lookup
	
    private ITimeMonitor timeMonitor;
    
    public LayoutRunner() {
        this(null);
    }

    public LayoutRunner(IHierarchicalNodeLayout root) {
        this(root, null);
    }

    public LayoutRunner(IHierarchicalNodeLayout root, IView view) {
        this(root, view, LayoutRunnerConfiguration.getDefault());
    }

    public LayoutRunner(IHierarchicalNodeLayout root, IView view, LayoutRunnerConfiguration settings) {
        configure(root, view, settings);
        initMonitor();
    }
    
    private void initMonitor() {
        timeMonitor = new TimeMonitor(this);
    }
    
    @Override
    public ITimeMonitor getTimeMonitor() {
        return timeMonitor;
    }

    @Override
	public void configure(IHierarchicalNodeLayout root) {
    	configure(root, null);
	}
	
	@Override
	public void configure(IHierarchicalNodeLayout root, IView view) {
		configure(root, view, LayoutRunnerConfiguration.getDefault());
	}
	
	@Override
	public void configure(IHierarchicalNodeLayout root, IView view,
			LayoutRunnerConfiguration settings) {
		this.layout = root;
        this.view = view;
        this.configuration = settings;
        this.counter = 0;
	}

    /* MAKE ORDERED LAYOUT COMPUTATION */

    @Override
	public void visit(IHierarchicalNodeLayout root) {
        super.visit(root);

        // compute edge
        if (configuration.isDoRunEdge()) {
            if (!layout.isDoRunEdgeLayout())
                layout.setDoRunEdgeLayout(true);
            root.goAlgoEdge();
        } else if (layout.isDoRunEdgeLayout())
            layout.setDoRunEdgeLayout(false);
        counter++;
    }

    @Override
    public void preVisit(IHierarchicalNodeLayout layout) {
        if (layout instanceof HierarchicalGraphLayout) {
            runGraphLayout(layout);
        } else {
            layout.goAlgo();
        }
    }

    private void runGraphLayout(IHierarchicalNodeLayout layout) {
        LayoutLevelSettings levelSettings = getLayoutSettings(layout);
        runGraphLayoutLevelAndFirePositionChanged((HierarchicalGraphLayout) layout, levelSettings);
    }


    /**
     * Lookup layout settings
     * 
     * @return
     */
    @Override
    public LayoutLevelSettings getLayoutSettings(IHierarchicalNodeLayout layout) {
        LayoutLevelSettings settings = layoutSettings.get(layout);
        if (settings == null) {
            settings = resolveFromXML(layout);
            if (settings == null) {
                Logger.getLogger(LayoutRunner.class).warn("using default layout settings for " + layout.getModel().getLabel());
                settings = new LayoutLevelSettings();
            }
            layoutSettings.put(layout, settings);
        }
        return settings;
    }
    
    @Override
	public void repulsionMultiplyAllBy(double i) {
        for(LayoutLevelSettings level: layoutSettings.values()){
            level.setRepulsion(level.getRepulsion()*i);
        }
    }

    @Override
	public void repulsionSetAllTo(double i) {
        for(LayoutLevelSettings level: layoutSettings.values()){
            level.setRepulsion(i);
        }
    }

    @Override
	public void attractionMultiplyAllBy(double i) {
        for(LayoutLevelSettings level: layoutSettings.values()){
            level.setAttraction(level.getAttraction()*i);
        }
    }

    @Override
	public void attractionSetAllTo(double i) {
        for(LayoutLevelSettings level: layoutSettings.values()){
            level.setAttraction(i);
        }
    }

    
    /**
     * Search the layout ML configuration of a group layout to setup its forces, max loop per visit, and break criteria.
     */
    protected LayoutLevelSettings resolveFromXML(IHierarchicalNodeLayout layout2) {
        int LOOPS = 100;

        String label = layout2.getModel().getLabel();

        Group xmlGroup = LMLEditor.findGroup(layoutML, label);
        if (xmlGroup != null && xmlGroup.getGrouplayout() != null) {
            Forces xmlForces = xmlGroup.getGrouplayout().getForces();
            if (xmlForces != null) {
                Attract a = xmlForces.getAttract();
                Repulse r = xmlForces.getRepulse();
                // Break b = xmlForces.getBreak();
                return new LayoutLevelSettings(-1, r.getValue(), a.getValue(), LOOPS);
            }
        } else {
            Groups xmlGroups = LMLEditor.findGroupsContaining(layoutML, label);
            if (xmlGroups == null)
                xmlGroups = LMLEditor.findGroupsContaining(layoutML, "*"); // try
                                                                           // the
                                                                           // any
                                                                           // group
                                                                           // definition
            if (xmlGroups != null && xmlGroups.getGrouplayout() != null) {
                Forces xmlForces = xmlGroups.getGrouplayout().getForces();
                if (xmlForces != null) {
                    Attract a = xmlForces.getAttract();
                    Repulse r = xmlForces.getRepulse();
                    // Break b = xmlForces.getBreak();
                    return new LayoutLevelSettings(-1, r.getValue(), a.getValue(), LOOPS);
                }
            }
        }
        return null;
    }

    @Override
    public void postVisit(IHierarchicalNodeLayout layout) {
    }

    /** Runs the delegate ForceAtlas layout at the given layout level. */
    protected void runGraphLayoutLevelAndFirePositionChanged(HierarchicalGraphLayout layout, LayoutLevelSettings levelSettings) {
        if(layout!=null && layout.getDelegate() instanceof BoundedForceAtlasLayout){
            BoundedForceAtlasLayout delegate = layout.getDelegate();
            delegate.setRepulsionStrength(levelSettings.getRepulsion());
            delegate.setAttractionStrength(levelSettings.getAttraction());

            boolean didChange = false;
            for (int i = 0; i < levelSettings.getRequiredLoops(); i++) {
                // do not update children has we already visit each layer
                if (!layout.goAlgo(false)) {
                    // if only one item, no need to do N time setPosition(new
                    // Coord(0,0))
                    break;
                } else
                    didChange = true;
            }
            if (didChange) {
                // avoid propagating parent position change event if
                // we did not changed positions
                layout.getModel().fireParentPositionChanged(); // update absolute
                                                               // coord
            }
        }
    }

    @Override
    public void autoFit(IHierarchicalNodeModel model) {
        if (model != null && view != null) {
            IHierarchicalNodeModel modelToFit = model;
            while (modelToFit.getChildren().size() == 1) {
                IBoundedItem soleChild = modelToFit.getChildren().get(0);
                if (soleChild instanceof IHierarchicalNodeModel)
                    modelToFit = (IHierarchicalNodeModel) soleChild;
                else
                    break;
            }

            Rectangle2D bounds = model.getInternalRectangleBounds().cloneAsRectangle2D();
            view.fit(bounds, true);
            // view.fit((Rectangle2D)bounds.clone(), false);
        }
    }

    /* CONTROL */

    /**
     * start using an executor rather than building own thread. should be
     * prefered to other start method.
     */
    public void start(Executor exec) {
        timeMonitor.startMonitor();

        isRunning = true;

        ILayoutRunnerSequence sequence = configuration.getSequence();
        sequence.setRunner(this);
        exec.execute(sequence);

        fireStart();
    }

    @Override
	public void start() {
        timeMonitor.startMonitor();
        
        isRunning = true;
        if (mainThread == null) {
            ILayoutRunnerSequence sequence = configuration.getSequence();
            sequence.setRunner(this);
            mainThread = getThread(sequence);
            mainThread.start();
        }

        fireStart();
    }

    @Override
	@SuppressWarnings("deprecation")
    public void stop() {
        isRunning = false;
        if (mainThread != null) {
            mainThread.stop();
            mainThread = null;
        }
        fireStop();
    }

    @Override
	public void oneEdgeStep() {
        layout.goAlgoEdge();
        if (view != null)
            view.refresh();
    }

    @Override
	public boolean isRunning() {
        return isRunning;
    }

    
    @Override
	public void setRunning(boolean isRunning) {
        setRunning(isRunning, false);
    }

    @Override
	public void setRunning(boolean isRunning, boolean notifyListeners) {
        this.isRunning = isRunning;
        if (isRunning)
            fireStart();
        else
            fireStop();
    }

    /**
     * use instead configuration.getSequence().getFirstPhaseBreakCriteria()
     */
    @Override
	@Deprecated
    public IBreakCriteria getBreakCriteria() {
        return breakCriteria;
    }

    /**
     * use instead configuration.getSequence().setFirstPhaseBreakCriteria(...)
     */
    @Override
	@Deprecated
    public void setBreakCriteria(IBreakCriteria breakCriteria) {
        this.breakCriteria = breakCriteria;
    }

    @Override
	public Layout getLayoutML() {
        return layoutML;
    }

    @Override
	public void setLayoutML(Layout layoutML) {
        this.layoutML = layoutML;
    }

    /* OPTIONS FOR RUNNING THE ALGORITHM */

    @Override
    public boolean isDoRunEdge() {
        return configuration.isDoRunEdge();
    }

    @Override
    public void setDoRunEdge(boolean doRunEdge) {
        layout.setDoRunEdgeLayout(doRunEdge);
        configuration.setDoRunEdge(doRunEdge);
    }

    @Override
	public LayoutRunnerConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(LayoutRunnerConfiguration settings) {
        this.configuration = settings;
    }
    
    

    /* TOOLS */

    @Override
	public IView getView() {
        return view;
    }

    @Override
	public IHierarchicalNodeLayout getLayout() {
        return layout;
    }

    public Thread getThread(){
        return mainThread;
    }
    
    
    /**
     * Return a thread able to run a {@link ILayoutRunnerSequence}, in other
     * word a sequence of layout phases.
     */
    protected Thread getThread(Runnable sequence) {
        return new Thread(sequence){
            @Override
			public void run(){
                try{
                    super.run();
                }
                catch(Exception e){
                    String m = "An exception occured while running layout";
                    fireFailed(m, e);
                    Logger.getLogger(LayoutRunner.class).error(m, e);
                }
            }
        };
    }

    public int getCounter() {
        return counter;
    }

    protected void fireStart() {
        for (ILayoutRunnerListener listener : listeners) {
            listener.runnerStarted();
        }
    }

    protected void fireStop() {
        for (ILayoutRunnerListener listener : listeners) {
            listener.runnerStopped();
        }
        
        timeMonitor.stopMonitor();
    }
    
    protected void fireFailed(String message, Exception e) {
        for (ILayoutRunnerListener listener : listeners) {
            listener.runnerFailed(message, e);
        }
    }

    /**
     * Made public for visibility reason. Should only be called at the end of a
     * LayoutRunnerSequence.
     */
    @Override
	public void fireFinished() {
        for (ILayoutRunnerListener listener : listeners) {
            listener.runnerFinished();
        }
    }

    @Override
	public void addListener(ILayoutRunnerListener listener) {
        listeners.add(listener);
    }

    @Override
	public void removeListener(ILayoutRunnerListener listener) {
        listeners.remove(listener);
    }
    
    public void startAndAwaitAtMost(int seconds) throws Exception {
        //final TicToc t = new TicToc();
        //t.tic();
        // Start and make test wait for end
        final BooleanStatusHolder holder = new BooleanStatusHolder();
        addListener(new LayoutRunnerListenerAdapter() {
            @Override
            public void runnerFinished() {
                holder.status = true;
    //            Logger.getAnonymousLogger().info("unnder finished after " + t.toc() + " s");
            }
        });
        /*runner.setBreakCriteria(new MaxStepCriteria(100){
            public void onBreak(){  
                holder.status = true;
            }
        });*/
        start();
        await().atMost(seconds, SECONDS).until(holder.getStatusTrueVerifier());
    }


    /* */
    
    @Deprecated
    protected IBreakCriteria breakCriteria;

    protected IView view;
    protected Thread mainThread;
    protected boolean isRunning = false;
    protected LayoutRunnerConfiguration configuration;
    protected Layout layoutML;
    protected int counter;
    // protected int[] levelCounter;

    protected Map<IHierarchicalNodeLayout, LayoutLevelSettings> layoutSettings = new HashMap<IHierarchicalNodeLayout, LayoutLevelSettings>();
    protected IHierarchicalNodeLayout layout;
    protected List<ILayoutRunnerListener> listeners = new ArrayList<ILayoutRunnerListener>();
}
