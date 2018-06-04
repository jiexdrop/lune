package com.jiexdrop.lune.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.jiexdrop.lune.LuneGame;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.entity.container.Item;
import com.jiexdrop.lune.model.world.Helpers;
import com.jiexdrop.lune.model.world.Terrain;
import com.jiexdrop.lune.view.ItemType;
import com.jiexdrop.lune.view.MainMenuRender;
import com.jiexdrop.lune.view.World;

/**
 * World Input processor and Gesture Listener
 * Created by jiexdrop on 03/07/17.
 */

public class WorldInput implements GestureDetector.GestureListener, InputProcessor {

    private LuneGame game;

    private PerspectiveCamera camera;

    private World world;

    public WorldInput(LuneGame game, World world, PerspectiveCamera camera) {
        this.game = game;
        this.world = world;
        this.camera = camera;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        world.player.getInventory().update();
        Vector3 res = getBlockInSight(camera, world.terrain);
        if(res != null) {
            if(world.player.getInventory().getSelectedSlot().hasItem()){
                world.terrain.setVoxel(res.add(0,1,0).cpy(), ItemType.valueOf(world.player.getInventory().getSelectedSlot().removeItem()));
            } else {
                world.dropBlock(res, world.terrain.getVoxel(res));
                world.terrain.setVoxel(res, ItemType.EMPTY);
            }
        }

        return false;

    }

    Vector3 dir = new Vector3();
    private Vector3 getBlockInSight(PerspectiveCamera camera, Terrain terrain) {
        dir.set(camera.position);
        for (int i = 0; i < GameVariables.CAMERA_FAR; i++) {
            dir.x += camera.direction.x / 10;
            dir.y += camera.direction.y / 10;
            dir.z += camera.direction.z / 10;
            ItemType res = terrain.getVoxel(Helpers.floorPos(dir));
            if(res!=null) return Helpers.floorPos(dir);
        }
        return null;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {


        return false;
    }

    Vector3 tmp = new Vector3();

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        world.player.getInventory().update();
        float deltaX = -Gdx.input.getDeltaX(pointer) * 0.5f;
        float deltaY = -Gdx.input.getDeltaY(pointer) * 0.5f;

        camera.direction.rotate(camera.up, deltaX);
        tmp.set(camera.direction).crs(camera.up).nor();
        camera.direction.rotate(tmp, deltaY);
        Vector2 camAngle = new Vector2(camera.direction.z, camera.direction.x);
        //System.out.println(camAngle.angle());
        world.player.setAngle(camAngle.angle());
        return true;

    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
