/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.jiexdrop.lune.view;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.world.Terrain;

public class VoxelChunk {
    public static int VERTEX_SIZE = 12;
    public final ItemType[][][] voxels = new ItemType[GameVariables.CHUNK_SIZE][GameVariables.CHUNK_SIZE][GameVariables.CHUNK_SIZE];
    public int blockCounter = 0;
    public final Vector3 position;

    public enum Face {
        TOP(0, new Vector3(0,1,0)),
        BOTTOM(1, new Vector3(0,-1,0)),
        LEFT(2, new Vector3(-1,0,0)),
        RIGHT(3, new Vector3(1,0,0)),
        FRONT(4, new Vector3(0,0,-1)),
        BACK(5, new Vector3(0,0,1));

        public final int id;
        public final Vector3 pos;

        Face(int id, Vector3 pos) {
            this.id = id;
            this.pos = pos;
        }
    }

    public VoxelChunk (Vector3 position) {
        this.position = position;
    }


    public void set (int x, int y, int z, ItemType itemType) {
        voxels[x][y][z] = itemType;
        if(itemType.equals(ItemType.EMPTY)){
            voxels[x][y][z] = null;
        }
    }

    public ItemType get(int x, int y, int z) {
        return voxels[x][y][z];
    }


    /**
     * Creates a mesh out of the chunk, returning the number of indices produced
     * @param vertices
     * @param indices
     * @param gameResources
     * @return the number of vertices produced
     */
    public void calculateVertices(Terrain terrain, FloatArray vertices, ShortArray indices, GameResources gameResources) {
        for (int i = 0; i < GameVariables.CHUNK_SIZE; i++) {
            for (int j = 0; j <GameVariables.CHUNK_SIZE; j++) {
                for (int k = 0; k < GameVariables.CHUNK_SIZE; k++) {
                    ItemType itemType = voxels[i][j][k];
                    if (itemType==null || itemType.equals(ItemType.EMPTY)) continue;

                    switch (itemType){
                        case WEEDS:
                            generateVegetation(gameResources, i, j, k, itemType, vertices, indices);
                            break;
                        case CACTUS:
                            generateVegetation(gameResources, i, j, k, itemType, vertices, indices);
                            break;
                        default:
                            generateBlock(gameResources, i, j, k, itemType, vertices, indices, terrain);
                            break;
                    }

                    blockCounter++;
                }
            }
        }
    }


    private void generateBlock(GameResources gameResources, int x, int y, int z, ItemType itemType, FloatArray vertices, ShortArray indices, Terrain terrain) {
        int vertexOffset = vertices.size / VERTEX_SIZE;
        if(faceToCreate(terrain, x, y+1, z, Face.TOP)) {
            indices.addAll((short) (vertexOffset), (short) (1 + vertexOffset), (short) (2 + vertexOffset), (short) (2 + vertexOffset), (short) (3 + vertexOffset), (short) (vertexOffset));
            createTop(itemType, gameResources.getTextureRegion(itemType), x, y, z, vertices);
        }

        vertexOffset = vertices.size / VERTEX_SIZE;
        if(faceToCreate(terrain, x, y-1, z, Face.BOTTOM)) {
            indices.addAll((short) (vertexOffset), (short) (1 + vertexOffset), (short) (2 + vertexOffset), (short) (2 + vertexOffset), (short) (3 + vertexOffset), (short) (vertexOffset));
            createBottom(itemType, gameResources.getTextureRegion(itemType), x, y, z, vertices);
        }

        vertexOffset = vertices.size / VERTEX_SIZE;
        if(faceToCreate(terrain, x-1, y, z, Face.LEFT)) {
            indices.addAll((short) (vertexOffset), (short) (1 + vertexOffset), (short) (2 + vertexOffset), (short) (2 + vertexOffset), (short) (3 + vertexOffset), (short) (vertexOffset));
            createLeft(itemType, gameResources.getTextureRegion(itemType), x, y, z, vertices);
        }

        vertexOffset = vertices.size / VERTEX_SIZE;
        if(faceToCreate(terrain, x+1, y, z, Face.RIGHT)) {
            indices.addAll((short) (vertexOffset), (short) (1 + vertexOffset), (short) (2 + vertexOffset), (short) (2 + vertexOffset), (short) (3 + vertexOffset), (short) (vertexOffset));
            createRight(itemType, gameResources.getTextureRegion(itemType), x, y, z, vertices);
        }

        vertexOffset = vertices.size / VERTEX_SIZE;
        if(faceToCreate(terrain, x, y, z-1, Face.FRONT)) {
            indices.addAll((short) (vertexOffset), (short) (1 + vertexOffset), (short) (2 + vertexOffset), (short) (2 + vertexOffset), (short) (3 + vertexOffset), (short) (vertexOffset));
            createFront(itemType, gameResources.getTextureRegion(itemType), x, y, z, vertices);
        }

        vertexOffset = vertices.size / VERTEX_SIZE;
        if(faceToCreate(terrain, x, y, z+1, Face.BACK)) {
            indices.addAll((short) (vertexOffset), (short) (1 + vertexOffset), (short) (2 + vertexOffset), (short) (2 + vertexOffset), (short) (3 + vertexOffset), (short) (vertexOffset));
            createBack(itemType, gameResources.getTextureRegion(itemType), x, y, z, vertices);
        }

    }

