package com.jiexdrop.lune.model.world;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
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

        if (world.entitiesBodies.size() < GameVariables.NB_ENEMIES) {
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

        btBoxShape collisionShape = new btBoxShape(living.getSize());

        btMotionState dynamicMotionState = new btDefaultMotionState();
        dynamicMotionState.setWorldTransform(new Matrix4().setToTranslation(position));
        Vector3 dynamicInertia = new Vector3(0, 0, 0);

        collisionShape.calculateLocalInertia(1f, dynamicInertia);


        btRigidBody.btRigidBodyConstructionInfo dynamicConstructionInfo = new btRigidBody.btRigidBodyConstructionInfo(1f, dynamicMotionState, collisionShape, dynamicInertia);
        world.constructions.add(dynamicConstructionInfo);

        btRigidBody body = new btRigidBody(dynamicConstructionInfo);

        world.collisionsWorld.addRigidBody(body);


        EntityView entityView = new EntityView(gameResources.getModel(entityType));
        world.entitiesBodies.put(entityView, body);
        world.entitiesViews.put(living, entityView);
    }


}
