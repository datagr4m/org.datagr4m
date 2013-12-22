package org.datagr4m.gui.editors.scale;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.ImagePanel;
import org.jzy3d.io.FileImage;


public class ViewScaleUI extends JPanel{
    private static final long serialVersionUID = -3663463471534212593L;
    public static String FIT_ICON = "data/images/buttons/loupe-16.png";
    public static int DEFAULT_HEIGHT = 30;
    protected JButton button;
    protected IView view;
    protected IHierarchicalModel model;
    
    public ViewScaleUI() {
        this(null, null);
    }
    
    public ViewScaleUI(IView view, IHierarchicalModel model) {
        this.view = view;
        this.model = model;
        BufferedImage img = null;
        try {
            img = FileImage.load(FIT_ICON);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon icon = new ImageIcon(img);
        
        button = new JButton();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                IHierarchicalModel model = ViewScaleUI.this.model;
                IView view = ViewScaleUI.this.view;
                
                if(model!=null && view!=null){
                    Rectangle2D bounds = model.getRawRectangleBounds().cloneAsRectangle2D();
                    view.fit(bounds);
                }
                /*final BufferedImage i = view.getDisplay().screenshot();
                try {
                    ImageIO.write(i, "png", new File("map.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //i.getGraphics().dispose();
                if(panel!=null)
                    panel.setImg(i);*/
            }
        });
        
        if(img!=null && icon!=null)
            button.setIcon(icon);
        else
            button.setText("Fit");
        
        setLayout(new MigLayout("flowx, insets 0 0 0 0", "", "[0::300]"));
        add(button, "align left");
        setBorder(BorderFactory.createBevelBorder(1));
    }
    
    public void setImagePanel(ImagePanel panel){
        this.panel = panel;
    }
    
    protected ImagePanel panel;
    
    public static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ViewScaleUI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    public IView getView() {
        return view;
    }
    public void setView(IView view) {
        this.view = view;
    }
    public IHierarchicalModel getModel() {
        return model;
    }
    public void setModel(IHierarchicalModel model) {
        this.model = model;
    }
}
