package com.jiexdrop.lune.model.entity.container;

import java.util.HashMap;
import java.util.Observable;

/**
 * Something that contains slots
 * Created by jiexdrop on 06/08/17.
 */

public class Container extends Observable {
    HashMap<Integer, ItemSlot> itemSlots = new HashMap<Integer, ItemSlot>();

    private int slots;

    public Container(int slots) {
        this.slots = slots;
        this.fill();
    }

    public HashMap<Integer,ItemSlot> getItemSlots() {
        return itemSlots;
    }

    public void copySlots(Container container){
        for (int i = 0; i < getItemSlots().size(); i++){
            this.itemSlots.remove(i);
            this.itemSlots.put(i,new ItemSlot(container.getItemSlots().get(i)));
        }
        update();
    }

    public void update(){
        setChanged();
        notifyObservers();
    }

    public void fill(){
        itemSlots.clear();

        for (int i = 0; i < slots; i++){
            itemSlots.put(i, new ItemSlot());
        }

        setChanged();
        notifyObservers();
    }

    public void setItemSlot(int key, ItemSlot itemSlot){
        itemSlots.get(key).setItemSlot(itemSlot);
    }
}
