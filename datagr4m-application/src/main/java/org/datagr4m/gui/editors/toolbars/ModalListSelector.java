package org.datagr4m.gui.editors.toolbars;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ModalListSelector {
    public static void main(String[] args){
        Object[] possibilities = {"ham", "spam", "yam"};
        System.out.println(ModalListSelector.ask("title", "question", possibilities, "images/middle.gif"));
    }
    /** supporte \n */
    public static String ask(String title, String question, Object[] possibilities, String image){
        ImageIcon icon = createImageIcon(image);
        JFrame frame = new JFrame();
        String s = (String)JOptionPane.showInputDialog(
                            frame,
                            question,
                            title,
                            JOptionPane.PLAIN_MESSAGE,
                            icon,
                            possibilities,
                            "");
        return s;
    }
    
    public static String askText(String title, String question, String image){
        ImageIcon icon = createImageIcon(image);
        JFrame frame = new JFrame();
        
        Object[] options = {"Enter", "Cancel"};
        
        String s = (String)JOptionPane.showInputDialog(
                frame,
                question,
                title,
                JOptionPane.QUESTION_MESSAGE,
                icon,
                null, "");
        return s;
    }
    
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ModalListSelector.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
