package org.datagr4m.layout.runner.readability;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Set;

import org.datagr4m.datastructures.pairs.CommutativePair;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.layout.runner.LayoutRunnerListenerAdapter;
import org.datagr4m.drawing.layout.runner.impl.BooleanStatusHolder;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunner;
import org.datagr4m.drawing.layout.runner.sequence.LayoutRunnerSequenceSinglePhase;
import org.datagr4m.drawing.layout.runner.stop.MaxStepCriteria;
import org.datagr4m.drawing.model.factories.HierarchicalTopologyModelFactory;
import org.datagr4m.drawing.model.factories.IHierarchicalModelFactory;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.QualityScores;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.topology.Topology;
import org.datagr4m.topology.generator.TopologyGenerator;
import org.datagr4m.trials.drawing.DisplayLauncher;
import org.datagr4m.viewer.IDisplay;
import org.junit.Assert;
import org.junit.Test;

public class TestLayoutNoOverlapOnSimpleHierarchicalGraph extends AbstractLayoutRunnerTest {
    @Test
    public void testNoOverlap() throws Exception{
        int MAX_TIME = 10; //s
        
        Topology<String,String> topology = TopologyGenerator.buildGraphNested(2, 3, 100);
        HierarchicalGraphModel model = buildModel(topology);
        HierarchicalGraphLayout layout = buildLayout(model);
        
        IDisplay d = DisplayLauncher.display(model);
        
        // Start and make test wait for end
        final BooleanStatusHolder holder = new BooleanStatusHolder();
        final LayoutRunner runner = new LayoutRunner(layout, d.getView());
        runner.getConfiguration().setAllowAutoFitAtStepEnd(true);
        LayoutRunnerSequenceSinglePhase seq = (LayoutRunnerSequenceSinglePhase)runner.getConfiguration().getSequence();
        seq.setFirstPhaseBreakCriteria(new MaxStepCriteria(10000));
        
        runner.addListener(new LayoutRunnerListenerAdapter() {
            @Override
            public void runnerFinished() {
                holder.status = true;
            }
        });
        runner.start();
        await().atMost(MAX_TIME, SECONDS).until(holder.getStatusTrueVerifier());
        
        Set<CommutativePair<IBoundedItem>> overlapping = QualityScores.countOverlappingItems(model);
        
        if(overlapping.size()>0)
            System.out.println(overlapping);
        Assert.assertTrue("no item overlap any other", overlapping.size()==0);
    }

    private HierarchicalGraphModel buildModel(Topology<String, String> topology) {
        IHierarchicalModelFactory factory = new HierarchicalTopologyModelFactory<String, String>();
        HierarchicalGraphModel model = (HierarchicalGraphModel) factory.getLayoutModel(topology);
        return model;
    }
}
