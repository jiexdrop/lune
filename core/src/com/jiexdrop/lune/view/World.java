package com.jiexdrop.lune.view;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
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


    public final Terrain terrain;

    public void setVoxelRenderer(VoxelRenderer voxelRenderer) {
        this.voxelRenderer = voxelRenderer;
    }

    public VoxelRenderer voxelRenderer;

    public EntitiesRenderer entitiesRenderer;

    public final HashMap<Vector3, VoxelMesh> meshes = new HashMap<Vector3, VoxelMesh>();

    ArrayList<Entity> toRemove = new ArrayList<Entity>();

    GameResources gameResources;

    private Populater populater;

    public float deltaTime;

    public btDiscreteDynamicsWorld collisionsWorld;

    private btBroadphaseInterface broadphase;

    private btCollisionDispatcher dispatcher;

    private btDefaultCollisionConfiguration collisionConfig;

    private btSequentialImpulseConstraintSolver solver;

    private ModelBuilder modelBuilder;

    public ArrayList<btRigidBody.btRigidBodyConstructionInfo> constructions = new ArrayList<btRigidBody.btRigidBodyConstructionInfo>();

    private ArrayList<btCollisionShape> shapes = new ArrayList<btCollisionShape>();

    private ArrayList<btMotionState> states = new ArrayList<btMotionState>();

    private ArrayList<btRigidBody> bodies = new ArrayList<btRigidBody>();

    public HashMap<EntityView, btRigidBody> entitiesBodies = new HashMap<EntityView, btRigidBody>(); //TODO Not public

    public HashMap<btRigidBody, EntityView> bodiesEntities = new HashMap<btRigidBody, EntityView>();

    public HashMap<Entity, EntityView> entitiesViews = new HashMap<Entity, EntityView>();

    public btAxisSweep3 btSweep3;

    private HashMap<VoxelMesh, btRigidBody> groundMeshes = new HashMap<VoxelMesh, btRigidBody>();

    private WorldInternalTickCallback worldInternalTickCallback;

    public DebugDrawer debugDrawer;

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
            rayResultCallback = new ClosestRayResultCallback(Vector3.Zero, Vector3.Z);
        }


        populater = new Populater(gameResources);
        populater.populate(this);

        worldInternalTickCallback = new WorldInternalTickCallback(collisionsWorld);

        if (GameVariables.DEBUG && Gdx.app.getType() == Application.ApplicationType.Desktop) {
            collisionsWorld.setDebugDrawer(debugDrawer = new DebugDrawer());
        }

    }

    private Runnable updateTerrain = new Runnable() {
        @Override
        public void run() {
            terrain.update(populater.player.getPosition());
        }
    };

    public void update(float delta) {
        deltaTime = delta;
        Helpers.executorService.submit(updateTerrain);

        for (Map.Entry<Vector3, VoxelMesh> entry : voxelRenderer.meshes.entrySet()) {
            if (entry.getValue().toUpdate) {
                entry.getValue().update();
                if (entry.getValue().verticesCalculated && entry.getValue().getNumVertices() > 0 && entry.getValue().getNumIndices() > 0) {
                    removeGroundMesh(entry.getValue());
                    addGroundMesh(entry.getValue(), entry.getKey());
                }
            }
        }


        for (Entity e:toRemove) {


        }

        for (Entity e:entitiesViews.keySet()) {
            //e.update(this);
        }

        cleanFarTimer++;
        if (cleanFarTimer > 600 || GameVariables.TOTAL_MESHES > 96) {
            voxelRenderer.cleanFar();
            cleanFarTimer = 0;
        }

        populater.populate(this);

//
//        playerGhostObject.getWorldTransform().getTranslation(player.getPosition());
//        playerBody.getWorldTransform().setTranslation(player.getPosition().cpy());
        collisionsWorld.stepSimulation(delta, 5, GameVariables.TIME_STEP);
        collisionsWorld.performDiscreteCollisionDetection();

        GameVariables.COLLISION_MESHES = collisionsWorld.getNumCollisionObjects();
        GameVariables.ENTITIES = entitiesBodies.size();
    }

    public void movePlayer(Vector3 pos) {

    }

    public btRigidBody getRigidBody(Entity e){
        return entitiesBodies.get(entitiesViews.get(e));
    }



    public void moveEntity(Entity entity, float deltaTime, float x, float z) {
//        getKinematicBody(entity).setWalkDirection(new Vector3(x *deltaTime, 0, z*deltaTime));

    }

    public Player getPlayer() {
        return populater.player;
    }

    class WorldInternalTickCallback extends InternalTickCallback {

        WorldInternalTickCallback(btDynamicsWorld dynamicsWorld) {
            super(dynamicsWorld, true);
        }

        @Override
        public void onInternalTick(btDynamicsWorld dynamicsWorld, float timeStep) {

        }
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

    public void removeGroundMesh(VoxelMesh mesh) {
        btRigidBody collisionMesh = groundMeshes.get(mesh);
        if (collisionMesh != null && collisionMesh.isInWorld()) {
            collisionsWorld.removeRigidBody(collisionMesh);
            collisionMesh.dispose();

            //groundMeshes.remove(mesh);
        }
    }

    public void addPlayerMesh(PerspectiveCamera camera) {
//
//        playerGhostObject = new btPairCachingGhostObject();
//        Matrix4 matrix4 = new Matrix4().setToTranslation(camera.position);
//        playerGhostObject.setWorldTransform(matrix4);
//
//        btSweep3.getOverlappingPairCache().setInternalGhostPairCallback(new btGhostPairCallback());
//
//
//        playerShape = new btBoxShape(player.getSize());
//
//
//        playerGhostObject.setCollisionShape(playerShape);
//        playerGhostObject.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT);
//        characterController = new btKinematicCharacterController(playerGhostObject, playerShape, 1f);
//
//        characterController.setGravity(GameVariables.GRAVITY);
//
//        btBoxShape collisionShape = new btBoxShape(player.getSize());
//
//
//        btMotionState dynamicMotionState = new btDefaultMotionState();
//        dynamicMotionState.setWorldTransform(new Matrix4().setToTranslation(camera.position));
//        Vector3 dynamicInertia = new Vector3(0, 0, 0);
//
//        collisionShape.calculateLocalInertia(1f, dynamicInertia);
//
//
//        btRigidBody.btRigidBodyConstructionInfo dynamicConstructionInfo = new btRigidBody.btRigidBodyConstructionInfo(1f, dynamicMotionState, collisionShape, dynamicInertia);
//        constructions.add(dynamicConstructionInfo);
//
//        playerBody = new btRigidBody(dynamicConstructionInfo);
//
//        collisionsWorld.addCollisionObject(playerGhostObject,
//                (short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter,
//                (short) (btBroadphaseProxy.CollisionFilterGroups.StaticFilter | btBroadphaseProxy.CollisionFilterGroups.DefaultFilter));
//        collisionsWorld.addAction(characterController);
//
//        entitiesBodies.put(playerView, playerBody);
//        bodiesEntities.put(playerBody, playerView);
//        entitiesViews.put(player, playerView);
//
//        controllers.add(characterController);
    }



    private Vector3 tmp = new Vector3();
    private Vector3 tmp2 = new Vector3();
    private Vector3 tmp3 = new Vector3();
    private Vector3 rayFrom = new Vector3();
    private Vector3 rayTo = new Vector3();
    private ClosestRayResultCallback rayResultCallback;

    public void rayPick(int button){

        Ray pickRay = camera.getPickRay(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        rayFrom.set(pickRay.origin);
        rayTo.set(pickRay.direction.scl(5f).add(rayFrom));


        rayResultCallback.setCollisionObject(null);
        rayResultCallback.setClosestHitFraction(1f);
        rayResultCallback.setCollisionFilterGroup((short)btBroadphaseProxy.CollisionFilterGroups.CharacterFilter);


        rayResultCallback.setRayFromWorld(rayFrom);
        rayResultCallback.setRayToWorld(rayTo);

        collisionsWorld.rayTest(rayFrom, rayTo, rayResultCallback);

        if(rayResultCallback.hasHit()) {
            System.out.println("collisionObject:" + rayResultCallback.getCollisionObject()+ " "+rayResultCallback.getFlags());
            rayResultCallback.getHitPointWorld(tmp);
            rayResultCallback.getHitNormalWorld(tmp2);

            Vector3 resDel = new Vector3(
                    (float) Math.floor(tmp.x - tmp2.x/2),
                    (float) Math.floor(tmp.y - tmp2.y/2),
                    (float) Math.floor(tmp.z - tmp2.z/2));


            Vector3 resAdd = new Vector3(
                    (float) Math.floor(tmp.x + tmp2.x/2),
                    (float) Math.floor(tmp.y + tmp2.y/2),
                    (float) Math.floor(tmp.z + tmp2.z/2));

            if (button == Input.Buttons.LEFT){
                if(getPlayer().getInventory().getSelectedSlot().hasItem()){
                    terrain.setVoxel(resAdd, ItemType.valueOf(getPlayer().getInventory().getSelectedSlot().removeItem()));
                } else {
                    dropBlock(resDel.cpy(), terrain.getVoxel(resDel)); //TODO HIT or DropBlock
                    terrain.setVoxel(resDel.cpy(), ItemType.EMPTY);
                }

            }
            if (button == Input.Buttons.RIGHT){
                if (tmp.dst(camera.position) < 1.5f){
                    System.out.println(tmp.dst(camera.position));
                    return;
                }

                terrain.setVoxel(resAdd, ItemType.WALL);
            }
        }
    }

    public void dropBlock(Vector3 pos, ItemType itemType) {
        System.out.println(itemType);

        //TODO not stable

        if(itemType != null) {
            Entity e = new Item(itemType, pos);
            e.setRoutine(new ToInventory(getPlayer()));
            EntityView ev = new EntityView(gameResources.getMiniCubeModel(itemType));

            btBoxShape collisionShape = new btBoxShape(e.getSize());


            btMotionState dynamicMotionState = new btDefaultMotionState();
            dynamicMotionState.setWorldTransform(new Matrix4().setToTranslation(pos));
            Vector3 dynamicInertia = new Vector3(0, 0, 0);

            collisionShape.calculateLocalInertia(1f, dynamicInertia);


            btRigidBody.btRigidBodyConstructionInfo dynamicConstructionInfo = new btRigidBody.btRigidBodyConstructionInfo(1f, dynamicMotionState, collisionShape, dynamicInertia);
            constructions.add(dynamicConstructionInfo);

            btRigidBody body = new btRigidBody(dynamicConstructionInfo);

            body.setContactCallbackFlag(btBroadphaseProxy.CollisionFilterGroups.StaticFilter);
            body.setContactCallbackFilter(0);

            collisionsWorld.addRigidBody(body);

            entitiesBodies.put(ev, body);
            bodiesEntities.put(body, ev);
            entitiesViews.put(e, ev);

        }
    }


    public void removeEntity(Entity entity) {
        toRemove.add(entity);
    }


}
