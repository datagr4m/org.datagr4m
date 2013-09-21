package org.datagr4m.drawing.layout.runner.factory;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.animation.ForceLayoutAnimation;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.openide.util.Lookup;

public class LookupLayoutRunnerFactory extends AbstractLayoutRunnerFactory implements ILayoutRunnerFactory{
	
	@Override
	public ILayoutRunner newLayoutRunner() {
		ILayoutRunner runner = Lookup.getDefault().lookup(ILayoutRunner.class);
		if (runner == null)
			throw new RuntimeException(
					"failed to lookup instance of layout runner. Do you have any Datagr4m Runtime in your classpath (e.g. artifact datagr4m-runtime-community)");
		else
			Logger.getLogger(ForceLayoutAnimation.class).info(
					"retrieved runner: " + runner.getClass().getName());
		return runner;
	}
}
