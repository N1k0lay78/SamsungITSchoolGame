package com.arkadgame.game;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.arkadgame.game.ArkadGame;

public class AndroidLauncher extends AndroidApplication {
	private SharedPreferences preferences;
	private ArkadGame game;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = getPreferences(0);
		game = new ArkadGame(preferences.getFloat("volume", 0.25f));
		System.out.println("LOAD "+game.getVolume());
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(game, config);

	}

	@Override
	protected void onDestroy() {
		SharedPreferences.Editor editor = preferences.edit();
		System.out.println("SAVE "+game.getVolume());
		editor.putFloat("volume", game.getVolume());
		editor.apply();
		super.onDestroy();
	}
}
