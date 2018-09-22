package com.jiexdrop.lune;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.jiexdrop.lune.view.GameResources;
import com.jiexdrop.lune.view.MainMenuRender;

public class LuneGame extends Game {
	public GameResources textures;
	
	@Override
	public void create () {
		textures = new GameResources();
		this.setScreen(new MainMenuRender(this));
	}


}
