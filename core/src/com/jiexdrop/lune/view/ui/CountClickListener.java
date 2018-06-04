package com.jiexdrop.lune.view.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by jiexdrop on 04/11/17.
 */

public class CountClickListener extends ClickListener {
    public int count;

    public CountClickListener() {

    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        count++;
    }
}
