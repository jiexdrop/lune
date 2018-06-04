package com.jiexdrop.lune.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.jiexdrop.lune.LuneGame;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.view.MainMenuRender;
import com.jiexdrop.lune.view.World;

public class KeyboardInput implements InputProcessor {

    private LuneGame game;

    private World world;

    private PerspectiveCamera debugCamera;

    public KeyboardInput(LuneGame game, World world, PerspectiveCamera debugCamera) {
        this.game = game;
        this.world = world;
        this.debugCamera = debugCamera;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode== Input.Keys.PLUS){
            GameVariables.CAMERA_ZOOM_POSITION-=5;
        }
        if(keycode== Input.Keys.MINUS){
            GameVariables.CAMERA_ZOOM_POSITION+=5;
        }

        if(keycode==Input.Keys.F) GameVariables.DEBUG = !GameVariables.DEBUG;

        if(keycode==Input.Keys.G) {
            if(GameVariables.GAMEMODE==0){
                GameVariables.GAMEMODE=1;
            } else {
                GameVariables.GAMEMODE=0;
            }
        }

        if(keycode==Input.Keys.ESCAPE) game.setScreen(new MainMenuRender(game));

        if(keycode==Input.Keys.H) {
            world.player.getPosition().set(debugCamera.position);

        }

        return false;
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
