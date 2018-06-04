package com.jiexdrop.lune.model.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.view.World;
import com.jiexdrop.lune.model.entity.routine.Routine;

/**
 *
 * Created by jiexdrop on 19/06/17.
 */

public abstract class Entity {

    protected String name;

    protected Vector3 velocity;
    protected Vector3 position;
    protected Vector3 acceleration;

    protected float angle;

    protected int size;

    protected int speed;
    protected Color color;

    protected Routine routine;

    protected float elapsedTime;



    public Entity(){
        this.position = new Vector3();
        this.velocity = new Vector3();
        this.acceleration = new Vector3();

        this.size = GameVariables.NORMAL_SIZE;
        this.speed = GameVariables.ENTITIES_SPEED;
    }

    public abstract boolean isAlive();

    public void setRoutine(Routine routine) {
        this.routine = routine;
    }

    public Routine getRoutine() {
        return routine;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getZ() {
        return position.z;
    }

    public abstract void update(World world);

    public Color getColor() {
        //return color.add(0, 0, 0, elapsedTime / 256); //BUGGY
        return color;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public String getName() {
        return name.toUpperCase();
    }

    public Vector3 getPosition() {
        return position;
    }

    public int getSize() {
        return size;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public float getAngle(){
        return angle;
    }

    public void applyForce(float x, float y, float z){
        acceleration.add(x, y, z);
    }

    public void move(float x, float y, float z){
        Vector3 sum = new Vector3(x, y, z);

        sum.rotate(angle, 0, 1, 0);

        Vector3 steer = sum.sub(getVelocity());
        steer.limit(getSpeed());
        applyForce(steer);
    }

    public void applyForce(Vector3 force){
        applyForce(force.x, force.y, force.z);
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public void setVelocity(float x, float y, float z) {
        velocity.set(x, y, z);
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
