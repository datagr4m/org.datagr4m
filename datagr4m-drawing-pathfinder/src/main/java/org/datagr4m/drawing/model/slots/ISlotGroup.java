/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.datagr4m.drawing.model.slots;

import java.util.Collection;

import com.google.common.collect.ArrayListMultimap;

/**
 *
 * @author jcr
 */
public interface ISlotGroup {
    Collection<SlotTarget> getSlotTargets();
    ArrayListMultimap<Integer,SlotTarget> getAllSlotTargets();

    boolean isEast();
    boolean isSouth();
    boolean isWest();
    boolean isNorth();

    void setSlotTargetAt(int i, final SlotTarget slotTarget);
    void clear();
}
