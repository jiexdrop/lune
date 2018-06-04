package com.jiexdrop.lune.model.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.entity.Entity;
import com.jiexdrop.lune.model.entity.container.Item;
import com.jiexdrop.lune.view.ItemType;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Helpers to convert coordinates from screen to map
 * Created by jiexdrop on 09/07/17.
 */

public class Helpers {

    public final static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);

    private static Random random = new Random();

    public static Vector3 playerPosToChunkPos(Vector3 pos){
        Vector3 blockPos = pos.cpy();
        blockPos.x = (int)Math.floor(blockPos.x / GameVariables.CHUNK_SIZE);
        blockPos.y = (int)Math.floor(blockPos.y / GameVariables.CHUNK_SIZE);
        blockPos.z = (int)Math.floor(blockPos.z / GameVariables.CHUNK_SIZE);

        return blockPos;
    }

    public static Vector3 floorPos(Vector3 pos){
        Vector3 blockPos = pos.cpy();
        blockPos.x = (int)Math.floor(blockPos.x);
        blockPos.y = (int)Math.floor(blockPos.y);
        blockPos.z = (int)Math.floor(blockPos.z);
        return blockPos;
    }


    public static Vector3 chunkPosToPlayerPos(Vector3 pos){
        Vector3 blockPos = pos.cpy();
        blockPos.x = blockPos.x * GameVariables.CHUNK_SIZE;
        blockPos.y = blockPos.y * GameVariables.CHUNK_SIZE;
        blockPos.z = blockPos.z * GameVariables.CHUNK_SIZE;
        return blockPos;
    }

    public static int randomSpacing(int h){
        return random.nextInt(h) - random.nextInt(h);
    }

    public static boolean intersect(Vector3 A, double rA, Vector3 B, double rB){
        double distance = Math.sqrt((A.x - B.x) * (A.x - B.x) +
                (A.y - B.y) * (A.y - B.y) +
                (A.z - B.z) * (A.z - B.z));
        return distance < rA + rB;
    }

    public static Material randomColorMaterial(){
        return new Material(new ColorAttribute(ColorAttribute.Diffuse, MathUtils.random(0.5f, 1f), MathUtils.random(
                0.5f, 1f), MathUtils.random(0.5f, 1f), 1), IntAttribute.createCullFace(GL20.GL_FRONT));
    }

    public static Material colorMaterial(Color color) {
        return new Material(new ColorAttribute(ColorAttribute.Diffuse, color.r, color.g, color.b, color.a),
                IntAttribute.createCullFace(GL20.GL_FRONT));
    }

    public static Item toItem(Entity entity) {
        if(entity instanceof Item) {
            return ((Item)entity);
        }
        return null;
    }
}
