package com.jiexdrop.lune.model.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;
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

    public Matrix4 transform;
    protected Vector3 position;
    public Vector3 origin;

    protected Quaternion rotation;

    protected Routine routine;

    protected float elapsedTime;

    public float angle;

    public Entity(){
        this.size = GameVariables.NORMAL_SIZE;
        this.transform = new Matrix4();
        this.position = new Vector3();
        this.rotation = new Quaternion();
        this.origin = new Vector3();
        this.speed = GameVariables.ENTITIES_SPEED;
    }

    public void setRoutine(Routine routine) {
        this.routine = routine;
    }

    public Routine getRoutine() {
        return routine;
    }

    public Vector3 getPosition(){
        return transform.getTranslation(position);
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

}
