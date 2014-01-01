package org.datagr4m.viewer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.datagr4m.viewer.animation.AnimationStack;
import org.datagr4m.viewer.layered.LayeredDisplay;
import org.datagr4m.viewer.mouse.ILocalizedMouse;
import org.datagr4m.viewer.mouse.KeyMemoryEventDispatcher;
import org.datagr4m.viewer.mouse.factory.DefaultMouseControllerFactory;
import org.datagr4m.viewer.mouse.factory.IMouseControllerFactory;
import org.datagr4m.viewer.renderer.AbstractRenderer;
import org.datagr4m.viewer.renderer.IRenderer;
import org.datagr4m.viewer.renderer.TextUtils;
import org.jzy3d.maths.TicToc;


// buffer strat http://www.javalobby.org/forums/thread.jspa?threadID=16867&tstart=0
public class Display extends JPanel implements IDisplay {
    protected BufferStrategy strategy;
    protected BufferedImage buffer = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
    
    protected LayeredDisplay layeredDisplay;
    protected IView view;
    protected Overlay overlay;
    protected Overlay underlay;

    protected IMouseControllerFactory factory;
    protected ILocalizedMouse mouse;
    protected boolean scalingAllowed = false;

    protected KeyMemoryEventDispatcher keyMemory;
    protected AnimationStack animator;
    
    protected List<IRepaintListener> listeners;
    
    protected Settings settings;
    
    protected TicToc t = new TicToc();

    
    public Display() {
        this(false, new DefaultMouseControllerFactory());
    }

    public Display(IMouseControllerFactory factory) {
        this(false, factory);
    }

    public Display(boolean start) {
        this(start, new DefaultMouseControllerFactory());
    }

