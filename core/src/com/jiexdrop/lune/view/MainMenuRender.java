package com.jiexdrop.lune.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.LuneGame;


/**
 * Created by jiexdrop on 25/08/17.
 */

public class MainMenuRender implements Screen {
    LuneGame main;

    GameRender gameRender;

    TextField textField;

    private Stage stage;

    public MainMenuRender(LuneGame main) {
        this.main = main;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        textField = new TextField(""+GameVariables.SEED, main.textures.getTextFieldStyle());
        table.add(textField).width(124).height(64);
        Button button = new Button( main.textures.getSlotButtonStyle());
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                init();
            }
        });

        table.add(button).width(64).height(64).row();
        stage.addActor(table);

    }

    public void init(){
        GameVariables.SEED = Integer.parseInt(textField.getText());
        gameRender = new GameRender(main);
        main.setScreen(gameRender);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
