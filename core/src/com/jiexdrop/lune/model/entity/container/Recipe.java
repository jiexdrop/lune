package com.jiexdrop.lune.model.entity.container;

import com.jiexdrop.lune.view.ItemType;
import java.util.HashMap;

/**
 * How do you make jelly ?
 * you use slime and sugar
 * or fruit and sugar
 * Created by jiexdrop on 07/08/17.
 */

public class Recipe {
    int duration;

    static ItemType[] STICK_TYPES = { ItemType.LOG, ItemType.BRANCHES };
    static ItemType[] EMPTY = { ItemType.EMPTY };

    static HashMap<Integer,ItemType[]> LOG_RECIPE = new HashMap<Integer, ItemType[]>(){
        {
            put(0, STICK_TYPES);
            put(1, EMPTY);
            put(2, STICK_TYPES);
        }
    };

    public static ItemSlot craft(HashMap<Integer, ItemSlot> itemSlots) {
        boolean[] craft = {false, false, false};

        int quantity = 16;
        for (int i = 0; i < LOG_RECIPE.size(); i++) {
            for (int j = 0; j < LOG_RECIPE.get(i).length; j++) {
                if(LOG_RECIPE.get(i)[j].name().toUpperCase().equals(itemSlots.get(i).getItemName())){
                    craft[i] = true;
                }
            }
        }

        if(validCraft(craft))
            return new ItemSlot(new Item(ItemType.WALL), quantity);

        return null;
    }

    public static boolean validCraft(boolean[] craft){
        for (int i = 0; i < craft.length; i++) {
            if(!craft[i]) return false;
        }
        return true;
    }
}
