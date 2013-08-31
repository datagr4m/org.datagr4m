package org.datagr4m.viewer.renderer.html;

import java.awt.geom.Point2D;
import java.util.List;

public class HtmlTable extends HtmlObject{
    public HtmlTable(List<String[]> lines, Point2D position, float width, float height) {
        super(Html.wrap(Html.table(lines)), position, width, height);
    }

    public HtmlTable(List<String[]> lines, int border, Point2D position, float width, float height) {
        super(Html.wrap(Html.table(lines, border)), position, width, height);
    }
}
