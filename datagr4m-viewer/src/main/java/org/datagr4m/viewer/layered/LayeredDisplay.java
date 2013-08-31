package org.datagr4m.viewer.layered;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import org.datagr4m.viewer.IDisplay;


/** A wrapper for {@link IDisplay} that enables management of {@link JInternalFrame}s.*/
public class LayeredDisplay extends JDesktopPane implements IPopupLayer{
    public LayeredDisplay(){
        this(null);
    }
    public LayeredDisplay(IDisplay display){
        setOpaque(true);
        if(display!=null)
            setDisplay(display);
        //setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        //setBorder(null);
        //DesktopPaneUI  biui = getUI();
        //biui.ge
    }
    
    public IDisplay getDisplay() {
        return display;
    }

    public void setDisplay(final IDisplay display) {
        this.display = display;
        this.add((JComponent)display, JLayeredPane.DEFAULT_LAYER);
        //this.add(new G2DGLCanvas(display), JLayeredPane.DEFAULT_LAYER);
        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent arg0) {
                int width = LayeredDisplay.this.getWidth();
                int height = LayeredDisplay.this.getHeight();
                ((JComponent)display).setBounds(0, 0, width, height);
            }
        });
    }
    
    /* HANDLING ADDITION OF WIDGETS AND TOOLBARS*/
    
    /**
     * Adding a toolbar means:
     * <ul>
     * <li>creating an internal frame to be added to {@link JLayeredPane.PALETTE_LAYER}.
     * <li>adding to the {@link LayeredDisplay} a {@link ComponentAdapter} to 
     * 	   fix the internal frame next to the window borders it was attached to (isUp/isLeft) once the main window is resized.
     * </ul>
     */
    public JInternalFrame setToolbar(String name, final JPanel toolbar, final int width, final int height, final int xmargin, final int ymargin, final boolean isUp, final boolean isLeft) {
        // Toolbar frame
        int x = 0;
        int y = 0;
        toolbar.setVisible(true);
        final JInternalFrame f = addFlatLayer(toolbar, name, x, y, width, height, JLayeredPane.PALETTE_LAYER);
        f.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
        unwindowFrame(f);
        applyBounds(isUp, isLeft, xmargin, ymargin, width, height, f, LayeredDisplay.this);

        // Ability to resize properly
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent arg0) {
                applyBounds(isUp, isLeft, xmargin, ymargin, width, height, f, LayeredDisplay.this);
            }
        });
        return f;
    }
    
    protected static int defaultToolbarWidth = 220;
    protected static int defaultToolbarHeight = 250;
    protected static int defaultToolbarXmargin = 0;
    protected static int defaultToolbarYmargin = 0;
    
    public JInternalFrame setToolbar(String name, final JPanel toolbar) {
    	return setToolbar(name, toolbar, defaultToolbarWidth, defaultToolbarHeight, defaultToolbarXmargin, defaultToolbarYmargin);
    }
    
    public JInternalFrame setToolbar(String name, final JPanel toolbar, int width, int height, int xmargin, int ymargin) {
    	return setToolbar(name, toolbar, width, height, xmargin, ymargin, true, false);
    }
    
    public JInternalFrame setToolbar(String name, final JPanel toolbar, int width, int height, int xmargin, int ymargin, final boolean isUp) {
        return setToolbar(name, toolbar, width, height, xmargin, ymargin, isUp, false);
    }
    
    /* FLAT LAYERS */
    
    /** Add a component to {@link JInternalFrame}, configure it to appear as a "flat" component (no title bar,etc),
     * add this frame to the {@link JLayeredPane.PALETTE_LAYER} layer and return it.*/
    public JInternalFrame addFlatLayer(JComponent component, String title, int x, int y, int width, int height){
        return addFlatLayer(component, title, x, y, width, height, JLayeredPane.PALETTE_LAYER);
    }
    public JInternalFrame addFlatLayer(JComponent component, String title, int x, int y, int width, int height, Object constraint){
        JInternalFrame frame = createFlatLayer(component, title, new Rectangle(x, y, width, height));
        add(frame, constraint);
        
        
        
        return frame;
    }
    
    /** Initialize an internal frame*/
    public JInternalFrame createFlatLayer(JComponent component, String title, Rectangle bounds) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JInternalFrame frame = new JInternalFrame(title, true, true, true, true);
        frame.getContentPane().add(component, BorderLayout.CENTER);
        frame.setBounds(bounds);
        
        /*frame.setResizable(false);
        frame.setClosable(false);
        frame.setMaximizable(false);
        frame.setIconifiable(false);*/
        

        
        /*frame.setResizable(true);
        frame.setClosable(false);
        frame.setMaximizable(false);
        frame.setIconifiable(true);
        frame.setTitle(title);*/
        frame.setVisible(true);
        
        //unwindowFrame(frame);
        return frame;
    }
    
    /* POPUP INTERNAL FRAME UTILS */
    @Override
    public JInternalFrame addPopupLayer(JComponent component, String title, int x, int y, int width, int height){
        return addPopupLayer(component, title, x, y, width, height, JLayeredPane.POPUP_LAYER);
    }
    
    @Override
    public JInternalFrame addPopupLayer(JComponent component, String title, int x, int y, int width, int height, Object constraint){
        JInternalFrame frame = createPopupLayer(component, title, new Rectangle(x, y, width, height));
        add(frame, constraint);
        return frame;
    }
    
    /** Initalize a {@link JInternalFrame} wrapping the input component.*/
    public JInternalFrame createPopupLayer(JComponent component, String title, Rectangle bounds) {
        JInternalFrame frame = new JInternalFrame();
        frame.getContentPane().add(component, BorderLayout.CENTER);
        frame.setBounds(bounds);
        frame.setResizable(true);
        frame.setClosable(true);
        frame.setMaximizable(false);
        frame.setIconifiable(true);
        frame.setTitle(title);
        frame.setVisible(true);
        //frame.setBorder(null);
        return frame;
    }
    
    /* INTERNAL FRAME UTILS */
    
    protected void unwindowFrame(JInternalFrame frame){
        frame.setBorder(null);
        BasicInternalFrameUI biui = ((javax.swing.plaf.basic.BasicInternalFrameUI) frame.getUI());
        biui.setNorthPane(null);
    }
    
    protected void applyBounds(final boolean isUp, final boolean isLeft,
			final int xmargin, final int ymargin,
			final int toolbarWidth, final int toolbarHeight,
			final JInternalFrame f, LayeredDisplay layered) {
    	
        int width = layered.getWidth();
        int height = layered.getHeight();
        
		int x;
		if(isLeft)
            x = xmargin;
        else
            x = width-toolbarWidth-xmargin;
        
        int y = 0;
        if(isUp)
            y = ymargin;
        else
            y = height-toolbarHeight-ymargin;
        f.setBounds(x, y, toolbarWidth, toolbarHeight);
	}

    /*************/

    private static final long serialVersionUID = 2652916235820770559L;
    protected IDisplay display;
    //protected JPanel toolbar;
}
