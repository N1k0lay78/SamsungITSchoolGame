package com.arkadgame.game;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.arkadgame.game.ArkadGame;

public class AndroidLauncher extends AndroidApplication {

	private SharedPreferences preferences;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = getPreferences(0);
		float volume = preferences.getFloat("volume",1f);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new ArkadGame(volume), config);
	}
}
