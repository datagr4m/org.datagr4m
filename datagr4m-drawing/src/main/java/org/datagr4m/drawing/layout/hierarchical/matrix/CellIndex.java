package org.datagr4m.drawing.layout.hierarchical.matrix;

public class CellIndex {
    public CellIndex(int lineIndex, int columnIndex) {
        this.lineIndex = lineIndex;
        this.columnIndex = columnIndex;
    }
    
    public int getLineIndex() {
        return lineIndex;
    }
    
    public int getColumnIndex() {
        return columnIndex;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + columnIndex;
        result = prime * result + lineIndex;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CellIndex other = (CellIndex) obj;
        if (columnIndex != other.columnIndex)
            return false;
        if (lineIndex != other.lineIndex)
            return false;
        return true;
    }
    
    @Override
	public String toString(){
        return "CellIndex("+lineIndex+","+columnIndex+")";
    }

    protected int lineIndex;
    protected int columnIndex;
}
