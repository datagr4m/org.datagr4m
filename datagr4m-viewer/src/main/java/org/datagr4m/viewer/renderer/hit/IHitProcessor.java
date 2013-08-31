package org.datagr4m.viewer.renderer.hit;

import java.util.List;

import org.datagr4m.viewer.mouse.IClickableItem;


public interface IHitProcessor {
    public List<IClickableItem> hit(int x, int y);
    public <T> List<IClickableItem> hitOnly(int x, int y, Class<T> type);
    public <T> List<IClickableItem> hitExcluding(int x, int y, Class<T> type);
}
