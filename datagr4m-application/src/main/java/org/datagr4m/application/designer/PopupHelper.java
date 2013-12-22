package org.datagr4m.application.designer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingConstants;

import org.datagr4m.view2d.icons.IconLibrary;


public class PopupHelper {
    public static void error(String title, Throwable t) {
        error(title, getStackTrace(t));
    }
    
    public static void error(Throwable t) {
        error("Error", getStackTrace(t));
    }
    
    /** A modal error popup USING JDIALOG. */
    public static void error(String title, String message) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE, JOptionPane.CLOSED_OPTION);
        JDialog dialog = optionPane.createDialog(title);
        dialog.setIconImage(IconLibrary.getIcon(DesignerConsts.WINDOW_ICON).getImage());
        dialog.show();
    }

    /* */
    
    /** A popup attached to a frame. */
    public static Popup popup(JFrame frame, Exception e) {
        return popup(frame, e.toString());
    }

    /** A popup attached to a frame, using POPUPFACTORY. */
    public static Popup popup(JFrame frame, String message) {
        JLabel text = new JLabel(message);
        text.setMinimumSize(new Dimension(300, 200));
        PopupFactory factory = PopupFactory.getSharedInstance();
        Popup popup = factory.getPopup(frame, text, frame.getSize().width / 2, frame.getSize().height / 2);
        popup.show();
        return popup;
    }
    
    /* */
    
    // create a pseudo popup by opening a frame
    public static JFrame popupFrame(String title, String message, int width, int height, WindowListener listener) {
        Color bg = Color.WHITE;
        
        JFrame frame = new JFrame(title);
        JLabel label = new JLabel(message);
        label.setBackground(bg);
        label.setBorder(BorderFactory.createBevelBorder(1));
        label.setVerticalTextPosition(SwingConstants.TOP);
        label.setHorizontalTextPosition(SwingConstants.RIGHT);
        label.setOpaque(true);
        frame.add(label);
        frame.setBackground(bg);
        frame.setSize(width, height);
        frame.addWindowListener(listener);
        frame.show();
        return frame;
    }

    protected static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }
}
