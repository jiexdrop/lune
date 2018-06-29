package com.jiexdrop.lune.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.jiexdrop.lune.GameVariables;

/**
 *
 * Created by jiexdrop on 14/06/17.
 */

public class GameResources {
    private BitmapFont font = new BitmapFont();

    private TextureRegion water;
    private TextureRegion grass;
    private TextureRegion log;
    private TextureRegion ground;
    private TextureRegion leaves;
    private TextureRegion wall;
    private TextureRegion door;
    private TextureRegion root;
    private TextureRegion cactusTexture;
    private TextureRegion rockTexture;
    private TextureRegion slimeTexture;
    private TextureRegion weedsTexture;

    private Material material;

    private Sprite grassSprite;
    private Sprite cactusSprite;

    private Sprite touchPadBackgroundTexture;
    private Sprite touchPadKnobTexture;
    private TextureRegion slotTexture;
    private TextureRegion inventoryTexture;
    private TextureRegion closeTexture;
    private TextureRegion crossTexture;
    private TextureRegion overlayTexture;
    private TextureRegion barKnobTexture;
    private TextureRegion barKnobAfterTexture;

    private Model slimeModel;
    private Model playerModel;
    private Model duckModel;
    private ModelBuilder modelBuilder;

    public GameResources(){
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal(GameVariables.MAIN_TEXTURE_ATLAS));
        AssetManager assets = new AssetManager();
        assets.load(GameVariables.SLIME_MODEL, Model.class);
        assets.load(GameVariables.PLAYER_MODEL, Model.class);
        assets.load(GameVariables.DUCK_MODEL, Model.class);

        assets.finishLoading();

