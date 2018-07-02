package com.jiexdrop.lune.view;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.btAxisSweep3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseProxy;
import com.badlogic.gdx.physics.bullet.collision.btBvhTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btGhostPairCallback;
import com.badlogic.gdx.physics.bullet.collision.btPairCachingGhostObject;
import com.badlogic.gdx.physics.bullet.dynamics.InternalTickCallback;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btKinematicCharacterController;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.utils.Array;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.entity.Entity;
import com.jiexdrop.lune.model.entity.Player;
import com.jiexdrop.lune.model.entity.container.Item;
import com.jiexdrop.lune.model.entity.routine.ToInventory;
import com.jiexdrop.lune.model.world.Helpers;
import com.jiexdrop.lune.model.world.Populater;
import com.jiexdrop.lune.model.world.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by jiexdrop on 14/08/17.
 */

public class World {

    public EntityView playerView;

    public Player player;

    public final Terrain terrain;

    public void setVoxelRenderer(VoxelRenderer voxelRenderer) {
        this.voxelRenderer = voxelRenderer;
    }

    public VoxelRenderer voxelRenderer;

    public EntitiesRenderer entitiesRenderer;

    public final HashMap<Vector3, VoxelMesh> meshes = new HashMap<Vector3, VoxelMesh>();

    ArrayList<Entity> toRemove = new ArrayList<Entity>();

    GameResources gameResources;

    Populater populater;

    public float deltaTime;

    public btDiscreteDynamicsWorld collisionsWorld;

    private btBroadphaseInterface broadphase;

    private btCollisionDispatcher dispatcher;

    private btDefaultCollisionConfiguration collisionConfig;

    private btSequentialImpulseConstraintSolver solver;

    private ModelBuilder modelBuilder;

    private ArrayList<btRigidBody.btRigidBodyConstructionInfo> constructions = new ArrayList<btRigidBody.btRigidBodyConstructionInfo>();

    private ArrayList<btCollisionShape> shapes = new ArrayList<btCollisionShape>();

    private ArrayList<btMotionState> states = new ArrayList<btMotionState>();

    private ArrayList<btRigidBody> bodies = new ArrayList<btRigidBody>();

    private HashMap<EntityView, btRigidBody> entitiesBodies = new HashMap<EntityView, btRigidBody>();

    // Player

    private btPairCachingGhostObject playerGhostObject;

    private btBoxShape playerShape;

    private btKinematicCharacterController characterController;

    private Array<btKinematicCharacterController> controllers = new Array<btKinematicCharacterController>();

    private btAxisSweep3 btSweep3;

    private HashMap<VoxelMesh, btRigidBody> groundMeshes = new HashMap<VoxelMesh, btRigidBody>();


    private WorldInternalTickCallback worldInternalTickCallback;


    private DebugDrawer debugDrawer;

    private PerspectiveCamera camera;

    private int cleanFarTimer = 0;

    public World(GameResources gameResources, EntitiesRenderer entitiesRenderer, PerspectiveCamera camera) {
        this.entitiesRenderer = entitiesRenderer;
        this.gameResources = gameResources;
        this.camera = camera;

        terrain = new Terrain();

        if (collisionConfig == null || dispatcher == null || solver == null || collisionsWorld == null || meshes == null || worldInternalTickCallback == null) {
            Bullet.init(true, true);
            btSweep3 = new btAxisSweep3(new Vector3(-1000f, -1000f, -1000f), new Vector3(1000, 1000, 1000));
            modelBuilder = new ModelBuilder();
            collisionConfig = new btDefaultCollisionConfiguration();
            dispatcher = new btCollisionDispatcher(collisionConfig);
            broadphase = new btDbvtBroadphase();
            solver = new btSequentialImpulseConstraintSolver();
//            collisionsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfig);
            collisionsWorld = new btDiscreteDynamicsWorld(dispatcher, btSweep3, solver, collisionConfig);
            collisionsWorld.setGravity(GameVariables.GRAVITY);

        }

        player = new Player();
        playerView = new EntityView(gameResources.getModel(EntityType.PLAYER), player);

        populater = new Populater(gameResources);
        entitiesRenderer.entityViews.add(playerView);


        worldInternalTickCallback = new WorldInternalTickCallback(collisionsWorld);

        if (GameVariables.DEBUG && Gdx.app.getType() == Application.ApplicationType.Desktop) {
            collisionsWorld.setDebugDrawer(debugDrawer = new DebugDrawer());
        }


    }


    private Runnable updateTerrain = new Runnable() {
        @Override
        public void run() {
            terrain.update(player.getPosition());
        }
    };


    private Vector3 tmp = new Vector3();

    public void update(float delta) {
        deltaTime = delta;
        Helpers.executorService.submit(updateTerrain);

        for (Map.Entry<Vector3, VoxelMesh> entry : voxelRenderer.meshes.entrySet()) {
            if (entry.getValue().toUpdate) {
                entry.getValue().update();
                if(entry.getValue().verticesCalculated && entry.getValue().getNumVertices() > 0 && entry.getValue().getNumIndices() > 0){
                    removeGroundMesh(entry.getValue());
                    addGroundMesh(entry.getValue(), entry.getKey());
                }
            }
        }


        for (EntityView ev : entitiesRenderer.entityViews) {
            ev.entity.update(this);
        }

        for (Entity e : toRemove) {
            for (EntityView ev : entitiesRenderer.entityViews) {
                if (ev.entity.equals(e)) {
                    entitiesRenderer.entityViews.remove(ev);
                }
            }
        }




        cleanFarTimer++;
        if (cleanFarTimer > 600 || GameVariables.TOTAL_MESHES > 96) {
            voxelRenderer.cleanFar();
            cleanFarTimer = 0;
        }

        if (GameVariables.DEBUG && Gdx.app.getType() == Application.ApplicationType.Desktop) {
            Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
            debugDrawer.getShapeRenderer().setProjectionMatrix(camera.combined);
            debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawAabb);
            debugDrawer.begin(camera);
            collisionsWorld.debugDrawWorld();
            debugDrawer.end();
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        }

