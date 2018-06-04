package com.jiexdrop.lune.model.world;

import com.badlogic.gdx.math.Vector3;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.entity.Enemy;
import com.jiexdrop.lune.model.entity.Living;
import com.jiexdrop.lune.model.entity.routine.Repeat;
import com.jiexdrop.lune.model.entity.routine.Routine;
import com.jiexdrop.lune.model.entity.routine.Wander;
import com.jiexdrop.lune.view.EntityType;
import com.jiexdrop.lune.view.EntityView;
import com.jiexdrop.lune.view.GameResources;
import com.jiexdrop.lune.view.World;

/**
 * Created by jiexdrop on 05/11/17.
 */

public class Populater {
    GameResources gameResources;
    public Populater (GameResources gameResources){
        this.gameResources = gameResources;
    }

    public void populate(World world) {
        if(world.entitiesRenderer.entityViews.size < GameVariables.NB_ENEMIES){
            Vector3 position = new Vector3(Helpers.randomSpacing(GameVariables.ENEMIES_SPACING),
                    GameVariables.SPAWN_HEIGHT,
                    Helpers.randomSpacing(GameVariables.ENEMIES_SPACING));


            createEnemy(world, position);

            createLiving(world, position);

        }
    }

    public void createEnemy(World world, Vector3 position){
        Enemy enemy = new Enemy("DUCK", position);
        Repeat repeat = new Repeat(new Wander(enemy));
        enemy.setRoutine(repeat);
        world.entitiesRenderer.entityViews.add(new EntityView(gameResources.getModel(EntityType.DUCK), enemy));
    }

    public void createLiving(World world, Vector3 position){
        Living living = new Enemy("SLIME", position);
        world.entitiesRenderer.entityViews.add(new EntityView(gameResources.getModel(EntityType.SLIME), living));
    }
}