    private boolean faceToCreate(Terrain terrain, int x, int y, int z, Face face) {
        VoxelChunk neighboor = terrain.chunks.get(position.cpy().add(face.pos));

        int nX, nY, nZ;

        if (x < 0) {
            nX = GameVariables.CHUNK_SIZE + x;
        } else {
            nX = x % GameVariables.CHUNK_SIZE;
        }

        if (y < 0) {
            nY = GameVariables.CHUNK_SIZE + y;
        } else {
            nY = y % GameVariables.CHUNK_SIZE;
        }

        if (z < 0) {
            nZ = GameVariables.CHUNK_SIZE + z;
        } else {
            nZ = z % GameVariables.CHUNK_SIZE;
        }

        if (notInBounds(x, y, z)) {
            if (neighboor == null) {
                return false;
            } else if (neighboor.voxels[nX][nY][nZ] != null) {
                return false;
            } else {
                return true;
            }
        }
        return voxels[x][y][z] == null || voxels[x][y][z].equals(ItemType.EMPTY);
    }

    private boolean notInBounds(int x, int y, int z){
        return (x<0 || y<0 || z<0
                || x>=GameVariables.CHUNK_SIZE
                || y>=GameVariables.CHUNK_SIZE
                || z>=GameVariables.CHUNK_SIZE);
    }

    private boolean inBounds(int x, int y, int z){
        return !notInBounds(x, y, z);
    }

    private void generateVegetation(GameResources gameResources, int x, int y, int z, ItemType itemType, FloatArray vertices,  ShortArray indices){
//        createFCross(itemType, gameResources.getTextureRegion(itemType), x, y, z, vertices);
//        createDCross(itemType, gameResources.getTextureRegion(itemType), x, y, z, vertices);
//        createF2Cross(itemType, gameResources.getTextureRegion(itemType), x, y, z, vertices);
//        createD2Cross(itemType, gameResources.getTextureRegion(itemType), x, y, z, vertices);
//
//        if(inBounds(x, y, z+1) && voxels[x][y][z+1] != null && !voxels[x][y][z+1].vegetation) {
//            createFront(voxels[x][y][z+1], gameResources.getTextureRegion(voxels[x][y][z+1]), x, y, z+1, vertices);
//        }
//
//        if(inBounds(x, y, z-1) && voxels[x][y][z-1] != null && !voxels[x][y][z-1].vegetation) {
//            createBack(voxels[x][y][z-1], gameResources.getTextureRegion(voxels[x][y][z-1]), x, y, z-1, vertices);
//        }
//
//        if(inBounds(x-1, y, z) && voxels[x-1][y][z] != null && !voxels[x-1][y][z].vegetation) {
//            createRight(voxels[x-1][y][z], gameResources.getTextureRegion(voxels[x-1][y][z]), x-1, y, z, vertices);
//        }
//
//        if(inBounds(x+1, y, z) && voxels[x+1][y][z] != null && !voxels[x+1][y][z].vegetation) {
//            createLeft(voxels[x+1][y][z], gameResources.getTextureRegion(voxels[x+1][y][z]), x+1, y, z, vertices);
//        }
//
//        if(inBounds(x, y+1, z) && voxels[x][y+1][z] != null && !voxels[x][y+1][z].vegetation) {
//            createBottom(voxels[x][y+1][z], gameResources.getTextureRegion(voxels[x][y+1][z]), x, y+1, z, vertices);
//        }
//
//
//        createTop(voxels[x][y-1][z], gameResources.getTextureRegion(voxels[x][y-1][z]), x, y-1, z, vertices);

    }

