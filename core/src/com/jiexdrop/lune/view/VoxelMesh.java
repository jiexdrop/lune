package com.jiexdrop.lune.view;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.IndexData;
import com.badlogic.gdx.graphics.glutils.VertexData;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.world.Helpers;
import com.jiexdrop.lune.model.world.Terrain;

import java.lang.reflect.Array;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class VoxelMesh extends Mesh {
    private FloatArray vertices = new FloatArray();
    private ShortArray indices = new ShortArray();


    public VoxelMesh(boolean isStatic, int maxVertices, int maxIndices, VertexAttribute... attributes) {
        super(isStatic, maxVertices, maxIndices, attributes);
    }

    public void calculateVertices(Terrain terrain, VoxelChunk chunk, GameResources gameResources) {
        chunk.calculateVertices(terrain, vertices, indices, gameResources);
        if (vertices.size > 0 && indices.size > 0) {
            setVertices(vertices.toArray());
            setIndices(indices.toArray());

            vertices.clear();
            indices.clear();
            vertices.shrink();
            indices.shrink();
        }
    }
}
