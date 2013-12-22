package org.datagr4m.gui.editors.treetable;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * A TreeTable example, showing a JTreeTable, operating on the local file
 * system.
 *
 * @version %I% %G%
 *
 * @author Philip Milne
 */

public class TreeTableExample0
{
    public static void main(String[] args) {
	new TreeTableExample0();
    }

    public TreeTableExample0() {
	JFrame frame = new JFrame("TreeTable");
	JTreeTable treeTable = new JTreeTable(new FileSystemModel(new File("./")));

	frame.addWindowListener(new WindowAdapter() {
	    @Override
		public void windowClosing(WindowEvent we) {
		System.exit(0);
	    }
	});

	frame.getContentPane().add(new JScrollPane(treeTable));
	frame.pack();
	frame.show();
    }
}

