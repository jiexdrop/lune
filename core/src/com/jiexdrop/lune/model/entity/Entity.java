package com.jiexdrop.lune.model.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.view.World;
import com.jiexdrop.lune.model.entity.routine.Routine;

/**
 *
 * Created by jiexdrop on 19/06/17.
 */

public abstract class Entity {

    protected String name;

    protected Vector3 size;

    protected int speed;
    protected Color color;

    protected Vector3 position;
    protected Quaternion rotation;

    protected Routine routine;

    protected float elapsedTime;


    public Entity(){
        this.size = GameVariables.DUCK_SIZE;
        this.position = Vector3.Zero;
        this.speed = GameVariables.ENTITIES_SPEED;
        this.rotation = new Quaternion();
    }

    public void setRoutine(Routine routine) {
        this.routine = routine;
    }

    public Routine getRoutine() {
        return routine;
    }

    public Vector3 getPosition(){
        return position;
    }

    public abstract void update(World world);

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name.toUpperCase();
    }

    public Vector3 getSize() {
        return size;
    }


    public float getAngle() {
        return rotation.getAngle();
    }
}
