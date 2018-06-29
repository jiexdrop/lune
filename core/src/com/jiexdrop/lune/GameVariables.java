package com.jiexdrop.lune;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

/**
 *
 * Created by jorge on 25/05/17.
 */

public class GameVariables {
    //TEXTURES
    public static final String MAIN_TEXTURE_ATLAS = "main.atlas";

    //FOLDERS
    private static final String MODELS_FOLDER = "models/";

    public static final String SLIME_MODEL = MODELS_FOLDER + "slime.g3db";

    public static final String PLAYER_MODEL = MODELS_FOLDER + "player.g3db";

    public static final String DUCK_MODEL = MODELS_FOLDER + "duck.g3db";


    //MODELS
    private static final String TEXTURES_FOLDER = "textures/";

    private static final String USER_INTERFACE_FOLDER = "ui/";

    private static final String TERRAIN_FOLDER = TEXTURES_FOLDER + "terrain/";

    private static final String ENTITIES_FOLDER = TEXTURES_FOLDER + "entities/";

    private static final String PLAYER_FOLDER = ENTITIES_FOLDER + "player/";

    private static final String ENEMIES_FOLDER = ENTITIES_FOLDER + "enemies/";

    private static final String PLAYER_CHEST_FOLDER = PLAYER_FOLDER + "chest/";

    private static final String PLAYER_HELMET_FOLDER = PLAYER_FOLDER + "helmet/";

    private static final String PLAYER_PANTS_FOLDER = PLAYER_FOLDER + "pants/";

    //TERRAIN
    public static final String WATER_TEXTURE = TERRAIN_FOLDER + "water";

    public static final String GROUND_TEXTURE = TERRAIN_FOLDER + "ground";

    public static final String WALL_TEXTURE = TERRAIN_FOLDER + "wall";

    public static final String LOG_TEXTURE = TERRAIN_FOLDER + "log";

    public static final String ROOT_TEXTURE = TERRAIN_FOLDER + "root";

    public static final String LEAVES_TEXTURE = TERRAIN_FOLDER + "leaves";

    public static final String GRASS_TEXTURE = TERRAIN_FOLDER + "grass";

    public static final String WEEDS_TEXTURE = TERRAIN_FOLDER + "weeds";

    public static final String CACTUS_TEXTURE = TERRAIN_FOLDER + "cactus";

    public static final String DOOR_TEXTURE = TERRAIN_FOLDER + "door";

    public static final String ROCK_TEXTURE = TERRAIN_FOLDER + "rock";

    public static final String SLIME_TEXTURE = TERRAIN_FOLDER + "slime";

    //ENEMIES
    public static final Color SLIME_COLOR = new Color(0x35b720ee);

    //PLAYER
    public static final Color PLAYER_SKIN_PLAINS = new Color(0xfaa87aff);

    public static final Color PLAYER_SKIN_MOUNTAINS = new Color(0xfab87aff);

    public static final Color PLAYER_SKIN_DESERT = new Color(0x6d4128ff);

    public static final float CAMERA_FAR = 124f;

    public static final float DEBUG_CAMERA_FAR = 512f;

    //CONSTANTS
    public static float CAMERA_ZOOM_POSITION = 20f;

    public static final float TERRAIN_FREQUENCY = 0.021f;//0.001f

    public static final float RARE_FREQUENCY = 0.001f;//0.001d

    public static final float TREES_FREQUENCY = 1f;//1d

    public static final Vector3 GRAVITY = new Vector3(0, -10f, 0);

    public static final int CHUNK_SIZE = 16;

    public static final int CHUNKS_HEIGHT = 16;

    public static final int SPAWN_HEIGHT = 64;

    public static final int PLAYER_SPEED = 10;

    public static final int SLIMES_SPEED = 10;

    public static final int ENTITIES_SPEED = 10;

    public static final int NORMAL_SPEED = 10;

    public static final int NB_ENEMIES = 12;

    public static final int ENEMIES_SPACING = 64;

    public static final int NORMAL_HEALTH = 256;

    public static final int NORMAL_STRENGTH = 64;

    public static final Vector3 TILES_SIZE = new Vector3(1,1,1);

    public static final Vector3 ENEMY_SIZE = new Vector3(1,2,1);

    public static final Vector3 ENTITY_SIZE = new Vector3(1,2,1);

