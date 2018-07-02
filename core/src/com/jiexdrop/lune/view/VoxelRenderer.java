
package com.jiexdrop.lune.view;

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

public class VoxelRenderer implements RenderableProvider {

    public int renderedChunks;

    private final GameResources gameResources;

    public HashMap<Vector3, Material> debug = new HashMap<Vector3, Material>();

    private Material specialColor = Helpers.colorMaterial(Color.WHITE);

    private PerspectiveCamera camera;

    private final World world;

    private final Terrain terrain;

    public HashMap<Vector3, VoxelMesh> meshes;

    public VoxelRenderer(GameResources gameResources, World world, PerspectiveCamera camera) {
        this.gameResources = gameResources;
        this.world = world;
        this.terrain = world.terrain;
        this.camera = camera;
        this.meshes = world.meshes;
    }

    //        if (GameVariables.DEBUG) {
    //            debug.put(chunkPos, Helpers.randomColorMaterial());
    //        }

    public synchronized void update() {
        for (final Vector3 chunkPos : terrain.chunks.keySet()) {

            final VoxelMesh mesh = meshes.get(chunkPos);
            if (mesh == null) continue;
            if (isVisible(Helpers.chunkPosToPlayerPos(chunkPos))) {

                final VoxelChunk chunk = terrain.chunks.get(chunkPos);


                if (terrain.dirty.contains(chunkPos)) {
                    mesh.update(world, terrain, chunk, gameResources);
//TODO multithreads
//                Runnable runnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        mesh.update(world, terrain, chunk, gameResources);
//                   }
//                };
//
//                Helpers.executorService.submit(runnable);
                    terrain.dirty.remove(chunkPos);
                }
            } else {

                world.removeGroundMesh(meshes.get(chunkPos));
                meshes.get(chunkPos).dispose();
                meshes.remove(chunkPos);
                terrain.dirty.add(chunkPos);
            }

        }


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

                    meshes.put(chunkPos, addMesh);
                }

                final VoxelMesh mesh = meshes.get(chunkPos);
                if (mesh == null) continue;

                //final VoxelChunk chunk = terrain.chunks.get(chunkPos);

                totalBlocks += terrain.chunks.get(chunkPos).blockCounter;


                addMeshToRenderables(mesh, pool, chunkPos, renderables);

                totalVerts += mesh.getNumVertices();
                totalIndices += mesh.getNumIndices();
            }
        }

        GameVariables.RENDERED_CHUNKS = renderedChunks;
        GameVariables.RENDERED_VERTICES = totalVerts;
        GameVariables.RENDERED_INDICES = totalIndices;
        GameVariables.RENDERED_BLOCKS = totalBlocks;
        GameVariables.RENDERED_MESHES = meshes.size();
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
        // Maximum distance
        return Helpers.intersect(position, GameVariables.CAMERA_FAR / 2, camera.position, 1) && camera.frustum.sphereInFrustum(position.sub(-GameVariables.CHUNK_SIZE / 2f, -GameVariables.CHUNK_SIZE / 2f, -GameVariables.CHUNK_SIZE / 2f), GameVariables.CHUNK_SIZE);
    }


    public PerspectiveCamera getCamera() {
        return camera;
    }
}