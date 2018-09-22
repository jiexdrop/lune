package com.jiexdrop.lune.view;

/**
 * Created by jiexdrop on 26/07/17.
 */

public enum EntityType {
    SLIME("slime", true),
    PLAYER("player", true),
    DUCK("duck", false),
    MINI_CUBE("mini_cube", false),
    BREAKING("breaking", false);

    private final String name;
    public final boolean enemy;

    EntityType(String name, boolean enemy) {
        this.name = name;
        this.enemy = enemy;
    }
}
