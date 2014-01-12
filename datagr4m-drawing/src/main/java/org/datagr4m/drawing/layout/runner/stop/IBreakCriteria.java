package org.datagr4m.drawing.layout.runner.stop;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;

public interface IBreakCriteria {
    public boolean shouldBreak(IHierarchicalNodeLayout layout);
    public void onBreak();
    //public void reset();
    public static final int THOUSAND_STEPS = 1000;
    public static final int MILLION_STEPS = 1000000;
    public static final int BILLION_STEPS = 1000000000;
}
