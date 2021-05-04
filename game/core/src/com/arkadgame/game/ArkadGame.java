package com.arkadgame.game;

import com.badlogic.gdx.Game;


public class ArkadGame extends Game {

	@Override
	public void create () {
		ProcessInput process = new ProcessInput();
		setScreen(new MainGameScreen(this, process));
	}
}
