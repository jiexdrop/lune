package com.jiexdrop.lune.model.entity.container;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.entity.Entity;
import com.jiexdrop.lune.view.ItemType;
import com.jiexdrop.lune.view.World;

/**
 *
 * Created by jiexdrop on 16/07/17.
 */

public class Item extends Entity {

    String description = "HELLO";

    public ItemType itemType;

    public Item() {
        this.name = GameVariables.EMPTY;
        this.color = Color.CLEAR;
    }

    public Item(Entity e) {
        super();
        position = e.getPosition().cpy();
        this.color = e.getColor().cpy();
        this.name = e.getName();

        this.size = GameVariables.ITEM_SIZE;
        this.speed = GameVariables.NORMAL_SPEED;
    }

    public Item(ItemType itemType) {
        super();
        position = new Vector3(0f,0f,0f);
        this.itemType = itemType;
        this.color = itemType.color.cpy();
        this.name = itemType.name();

        this.size = GameVariables.ITEM_SIZE;
        this.speed = GameVariables.NORMAL_SPEED;
    }

    public boolean equals(Item item) {
        return this.name.equals(item.name) && this.description.equals(item.description);
    }

    @Override
    public void update(World world) {
        if(routine !=null) {
            routine.act(this, world);
        }

    }

}
