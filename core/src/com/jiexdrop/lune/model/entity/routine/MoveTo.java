package com.jiexdrop.lune.model.entity.routine;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.entity.Entity;
import com.jiexdrop.lune.view.World;

/**
 * Created by jiexdrop on 27/10/17.
 */

public class MoveTo extends Routine {

    float distance;

    Vector3 destination;
    Entity toEntity;


    public MoveTo(Vector3 destination) {
        start();

        this.destination = destination;
    }

    public MoveTo(Entity toEntity) {
        start();
        this.toEntity = toEntity;
        this.destination = toEntity.getPosition();
    }

    @Override
    public void act(Entity entity, World world) {
        if(toEntity!=null) {
            destination = toEntity.getPosition();
        }

        moveTo(entity, destination, world);

        distance = Vector2.dst(entity.getX(), entity.getZ(), destination.x, destination.z);

        if(distance < GameVariables.MAGNET_SIZE.x){
            succeed();
        }
    }

    void moveTo(Entity entity, Vector3 destination, World world){

        Vector3 direction = destination.cpy().sub(entity.getX(), entity.getY(), entity.getZ()).nor();

        world.terrain.moveEntity(entity, world.deltaTime, direction.x, direction.z);
    }
}
