package org.datagr4m.viewer.renderer;

import java.awt.Graphics2D;

import org.datagr4m.viewer.renderer.hit.IHitProcessor;


public interface IRenderer extends IHitProcessor{
	public void render(Graphics2D graphic);
	
	public void setHitProcessor(IHitProcessor hitProcessor);
    public IHitProcessor getHitProcessor();
}
