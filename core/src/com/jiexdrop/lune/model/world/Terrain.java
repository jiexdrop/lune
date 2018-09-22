package com.jiexdrop.lune.model.world;

import com.badlogic.gdx.math.Vector3;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.entity.Entity;
import com.jiexdrop.lune.view.ItemType;
import com.jiexdrop.lune.view.VoxelChunk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jiexdrop on 11/09/17.
 */

public class Terrain {

    public final Map<Vector3, VoxelChunk> chunks = new ConcurrentHashMap<Vector3, VoxelChunk>();
    public final Set<Vector3> dirty = new HashSet<Vector3>();
    public final Set<Vector3> indices = new HashSet<Vector3>();

    Generation generation = new Generation();

    public Vector3 activeChunk;

    public Terrain() {
        int drawChunks = 2;

        for (int i = -drawChunks; i < drawChunks; i++) {
            for (int j = -drawChunks; j < drawChunks; j++) {
                for (int k = -drawChunks; k < drawChunks; k++) {
                    indices.add(new Vector3(i, j, k));
                }
            }
        }
    }

    public void update(Vector3 pos) {
        activeChunk = Helpers.playerPosToChunkPos(pos);

        for (Vector3 v : indices) {
            generation.generateChunk(this, activeChunk.cpy().add(v.x, v.y, v.z));
        }
    }

    public void setVoxel(Vector3 pos, ItemType voxel) {
        Vector3 chunkPos = Helpers.playerPosToChunkPos(pos);
//        if(!chunks.containsKey(chunkPos)){  // Why ?
//            chunks.put(chunkPos, new VoxelChunk());
//        }
        int xP = (((int) pos.x % GameVariables.CHUNK_SIZE) + GameVariables.CHUNK_SIZE) % GameVariables.CHUNK_SIZE;
        int yP = (((int) pos.y % GameVariables.CHUNK_SIZE) + GameVariables.CHUNK_SIZE) % GameVariables.CHUNK_SIZE;
        int zP = (((int) pos.z % GameVariables.CHUNK_SIZE) + GameVariables.CHUNK_SIZE) % GameVariables.CHUNK_SIZE;
        chunks.get(chunkPos).set(xP, yP, zP, voxel);
        cleanNeighboors(chunkPos);
    }

    public void cleanNeighboors(Vector3 chunkPos){
        dirty.add(chunkPos);
        dirty.add(chunkPos.cpy().add(VoxelChunk.Face.TOP.pos));
        dirty.add(chunkPos.cpy().add(VoxelChunk.Face.BOTTOM.pos));
        dirty.add(chunkPos.cpy().add(VoxelChunk.Face.FRONT.pos));
        dirty.add(chunkPos.cpy().add(VoxelChunk.Face.BACK.pos));
        dirty.add(chunkPos.cpy().add(VoxelChunk.Face.LEFT.pos));
        dirty.add(chunkPos.cpy().add(VoxelChunk.Face.RIGHT.pos));
    }

    public ItemType getVoxel(Vector3 pos) {
        Vector3 chunkPos = Helpers.playerPosToChunkPos(pos);
        if (!chunks.containsKey(chunkPos)) {
            return null;
        }
        int xP = (((int) pos.x % GameVariables.CHUNK_SIZE) + GameVariables.CHUNK_SIZE) % GameVariables.CHUNK_SIZE;
        int yP = (((int) pos.y % GameVariables.CHUNK_SIZE) + GameVariables.CHUNK_SIZE) % GameVariables.CHUNK_SIZE;
        int zP = (((int) pos.z % GameVariables.CHUNK_SIZE) + GameVariables.CHUNK_SIZE) % GameVariables.CHUNK_SIZE;

        return chunks.get(chunkPos).get(xP, yP, zP);
    }

    public boolean chunkExists(Vector3 pos) {
        return chunks.containsKey(Helpers.playerPosToChunkPos(pos));
    }
}
