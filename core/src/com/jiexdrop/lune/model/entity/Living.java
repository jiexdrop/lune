package com.jiexdrop.lune.model.entity;

import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.view.ItemType;

/**
 * Created by jiexdrop on 20/07/17.
 */

public abstract class Living extends Entity {
    //RPG
    int health = GameVariables.NORMAL_HEALTH;

    int satiety = GameVariables.MAX_SATIETY;

    int strength = GameVariables.NORMAL_STRENGTH;


    public Living(){
        super();
    }


    @Override
    public boolean isAlive(){
        return health>0;
    }

    public abstract void hit(Living e);

    public void eat(ItemType selectedItem) {
        satiety += selectedItem.nourishment;
    }
}
