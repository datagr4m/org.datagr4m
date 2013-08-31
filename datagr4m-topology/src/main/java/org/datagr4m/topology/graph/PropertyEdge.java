package org.datagr4m.topology.graph;



public class PropertyEdge implements IPropertyEdge{
	public static final String UNTYPED = "untyped";
    protected String name;
    protected String typeName;
    
    public PropertyEdge() {
        this("", UNTYPED);
    }

    public PropertyEdge(String name) {
        this(name, UNTYPED);
    }

    public PropertyEdge(String name, String typeName) {
        super();
        this.name = name;
        this.typeName = typeName;
    }

    @Override
    public String toString(){
        return name;
    }
    
	@Override
	public String getTypeName() {
		return typeName;
	}
}
