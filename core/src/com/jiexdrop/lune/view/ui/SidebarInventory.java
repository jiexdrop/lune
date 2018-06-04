package com.jiexdrop.lune.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.entity.container.Inventory;
import com.jiexdrop.lune.model.entity.container.ItemSlot;
import com.jiexdrop.lune.view.World;
import com.jiexdrop.lune.view.GameResources;
import com.jiexdrop.lune.view.ItemType;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by jiexdrop on 06/08/17.
 */

public class SidebarInventory extends Table implements Observer {
    private World world;

    private Inventory inventory;

    private ButtonGroup<ViewItemSlot> sideBarItemSlots;

    private GameResources gameResources;

    SidebarInventory(World world, GameResources gameResources){
        this.world = world;
        this.gameResources = gameResources;
        sideBarItemSlots = new ButtonGroup<ViewItemSlot>();
        this.inventory = world.player.getInventory();

        this.inventory.addObserver(this);
    }

    public void setup(int height){
        clearChildren();
        sideBarItemSlots.clear();

        int items = (height/ GameVariables.SIDEBAR_BUTTON_HEIGHT)-2;

        for(int i =0; i < items; i++) {
            ViewItemSlot viewItemSlot = new ViewItemSlot(i, inventory, inventory.getItemSlots().get(i), gameResources.getSlotButtonStyle());
            viewItemSlot.clearChildren();

            viewItemSlot.add(viewItemSlot.getImage()).height(GameVariables.SIDEBAR_BUTTON_HEIGHT/2)
                    .width(GameVariables.SIDEBAR_BUTTON_HEIGHT/2).fill().row();
            viewItemSlot.add(viewItemSlot.getLabel());

            sideBarItemSlots.add(viewItemSlot);
            add(viewItemSlot).width(GameVariables.SIDEBAR_WIDTH).height(GameVariables.SIDEBAR_BUTTON_HEIGHT).row();
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        for (ViewItemSlot vis : sideBarItemSlots.getButtons()) {
            ItemSlot item = vis.itemSlot;
            vis.setText(item.toString());

            ItemType itemType = ItemType.valueOf(item.getItemName());
            vis.getStyle().imageUp = gameResources.getTextureRegionDrawable(itemType);

        }

        world.player.getInventory().setSelectedSlot(sideBarItemSlots.getCheckedIndex());
    }
}
