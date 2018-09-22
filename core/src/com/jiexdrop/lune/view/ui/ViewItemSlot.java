package com.jiexdrop.lune.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.jiexdrop.lune.model.entity.container.Container;
import com.jiexdrop.lune.model.entity.container.ItemSlot;

/**
 *
 * Created by jiexdrop on 06/08/17.
 */

public class ViewItemSlot extends ImageTextButton {
    ItemSlot itemSlot;
    Container container;
    int id;

    public ViewItemSlot(int id, Container container, ItemSlot itemSlot, ImageTextButtonStyle style) {
        super(itemSlot.getItemName(), style);
        this.itemSlot = itemSlot;
        this.container = container;
        this.id = id;

        addListener(new UpdateListener(container));
    }

    private class UpdateListener extends ClickListener {
        Container container;

        UpdateListener(Container container){
            this.container = container;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            container.update();
        }

        @Override
        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            container.update();
        }

        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            container.update();
        }
    }
}
