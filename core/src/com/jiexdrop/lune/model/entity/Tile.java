package com.jiexdrop.lune.model.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.view.ItemType;
import com.jiexdrop.lune.view.World;

/**
 * Created by jiexdrop on 27/07/17.
 */

public class Tile extends Entity {

    public Tile(String name, Vector3 pos) {
        color = new Color(ItemType.valueOf(name.toUpperCase()).color);

        position = pos;

        this.name = name;

        this.size = GameVariables.TILES_SIZE;
    }

    @Override
    public void update(World world) {

    }
}
