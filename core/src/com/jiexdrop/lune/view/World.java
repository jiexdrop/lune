package com.jiexdrop.lune.view;

import com.badlogic.gdx.math.Vector3;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.entity.Enemy;
import com.jiexdrop.lune.model.entity.Entity;
import com.jiexdrop.lune.model.entity.Player;
import com.jiexdrop.lune.model.entity.container.Item;
import com.jiexdrop.lune.model.entity.routine.ToInventory;
import com.jiexdrop.lune.model.world.Helpers;
import com.jiexdrop.lune.model.world.Populater;
import com.jiexdrop.lune.model.world.Terrain;

import java.util.ArrayList;

/**
 *
 * Created by jiexdrop on 14/08/17.
 */

public class World {

    public EntityView playerView;

    public Player player = new Player();

    public Terrain terrain = new Terrain();

    public VoxelRenderer voxelRenderer;

    public EntitiesRenderer entitiesRenderer;

    ArrayList<Entity> toRemove = new ArrayList<Entity>();

    GameResources gameResources;

    Populater populater;

    public float deltaTime;

    public World(GameResources gameResources, EntitiesRenderer entitiesRenderer, VoxelRenderer voxelRenderer) {
        this.entitiesRenderer = entitiesRenderer;
        this.voxelRenderer = voxelRenderer;
        this.gameResources = gameResources;

        playerView = new EntityView(gameResources.getModel(EntityType.PLAYER), player);

        populater = new Populater(gameResources);
        entitiesRenderer.entityViews.add(playerView);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            terrain.update(player.getPosition());
        }
    };

    public void update(float delta){
        deltaTime = delta;

        for (EntityView ev:entitiesRenderer.entityViews) {
            ev.entity.update(this);
        }

        for (Entity e: toRemove) {
            for (EntityView ev:entitiesRenderer.entityViews) {
                if (ev.entity.equals(e)) {
                    entitiesRenderer.entityViews.remove(ev);
                }
            }
        }

        Helpers.executorService.submit(runnable);


        populate();

        GameVariables.ENTITIES = entitiesRenderer.entityViews.size;
    }

    public void highlightBlock(Vector3 pos) {

    }

    public void dropBlock(Vector3 pos, ItemType itemType){
        //System.out.println(itemType);
        Entity e = new Item(itemType, pos.cpy().add(1f,1f,1f));
        e.setRoutine(new ToInventory(player));
        EntityView ev = new EntityView(gameResources.getMiniCubeModel(itemType),e);

        entitiesRenderer.entityViews.add(ev);
    }


    private void populate() {
        populater.populate(this);
    }


    public void removeEntity(Entity entity) {
        toRemove.add(entity);
    }
}
