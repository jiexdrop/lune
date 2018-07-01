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
import com.jiexdrop.lune.model.world.Terrain;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class VoxelMesh extends Mesh {
    FloatArray vertices = new FloatArray();
    ShortArray indices = new ShortArray();

    protected VoxelMesh(VertexData vertices, IndexData indices, boolean isVertexArray) {
        super(vertices, indices, isVertexArray);
    }

    public VoxelMesh(boolean isStatic, int maxVertices, int maxIndices, VertexAttribute... attributes) {
        super(isStatic, maxVertices, maxIndices, attributes);
    }

    public VoxelMesh(boolean isStatic, int maxVertices, int maxIndices, VertexAttributes attributes) {
        super(isStatic, maxVertices, maxIndices, attributes);
    }

    public VoxelMesh(boolean staticVertices, boolean staticIndices, int maxVertices, int maxIndices, VertexAttributes attributes) {
        super(staticVertices, staticIndices, maxVertices, maxIndices, attributes);
    }

    public VoxelMesh(VertexDataType type, boolean isStatic, int maxVertices, int maxIndices, VertexAttribute... attributes) {
        super(type, isStatic, maxVertices, maxIndices, attributes);
    }

    public VoxelMesh(VertexDataType type, boolean isStatic, int maxVertices, int maxIndices, VertexAttributes attributes) {
        super(type, isStatic, maxVertices, maxIndices, attributes);
    }

    public synchronized void update(World world, Terrain terrain, VoxelChunk chunk, GameResources gameResources) {



        chunk.calculateVertices(terrain, vertices, indices, gameResources);
        setVertices(vertices.toArray());
        setIndices(indices.toArray());

        if (getNumVertices() > 0 && getNumIndices() > 0) {
            world.removeGroundMesh(this);
            world.addGroundMesh(this, chunk.position);
        }


        vertices.clear();
        indices.clear();
        vertices.shrink();
        indices.shrink();



    }


}
