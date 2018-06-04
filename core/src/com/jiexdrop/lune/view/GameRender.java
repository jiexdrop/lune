package com.jiexdrop.lune.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectSet;
import com.jiexdrop.lune.LuneGame;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.controller.DebugInput;
import com.jiexdrop.lune.controller.KeyboardInput;
import com.jiexdrop.lune.controller.WorldInput;
import com.jiexdrop.lune.view.ui.UserInterface;

import java.nio.FloatBuffer;

/**
 * Renders the entities
 * Created by jiexdrop on 14/06/17.
 */

public class GameRender implements Screen {

    private ModelBatch voxelBatch = new ModelBatch();

    private ModelBatch entitiesBatch = new ModelBatch();

    private Environment environment = new Environment();

    private SpriteBatch batch = new SpriteBatch();

    private VoxelRenderer voxelRenderer;

    private PerspectiveCamera camera = Camera.setupCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

    private PerspectiveCamera debugCamera = Camera.setupCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

    private DebugInput debugInput = new DebugInput(debugCamera);

    private LuneGame game;

    private WorldInput worldInput;

    private KeyboardInput keyboardInput;

    private UserInterface userInterface;

    private World world;

    public EntitiesRenderer entitiesRenderer = new EntitiesRenderer(camera);

    private InputMultiplexer inputMultiplexer;

    public GameRender(LuneGame game){
        this.game = game;
        world = new World(game.textures, entitiesRenderer, voxelRenderer);

        voxelRenderer = new VoxelRenderer(game.textures, world.terrain, camera);

        worldInput = new WorldInput(game, world, camera);
        keyboardInput = new KeyboardInput(game, world, debugCamera);

        userInterface = new UserInterface(world, game.textures, batch);

        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        //environment.set(new ColorAttribute(ColorAttribute.Fog, GameVariables.WATER));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1.0f, -0.8f, -0.2f));

        debugCamera.far = GameVariables.DEBUG_CAMERA_FAR;


        GestureDetector gestureDetector = new GestureDetector(worldInput);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(userInterface);
        inputMultiplexer.addProcessor(gestureDetector);
        inputMultiplexer.addProcessor(debugInput);
        inputMultiplexer.addProcessor(keyboardInput);


        Gdx.input.setInputProcessor(inputMultiplexer);


    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(GameVariables.WATER.r, GameVariables.WATER.g, GameVariables.WATER.b, GameVariables.WATER.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT) ;


        world.update(Math.min(Gdx.graphics.getDeltaTime(), GameVariables.TIME_STEP));
        userInterface.act(Math.min(Gdx.graphics.getDeltaTime(), GameVariables.TIME_STEP));

        camera.position.set(world.player.getX(), world.player.getY() + world.player.getSize(), world.player.getZ());
        camera.update();

        voxelRenderer.update();

        if(GameVariables.GAMEMODE==1) {
            if(inputMultiplexer.getProcessors().contains(worldInput, true)) {
                inputMultiplexer.removeProcessor(worldInput);
                inputMultiplexer.addProcessor(debugInput);
                debugCamera.position.set(world.player.getX(), world.player.getY(), world.player.getZ());
            }
            debugCamera.update();

            world.player.getPosition().set(debugCamera.position);
            camera.direction.set(debugCamera.direction);
            debugInput.update(delta);
            voxelBatch.begin(debugCamera);
            voxelBatch.render(voxelRenderer, environment);
            voxelBatch.end();

//            entitiesBatch.begin(debugCamera);
//            entitiesBatch.render(entitiesRenderer, environment);
//            entitiesBatch.end();
        } else {
            if(inputMultiplexer.getProcessors().contains(debugInput, true)) {
                inputMultiplexer.addProcessor(worldInput);
                inputMultiplexer.removeProcessor(debugInput);
            }

            voxelBatch.begin(camera);
            voxelBatch.render(voxelRenderer, environment);
            voxelBatch.end();

            entitiesBatch.begin(camera);
            entitiesBatch.render(entitiesRenderer, environment);
            entitiesBatch.end();
        }


        userInterface.draw();



        GameVariables.ENTITIES = 0;
        GameVariables.VISIBLE_ENTITIES = 0;
    }



    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        debugCamera.viewportHeight = height;
        debugCamera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.viewportWidth = width;


        userInterface.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        userInterface.dispose();
    }

    @Override
    public void dispose() {

    }


}

