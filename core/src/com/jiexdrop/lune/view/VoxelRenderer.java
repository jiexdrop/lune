
package com.jiexdrop.lune.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.world.Helpers;
import com.jiexdrop.lune.model.world.Terrain;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class VoxelRenderer implements RenderableProvider {

    public int renderedChunks;

    private final GameResources gameResources;

    public HashMap<Vector3, Material> debug = new HashMap<Vector3, Material>();

    private Material specialColor = Helpers.colorMaterial(Color.WHITE);

    private PerspectiveCamera camera;

    private final World world;

    private final Terrain terrain;

    public HashMap<Vector3, VoxelMesh> meshes;

    public HashMap<Vector3, VoxelMesh> permeables;


    public VoxelRenderer(GameResources gameResources, World world, PerspectiveCamera camera) {
        this.gameResources = gameResources;
        this.world = world;
        this.terrain = world.terrain;
        this.camera = camera;
        this.meshes = world.meshes;
        this.permeables = world.permeables;
    }

    //        if (GameVariables.DEBUG) {
    //            debug.put(chunkPos, HelpvoxelMeshers.randomColorMaterial());
    //        }

    public void update() {
        for (final Vector3 chunkPos : terrain.chunks.keySet()) {

            final VoxelMesh mesh = meshes.get(chunkPos);
            final VoxelMesh permeable = permeables.get(chunkPos);
            if (mesh == null) continue;
            if (permeable == null) continue;
            final VoxelChunk chunk = terrain.chunks.get(chunkPos);

            if (isVisible(Helpers.chunkPosToPlayerPos(chunkPos))) {
                if (terrain.dirty.contains(chunkPos)) {
                    mesh.calculateSolidVertices(terrain, chunk, gameResources);
                    permeable.calculatePermeableVertices(terrain, chunk, gameResources);
                    if (mesh.getNumIndices() > 0 && mesh.getNumVertices() > 0) {
                        world.removeGroundMesh(mesh);
                        world.addGroundMesh(mesh, chunkPos);
                    }
                    if (permeable.getNumIndices() > 0 && permeable.getNumVertices() > 0) {
                        world.removeGroundMesh(permeable);
                        world.addPermeableMesh(permeable, chunkPos);
                    }
                    
                    terrain.dirty.remove(chunkPos);
                }
            }

        }
    }

    public synchronized void cleanFar() {

        int cleaned_meshes = 0;
        for (final Vector3 chunkPos : terrain.chunks.keySet()) {
            final VoxelMesh mesh = meshes.get(chunkPos);
            if (mesh == null) continue;
            if (!isVisible(Helpers.chunkPosToPlayerPos(chunkPos))) {
                cleaned_meshes++;
                world.removeGroundMesh(mesh);
                mesh.dispose();
                meshes.remove(chunkPos);
                permeables.remove(chunkPos);
                terrain.dirty.add(chunkPos);
            }
        }
        GameVariables.CLEANED_MESHES = cleaned_meshes;

    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        renderedChunks = 0;
        int totalBlocks = 0;
        int totalVerts = 0;
        int totalIndices = 0;

        for (Vector3 chunkPos : terrain.chunks.keySet()) {
            if (isVisible(Helpers.chunkPosToPlayerPos(chunkPos))) {
                if (!meshes.containsKey(chunkPos)) {
                    VoxelMesh addMesh = new VoxelMesh(true, GameVariables.CHUNK_SIZE * GameVariables.CHUNK_SIZE * GameVariables.CHUNK_SIZE * 12 * 4, // 12 = Pos + Normal + tex + Color
                            GameVariables.CHUNK_SIZE * GameVariables.CHUNK_SIZE * GameVariables.CHUNK_SIZE * 36 / 3,
                            VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0), VertexAttribute.ColorUnpacked());
                    VoxelMesh addPermeables = new VoxelMesh(true, GameVariables.CHUNK_SIZE * GameVariables.CHUNK_SIZE * GameVariables.CHUNK_SIZE * 12 * 4, // 12 = Pos + Normal + tex + Color
                            GameVariables.CHUNK_SIZE * GameVariables.CHUNK_SIZE * GameVariables.CHUNK_SIZE * 36 / 3,
                            VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0), VertexAttribute.ColorUnpacked());

                    meshes.put(chunkPos, addMesh);
                    permeables.put(chunkPos, addPermeables);
                }

                final VoxelMesh mesh = meshes.get(chunkPos);
                final VoxelMesh permeable = permeables.get(chunkPos);
                if (mesh == null) continue;
                if (permeable == null) continue;

                //final VoxelChunk chunk = terrain.chunks.get(chunkPos);

                totalBlocks += terrain.chunks.get(chunkPos).blockCounter;


                addMeshToRenderables(mesh, pool, chunkPos, renderables);
                addMeshToRenderables(permeable, pool, chunkPos, renderables);

                totalVerts += mesh.getNumVertices();
                totalIndices += mesh.getNumIndices();
            }
        }

        GameVariables.RENDERED_CHUNKS = renderedChunks;
        GameVariables.RENDERED_VERTICES = totalVerts;
        GameVariables.RENDERED_INDICES = totalIndices;
        GameVariables.RENDERED_BLOCKS = totalBlocks;
        GameVariables.TOTAL_MESHES = meshes.size();
    }


    private void addMeshToRenderables(VoxelMesh mesh, Pool<Renderable> pool, Vector3 chunkPos, Array<Renderable> renderables) {
        if (mesh.getNumVertices() < 1 && mesh.getNumIndices() < 1) return;

        Renderable renderable = pool.obtain();
        renderable.material = gameResources.getMaterial();
        if (GameVariables.DEBUG) {
            if (debug.get(chunkPos) != null) {
                renderable.material = debug.get(chunkPos);
            } else {
                debug.put(chunkPos, Helpers.randomColorMaterial());
                renderable.material = debug.get(chunkPos);
            }
            if (chunkPos.x == terrain.activeChunk.x && chunkPos.z == terrain.activeChunk.z) {
                renderable.material = specialColor;
            }
        }


        renderable.meshPart.offset = 0;
        //renderable.meshPart.size = numVerts / 4 * 6;
        renderable.meshPart.size = mesh.getNumIndices();
        renderable.meshPart.primitiveType = GL20.GL_TRIANGLES;
        //renderable.meshPart.primitiveType = GL20.GL_LINES;
        renderable.meshPart.mesh = mesh;
        renderable.worldTransform.setTranslation(Helpers.chunkPosToPlayerPos(chunkPos));

        renderables.add(renderable);

        renderedChunks++;
    }

    protected boolean isVisible(Vector3 position) {
        return Helpers.intersect(camera.position, GameVariables.CAMERA_FAR / 2, position, 1) && camera.frustum.sphereInFrustum(position.sub(-GameVariables.CHUNK_SIZE / 2f, -GameVariables.CHUNK_SIZE / 2f, -GameVariables.CHUNK_SIZE / 2f), GameVariables.CHUNK_SIZE);
    }


}