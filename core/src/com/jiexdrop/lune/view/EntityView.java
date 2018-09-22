package com.jiexdrop.lune.view;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.math.Vector3;
import com.jiexdrop.lune.model.entity.Entity;

import java.util.ArrayList;

/**
 * Created by jiexdrop on 31/08/17.
 */

public class EntityView{

    public ArrayList<Renderable> renderables = new ArrayList<Renderable>();

    public EntityView(Model model){
        int i = 0;
        for (MeshPart m: model.meshParts) {
            Renderable r = new Renderable();
            r.meshPart.set(m);
            r.material= model.materials.get(i);
            renderables.add(r);
            i++;
        }

    }


}
