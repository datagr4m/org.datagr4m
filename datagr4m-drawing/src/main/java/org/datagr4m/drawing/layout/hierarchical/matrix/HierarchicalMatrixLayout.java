package org.datagr4m.drawing.layout.hierarchical.matrix;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.layout.hierarchical.AbstractHierarchicalLayout;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;
import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.monitors.ITimeMonitor;
import org.datagr4m.monitors.TimeMonitor;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * La position de la matrice est la position du mod�le associ� � ce layout La position des cellules est donc relative �
 * la position du mod�le
 */
public class HierarchicalMatrixLayout extends AbstractHierarchicalLayout implements IHierarchicalMatrixLayout {
    private static final long serialVersionUID = 2988838649895956485L;

    protected int nLine;

    protected int nColumn;

    protected float[] lineHeight;

    protected float[] columnWidth;

    protected float allLineHeight;

    protected float allColumnWidth;

    protected IHierarchicalNodeModel model;

    protected BiMap<IBoundedItem, CellIndex> mapIndex;

    private ITimeMonitor timeMonitor;

    public HierarchicalMatrixLayout() {
        this(null);
    }

    public HierarchicalMatrixLayout(IHierarchicalNodeLayout parent) {
        super(parent);
        initMonitor();
        init();
    }

    private void initMonitor() {
        timeMonitor = new TimeMonitor(this);
    }
    
    @Override
    public ITimeMonitor getTimeMonitor() {
        return timeMonitor;
    }

    @Override
    public void setModel(IHierarchicalNodeModel model) {
        this.model = model;
    }

    @Override
    public IHierarchicalNodeModel getModel() {
        return model;
    }

    @Override
    public void setItemCell(IBoundedItem item, int lineIndex, int columnIndex) throws IllegalArgumentException {
        if (!model.hasChild(item))
            Logger.getLogger(HierarchicalMatrixLayout.class).error("Item " + item + " is not an IMMEDIATE child of current model " + model);
        // throw new IllegalArgumentException("Item " + item + " is not child of current model " + model);
        else
            mapIndex.put(item, new CellIndex(lineIndex, columnIndex));
    }

    protected void autoColumnGrid() {
        int k = 0;
        for (IBoundedItem i : getModel().getChildren()) {
            setItemCell(i, k, 0);
            k++;
        }
    }

    protected void autoLineGrid() {
        int k = 0;
        for (IBoundedItem i : getModel().getChildren()) {
            setItemCell(i, 0, k);
            k++;
        }
    }

    /********* DIMENSIONS **********/

    @Override
    public void setSize(int nLine, int nColumn) throws IllegalArgumentException {
        this.nLine = nLine;
        this.nColumn = nColumn;
        this.lineHeight = new float[nLine];
        this.columnWidth = new float[nColumn];
    }

    @Override
    public void setLineHeight(float height) {
        if (!isReady())
            throw new RuntimeException("Algorithm not ready. Missing some parameters. Maybe Size?");
        for (int i = 0; i < lineHeight.length; i++)
            setLineHeight(i, height);
        allLineHeight = height;
    }

    @Override
    public void setColumnWidth(float width) {
        for (int i = 0; i < columnWidth.length; i++)
            setColumnWidth(i, width);
        allColumnWidth = width;
    }

    // @Override
    protected void setLineHeight(int line, float height) {
        lineHeight[line] = height;
    }

    // @Override
    protected void setColumnWidth(int column, float width) {
        columnWidth[column] = width;
    }

    @Override
    public float getLineHeight(int line) {
        return lineHeight[line];
    }

    @Override
    public float getColumnWidth(int column) {
        return columnWidth[column];
    }

    /******************/

    @Override
    public void initAlgo() {
        if (isReady()) {
            super.initAlgo(); // handle children first
            computeLayout();
        } else
            throw new RuntimeException("Layout has not been completely set!");
    }

    @Override
    public void goAlgo() {
        if (isReady()) {
            super.goAlgo();
            computeLayout();
        } else
            throw new RuntimeException("Layout has not been completely set!");

        timeMonitor.stopMonitor();
    }

    /********* ACTUAL MATRIX LAYOUT ********/

    protected void computeLayout() {
        for (IBoundedItem item : mapIndex.keySet()) {
            CellIndex index = mapIndex.get(item);
            // System.out.println(item + " " + index);
            item.changePosition(getXPosition(index), getYPosition(index));
        }
    }

    protected float getXPosition(CellIndex index) {
        if (isEven(nColumn)) {
            // float middle = nColumn/2f;
            // return (index.getColumnIndex()-middle+0.5f) * allColumnWidth;
            float start = -(allColumnWidth / 2 + nColumn / 2);
            return index.getColumnIndex() * allColumnWidth + start;
        } else {
            int middle = (nColumn - 1) / 2;
            return (index.getColumnIndex() - middle) * allColumnWidth;
        }
    }

    protected float getYPosition(CellIndex index) {
        if (isEven(nLine)) {
            float start = -(allLineHeight / 2 + nLine / 2);
            return index.getLineIndex() * allLineHeight + start;
            // float middle = nLine/2f;
            // //(index.getLineIndex()-middle+0.5f) * allLineHeight;
        } else {
            int middle = (nLine - 1) / 2;
            return (index.getLineIndex() - middle) * allLineHeight;
        }
    }

    protected boolean isEven(int value) {
        return value % 2 == 0;
    }

    protected void autoRowSize() {
        float max = 0;
        for (IBoundedItem i : getModel().getChildren()) {
            RectangleBounds b = i.getRawCorridorRectangleBounds();
            float h = b.getHeight();
            if (h > max)
                max = h;
        }
        allLineHeight = max;
    }

    protected void autoColSize() {
        float max = 0;
        for (IBoundedItem i : getModel().getChildren()) {
            RectangleBounds b = i.getRawCorridorRectangleBounds();
            float w = b.getWidth();
            if (w > max)
                max = w;
        }
        allColumnWidth = max;
    }

    public boolean isReady() {
        return (nLine != -1 && nColumn != -1);
    }

    protected void init() {
        nLine = -1;
        nColumn = -1;
        lineHeight = null;
        columnWidth = null;
        mapIndex = HashBiMap.create();
    }
}
