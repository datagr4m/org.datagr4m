package org.datagr4m.drawing.renderer;

import java.util.List;

import org.datagr4m.viewer.renderer.DifferedRenderer;


public interface IHasDifferedRenderingSupport {
    public void clearDiffered();
    public List<DifferedRenderer> getDiffered();
    public void addDiffered(List<DifferedRenderer> differed);
    public void addDiffered(DifferedRenderer differed);
}
