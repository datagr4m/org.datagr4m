package org.datagr4m.drawing.model.items;

import java.util.HashMap;
import java.util.Map;


public class AvatarManager {
    public IBoundedItem createAvatar(IBoundedItem item){
        IBoundedItem avatar = ((BoundedItemIcon) item).clone();
        avatar.changePosition(item.getAbsolutePosition());
        avatarSource.put(avatar, item);
        return avatar;
    }
    
    public IBoundedItem getSource(IBoundedItem avatar){
        return avatarSource.get(avatar);
    }
    
    protected Map<IBoundedItem,IBoundedItem> avatarSource = new HashMap<IBoundedItem,IBoundedItem>();
}
