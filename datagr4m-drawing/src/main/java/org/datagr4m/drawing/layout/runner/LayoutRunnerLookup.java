package org.datagr4m.drawing.layout.runner;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.animation.ForceLayoutAnimation;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.viewer.IView;
import org.openide.util.Lookup;

public class LayoutRunnerLookup {
	public static ILayoutRunner get() {
		ILayoutRunner runner = Lookup.getDefault().lookup(ILayoutRunner.class);
		if (runner == null)
			throw new RuntimeException(
					"failed to lookup instance of layout runner. Do you have any Datagr4m Runtime in your classpath (e.g. artifact datagr4m-runtime-community)");
		else
			Logger.getLogger(ForceLayoutAnimation.class).info(
					"retrieved runner: " + runner.getClass().getName());
		return runner;
	}
	

    public static ILayoutRunner get(IHierarchicalLayout root) {
        return get(root, null);
    }

    public static ILayoutRunner get(IHierarchicalLayout root, IView view) {
        return get(root, view, LayoutRunnerConfiguration.getDefault());
    }

    public static ILayoutRunner get(IHierarchicalLayout root, IView view, LayoutRunnerConfiguration settings) {
    	ILayoutRunner runner = get();
    	runner.configure(root, view, settings);
    	return runner;
    }
}
