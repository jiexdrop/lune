package com.jiexdrop.lune.model.entity;

import com.badlogic.gdx.math.Matrix4;
import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.entity.container.Inventory;
import com.jiexdrop.lune.view.EntityView;
import com.jiexdrop.lune.view.World;
import com.jiexdrop.lune.view.ItemType;

/**
 *
 * Created by on 23/05/17.
 */

public class Player extends Living {

    private Inventory inventory;

    public Player(){
        name = ItemType.PLAYER.name();

        origin.set(0.0f,1.50f,0.0f);

        speed = GameVariables.PLAYER_SPEED;
        size = GameVariables.PLAYER_SIZE;

        inventory = new Inventory(GameVariables.PLAYER_ITEM_SLOTS);

        color = GameVariables.PLAYER_SKIN_MOUNTAINS;
    }


    @Override
    public void update(World world) {
        elapsedTime += world.deltaTime;

        if(Math.round(elapsedTime*100)%GameVariables.SATIETY_TIME==0){
            satiety-= GameVariables.SATIETY_STEP;
        }


        updateGameVariables();
    }

    @Override
    public void hit(Living e) {
        if(!e.equals(this)) {
            e.health -= GameVariables.HEALTH_STEP * (strength/GameVariables.NORMAL_STRENGTH);
            satiety -= GameVariables.SATIETY_STEP;
        }
    }

    private void updateGameVariables() {
        GameVariables.PLAYER_POSITION.x = getPosition().x;
        GameVariables.PLAYER_POSITION.y = getPosition().y;
        GameVariables.PLAYER_POSITION.z = getPosition().z;

        GameVariables.PLAYER_HEALTH = health;
    }

    public int getSatiety(){
        return satiety;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
