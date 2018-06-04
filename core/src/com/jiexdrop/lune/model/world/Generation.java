package com.jiexdrop.lune.model.world;

import com.badlogic.gdx.math.Vector3;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.view.ItemType;
import com.jiexdrop.lune.view.VoxelChunk;

import java.util.HashMap;

/**
 * Generate strucures with mobs
 * Created by jiexdrop on 16/09/17.
 */

public class Generation {


    public enum StructureType {
        PLATFORM(16), TREE(3), GRASS_PATCH(3), DUNGEON(16);

        public final int size;

        StructureType(int size) {
            this.size = size;
        }
    }

    public class StructureChunk {
        public HashMap<Vector3, StructureType> structures = new HashMap<Vector3, StructureType>();

        public boolean addStructure(Vector3 pos, StructureType structureType) {
            if (canAddStructure(pos, structureType)) {
                structures.put(pos, structureType);
                return true;
            }
            return false;
        }

        private boolean canAddStructure(Vector3 pos, StructureType structureType) {
            if (sizeInBounds(pos, structureType)) {
                for (Vector3 vector3 : structures.keySet()) {
                    if (Helpers.intersect(vector3, structures.get(vector3).size,
                            pos, structureType.size)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        private boolean sizeInBounds(Vector3 pos, StructureType structureType) {
            return pos.x > structureType.size
                    && pos.z > structureType.size
                    && pos.x + structureType.size <= GameVariables.CHUNK_SIZE
                    && pos.z + structureType.size <= GameVariables.CHUNK_SIZE;
        }
    }

    public final HashMap<Vector3, ItemType> tree = new HashMap<Vector3, ItemType>();
    public final HashMap<Vector3, ItemType> dungeon = new HashMap<Vector3, ItemType>();
    public final HashMap<Vector3, ItemType> platform = new HashMap<Vector3, ItemType>();
    public final HashMap<Vector3, ItemType> grass_patch = new HashMap<Vector3, ItemType>();
    public HashMap<Vector3, StructureChunk> structureChunks = new HashMap<Vector3, StructureChunk>();


    public Generation() {
        createTree();
        createDungeon();
        createPlatform();
    }


    public void generateChunk(Terrain terrain, Vector3 chunkPos) {
        VoxelChunk voxelChunk = terrain.chunks.get(chunkPos);
        if (voxelChunk == null) {
            voxelChunk = new VoxelChunk(chunkPos);
            terrain.chunks.put(chunkPos, voxelChunk);
            structureChunks.put(chunkPos, new StructureChunk());

            for (int i = 0; i < GameVariables.CHUNK_SIZE; i++) {
                for (int j = 0; j < GameVariables.CHUNK_SIZE; j++) {
                    for (int k = 0; k < GameVariables.CHUNK_SIZE; k++) {
                        double sX = ((chunkPos.x * GameVariables.CHUNK_SIZE) + i) * GameVariables.TERRAIN_FREQUENCY;
                        double sY = ((chunkPos.y * GameVariables.CHUNK_SIZE) + j) * GameVariables.TERRAIN_FREQUENCY;
                        double sZ = ((chunkPos.z * GameVariables.CHUNK_SIZE) + k) * GameVariables.TERRAIN_FREQUENCY;
                        double s = SimplexNoise.noise(sX, sY, sZ) * 10;

                        if (s < 1) {
                            voxelChunk.set(i, j, k, ItemType.GRASS);
                        }

                    }
                }
            }

            terrain.cleanNeighboors(chunkPos);
        }

    }

    private void generateStructure(VoxelChunk chunk, int x, int y, int z, StructureType structureType) {
        HashMap<Vector3, ItemType> stuct = getStructure(structureType);
        for (Vector3 v : stuct.keySet()) {
            chunk.set((int) v.x + x, (int) v.y + y, (int) v.z + z, stuct.get(v));
        }
    }

    private HashMap<Vector3, ItemType> getStructure(StructureType structureType) {
        switch (structureType) {
            case TREE:
                return tree;
            case DUNGEON:
                return dungeon;
            case PLATFORM:
                return platform;
            default:
                return tree;
        }
    }

    private void genColumnChunk(VoxelChunk chunk, int x, int h, int z, ItemType item) {
        for (int i = 0; i < h; i++) {
            chunk.set(x, i, z, item);
        }
    }

    private void createPlatform() {
        int width = 16;

        createCube(platform, width, 1, width, 0, 2, 0, ItemType.LOG);
    }

    private void createDungeon() {
        int height = 3;
        int width = 6;

        //WALLS
        createCube(dungeon, width, height, 1, 0, 0, 0, ItemType.LOG);
        createCube(dungeon, 1, height, width, 0, 0, 0, ItemType.LOG);
        createCube(dungeon, 1, height, width, width, 0, 0, ItemType.LOG);
        createCube(dungeon, width + 1, height, 1, 0, 0, width, ItemType.LOG);
        //ROOF
        createCube(dungeon, width + 3, 1, width + 3, -1, height, -1, ItemType.WALL);
        //DOORS
        createCube(dungeon, 2, 2, 1, width / 2, 0, width, ItemType.EMPTY);
    }


    private void createCube(HashMap<Vector3, ItemType> res, int w, int h, int d, int x, int y, int z, ItemType it) {
        for (int i = x; i < w + x; i++) {
            for (int j = y; j < h + y; j++) {
                for (int k = z; k < d + z; k++) {
                    if (it.equals(ItemType.EMPTY)) {
                        res.remove(new Vector3(i, j, k));
                    } else {
                        res.put(new Vector3(i, j, k), it);
                    }
                }
            }
        }

    }

    private void createTree() {
        int height = 4;
        for (int i = 0; i < height; i++) {
            tree.put(new Vector3(0, i, 0), ItemType.LOG);
        }
        createCube(tree, 3, 3, 3, -1, height - 2, -1, ItemType.LEAVES);
    }

}