        material = new Material(TextureAttribute.createDiffuse(textureAtlas.getTextures().first()),
                IntAttribute.createCullFace(GL20.GL_FRONT),
                new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA),
                FloatAttribute.createAlphaTest(0.5f));

        modelBuilder = new ModelBuilder();

        slimeModel = assets.get(GameVariables.SLIME_MODEL, Model.class);
        playerModel = assets.get(GameVariables.PLAYER_MODEL, Model.class);
        duckModel = assets.get(GameVariables.DUCK_MODEL, Model.class);

        water = textureAtlas.findRegion(GameVariables.WATER_TEXTURE);
        ground = textureAtlas.findRegion(GameVariables.GROUND_TEXTURE);
        grass = textureAtlas.findRegion(GameVariables.GRASS_TEXTURE);
        log = textureAtlas.findRegion(GameVariables.LOG_TEXTURE);
        wall = textureAtlas.findRegion(GameVariables.WALL_TEXTURE);
        root = textureAtlas.findRegion(GameVariables.ROOT_TEXTURE);
        leaves = textureAtlas.findRegion(GameVariables.LEAVES_TEXTURE);
        cactusTexture = textureAtlas.findRegion(GameVariables.CACTUS_TEXTURE);
        weedsTexture = textureAtlas.findRegion(GameVariables.WEEDS_TEXTURE);
        door = textureAtlas.findRegion(GameVariables.DOOR_TEXTURE);
        rockTexture = textureAtlas.findRegion(GameVariables.ROCK_TEXTURE);
        slimeTexture = textureAtlas.findRegion(GameVariables.SLIME_TEXTURE);

        grassSprite = new Sprite(grass);
        cactusSprite = new Sprite(cactusTexture);

        closeTexture = textureAtlas.findRegion(GameVariables.CLOSE_TEXTURE);
        slotTexture = textureAtlas.findRegion(GameVariables.SLOT_TEXTURE);
        overlayTexture = textureAtlas.findRegion(GameVariables.SLOT_ACTIVE_TEXTURE);
        barKnobAfterTexture = textureAtlas.findRegion(GameVariables.TOUCHPAD_KNOB_TEXTURE);
        barKnobTexture = textureAtlas.findRegion(GameVariables.TOUCHPAD_KNOB_TEXTURE);
        inventoryTexture = textureAtlas.findRegion(GameVariables.SLOT_INVENTORY_TEXTURE);
        touchPadBackgroundTexture = textureAtlas.createSprite(GameVariables.TOUCHPAD_TEXTURE);
        touchPadKnobTexture = textureAtlas.createSprite(GameVariables.TOUCHPAD_KNOB_TEXTURE);
        crossTexture = textureAtlas.createSprite(GameVariables.CROSS_TEXTURE);
    }


    private Sprite getTouchPadBackgroundTexture() {
        return touchPadBackgroundTexture;
    }

    private Sprite getTouchPadKnobTexture() {
        return touchPadKnobTexture;
    }

    private TextureRegion getSlotTexture() {
        return slotTexture;
    }

    private TextureRegion getInventoryTexture() {
        return inventoryTexture;
    }

    private TextureRegion getCrossTexture() {
        return crossTexture;
    }

    private TextureRegion getBarKnobTexture() {
        return barKnobTexture;
    }

    private TextureRegion getCloseTexture() {
        return closeTexture;
    }

    private TextureRegion getBarKnobAfterTexture() {
        return barKnobAfterTexture;
    }

    private TextureRegion getSlotOverlayTexture() {
        return overlayTexture;
    }

    TextureRegion getTextureRegion(ItemType itemType){
        switch (itemType){
            case WATER:
                return water;
            case LEAVES:
                return leaves;
            case LOG:
                return log;
            case ROOT:
                return root;
            case CACTUS:
                return cactusTexture;
            case DOOR:
                return door;
            case ROCK:
                return rockTexture;
            case WALL:
                return wall;
            case GRASS:
                return grass;
            case SLIME:
                return slimeTexture;
            case WEEDS:
                return weedsTexture;
            default:
                return ground;
        }
    }

    public Model getModel(EntityType entityType){
        switch (entityType){
            case PLAYER:
                return playerModel;
            case SLIME:
                return slimeModel;
            case DUCK:
                return duckModel;
            default:
                return slimeModel;
        }
    }

    /**
     *
     * @param itemType
     * @return new model for every item wich is bad TODO may need easy perf improvements
     */
    public Model getMiniCubeModel(ItemType itemType){

        return modelBuilder.createBox(1,1,1, new Material(TextureAttribute.createDiffuse(getTextureRegion(itemType)),
                        ColorAttribute.createDiffuse(itemType.color),
                        new BlendingAttribute()),
                VertexAttributes.Usage.Position
                        | VertexAttributes.Usage.Normal
                        | VertexAttributes.Usage.TextureCoordinates);

    }

    public Drawable getTextureRegionDrawable(ItemType itemType){
        return new TextureRegionDrawable(getTextureRegion(itemType)).tint(itemType.color);
    }

    public BitmapFont getFont(){
        return font;
    }

    public Touchpad.TouchpadStyle getTouchPadStyle() {
        Touchpad.TouchpadStyle touchPadStyle = new Touchpad.TouchpadStyle();

        Skin touchPadSkin = new Skin();
        touchPadSkin.add("touchBackground", getTouchPadBackgroundTexture());
        touchPadSkin.add("touchKnob", getTouchPadKnobTexture());

        touchPadStyle.background  = touchPadSkin.getDrawable("touchBackground");
        touchPadStyle.knob  = touchPadSkin.getDrawable("touchKnob");

        touchPadStyle.knob.setMinHeight(GameVariables.TOUCH_PAD_SIZE/2);
        touchPadStyle.knob.setMinWidth(GameVariables.TOUCH_PAD_SIZE/2);

        return touchPadStyle;
    }

    public ImageTextButton.ImageTextButtonStyle getSlotButtonStyle() {
        TextureRegionDrawable buttonBackgroundUp = new TextureRegionDrawable(getSlotTexture());
        TextureRegionDrawable buttonBackgroundChecked = new TextureRegionDrawable(getSlotOverlayTexture());
        ImageTextButton.ImageTextButtonStyle imageTextButtonStyle = new ImageTextButton.ImageTextButtonStyle(buttonBackgroundUp,
                null,buttonBackgroundChecked, getFont());

        imageTextButtonStyle.imageUp = new TextureRegionDrawable(getTouchPadKnobTexture());

        return imageTextButtonStyle;
    }

    public TextField.TextFieldStyle getTextFieldStyle() {
        TextureRegionDrawable buttonBackgroundUp = new TextureRegionDrawable(getSlotTexture());
        TextureRegionDrawable buttonBackgroundChecked = new TextureRegionDrawable(getSlotOverlayTexture());
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();

        textFieldStyle.font = font;

        textFieldStyle.fontColor = Color.WHITE;

        return textFieldStyle;
    }

    public ImageButton.ImageButtonStyle getInventoryButtonStyle() {
        TextureRegionDrawable buttonBackground = new TextureRegionDrawable(getInventoryTexture());
        return new ImageButton.ImageButtonStyle(buttonBackground, null, null, null, null, null);
    }

    public ImageButton.ImageButtonStyle getCrossButtonStyle() {
        TextureRegionDrawable buttonBackground = new TextureRegionDrawable(getCrossTexture());
        return new ImageButton.ImageButtonStyle(buttonBackground, null, null, null, null, null);
    }

    public ProgressBar.ProgressBarStyle getProgressBarStyle(){
        NinePatch ninePatch = new NinePatch(getBarKnobTexture(), 2,2,2,2);
        Drawable barKnob = new NinePatchDrawable(ninePatch);
        NinePatch ninePatchAfter = new NinePatch(getBarKnobAfterTexture(), 2,2,2,2);
        Drawable barKnobAfter = new NinePatchDrawable(ninePatchAfter);

        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle(null, null);
        progressBarStyle.knobBefore = barKnob;
        progressBarStyle.knobAfter = barKnobAfter;

        return progressBarStyle;
    }

    public Material getMaterial() {
        return material;
    }

    public Sprite getSprite(ItemType itemType) {
        switch (itemType) {
            case GRASS:
                return grassSprite;
            case CACTUS:
                return cactusSprite;
            default:
                return grassSprite;
        }
    }
}
