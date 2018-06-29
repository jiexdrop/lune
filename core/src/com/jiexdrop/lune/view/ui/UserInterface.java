package com.jiexdrop.lune.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.view.World;
import com.jiexdrop.lune.view.GameResources;

/**
 * Display user controls
 * Created by jiexdrop on 30/06/17.
 */

public class UserInterface extends Stage {
    private World world;

    private Touchpad touchpad;

    private Table mainTable = new Table();
    private Table leftTable = new Table();
    private Table rightTable = new Table();
    private Table centerTable = new Table();

    private Label debugLabel;

    private ImageButton inventoryButton;

    private GameResources gameResources;

    private ViewContainers viewContainers;

    private SidebarInventory sidebarInventory;

    private CountClickListener debugListener = new CountClickListener();

    public UserInterface(World world, GameResources gameResources, Batch batch){
        super(new ScreenViewport(), batch);

        this.world = world;

        this.gameResources = gameResources;

        inventoryButton = new ImageButton(gameResources.getInventoryButtonStyle());
        debugLabel = new Label(GameVariables.CLEAR, new Label.LabelStyle(gameResources.getFont(), Color.WHITE));
        touchpad = new Touchpad(10, gameResources.getTouchPadStyle());


        viewContainers = new ViewContainers(world, gameResources);
        sidebarInventory = new SidebarInventory(world, gameResources);

        inventoryButton.addListener(new HideClickListener(viewContainers));

        this.addActor(mainTable);
        this.addActor(viewContainers);

        mainTable.setFillParent(true);

        mainTable.defaults().pad(GameVariables.UI_OFFSET);

        mainTable.add(leftTable).left().fill().width(GameVariables.TOUCH_PAD_SIZE).expand();
        mainTable.add(centerTable).expand();
        mainTable.add(rightTable).right().fill().width(GameVariables.TOUCH_PAD_SIZE).expand();

        ImageButton imageButton = new ImageButton(gameResources.getCrossButtonStyle());
        imageButton.addListener(debugListener);


        centerTable.add(imageButton);
        leftTable.add(debugLabel).left().top().expand().row();
        leftTable.add(touchpad).width(GameVariables.TOUCH_PAD_SIZE).height(GameVariables.TOUCH_PAD_SIZE).left().bottom().expand().row();
        rightTable.add(sidebarInventory).right().expand();
    }

    private void setup(){
        sidebarInventory.setup(Math.round(getHeight()));
        sidebarInventory.add(inventoryButton).width(GameVariables.SIDEBAR_WIDTH).height(GameVariables.SIDEBAR_BUTTON_HEIGHT).row();
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
        setup();
    }

    @Override
    public void draw(){
        super.draw();

        world.movePlayer(world.deltaTime, touchpad.getKnobPercentX(), touchpad.getKnobPercentY());

        if(debugListener.count == 5){
            debugListener.count = 0;
            GameVariables.DEBUG = !GameVariables.DEBUG;
        }

        if(GameVariables.DEBUG) {
            debugLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond()
                    + "\nPLAYER_POS: " + GameVariables.PLAYER_POSITION
                    + "\nENTITIES: " + GameVariables.ENTITIES
                    + "\nVISIBLE_ENTITIES: " + GameVariables.VISIBLE_ENTITIES
                    + "\nRENDERED_CHUNKS: " + GameVariables.RENDERED_CHUNKS + "/" + GameVariables.RENDERED_MESHES
                    + "\nRENDERED_BLOCKS: " + GameVariables.RENDERED_BLOCKS
                    + "\nRENDERED_VERTICES: " + GameVariables.RENDERED_VERTICES
                    + "\nRENDERED_INDICES: " + GameVariables.RENDERED_INDICES
                    + "\nPLAYER_HEALTH: " + GameVariables.PLAYER_HEALTH
                    + "\nSEED:" + GameVariables.SEED
                    + "\nHOLD_TIME:" + GameVariables.HOLD_TIME);
        }
        else {
            debugLabel.setText(GameVariables.CLEAR);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
