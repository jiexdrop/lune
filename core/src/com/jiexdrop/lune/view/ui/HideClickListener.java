package com.jiexdrop.lune.view.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

/**
 *
 * Created by jiexdrop on 29/07/17.
 */

public class HideClickListener extends ClickListener {
    ArrayList<Displayable> displayables;

    public HideClickListener(ArrayList<Displayable> displayable){
        this.displayables = displayable;
    }

    public HideClickListener(ViewContainers viewContainers) {
        this.displayables = new ArrayList<Displayable>();
        displayables.add(viewContainers);
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        for (Displayable displayable : displayables) {
            if(displayable.isVisible()) {
                displayable.hide();
            }
            else {
                displayable.show();
            }
        }
    }
}