package org.datagr4m.viewer;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import org.datagr4m.viewer.animation.IAnimationStack;
import org.datagr4m.viewer.layered.LayeredDisplay;
import org.datagr4m.viewer.mouse.ILocalizedMouse;
import org.datagr4m.viewer.mouse.KeyMemoryEventDispatcher;
import org.datagr4m.viewer.renderer.IRenderer;

public interface IDisplay {
	public IView getView();
	public void setView(IView view);
	public void createViewFor(IRenderer r);
	
	public ILocalizedMouse getMouse();
	public void setMouse(ILocalizedMouse mouse);

	public BufferedImage screenshot();
	public BufferedImage screenshot(int width, int height);

	public void refresh();
	public void paint(Graphics g);

	public BufferedImage getImage(int width, int height);

	//http://download.oracle.com/javase/tutorial/2d/advanced/quality.html
	public void render(Graphics2D g2d);
	public void render(Graphics2D g2d, boolean notify);

	public LayeredDisplay getLayeredDisplay();
	public void setLayeredDisplay(LayeredDisplay parent);

	public KeyMemoryEventDispatcher getKeyMemory();
	public IAnimationStack getAnimator();

	public Overlay getOverlay();
	public Overlay getUnderlay();
	
	public Dimension getSize();

	public List<IRepaintListener> getListeners();

	public void addListeners(IRepaintListener listener);

	public void removeListeners(IRepaintListener listener);

	public void doPaintWithStrategy();
	public void createDoubleBuffer();

	public Frame openFrame();
	public Frame openFrame(int width, int heigth);
}