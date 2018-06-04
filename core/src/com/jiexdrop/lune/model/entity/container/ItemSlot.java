package com.jiexdrop.lune.model.entity.container;

import com.badlogic.gdx.graphics.Color;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.view.ItemType;

/**
 *
 * Created by jiexdrop on 16/07/17.
 */

public class ItemSlot {
    Item item = new Item();
    int quantity;

    public ItemSlot(){
        quantity = 0;
    }

    public ItemSlot(Item item, int quantity){
        this.item = item;
        this.quantity = quantity;
    }

    public ItemSlot(ItemSlot itemSlot){
        this.item = new Item(itemSlot.item);
        this.quantity = itemSlot.quantity;
    }

    public boolean hasItem(){
        return quantity > 0;
    }


    public boolean equals(Item item){
        return this.item.getName().equals(item.getName());
    }

    public void addItem(Item i){
        if(equals(i)){
            quantity++;
        } else {
            this.item = i;
            quantity = 1;
        }
    }

    public String removeItem(){
        String res = item.getName();
        if(hasItem()){
            if(quantity > 0){
                quantity--;
                if(quantity==0) this.item = new Item();
            }
        }
        return res;
    }

    public String getItemName(){
        return item.getName();
    }

    public Color getItemColor(){
        return item.getColor();
    }

    @Override
    public String toString() {
        if(GameVariables.DEBUG)
            return item.getName() + ":" + quantity;

        return Integer.toString(quantity);
    }

    public void setItemSlot(ItemSlot itemSlot) {
        this.item = new Item(itemSlot.item);
        this.quantity = itemSlot.quantity;
    }

    public void editItemSlot(ItemSlot itemSlot) {
        if(itemSlot.item.equals(this.item)){
            this.quantity += itemSlot.quantity;
        } else {
            this.item = new Item(itemSlot.item);
            this.quantity = itemSlot.quantity;
        }
    }
}
