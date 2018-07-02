package com.jiexdrop.lune.model.world;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.entity.Enemy;
import com.jiexdrop.lune.model.entity.Entity;
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

    public Populater(GameResources gameResources) {
        this.gameResources = gameResources;
    }

    public void populate(World world) {
        if (world.entitiesBodies.size() < GameVariables.NB_ENEMIES) {
            Vector3 position = new Vector3(Helpers.randomSpacing(GameVariables.ENEMIES_SPACING),
                    GameVariables.SPAWN_HEIGHT,
                    Helpers.randomSpacing(GameVariables.ENEMIES_SPACING));


            addEntity(world, position);


        }
    }

    public void addEntity(World world, Vector3 position) {
        Enemy enemy = new Enemy("DUCK", position);
        Repeat repeat = new Repeat(new Wander(enemy));
        enemy.setRoutine(repeat);

        btBoxShape collisionShape = new btBoxShape(enemy.getSize());


        btMotionState dynamicMotionState = new btDefaultMotionState();
        dynamicMotionState.setWorldTransform(new Matrix4().setToTranslation(position));
        Vector3 dynamicInertia = new Vector3(0, 0, 0);

        collisionShape.calculateLocalInertia(1f, dynamicInertia);


        btRigidBody.btRigidBodyConstructionInfo dynamicConstructionInfo = new btRigidBody.btRigidBodyConstructionInfo(1f, dynamicMotionState, collisionShape, dynamicInertia);
        world.constructions.add(dynamicConstructionInfo);

        btRigidBody body = new btRigidBody(dynamicConstructionInfo);

//        body.setActivationState(4);
//        body.setContactProcessingThreshold(0.0f);
//        body.setRestitution(0);
//        body.setDamping(0.9f, 0.9f);
//        body.setLinearFactor(new Vector3(1, 1, 1));
//        body.setAngularFactor(Vector3.Zero);
//        body.setContactCallbackFlag(2);
//        body.setContactCallbackFilter(2);

        world.collisionsWorld.addRigidBody(body);

        EntityView entityView = new EntityView(gameResources.getModel(EntityType.DUCK));
        world.entitiesBodies.put(entityView, body);
        world.entitiesViews.put(enemy, entityView);
    }


}
