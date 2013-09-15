package org.datagr4m.io.xml.layout;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.datagr4m.io.xml.JAXBHandler;
import org.datagr4m.io.xml.generated.layout.Group;
import org.datagr4m.io.xml.generated.layout.Groups;
import org.datagr4m.io.xml.generated.layout.Layout;

public class LMLEditor {
	JAXBHandler h;
	
	public LMLEditor(){
		h = new JAXBHandler(Layout.class.getPackage().getName());
	}
	
    public void save(Layout xmlLayout, String file) throws FileNotFoundException, JAXBException{
        h.marshall(xmlLayout, file);
    }
    
    public Layout load(String file) throws Exception{
        Object o = h.unmarshall(file);
        if(o!=null)
            return (Layout) o;
        else
            return null;
    }
    
    public static Group findGroup(Layout xmlLayout, String group){
        if(group==null || xmlLayout==null)
            return null;
        for(Group g: xmlLayout.getGroup())
            if(group.equals(g.getName()))
                return g;
        return null;
    }
    
    public static Groups findGroupsContaining(Layout xmlLayout, String group){
        if(group==null || xmlLayout==null)
            return null;
        for(Groups g: xmlLayout.getGroups())
            if(g.getNames().contains(group))
                return g;
        return null;
    }
}
