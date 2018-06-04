package com.jiexdrop.lune.model.entity;

import com.jiexdrop.lune.GameVariables;
import com.jiexdrop.lune.model.entity.container.Inventory;
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

        position.y = GameVariables.SPAWN_HEIGHT;

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

        velocity.add(acceleration);
        velocity.limit(speed);
        position.add(velocity);
        acceleration.setZero();

        updateGameVariables();
    }

    @Override
    public void hit(Living e) {
        if(!e.equals(this)) {
            e.health-= GameVariables.HEALTH_STEP * (strength/GameVariables.NORMAL_STRENGTH);
            satiety-= GameVariables.SATIETY_STEP;
        }
    }

    private void updateGameVariables(){
        GameVariables.PLAYER_POSITION.x = getX();
        GameVariables.PLAYER_POSITION.y = getY();
        GameVariables.PLAYER_POSITION.z = getZ();

        GameVariables.PLAYER_HEALTH = health;
    }

    public int getSatiety(){
        return satiety;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
