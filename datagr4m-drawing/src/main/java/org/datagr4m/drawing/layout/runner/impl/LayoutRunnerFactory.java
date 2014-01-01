package org.datagr4m.drawing.layout.runner.impl;

import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.factory.AbstractLayoutRunnerFactory;

public class LayoutRunnerFactory extends AbstractLayoutRunnerFactory {

	@Override
	public ILayoutRunner newLayoutRunner() {
		return new LayoutRunner();
	}
}
