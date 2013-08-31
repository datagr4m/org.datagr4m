package org.datagr4m.drawing.renderer.policy;

import java.awt.Color;

import org.datagr4m.topology.Group;


// http://fr.wikipedia.org/wiki/Aide:Couleurs
public class DefaultStyleSheet {
    public static boolean DEFAULT_TUBE_IS_OPEN = true;
    
    public static Color NETSCOPE_BLUE_1 = new Color(0,64,84);
    public static Color NETSCOPE_BLUE_2 = new Color(46,96,108);
    
    public static Color MAN_COLOR = new Color(255,255,200);//new Color(255,51,102);
    public static Color SAN_COLOR = new Color(231,193,28);//Color.ORANGE;
    public static Color LAN_COLOR = NETSCOPE_BLUE_1;//new Color(220, 220, 255);//new Color(51, 102, 255);
    public static Color CLOUD_COLOR = new Color(250, 220, 250);
    public static Color HA_COLOR = new Color(150, 220, 150);
    
    public static Color TUBE_COLOR = new Color(100,100,100);
    public static Color TUBE_COLOR_SELECTED = new Color(250,0,0);
    public static Color LOCAL_EDGE_COLOR = TUBE_COLOR;
    
    public static boolean USE_CONTEXT_TREE = false;
    
    public static Color SELECTED_ITEM_COLOR = new Color(0,200,200);

    public static int DEFAULT_TEXT_SIZE = 12;
    public static int DEFAULT_FLOWER_NETWORK_TEXT_SIZE = 12;

    // hexa color
    // http://www.javalobby.org/java/forums/t19183.html
    public static Color getColor(Object o){
        if(o!=null){
        	if(o instanceof Group<?>){
                Group<?> g = (Group<?>)o;
                
                if("module".equals(g.getType())){
                    //return new Color(229,236,255);
                    return Color.decode("#cde3e7");
                }
                else if("security".equals(g.getType()) || "pair<firewall>".equals(g.getType()) || "pair&lt;firewall&gt;".equals(g.getType())){
                    return Color.decode("#fcee21");
                }
                else if("stratum".equals(g.getType())){
                    return Color.decode("#a8aca6").brighter();
                }
            }
        }
        return Color.WHITE;
    }
    
    
}
