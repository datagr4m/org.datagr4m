package org.datagr4m.neo4j.topology.graph;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.datagr4m.topology.graph.NodeType;
import org.datagr4m.utils.IParserHelper;


public class Neo4jGraphModelIO implements IParserHelper {
    Neo4jGraphModel model = new Neo4jGraphModel();
    
    public Neo4jGraphModel getGraphModel(){
        return model;
    }
        
    public void read(List<String> lines) {
        model.reset();
        readTypes(lines);
        computeSuperTypes();
    }

    private void computeSuperTypes() {
        for(NodeType t: model.getTypes())
            if(t.getInheritance()!=null)
                t.setSuperType(model.getNodeType(t.getInheritance()));
    }

    public void readTypes(List<String> lines) {
        Iterator<String> it = lines.iterator();
        while (it.hasNext()) {
            String line = it.next();
            String type = getType(line);

            if (match(type)) {
                readType(new NodeType(type), it);
            }
        }
    }

    private void readType(NodeType newType, Iterator<String> it) {
        while (it.hasNext()) {
            String line = it.next();
            
            String property = getProperty(line);
            if(match(property))
                newType.getProperties().add(property);
            
            String relation = getRelation(line);
            if(match(relation))
                newType.getRelations().add(relation);
                        
            String inherit = getExtends(line);
            if(match(inherit))
                newType.setInheritance(inherit);
            
            String label = getLabel(line);
            if(match(label))
                newType.setLabel(label);

            String icon = getIcon(line);
            if(match(icon))
                newType.setIcon(icon);
            
            boolean isProperty = isPropertyNode(line);
            if(isProperty){
                newType.setPropertyNode(true);
            }

            // exit
            String end = getEnd(line);
            if (match(end)) {
                model.getTypes().add(newType);
                return;
            }
        }
    }

    protected String getType(String line) {
        return getSingleToken(typePattern, line);
    }
    protected String getExtends(String line) {
        return getSingleToken(extendsPattern, line);
    }
    protected String getProperty(String line) {
        return getSingleToken(propertyPattern, line);
    }
    protected String getRelation(String line) {
        return getSingleToken(relationPattern, line);
    }
    protected String getLabel(String line) {
        return getSingleToken(labelPattern, line);
    }
    protected String getIcon(String line) {
        return getSingleToken(iconPattern, line);
    }
    protected boolean isPropertyNode(String line){
        return match(propertyNodePattern,line);
    }
    protected String getEnd(String line) {
        return get(endPattern, line);
    }

    protected boolean match(String s) {
        return s != null;
    }
    protected String getSingleToken(Pattern pattern, String line){
        Matcher m = pattern.matcher(line);
        if (m.matches())
            return m.group(1);
        else
            return null;
    }
    protected boolean match(Pattern pattern, String line){
        Matcher m = pattern.matcher(line);
        if (m.matches())
            return true;
        else
            return false;
    }
    
    protected String get(Pattern pattern, String line){
        if (pattern.matcher(line).matches())
            return OK;
        else
            return null;
    }
    
    static String OK = "";

    // Node Type definition
    Pattern typePattern = Pattern.compile(spnt + "type" + space + "(" + word + ")" + spnt + "\\{" + spnt);
    Pattern extendsPattern = Pattern.compile(spnt + "extends" + spnt + ":" + spnt + "(" + word + ")" + spnt);
    Pattern propertyPattern = Pattern.compile(spnt + "property" + spnt + ":" + spnt + "(" + word + ")" + spnt);
    Pattern relationPattern = Pattern.compile(spnt + "relation" + spnt + ":" + spnt + "(" + word + ")" + spnt);
    Pattern labelPattern = Pattern.compile(spnt + "label" + spnt + ":" + spnt + "(" + word + ")" + spnt);
    Pattern iconPattern = Pattern.compile(spnt + "icon" + spnt + ":" + spnt + "(" + path + ")" + spnt);
    Pattern propertyNodePattern = Pattern.compile(spnt + "propertynode" + spnt);
    
    Pattern endPattern = Pattern.compile(spnt + "\\}" + spnt);

    public void serialize(Collection<NodeType> types, StringBuilder sb) {
        for (NodeType nt : types) {
            sb.append("type " + nt.getName() + "{\n");
            for (String property : nt.getProperties()) {
                sb.append(" property: " + property + "\n");
            }

            for (String relation : nt.getRelations()) {
                sb.append(" relation: " + relation + "\n");
            }
            sb.append("}\n\n");
        }
    }
}
