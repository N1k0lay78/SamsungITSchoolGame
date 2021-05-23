package com.arkadgame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Null;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import jdk.nashorn.api.scripting.JSObject;

public class ArkadGame extends Game {
	private Socket socket;
	private boolean isScreensaverOver;
	private MainGameScreen mainGameScreen;
	private MainMenu mainMenu;
	private OpenGameScreen openGameScreen;
	private SettingsScreen settingsScreen;
	private Sound sound;
	private Sound music_1;
	private Sound music_2;
	private int musicNum = -1;
	private float volume;
	private long bgMusicID;
	private AssetManager assetManager;
	private Preferences prefs;

	public ArkadGame() {
		// Preferences prefs = Gdx.app.getPreferences("arkad-game");
		// volume = prefs.getFloat("volume");
		volume = 1f;
		// System.out.println("LOAD "+volume);
	}

	private String currentScene;
	private ProcessInput process;

	@Override
	public void create () {
		assetManager = new AssetManager();
		assetManager.load("main menu (1).mp3", Sound.class);
		assetManager.load("music_2.mp3", Sound.class);
		assetManager.load("music_3.mp3", Sound.class);
		assetManager.finishLoading(); //Important!
		System.out.println("ISLOADED "+assetManager.isLoaded(Gdx.files.internal("main menu (1).mp3").toString()));
		sound = assetManager.get("main menu (1).mp3", Sound.class);
		//connectSocket();
		//configSocketEvents();
		process = new ProcessInput();
		// Gdx.input.vibrate(10000);
		mainMenu = new MainMenu(this, process, 700f);
		mainGameScreen = new MainGameScreen(this, process, socket);
		settingsScreen = new SettingsScreen(this, process, 0.5f);
		currentScene = "OpenGameScreen";
		//connectSocket();
		//configSocketEvents();
		Gdx.input.setInputProcessor(this.process);
		mainGameScreen = new MainGameScreen(this, process, socket);
		openGameScreen = new OpenGameScreen(this, process, 1f, 2.5f); // будет показываться deltaSpeed * 3 + waitTime * 2 секунд
		setScreen(openGameScreen);
	}

	public void setMusic(int num) {
		if (num != musicNum) {
			if (num == 0) {
				bgMusicID = sound.play(volume);
				sound.setLooping(bgMusicID, true);
			} else if (sound != null) {
				sound.stop();
			}
			if (num == 1) {
				if (music_1 == null) {
					music_1 = assetManager.get("music_2.mp3", Sound.class);
				}
				bgMusicID = music_1.play(volume);
				music_1.setLooping(bgMusicID, true);
			} else if (music_1 != null) {
				music_1.stop();
			}
			if (num == 2) {
				if (music_2 == null) {
					music_2 = assetManager.get("music_3.mp3", Sound.class);
				}
				bgMusicID = music_2.play(volume);
				music_2.setLooping(bgMusicID, true);
			} else if (music_2 != null) {
				music_2.stop();
			}
			musicNum = num;
		}
	}

	public void setVolume(float vol) {
		volume = vol;
		if (musicNum == 0) { sound.setVolume(bgMusicID, vol); }
		if (musicNum == 1) { music_1.setVolume(bgMusicID, vol); }
		if (musicNum == 2) { music_2.setVolume(bgMusicID, vol); }
	}

	public void render() {
		if ((process.getEsc()&&currentScene.equals("Level1"))||isScreensaverOver) {
			if (process.getEsc()&&currentScene.equals("Level1")) {
				setMusic(0);
			}
			isScreensaverOver = false;
			currentScene = "MainMenu";
			setScreen(mainMenu);
		}
		if (mainMenu.getCurrButton().equalsIgnoreCase("settingsButton")) {
			mainMenu.clearAction();
			setScreen(settingsScreen);
		}
		if (settingsScreen.getCurrButton().equalsIgnoreCase("ReadyButton")) {
			settingsScreen.clearAction();
			setScreen(mainMenu);
		}
		if (mainMenu.getCurrButton().equalsIgnoreCase("ExitButton")) {
			Gdx.app.exit();
		}
		if (currentScene.equals("MainMenu")&&mainMenu.getCurrButton().equalsIgnoreCase("PlayButton")) {
			setMusic(1);
			currentScene = "Level1";
			mainMenu.clearAction();
			setScreen(mainGameScreen);
		}
		super.render();
	}

	public void recreateLevel() {this.mainGameScreen.recreate();}

	public void setScreenMainMenu() {
		setScreen(mainMenu);
	}

	public void setScreenMainGameScreen() {
		setScreen(mainGameScreen);
	}

	public void configSocketEvents() {
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				Gdx.app.log("ONLINE", "Connected");
			}
		});
		socket.on("newPlayer", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject)args[0];
				try {
					String id = data.getString("id");
					Gdx.app.log("ONLINE", "my id: " + id);
				} catch (JSONException e) {
					Gdx.app.error("ONLINE", "Error on read data from connect");
				}
			}
		});
		socket.on("getPlayers", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONArray data = (JSONArray) args[0];
				try {
					for (int i=0; i < data.length(); i++) {
						Gdx.app.log("ONLINE", "player at: "+ data.getJSONObject(i).getString("x") + " " + data.getJSONObject(i).getString("y"));
					}
				} catch (JSONException e) {
					Gdx.app.error("ONLINE", "Error on read data from getting players");
				}
			}
		});
	}

	public float getVolume() { return volume; }

	public boolean isScreensaverOver() {
		return isScreensaverOver;
	}

	public void setScreensaverOver(boolean screensaverOver) {
		isScreensaverOver = screensaverOver;
	}

	@Override
	public void dispose() {
		/* System.out.println("SAVE  "+volume);
		prefs.putFloat("volume", volume);
		prefs.flush(); */
		super.dispose();
	}
}
