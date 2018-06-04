package com.jiexdrop.lune.model.entity.routine;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.entity.Entity;
import com.jiexdrop.lune.model.world.Helpers;
import com.jiexdrop.lune.view.GameResources;
import com.jiexdrop.lune.view.World;

import java.util.Random;

/**
 * Created by jiexdrop on 26/09/17.
 */

public class Wander extends Routine {


    Entity entity;
    MoveTo moveTo;

    public Wander(Entity entity){
        this.entity = entity;
        this.start();

    }

    @Override
    public void start() {
        super.start();
        moveTo = new MoveTo(new Vector3(entity.getX() + Helpers.randomSpacing(GameVariables.ENEMIES_SPACING),
                entity.getY(), entity.getZ() + Helpers.randomSpacing(GameVariables.ENEMIES_SPACING) ));
    }

    @Override
    public void act(Entity entity, World world) {
        if (!moveTo.isRunning()) {
            return;
        }

        if(!world.terrain.chunkExists(moveTo.destination)){
            start();
        }

        this.moveTo.act(entity, world);

        if (this.moveTo.hasSucceeded()) {
            succeed();


        } else if (this.moveTo.hasFailed()) {
            fail();
        }

    }

}
