package com.jiexdrop.lune.model.world;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseProxy;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btGhostPairCallback;
import com.badlogic.gdx.physics.bullet.collision.btPairCachingGhostObject;
import com.badlogic.gdx.physics.bullet.dynamics.btKinematicCharacterController;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.entity.Enemy;
import com.jiexdrop.lune.model.entity.Living;
import com.jiexdrop.lune.model.entity.Player;
import com.jiexdrop.lune.model.entity.routine.Repeat;
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

    public boolean init = true;

    public Player player;

    public Populater(GameResources gameResources) {
        this.gameResources = gameResources;
    }

    public void populate(World world) {
        if(init){
            addEntity(world, Vector3.Zero, EntityType.PLAYER);
            init = false;
        }

        if (world.entitiesViews.size() < GameVariables.NB_ENEMIES) {
            Vector3 position = new Vector3(Helpers.randomSpacing(GameVariables.ENEMIES_SPACING),
                    GameVariables.SPAWN_HEIGHT,
                    Helpers.randomSpacing(GameVariables.ENEMIES_SPACING));


           addEntity(world, position, EntityType.DUCK);

        }
    }

    public void addEntity(World world, Vector3 position, EntityType entityType) {
        Living living;

        switch (entityType) {
            case PLAYER:
                player = new Player();
                living = player;
                break;
            default:
                Enemy enemy = new Enemy(entityType.name(), position);
                Repeat repeat = new Repeat(new Wander(enemy));
                enemy.setRoutine(repeat);
                living = enemy;
                break;

        }





        EntityView entityView = new EntityView(gameResources.getModel(entityType));
        //world.entitiesBodies.put(entityView, body);

        world.entitiesViews.put(living, entityView);
    }


}
