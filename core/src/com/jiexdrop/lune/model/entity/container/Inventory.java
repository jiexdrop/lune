package com.jiexdrop.lune.model.entity.container;

import java.util.Map;

/**
 *
 * Created by jiexdrop on 01/08/17.
 */

public class Inventory extends Container {

    private ItemSlot selectedSlot;

    public Inventory(int slots){
        super(slots);
    }

    public ItemSlot getSelectedSlot() {
        return selectedSlot;
    }

    public void setSelectedSlot(int index) {
        this.selectedSlot = itemSlots.get(index);
    }

    public void addItem(Item i) {
        ItemSlot is = nextSlot(i);
        if(is!=null){
            is.addItem(i);
        }
        update();
    }

    public void removeSelectedItem(){
        this.selectedSlot.removeItem();
        update();
    }

    private ItemSlot nextSlot(Item i){
        for (Map.Entry<Integer,ItemSlot> is :itemSlots.entrySet()) {
            if(is.getValue().equals(i)){
                return is.getValue();
            }
        }
        for (Map.Entry<Integer,ItemSlot> is :itemSlots.entrySet()) {
            if(!is.getValue().hasItem()){
                return is.getValue();
            }
        }
        return null;
    }

}
