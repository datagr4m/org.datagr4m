package org.datagr4m.drawing.layout.slots.geometry.global;

import org.datagr4m.drawing.layout.slots.geometry.ISlotGeometryBuilder;

public interface IGlobalSlotGeometryBuilder extends ISlotGeometryBuilder{
    public ISlotGeometryBuilder getDelegate();
    public void setDelegate(ISlotGeometryBuilder builder);
}
