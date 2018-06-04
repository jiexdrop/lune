package com.jiexdrop.lune.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.entity.container.Container;
import com.jiexdrop.lune.model.entity.container.Crafter;
import com.jiexdrop.lune.model.entity.container.Inventory;
import com.jiexdrop.lune.model.entity.container.ItemSlot;
import com.jiexdrop.lune.view.World;
import com.jiexdrop.lune.view.GameResources;
import com.jiexdrop.lune.view.ItemType;

import java.util.Observable;
import java.util.Observer;

/**
 *
 * Created by jiexdrop on 01/08/17.
 */

public class ViewContainers extends Table implements Observer, Displayable {
    private Inventory inventory;

    private Crafter crafter;

    private Table inventoryTable;

    private Table containerTable;

    private ButtonGroup<ViewItemSlot> itemSlots;

    private GameResources gameResources;

    private World world;

    ViewContainers(World world, GameResources gameResources){
        this.world = world;
        this.inventory = world.player.getInventory();
        this.gameResources = gameResources;
        this.itemSlots = new ButtonGroup<ViewItemSlot>();

        this.crafter = new Crafter();

        this.inventory.addObserver(this);
        this.crafter.addObserver(this);

        itemSlots.setMaxCheckCount(2);
        itemSlots.setMinCheckCount(0);

        setFillParent(true);

        this.inventoryTable = new Table();
        this.containerTable = new Table();

        this.add(inventoryTable);
        this.add(containerTable);

        setupInventory();
        setupCrafter();
    }

    private void setupInventory(){
        for(int i = 0; i < this.inventory.getItemSlots().size(); i++) {
            ViewItemSlot viewItemSlot = new ViewItemSlot(i, inventory, this.inventory.getItemSlots().get(i), gameResources.getSlotButtonStyle());
            viewItemSlot.clearChildren();

            viewItemSlot.add(viewItemSlot.getImage()).height(GameVariables.SIDEBAR_BUTTON_HEIGHT/2)
                    .width(GameVariables.SIDEBAR_BUTTON_HEIGHT/2).fill().row();
            viewItemSlot.add(viewItemSlot.getLabel());

            itemSlots.add(viewItemSlot);

            if((i+1)%(Math.sqrt(this.inventory.getItemSlots().size()))==0) {
                inventoryTable.add(viewItemSlot).width(GameVariables.SIDEBAR_WIDTH).height(GameVariables.SIDEBAR_BUTTON_HEIGHT).row();
            } else {
                inventoryTable.add(viewItemSlot).width(GameVariables.SIDEBAR_WIDTH).height(GameVariables.SIDEBAR_BUTTON_HEIGHT);
            }
        }
    }

    private void setupCrafter(){
        for(int i = 0; i < this.crafter.getItemSlots().size(); i++) {
            ViewItemSlot viewItemSlot = new ViewItemSlot(i, crafter, this.crafter.getItemSlots().get(i), gameResources.getSlotButtonStyle());
            viewItemSlot.clearChildren();

            viewItemSlot.add(viewItemSlot.getImage()).height(GameVariables.SIDEBAR_BUTTON_HEIGHT/2)
                    .width(GameVariables.SIDEBAR_BUTTON_HEIGHT/2).fill().row();
            viewItemSlot.add(viewItemSlot.getLabel());

            itemSlots.add(viewItemSlot);

            if((i+1)%(Math.sqrt(this.crafter.getItemSlots().size()))==0) {
                containerTable.add(viewItemSlot).width(GameVariables.SIDEBAR_WIDTH).height(GameVariables.SIDEBAR_BUTTON_HEIGHT).row();
            } else {
                containerTable.add(viewItemSlot).width(GameVariables.SIDEBAR_WIDTH).height(GameVariables.SIDEBAR_BUTTON_HEIGHT);
            }
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        update();
    }

    private void update(){
        if(itemSlots.getAllChecked().size==2){
            ViewItemSlot firstChecked = itemSlots.getAllChecked().get(0);
            ViewItemSlot secondChecked = itemSlots.getAllChecked().get(1);

            switchItemSlots(firstChecked.container, firstChecked.id, secondChecked.container,
                    secondChecked.id);

            itemSlots.uncheckAll();
        }

        for (ViewItemSlot vis: itemSlots.getButtons()){
            ItemSlot item = vis.container.getItemSlots().get(vis.id);
            vis.setText(item.toString());

            ItemType itemType = ItemType.valueOf(item.getItemName());
            vis.getStyle().imageUp = gameResources.getTextureRegionDrawable(itemType);
        }

    }

    public void switchItemSlots(Container containerA, int keyA, Container containerB, int keyB) {
        ItemSlot copySlot = new ItemSlot(containerA.getItemSlots().get(keyA));
        containerA.setItemSlot(keyA, containerB.getItemSlots().get(keyB));
        containerB.setItemSlot(keyB, copySlot);
    }

    @Override
    public void show() {
        this.setVisible(true);
    }

    @Override
    public void hide() {
        this.setVisible(false);
    }
}
