/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ToolBarDemo.java requires the following addditional files:
 * images/Back24.gif
 * images/Forward24.gif
 * images/Up24.gif
 */
package org.datagr4m.application.designer.toolbars.runner;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.datagr4m.viewer.Display;


public class ToolbarHelper {
    public static void main(String[] args) {
        JPanel p = new JPanel(new BorderLayout());
        JToolBar toolbar = ToolbarHelper.createToolbar();

        // Lay out the main panel.
        p.setPreferredSize(new Dimension(450, 130));
        addToolbar(p, toolbar);
        Display.show(p);
    }
    
    public static void addToolbar(JPanel panel, JToolBar bar){
        panel.add(bar, BorderLayout.PAGE_START);
    }
    
    /* */

    public static JToolBar createToolbar() {
        JToolBar toolBar = new JToolBar("Still draggable");
        addToolbarButtons(toolBar);
        toolBar.setFloatable(false);
        return toolBar;
    }

    public static String imageRoot = "";

    public static void addToolbarButtons(JToolBar toolBar) {
        toolBar.add(makeNavigationButton(imageRoot + "import.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                throw new RuntimeException("ploum");
            }
        }));
        toolBar.add(makeNavigationButton(imageRoot + "parse.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                throw new RuntimeException("ploum");
            }
        }));
        toolBar.add(makeNavigationButton(imageRoot + "topology.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                throw new RuntimeException("ploum");
            }
        }));
        toolBar.add(makeNavigationButton(imageRoot + "layout.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                throw new RuntimeException("ploum");
            }
        }));
        
        toolBar.add(new JTextField("coucou"));
    }

    protected static JButton makeNavigationButton(String imageName, ActionListener listener) {
        return makeNavigationButton(imageName, imageName, null, listener);
    }

    protected static JButton makeNavigationButton(String imageName, String altText, String toolTipText, ActionListener listener) {
        // Look for the image.
        URL imageURL = ToolbarHelper.class.getResource(imageName);

        // Create and initialize the button.
        JButton button = new JButton();

        if (toolTipText != null)
            button.setToolTipText(toolTipText);
        if (listener != null) {
            button.addActionListener(listener);
        }

        if (imageURL != null) { // image found
            button.setIcon(new ImageIcon(imageURL, altText));
        } else { // no image found
            button.setText(altText);
            System.err.println("Resource not found: " + imageName);
        }

        return button;
    }
}
