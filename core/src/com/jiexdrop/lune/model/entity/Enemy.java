package com.jiexdrop.lune.model.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.view.EntityView;
import com.jiexdrop.lune.view.World;
import com.jiexdrop.lune.view.ItemType;

/**
 *
 * Created by jiexdrop on 04/07/17.
 */

public class Enemy extends Living {

    public Enemy(String name, Vector3 pos) {
        color = new Color(ItemType.valueOf(name.toUpperCase()).color);

        position = pos;

        this.name = name;

        this.size = GameVariables.ENEMY_SIZE;
    }

    @Override
    public void update(World world) {
        elapsedTime += world.deltaTime;

        if(routine !=null) {
            routine.act(this, world);
        }


    }

    @Override
    public void hit(Living l) {

    }
}
