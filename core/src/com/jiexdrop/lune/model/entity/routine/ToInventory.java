package com.jiexdrop.lune.model.entity.routine;

import com.badlogic.gdx.math.Vector3;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.entity.Entity;
import com.jiexdrop.lune.model.entity.Player;
import com.jiexdrop.lune.model.world.Helpers;
import com.jiexdrop.lune.view.World;

/**
 * Created by jiexdrop on 02/11/17.
 */

public class ToInventory extends Routine {

    MoveTo moveTo;
    Player player;

    private float distance;
    public ToInventory(Player player){
        start();
        moveTo = new MoveTo(player);
        this.player = player;
    }


    @Override
    public void act(Entity entity, World world) {
        if (!moveTo.isRunning()) {
            return;
        }

        this.moveTo.act(entity, world);

        if(moveTo.hasSucceeded()){
            succeed();
            world.getPlayer().getInventory().addItem(Helpers.toItem(entity));
            world.removeEntity(entity);
        }


    }

}
