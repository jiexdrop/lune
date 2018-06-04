package com.jiexdrop.lune.view;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Pool;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.entity.Player;

/**
 * Created by jiexdrop on 01/09/17.
 */

public class EntitiesRenderer implements RenderableProvider {
    public int renderedEntities;

    public ObjectSet<EntityView> entityViews = new ObjectSet<EntityView>();

    public PerspectiveCamera camera;

    public EntitiesRenderer(PerspectiveCamera camera) {
        this.camera = camera;
    }


    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        renderedEntities = 0;

        for (EntityView entityView:entityViews) {
            if (isVisible(entityView.entity.getPosition())) {

                for (Renderable r:entityView.renderables) {
                    Renderable renderable = pool.obtain();
                    renderable.set(r);
                    renderable.worldTransform.rotate(new Vector3(0, 1, 0), entityView.entity.getAngle() - 90);

                    renderable.worldTransform.setTranslation(entityView.entity.getPosition());
                    renderables.add(renderable);
                }

                renderedEntities++;
            }
        }

        GameVariables.VISIBLE_ENTITIES = renderedEntities;
    }

    protected boolean isVisible(Vector3 position) {
        return camera.frustum.sphereInFrustum(position, GameVariables.CHUNK_SIZE/2f);
    }

}
