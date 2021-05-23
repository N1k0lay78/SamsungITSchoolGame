package com.arkadgame.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.arkadgame.game.ArkadGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "CyberKong";
		cfg.height = 480;
		cfg.width = 720;
		new LwjglApplication(new ArkadGame(), cfg);
	}
}
