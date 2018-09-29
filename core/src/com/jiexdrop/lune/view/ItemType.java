package com.jiexdrop.lune.view;

import com.badlogic.gdx.graphics.Color;
import com.jiexdrop.lune.GameVariables;

/**
 *
 * Created by jiexdrop on 20/07/17.
 */

public enum ItemType {

    //4 ELEMENTS
    WATER("water", true,false, 1000, GameVariables.WATER, 0),
    EARTH("earth", true,false, 1000, GameVariables.EARTH, 0),
    AIR("air", false,false, 1000, GameVariables.AIR, 0),
    FIRE("fire", false,false, 1000, GameVariables.FIRE, 0),

    //CLASSING
    ENERGY("energy", false,false, 1000, Color.BLACK, 0),
    LIVING("living", false,false, 1000, Color.BLACK, 0),

    //TILES
    GROUND("ground", true,false, 100, GameVariables.GROUND_DESERT, 0),
    DIRT("dirt", true,false, 100, GameVariables.DIRT_PLAIN, 0),
    GRASS("grass", true,false, 100, GameVariables.GRASS_PLAIN, GameVariables.SATIETY_STEP*2),
    WEEDS("weeds", false,true, 100, GameVariables.GRASS_PLAIN, GameVariables.SATIETY_STEP*2),
    LOG("log",true,false, 800, GameVariables.WOOD_PLAIN, 0),
    ROOT("root",true,false, 0, GameVariables.WOOD_PLAIN, GameVariables.SATIETY_STEP*2),
    DOOR("door", true,false, 1000, GameVariables.WOOD_PLAIN, 0),
    WALL("wall",true,false, 2000, GameVariables.WOOD_PLAIN, 0),
    LEAVES("leaves", false, true, 200, GameVariables.GRASS_PLAIN, GameVariables.SATIETY_STEP*5),
    CACTUS("cactus",false,false, 350, GameVariables.CACTUS_DESERT, GameVariables.SATIETY_STEP*5),
    BRANCHES("branches", false, false, -1, GameVariables.WOOD_PLAIN, 0),
    ROCK("rock",true,false, 500, GameVariables.ROCK_PLAIN, -GameVariables.SATIETY_STEP*2),

    //ITEMS
    SLIME("slime", true, false, -1, GameVariables.SLIME_COLOR, GameVariables.SATIETY_STEP*5),
    DUCK("duck", true, false, -1, GameVariables.SLIME_COLOR, GameVariables.SATIETY_STEP*5),
    PLAYER("player", true, false, -1, GameVariables.PLAYER_SKIN_MOUNTAINS, GameVariables.SATIETY_STEP*20),

    //MISC
    EMPTY("empty", true, false, -1, Color.CLEAR, 0);

    private final String name;

    public final boolean solid;
    public final boolean vegetation;
    public final int resistance;
    public final Color color;
    public final int nourishment;

    ItemType(String name, boolean solid, boolean vegetation, int resistance, Color color, int nourishment) {
        this.name = name;
        this.solid = solid;
        this.resistance = resistance;
        this.color = color;
        this.vegetation = vegetation;
        this.nourishment = nourishment;
    }

    public boolean isEatable(){
        return nourishment != 0;
    }


}
