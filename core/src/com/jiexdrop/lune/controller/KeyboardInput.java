package com.jiexdrop.lune.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.IntIntMap;
import com.jiexdrop.lune.LuneGame;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.view.MainMenuRender;
import com.jiexdrop.lune.view.World;

public class KeyboardInput implements InputProcessor {

    private LuneGame game;

    private World world;

    private PerspectiveCamera camera;
    private final IntIntMap keys = new IntIntMap();
    private int STRAFE_LEFT = Input.Keys.A;
    private int STRAFE_RIGHT = Input.Keys.E;
    private int FORWARD = Input.Keys.Z;
    private int BACKWARD = Input.Keys.S;
    private int LEFT = Input.Keys.Q;
    private int RIGHT = Input.Keys.D;
    private int REPOS_CAMERA = Input.Keys.H;
    private int EXIT = Input.Keys.ESCAPE;
    private int PLUS = Input.Keys.PLUS;
    private int MINUS = Input.Keys.MINUS;
    private int DEBUG = Input.Keys.F;
    private int GAMEMODE = Input.Keys.G;

    public KeyboardInput(LuneGame game, World world, PerspectiveCamera camera) {
        this.game = game;
        this.world = world;
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode) {

        keys.put(keycode, keycode);
        return false;
    }

    public void update(float deltaTime) {
        //When you move forward you go back because everything has to make sense
        if (keys.containsKey(FORWARD)) {
            world.moveEntity(world.getPlayer(), deltaTime, 0, -1, true);
        }
        if (keys.containsKey(BACKWARD)) {
            world.moveEntity(world.getPlayer(), deltaTime, 0, 1, true);
        }
        if (keys.containsKey(LEFT)) {
            world.moveEntity(world.getPlayer(), deltaTime, -1, 0, true);
        }
        if (keys.containsKey(RIGHT)) {
            world.moveEntity(world.getPlayer(), deltaTime, 1, 0, true);
        }
        if (keys.containsKey(STRAFE_LEFT)) {
            world.moveEntity(world.getPlayer(), deltaTime, -1, -1, true);
        }
        if (keys.containsKey(STRAFE_RIGHT)) {
            world.moveEntity(world.getPlayer(), deltaTime, 1, -1, true);
        }

        if(keys.size == 0){
            world.moveEntity(world.getPlayer(), deltaTime, 0, 0, true);
        }

    }

    @Override
    public boolean keyUp(int keycode) {

        if(keys.containsKey(PLUS)){
            GameVariables.CAMERA_ZOOM_POSITION-=5;
        }
        if(keys.containsKey(MINUS)){
            GameVariables.CAMERA_ZOOM_POSITION+=5;
        }

        if(keys.containsKey(DEBUG)) GameVariables.DEBUG = !GameVariables.DEBUG;

        if(keys.containsKey(GAMEMODE)) {
            if(GameVariables.GAMEMODE==0){
                GameVariables.GAMEMODE=1;
            } else {
                GameVariables.GAMEMODE=0;
            }
        }

        if(keys.containsKey(EXIT)) game.setScreen(new MainMenuRender(game));

        if(keys.containsKey(REPOS_CAMERA)) {
            world.movePlayer(camera.position);
            System.out.println("Player pos set to debug camera");
        }

        keys.remove(keycode, 0);
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
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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

}