    public Display(boolean start, IMouseControllerFactory factory) {
        // setOpaque(false);
        this.factory = factory;
        Timer timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });
        if(start)
            timer.start();
        
        keyMemory = new KeyMemoryEventDispatcher(this);
        animator = new AnimationStack(this);
        overlay = new Overlay();
        underlay = new Overlay();
        listeners = new ArrayList<IRepaintListener>();
        settings = new Settings(true, true);
        
        /*addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e){
                createBufferStrategy(3);
                strategy = getBufferStrategy();
            }
        });*/
    }

    @Override
	public IView getView() {
        return view;
    }

    @Override
	public void setView(IView view) {
        this.view = view;
        setMouse(factory.getController(this, view));
    }
    
    @Override
	public void createViewFor(IRenderer r){
    	setView(new View(r, this));
    }

    @Override
	public ILocalizedMouse getMouse() {
        return mouse;
    }

    @Override
	public void setMouse(ILocalizedMouse mouse) {
        this.mouse = mouse;
        mouse.setKeyMemory(keyMemory);

        addMouseListener((MouseListener)mouse);
        addMouseMotionListener((MouseMotionListener)mouse);
        addMouseWheelListener((MouseWheelListener)mouse);
    }

    /*************/
    
    @Override
	public BufferedImage screenshot() {
        return screenshot(getWidth(), getHeight());
    }
    @Override
	public BufferedImage screenshot(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //paint(img.getGraphics());
        render((Graphics2D)img.getGraphics(), false);
        return img;
    }

    /*************/
    
    @Override
	public void refresh(){
        if(strategy!=null)
            doPaintWithStrategy();
        else
            repaint();
    }
    
    @Override
	public void paint(Graphics g) {
        if(strategy!=null){
            doPaintWithStrategy();
        }
        else{
            Graphics2D g2d = (Graphics2D)g;
            render(g2d);
        }
    }
    
    @Override
	public BufferedImage getImage(int width, int height){
        BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        render(g2d);
        g2d.dispose();
        return image;
    }
    
    //http://download.oracle.com/javase/tutorial/2d/advanced/quality.html
    @Override
	public void render(Graphics2D g2d){
        render(g2d, true);
    }
    

    @Override
	public void render(Graphics2D g2d, boolean notify){
        //t.tic();
        
        clear(g2d);
        
        TextUtils.changeFontSize(g2d, AbstractRenderer.DEFAULT_TEXT_SIZE);
        
        if(settings!=null){
            settings.apply(g2d);
        }
        
        if(underlay != null)
            underlay.render(g2d);

        if (view != null)
            view.render(g2d);
        
        if(overlay != null)
            overlay.render(g2d);

        //t.toc();
        //System.out.println("rendering took:" + t.elapsedSecond() + " with " + g2d.getClass());
        
        if(notify)
            fireRepaintDone();        
    }
    
    protected void clear(Graphics2D g2d) {
        // erase background
        Color color = null;
        if(view!=null){
            color = view.getBackgroundColor();
        }
        if(color==null)
            color = Color.WHITE;

        g2d.setColor(color);
        g2d.fillRect(0, 0, getSize().width, getSize().height);
    }

    @Override
	public LayeredDisplay getLayeredDisplay() {
        return layeredDisplay;
    }

    @Override
	public void setLayeredDisplay(LayeredDisplay parent) {
        this.layeredDisplay = parent;
    }
    
    @Override
	public KeyMemoryEventDispatcher getKeyMemory() {
        return keyMemory;
    }
    
    @Override
	public AnimationStack getAnimator() {
        return animator;
    }
    
    @Override
	public Overlay getOverlay() {
        return overlay;
    }
    
    @Override
	public Overlay getUnderlay() {
        return underlay;
    }
    
    @Override
	public List<IRepaintListener> getListeners() {
        return listeners;
    }

    @Override
	public void addListeners(IRepaintListener listener) {
        listeners.add(listener);
    }
    
    @Override
	public void removeListeners(IRepaintListener listener) {
        listeners.remove(listener);
    }
    
    protected void fireRepaintDone(){
        for(IRepaintListener lis: listeners)
            lis.repaint();
    }
    
    /*********/
    
    @Override
	public void doPaintWithStrategy(){
        Graphics2D g2d = (Graphics2D) strategy.getDrawGraphics();
        render(g2d);
        strategy.show();
    }

    @Override
	public void createDoubleBuffer() {
        //createBufferStrategy(3);
        //strategy = getBufferStrategy();
        System.err.println("createDoubleBuffer() if display is a jpanel, don't work");
    }
    
    
    /*if (dblBuffer) {
    g2d.drawImage(buffer, null, 0, 0);
    g2d = (Graphics2D) buffer.getGraphics();
}*/
    
    
    /********************/

    @Override
	public Frame openFrame() {
        return show(this);
    }
    
    @Override
	public Frame openFrame(int width, int heigth) {
        JPanel p = new JPanel();
        p.add(this);
        return show(p, new Dimension(width, heigth));
    }
    
    public static void show(IRenderer renderer) {
        final Display display = new Display();
        display.setView(new View(renderer, display));
        display.openFrame();
    }

    public static Frame show(Canvas canvas) {
        JPanel p = new JPanel();
        p.add(canvas);
        return show(p, new Dimension(800, 700));
    }

    public static Frame show(JComponent panel) {
        return show(panel, new Dimension(800, 700));
    }

    public static Frame show(JComponent panel, Dimension dimension) {
        // open a window for display
        Frame frame = new Frame();
        //frame.createBufferStrategy(3);
        //frame.add(new G2DGLCanvas(panel));
        frame.add(panel);

        frame.addWindowListener(new WindowAdapter() {
            @Override
			public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        // frame.setTitle(folder);
        frame.setSize((int) (dimension.getWidth() + 30), ((int) dimension.getHeight() + 30));
        frame.setVisible(true);
        return frame;
    }
    
    public static Frame show(Panel panel) {
        return show(panel, new Dimension(800, 700));
    }
    
    public static Frame show(Panel panel, Dimension dimension) {
        // open a window for display
        Frame frame = new Frame();
        //frame.createBufferStrategy(3);
        //frame.add(new G2DGLCanvas(panel));
        frame.add(panel);

        frame.addWindowListener(new WindowAdapter() {
            @Override
			public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        // frame.setTitle(folder);
        frame.setSize((int) (dimension.getWidth() + 30), ((int) dimension.getHeight() + 30));
        frame.setVisible(true);
        return frame;
    }
    
    public static void show(JFrame frame, Dimension dimension) {
        frame.addWindowListener(new WindowAdapter() {
            @Override
			public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        // frame.setTitle(folder);
        frame.setSize((int) (dimension.getWidth() + 30), ((int) dimension.getHeight() + 30));
        frame.setVisible(true);
    }
    
    private static final long serialVersionUID = -2360784987084370320L;

}
