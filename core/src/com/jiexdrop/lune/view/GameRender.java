package com.jiexdrop.lune.view;

import com.badlogic.gdx.Application;
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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectSet;
import com.jiexdrop.lune.LuneGame;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.controller.DebugInput;
import com.jiexdrop.lune.controller.KeyboardInput;
import com.jiexdrop.lune.controller.WorldInput;
import com.jiexdrop.lune.model.world.Helpers;
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

    private PerspectiveCamera actualCamera;

    private final World world;

    public EntitiesRenderer entitiesRenderer;

    private InputMultiplexer inputMultiplexer;

    public GameRender(LuneGame game){
        this.game = game;

        actualCamera = camera;

        world = new World(game.textures, entitiesRenderer,camera);

        voxelRenderer = new VoxelRenderer(game.textures, world, actualCamera);
        world.setVoxelRenderer(voxelRenderer);
        entitiesRenderer = new EntitiesRenderer(world, camera);
        worldInput = new WorldInput(game, world, actualCamera);
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

        //world.addPlayerMesh(actualCamera);
    }

    Vector2 converter = new Vector2();

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(GameVariables.WATER.r, GameVariables.WATER.g, GameVariables.WATER.b, GameVariables.WATER.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        voxelRenderer.update();

        world.update(Math.min(Gdx.graphics.getDeltaTime(), GameVariables.TIME_STEP));
        userInterface.act(Math.min(Gdx.graphics.getDeltaTime(), GameVariables.TIME_STEP));


        if(GameVariables.GAMEMODE==1) {
            if(inputMultiplexer.getProcessors().contains(worldInput, true)) {
                actualCamera = debugCamera;
                inputMultiplexer.removeProcessor(worldInput);
                inputMultiplexer.addProcessor(debugInput);
                actualCamera.position.set(world.getPlayer().getPosition().x, world.getPlayer().getPosition().y + 2, world.getPlayer().getPosition().z);
            }
            //world.player.getPosition().set(actualCamera.position.x, actualCamera.position.y  - world.player.getSize().y, actualCamera.position.z);
            //Vector2 camAngle = new Vector2(actualCamera.direction.x, actualCamera.direction.z);
            //world.player.setAngle(-camAngle.angle());

            debugInput.update(delta);
        } else {

            if(inputMultiplexer.getProcessors().contains(debugInput, true)) {
                actualCamera = camera;
                inputMultiplexer.addProcessor(worldInput);
                inputMultiplexer.removeProcessor(debugInput);
            }
            world.getPlayer().angle = - converter.set(actualCamera.direction.x, actualCamera.direction.z).angle() - 90; //TODO Not here
            keyboardInput.update(delta);
            actualCamera.position.set(world.getPlayer().getPosition().x, world.getPlayer().getPosition().y + 2, world.getPlayer().getPosition().z);
        }

        actualCamera.update();


        voxelBatch.begin(actualCamera);
        voxelBatch.render(voxelRenderer, environment);
        voxelBatch.end();

        entitiesBatch.begin(actualCamera);
        entitiesBatch.render(entitiesRenderer, environment);
        entitiesBatch.end();

        if (GameVariables.DEBUG && Gdx.app.getType() == Application.ApplicationType.Desktop && GameVariables.GAMEMODE==1) {
            Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
            world.debugDrawer.getShapeRenderer().setProjectionMatrix(debugCamera.combined);
            world.debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawAabb);
            world.debugDrawer.begin(debugCamera);
            world.collisionsWorld.debugDrawWorld();
            world.debugDrawer.end();
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
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

