package org.datagr4m.tests.io.xml;

import org.datagr4m.io.xml.generated.layout.Group;
import org.datagr4m.io.xml.generated.layout.Layout;
import org.datagr4m.io.xml.layout.LMLEditor;

public class DesactivatedTestReadLayout{
    public void testReadLayout() throws Exception{
        LMLEditor e = new LMLEditor();
        Layout layout = e.load("com/netlight/tests/workspace/layout.xml");
        Group group = LMLEditor.findGroup(layout, "g6");
        //assertTrue(group!=null);
        
        //group.getGrouplayout().getStratums().getStratum().
    }
}
