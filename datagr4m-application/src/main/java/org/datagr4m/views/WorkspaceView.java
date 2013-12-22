package org.datagr4m.views;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.datagr4m.application.neo4j.workspace.Neo4jWorkspaceSettings;
import org.datagr4m.io.xml.generated.layout.Layout;
import org.datagr4m.io.xml.layout.LMLEditor;
import org.datagr4m.utils.IParserHelper;
import org.jzy3d.io.SimpleFile;

import com.datagr4m.neo4j.topology.graph.readers.FullGraphReader;
import com.datagr4m.neo4j.topology.graph.readers.INeo4jGraphReader;
import com.datagr4m.neo4j.topology.graph.readers.ReaderFactory;

public class WorkspaceView implements IParserHelper{
    String name;
    String dataPath;
    INeo4jGraphReader filter;
    String topologyName;
    ReaderFactory factory = new ReaderFactory();
    LMLEditor layoutEditor = new LMLEditor();
    Layout layout;
    
    public WorkspaceView(Neo4jWorkspaceSettings s, String name) throws Exception {
        super();
        this.name = name;
        load(s, name);
        layout= layoutEditor.load(s.getViewPath(name) + "/layout.xml");
    }

    public INeo4jGraphReader getFilter(){
        return filter;
    }
    
    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopologyName() {
        return topologyName;
    }

    public void setTopologyName(String topologyName) {
        this.topologyName = topologyName;
    }

    public ReaderFactory getFactory() {
        return factory;
    }

    public void setFactory(ReaderFactory factory) {
        this.factory = factory;
    }

    public void setFilter(INeo4jGraphReader filter) {
        this.filter = filter;
    }

    public void load(Neo4jWorkspaceSettings s, String view) throws IOException{
        String f = s.getViewDataPath(view);
        
        List<String> lines = SimpleFile.read(f);
        for(String line: lines){
            Matcher m = filterPattern.matcher(line);
            if(m.matches()){
                String name = m.group(1);
                filter = factory.getFilter(name);
            }
            
            Matcher m2 = dataPattern.matcher(line);
            if(m2.matches()){
                String name = m2.group(1);
                dataPath = name;
            }

            Matcher m3 = topologyPattern.matcher(line);
            if(m3.matches()){
                String name = m3.group(1);
                topologyName = name;
            }

        }
        
        if(filter==null)
            filter = new FullGraphReader();
    }

    /*public IGraphFilter loadFilter(Neo4jWorkspaceSettings s, String view) throws IOException{
        String f = s.getViewPath(view) + "/filter.ns";
        
        List<String> lines = SimpleFile.read(f);
        for(String line: lines){
            Matcher m = filterPattern.matcher(line);
            if(m.matches()){
                String name = m.group(1);
                return factory.getFilter(name);
            }
        }
        return null;
    }*/
    
    static Pattern dataPattern = Pattern.compile(spnt + "data" + spnt + "=" + spnt + "(" + path + ")" + spnt);
    static String classname = "[\\w|\\d|\\-|\\/|\\.|~]+";
    static Pattern filterPattern = Pattern.compile(spnt + "filter" + spnt + "=" + spnt + "(" + classname + ")" + spnt);
    static Pattern topologyPattern = Pattern.compile(spnt + "topology" + spnt + "=" + spnt + "(" + classname + ")" + spnt);

}