    public static void createFCross(ItemType itemType, TextureRegion textureRegion, int x, int y, int z, FloatArray vertices) {
        //1---2
        //|   |
        //0---3

        vertices.add(x); // 0
        vertices.add(y);
        vertices.add(z);
        vertices.add(0);
        vertices.add(0);
        vertices.add(1);
        vertices.add(textureRegion.getU());
        vertices.add(textureRegion.getV2());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x); // 1
        vertices.add(y + 1);
        vertices.add(z);
        vertices.add(0);
        vertices.add(0);
        vertices.add(1);
        vertices.add(textureRegion.getU());
        vertices.add(textureRegion.getV());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x + 1); // 2
        vertices.add(y + 1);
        vertices.add(z + 1);
        vertices.add(0);
        vertices.add(0);
        vertices.add(1);
        vertices.add(textureRegion.getU2());
        vertices.add(textureRegion.getV());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x + 1); // 3
        vertices.add(y);
        vertices.add(z + 1);
        vertices.add(0);
        vertices.add(0);
        vertices.add(1);
        vertices.add(textureRegion.getU2());
        vertices.add(textureRegion.getV2());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

    }

    public static void createF2Cross(ItemType itemType, TextureRegion textureRegion, int x, int y, int z, FloatArray vertices) {
         //1---2
         //|   |
         //0---3


        vertices.add(x+1); // 0
        vertices.add(y);
        vertices.add(z+1);
        vertices.add(0);
        vertices.add(0);
        vertices.add(1);
        vertices.add(textureRegion.getU());
        vertices.add(textureRegion.getV2());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x + 1); // 1
        vertices.add(y + 1);
        vertices.add(z + 1);
        vertices.add(0);
        vertices.add(0);
        vertices.add(1);
        vertices.add(textureRegion.getU());
        vertices.add(textureRegion.getV());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x); // 2
        vertices.add(y + 1);
        vertices.add(z);
        vertices.add(0);
        vertices.add(0);
        vertices.add(1);
        vertices.add(textureRegion.getU2());
        vertices.add(textureRegion.getV());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x); // 3
        vertices.add(y);
        vertices.add(z);
        vertices.add(0);
        vertices.add(0);
        vertices.add(1);
        vertices.add(textureRegion.getU2());
        vertices.add(textureRegion.getV2());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

    }


    public static void createD2Cross(ItemType itemType, TextureRegion textureRegion, int x, int y, int z, FloatArray vertices) {
        //1---2
        //|   |
        //0---3

        vertices.add(x +1); // 0
        vertices.add(y);
        vertices.add(z);
        vertices.add(0);
        vertices.add(0);
        vertices.add(1);
        vertices.add(textureRegion.getU());
        vertices.add(textureRegion.getV2());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x +1); // 1
        vertices.add(y +1);
        vertices.add(z);
        vertices.add(0);
        vertices.add(0);
        vertices.add(1);
        vertices.add(textureRegion.getU());
        vertices.add(textureRegion.getV());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x); // 2
        vertices.add(y + 1);
        vertices.add(z+1);
        vertices.add(0);
        vertices.add(0);
        vertices.add(1);
        vertices.add(textureRegion.getU2());
        vertices.add(textureRegion.getV());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x); // 3
        vertices.add(y);
        vertices.add(z+1);
        vertices.add(0);
        vertices.add(0);
        vertices.add(1);
        vertices.add(textureRegion.getU2());
        vertices.add(textureRegion.getV2());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

    }


    public static void createDCross(ItemType itemType, TextureRegion textureRegion, int x, int y, int z, FloatArray vertices) {
        //1---2
        //|   |
        //0---3

        vertices.add(x); // 0
        vertices.add(y);
        vertices.add(z + 1);
        vertices.add(0);
        vertices.add(0);
        vertices.add(1);
        vertices.add(textureRegion.getU());
        vertices.add(textureRegion.getV2());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x); // 1
        vertices.add(y + 1);
        vertices.add(z + 1);
        vertices.add(0);
        vertices.add(0);
        vertices.add(1);
        vertices.add(textureRegion.getU());
        vertices.add(textureRegion.getV());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x + 1); // 2
        vertices.add(y + 1);
        vertices.add(z);
        vertices.add(0);
        vertices.add(0);
        vertices.add(1);
        vertices.add(textureRegion.getU2());
        vertices.add(textureRegion.getV());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x + 1); // 3
        vertices.add(y);
        vertices.add(z);
        vertices.add(0);
        vertices.add(0);
        vertices.add(1);
        vertices.add(textureRegion.getU2());
        vertices.add(textureRegion.getV2());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

    }

    public static void createTop(ItemType itemType, TextureRegion textureRegion, int x, int y, int z, FloatArray vertices) {
        vertices.add(x);
        vertices.add(y + 1);
        vertices.add(z);
        vertices.add(0);
        vertices.add(1);
        vertices.add(0);
        vertices.add(textureRegion.getU());
        vertices.add(textureRegion.getV());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x + 1);
        vertices.add(y + 1);
        vertices.add(z);
        vertices.add(0);
        vertices.add(1);
        vertices.add(0);
        vertices.add(textureRegion.getU2());
        vertices.add(textureRegion.getV());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x + 1);
        vertices.add(y + 1);
        vertices.add(z + 1);
        vertices.add(0);
        vertices.add(1);
        vertices.add(0);
        vertices.add(textureRegion.getU2());
        vertices.add(textureRegion.getV2());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x);
        vertices.add(y + 1);
        vertices.add(z + 1);
        vertices.add(0);
        vertices.add(1);
        vertices.add(0);
        vertices.add(textureRegion.getU());
        vertices.add(textureRegion.getV2());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

    }

    public static void createBottom(ItemType itemType, TextureRegion textureRegion, int x, int y, int z, FloatArray vertices) {
        vertices.add(x);
        vertices.add(y);
        vertices.add(z);
        vertices.add(0);
        vertices.add(-1);
        vertices.add(0);
        vertices.add(textureRegion.getU());
        vertices.add(textureRegion.getV());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x);
        vertices.add(y);
        vertices.add(z + 1);
        vertices.add(0);
        vertices.add(-1);
        vertices.add(0);
        vertices.add(textureRegion.getU2());
        vertices.add(textureRegion.getV());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x + 1);
        vertices.add(y);
        vertices.add(z + 1);
        vertices.add(0);
        vertices.add(-1);
        vertices.add(0);
        vertices.add(textureRegion.getU2());
        vertices.add(textureRegion.getV2());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x + 1);
        vertices.add(y);
        vertices.add(z);
        vertices.add(0);
        vertices.add(-1);
        vertices.add(0);
        vertices.add(textureRegion.getU());
        vertices.add(textureRegion.getV2());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

    }

    public static void createLeft(ItemType itemType, TextureRegion textureRegion, int x, int y, int z, FloatArray vertices) {
        vertices.add(x);
        vertices.add(y);
        vertices.add(z);
        vertices.add(-1);
        vertices.add(0);
        vertices.add(0);
        vertices.add(textureRegion.getU());
        vertices.add(textureRegion.getV());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x);
        vertices.add(y + 1);
        vertices.add(z);
        vertices.add(-1);
        vertices.add(0);
        vertices.add(0);
        vertices.add(textureRegion.getU2());
        vertices.add(textureRegion.getV());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x);
        vertices.add(y + 1);
        vertices.add(z + 1);
        vertices.add(-1);
        vertices.add(0);
        vertices.add(0);
        vertices.add(textureRegion.getU2());
        vertices.add(textureRegion.getV2());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add( x);
        vertices.add(y);
        vertices.add(z + 1);
        vertices.add(-1);
        vertices.add(0);
        vertices.add(0);
        vertices.add(textureRegion.getU());
        vertices.add(textureRegion.getV2());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

    }

    public static void createRight(ItemType itemType, TextureRegion textureRegion, int x, int y, int z, FloatArray vertices) {
        vertices.add(x + 1);
        vertices.add(y);
        vertices.add(z);
        vertices.add(1);
        vertices.add(0);
        vertices.add(0);
        vertices.add(textureRegion.getU());
        vertices.add(textureRegion.getV());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x + 1);
        vertices.add(y);
        vertices.add(z + 1);
        vertices.add(1);
        vertices.add(0);
        vertices.add(0);
        vertices.add(textureRegion.getU2());
        vertices.add(textureRegion.getV());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x + 1);
        vertices.add(y + 1);
        vertices.add(z + 1);
        vertices.add(1);
        vertices.add(0);
        vertices.add(0);
        vertices.add(textureRegion.getU2());
        vertices.add(textureRegion.getV2());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x + 1);
        vertices.add(y + 1);
        vertices.add(z);
        vertices.add(1);
        vertices.add(0);
        vertices.add(0);
        vertices.add(textureRegion.getU());
        vertices.add(textureRegion.getV2());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);
    }

    public static void createFront(ItemType itemType, TextureRegion textureRegion, int x, int y, int z, FloatArray vertices) {
        vertices.add(x);
        vertices.add(y);
        vertices.add(z);
        vertices.add(0);
        vertices.add(0);
        vertices.add(1);
        vertices.add(textureRegion.getU());
        vertices.add(textureRegion.getV());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x + 1);
        vertices.add(y);
        vertices.add(z);
        vertices.add(0);
        vertices.add(0);
        vertices.add(1);
        vertices.add(textureRegion.getU2());
        vertices.add(textureRegion.getV());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x + 1);
        vertices.add(y + 1);
        vertices.add(z);
        vertices.add(0);
        vertices.add(0);
        vertices.add(1);
        vertices.add(textureRegion.getU2());
        vertices.add(textureRegion.getV2());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x);
        vertices.add(y + 1);
        vertices.add(z);
        vertices.add(0);
        vertices.add(0);
        vertices.add(1);
        vertices.add(textureRegion.getU());
        vertices.add(textureRegion.getV2());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);
    }

    public static void createBack(ItemType itemType, TextureRegion textureRegion, int x, int y, int z, FloatArray vertices) {
        vertices.add(x); //1
        vertices.add(y);
        vertices.add(z + 1);
        vertices.add(0);
        vertices.add(0);
        vertices.add(-1);
        vertices.add(textureRegion.getU());
        vertices.add(textureRegion.getV());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x); //2
        vertices.add(y + 1);
        vertices.add(z + 1);
        vertices.add(0);
        vertices.add(0);
        vertices.add(-1);
        vertices.add(textureRegion.getU2());
        vertices.add(textureRegion.getV());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x + 1); //3
        vertices.add(y + 1);
        vertices.add(z + 1);
        vertices.add(0);
        vertices.add(0);
        vertices.add(-1);
        vertices.add(textureRegion.getU2());
        vertices.add(textureRegion.getV2());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);

        vertices.add(x + 1); //4
        vertices.add(y);
        vertices.add(z + 1);
        vertices.add(0);
        vertices.add(0);
        vertices.add(-1);
        vertices.add(textureRegion.getU());
        vertices.add(textureRegion.getV2());
        vertices.add(itemType.color.r);
        vertices.add(itemType.color.g);
        vertices.add(itemType.color.b);
        vertices.add(itemType.color.a);
    }

}