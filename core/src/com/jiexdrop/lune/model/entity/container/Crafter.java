package com.jiexdrop.lune.model.entity.container;

import com.jiexdrop.lune.GameVariables;

/**
 *
 * Created by jiexdrop on 06/08/17.
 */

public class Crafter extends Container {

    public Crafter(){
        super(GameVariables.PLAYER_CRAFT_ITEM_SLOTS);
    }

    @Override
    public void update(){
        setChanged();
        notifyObservers();

        craft();

        setChanged();
        notifyObservers();
    }

    private void craft(){
        ItemSlot craft = Recipe.craft(itemSlots);
        if(craft!=null) {
            use(craft);
        }
    }

    private void use(ItemSlot itemSlot){
        super.fill();

        itemSlots.put(GameVariables.PLAYER_CRAFT_ITEM_SLOTS-1, itemSlot);
    }

}