        populate();

//        for (Map.Entry<EntityView, btRigidBody> object : entitiesBodies.entrySet()) {
//            object.getValue().getWorldTransform().getTranslation(tmp);
//            object.getKey().entity.getPosition().set(tmp);
//
//            object.getValue().applyCentralImpulse(object.getKey().entity.getVelocity());
//        }

        playerGhostObject.getWorldTransform().getTranslation(player.getPosition());
        collisionsWorld.stepSimulation(delta, 5, GameVariables.TIME_STEP);
        collisionsWorld.performDiscreteCollisionDetection();

        GameVariables.COLLISION_MESHES = collisionsWorld.getNumCollisionObjects();
        GameVariables.ENTITIES = entitiesRenderer.entityViews.size;
    }


    public void movePlayer(float deltaTime, float x, float z) {
        Vector3 playerMovement = new Vector3(x, 0, -z);
        playerMovement.rotate(player.getAngle() - 90, 0, 1, 0); //FIXME
        playerMovement.x *= 0.1f;
        playerMovement.z *= 0.1f;
        characterController.setWalkDirection(playerMovement);
    }

    public void moveEntity(Entity entity, float deltaTime, float x, float z) {
        //characterController.setWalkDirection(new Vector3(x, 0, z));
    }

    class WorldInternalTickCallback extends InternalTickCallback {

        WorldInternalTickCallback(btDynamicsWorld dynamicsWorld) {
            super(dynamicsWorld, true);
        }

        @Override
        public void onInternalTick(btDynamicsWorld dynamicsWorld, float timeStep) {

        }
    }

    public void resetCleanFarTimer() {
        cleanFarTimer = 0;
    }


    public void addGroundMesh(VoxelMesh mesh, Vector3 position) {

        modelBuilder.begin();
        MeshPart part = modelBuilder.part(UUID.randomUUID().toString(), mesh, GL20.GL_TRIANGLES, null);
        modelBuilder.end();

        Array<MeshPart> meshPart = new Array<MeshPart>();
        meshPart.add(part);


        btBvhTriangleMeshShape btBvhTriangleMeshShape = new btBvhTriangleMeshShape(meshPart);
        shapes.add(btBvhTriangleMeshShape);

        btMotionState groundMotionState = new btDefaultMotionState();
        Matrix4 transform = new Matrix4().setToTranslation(Helpers.chunkPosToPlayerPos(position));
        //System.out.println(transform);
        groundMotionState.setWorldTransform(transform);
        states.add(groundMotionState);

        btRigidBody.btRigidBodyConstructionInfo groundBodyConstructionInfo = new btRigidBody.btRigidBodyConstructionInfo(0, groundMotionState, btBvhTriangleMeshShape, new Vector3(0, 0, 0));
        groundBodyConstructionInfo.setFriction(0);
        constructions.add(groundBodyConstructionInfo);

        btRigidBody meshRigidBody = new btRigidBody(groundBodyConstructionInfo);

        collisionsWorld.addRigidBody(meshRigidBody,
                (short) btBroadphaseProxy.CollisionFilterGroups.StaticFilter,
                (short) (btBroadphaseProxy.CollisionFilterGroups.CharacterFilter | btBroadphaseProxy.CollisionFilterGroups.DefaultFilter));

        groundMeshes.put(mesh, meshRigidBody);

    }

    public synchronized void removeGroundMesh(VoxelMesh mesh) {
        btRigidBody collisionMesh = groundMeshes.get(mesh);
        if (collisionMesh != null && collisionMesh.isInWorld()) {
            collisionsWorld.removeRigidBody(collisionMesh);
            collisionMesh.dispose();

            //groundMeshes.remove(mesh);
        }
    }

    public void addPlayerMesh(PerspectiveCamera camera) {

        playerGhostObject = new btPairCachingGhostObject();
        Matrix4 matrix4 = new Matrix4().setToTranslation(camera.position);
        playerGhostObject.setWorldTransform(matrix4);

        btSweep3.getOverlappingPairCache().setInternalGhostPairCallback(new btGhostPairCallback());


        playerShape = new btBoxShape(new Vector3(0.3f, 0.3f, 0.3f));


        playerGhostObject.setCollisionShape(playerShape);
        playerGhostObject.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT);
        characterController = new btKinematicCharacterController(playerGhostObject, playerShape, 1f);

        characterController.setGravity(GameVariables.GRAVITY);


        collisionsWorld.addCollisionObject(playerGhostObject,
                (short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter,
                (short) (btBroadphaseProxy.CollisionFilterGroups.StaticFilter | btBroadphaseProxy.CollisionFilterGroups.DefaultFilter));
        collisionsWorld.addAction(characterController);


        controllers.add(characterController);
    }


    public synchronized void dropBlock(Vector3 pos, ItemType itemType) {
        //System.out.println(itemType);
        Entity e = new Item(itemType, pos.cpy().add(0.25f, 0.25f, 0.25f));
        e.setRoutine(new ToInventory(player));
        EntityView ev = new EntityView(gameResources.getMiniCubeModel(itemType), e);

        entitiesRenderer.entityViews.add(ev);
    }


    private void populate() {
        populater.populate(this);
    }


    public void removeEntity(Entity entity) {
        toRemove.add(entity);
    }


}
