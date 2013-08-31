package org.datagr4m.viewer.renderer.html;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.mouse.IClickableItem;
import org.datagr4m.viewer.renderer.AbstractRenderer;
import org.datagr4m.viewer.renderer.IRenderer;


public class HtmlRenderer extends AbstractRenderer implements IRenderer{
    public HtmlRenderer(){
        this(null);
    }
    
    public HtmlRenderer(HtmlObject html){
        this.html = html;
        this.label = new JLabel(html!=null?html.getHtml():""); // ,JLabel.CENTER
        //this.label.setFont(new Font())
        this.label.setVerticalAlignment(SwingConstants.TOP); // affiche le haut de la table en haut
        this.label.setBorder(BorderFactory.createLineBorder(Color.pink, 1));
        this.label.setAutoscrolls(true);
        this.container = new Container();
    }
    
    @Override
    public void render(Graphics2D graphic) {
        render(graphic, html);
    }

    public void render(Graphics2D graphic, HtmlObject html){
        label.setText(html.getHtml());
        SwingUtilities.paintComponent(graphic, label, container, html.getRectangle());
    }
    
    @Override
    public List<IClickableItem> hit(int x, int y) {
        return null;
    }
    
    protected HtmlObject html;
    
    protected Container container;
    protected JScrollPane pane;
    protected JLabel label;
    protected IDisplay display;
}
