package com.jiexdrop.lune.view;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Pool;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.entity.Player;

import java.util.Map;

/**
 * Created by jiexdrop on 01/09/17.
 */

public class EntitiesRenderer implements RenderableProvider {
    public int renderedEntities;

    public PerspectiveCamera camera;

    private World world;

    public EntitiesRenderer(World world, PerspectiveCamera camera) {
        this.world = world;
        this.camera = camera;
    }


    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        renderedEntities = 0;

        for (Map.Entry<EntityView, btRigidBody> object : world.entitiesBodies.entrySet()) {
            if (isVisible(object.getValue().getWorldTransform())) {
                for (Renderable r:object.getKey().renderables) {
                    Renderable renderable = pool.obtain();
                    renderable.set(r);
                    renderable.worldTransform.set(object.getValue().getWorldTransform());
                    renderables.add(renderable);
                }

                renderedEntities++;
            }
        }

        GameVariables.VISIBLE_ENTITIES = renderedEntities;
    }

    private Vector3 tmp = new Vector3();
    protected boolean isVisible(Matrix4 worldTransform) {
        worldTransform.getTranslation(tmp);
        return camera.frustum.sphereInFrustum(tmp, GameVariables.CHUNK_SIZE/2f);
    }

}
