package org.datagr4m.drawing.layout.slots;

import java.io.Serializable;
import java.util.List;

import org.datagr4m.drawing.model.slots.ISlotableItem;
import org.datagr4m.drawing.model.slots.SlotGroup;
import org.datagr4m.drawing.model.slots.SlotSide;
import org.datagr4m.drawing.model.slots.SlotTarget;
import org.jzy3d.maths.Coord2d;


public interface ISlotGroupLayout extends Serializable {
    /** Utilis� � l'�tape 1: d�finir le meilleur c�t� pour placer le slot sur l'item SOURCE, pour atteindre l'item TARGET. */
    public SlotSide getTargetBestSlotSide(ISlotableItem source, ISlotableItem target);
    public SlotSide getTargetBestSlotSide(Coord2d c1, Coord2d c2) ;
    
    /** Utilis� � l'�tape 2 pour d�terminer le slot utilis� par chaque lien d'un slot group. */
    public int[] getSlotTargetBestId(ISlotableItem source, SlotGroup group, List<SlotTarget> targets);
    public int[] getTargetBestSlotId(ISlotableItem source, SlotGroup group, List<ISlotableItem> targets);
}