    public static final Vector3 NORMAL_SIZE = new Vector3(1,1,1);

    public static final Vector3 PLAYER_SIZE = new Vector3(1,2,1);

    public static final Vector3 MAGNET_SIZE = new Vector3(1,1,1);

    public static final Vector3 ITEM_SIZE = new Vector3(1,1,1);

    public static final float TIME_STEP = 1/60f;

    public static final int SATIETY_TIME = 256;

    public static final int MAX_SATIETY = 1024;

    public static final int SATIETY_STEP = 16;

    public static final int HEALTH_STEP = 16;

    public static final int FIELD_OF_VIEW = 67;

    //DESERT COLORS
    public static final Color CACTUS_DESERT = new Color(0x35b720ff);


    public static final Color GROUND_DESERT = new Color(0xe5e47cff);

    //PLAIN COLORS
    public static final Color WATER_PLAIN = new Color(0x35b7c1ff);

    public static final Color GRASS_PLAIN = new Color(0x35b720ff);

    public static final Color TREE_PLAIN = new Color(0x35b720ff);

    public static final Color WOOD_PLAIN = new Color(0x635230ff);

    public static final Color GROUND_PLAIN = new Color(0x5a553bff);

    public static final Color ROCK_PLAIN = new Color(0xe3e3e3ff);

    public static final Color DIRT_PLAIN = new Color(0x5a553bff);
    //COLORS
    public static final Color WATER = new Color(0x35b7c1ff);
    public static final Color FIRE = new Color(0x35b7c1ff);
    public static final Color AIR = new Color(0x35b7c1ff);

    public static final Color EARTH = new Color(0x35b7c1ff);


    //USER INTERFACE
    public static final String SLOT_TEXTURE = USER_INTERFACE_FOLDER + "slot";

    public static final String TOUCHPAD_TEXTURE = USER_INTERFACE_FOLDER + "touchpad";

    public static final String TOUCHPAD_KNOB_TEXTURE = USER_INTERFACE_FOLDER + "knob";

    public static final String CLOSE_TEXTURE = USER_INTERFACE_FOLDER + "close";

    public static final String SLOT_ACTIVE_TEXTURE = USER_INTERFACE_FOLDER + "slot_active";

    public static final String CROSS_TEXTURE = USER_INTERFACE_FOLDER + "cross";

    public static final String SLOT_INACTIVE_TEXTURE = USER_INTERFACE_FOLDER + "slot_inactive";

    public static final String SLOT_INVENTORY_TEXTURE = USER_INTERFACE_FOLDER + "slot_inventory";

    public static final String SLOT_ENERGY_INACTIVE_TEXTURE = USER_INTERFACE_FOLDER + "energy_inactive";

    public static final String SLOT_ENERGY_ACTIVE_TEXTURE = USER_INTERFACE_FOLDER + "energy_active";

    public static final int UI_TEXTURE_SIZE = 16;

    public static final int TOUCH_PAD_SIZE = 192;

    public static final int UI_OFFSET = 16;

    public static int SIDEBAR_WIDTH = UI_TEXTURE_SIZE*6;

    public static int DISPLAY_BAR_HEIGHT = UI_TEXTURE_SIZE;

    public static int DISPLAY_BAR_KNOB_WIDTH = DISPLAY_BAR_HEIGHT;

    public static int SIDEBAR_BUTTON_HEIGHT = UI_TEXTURE_SIZE*6;

    public static int ENTITIES = 0;

    public static int VISIBLE_ENTITIES = 0;

    public static int HOLD_TIME = 0;

    public static int PLAYER_HEALTH = 0;

    public static final int PLAYER_ITEM_SLOTS = 16;

    public static final int PLAYER_CRAFT_ITEM_SLOTS = 4;

    public static String EMPTY = "EMPTY";

    public static String CLEAR = "";

    public static Vector3 PLAYER_POSITION = new Vector3(0,0,0);

    public static int RENDERED_CHUNKS;

    public static int RENDERED_BLOCKS;

    public static int RENDERED_VERTICES;

    public static int RENDERED_INDICES;

    public static int RENDERED_MESHES;

    public static int GAMEMODE = 0;

    public static int SEED = (int) (Math.random() * Math.pow(2, 20));

    //DEBUG
    public static boolean DEBUG = true;


}