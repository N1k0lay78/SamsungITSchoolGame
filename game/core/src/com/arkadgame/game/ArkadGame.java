package com.arkadgame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

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

	public ArkadGame() {
	}
	private String currentScene;
	private ProcessInput process;

	@Override
	public void create () {
		connectSocket();
		configSocketEvents();
		process = new ProcessInput();
		mainMenu = new MainMenu(this, process, 700f);
		mainGameScreen = new MainGameScreen(this, process, socket);
		settingsScreen = new SettingsScreen(this, process, 0.5f);
		setScreen(mainMenu);
		currentScene = "OpenGameScreen";
		//connectSocket();
		//configSocketEvents();
		this.process = new ProcessInput();
		Gdx.input.setInputProcessor(this.process);

		mainGameScreen = new MainGameScreen(this, process, socket);
		openGameScreen = new OpenGameScreen(this, process, 1f, 1f); // будет показываться deltaSpeed * 4 + waitTime * 2 секунд
		setScreen(openGameScreen);
	}

	public void render() {
		if ((process.getEsc()&&currentScene.equals("Level1"))||isScreensaverOver) {
			isScreensaverOver = false;
			currentScene = "MainMenu";
			setScreen(mainMenu);
		}
		if (mainMenu.getCurrButton().equalsIgnoreCase("settingsButton")) {
			mainMenu.clearAction();
			setScreen(settingsScreen);
		}
		if (mainMenu.getCurrButton().equalsIgnoreCase("ExitButton")) {
			Gdx.app.exit();
		}
		if (currentScene.equals("MainMenu")&&mainMenu.getCurrButton().equalsIgnoreCase("PlayButton")) {
			currentScene = "Level1";
			mainMenu.clearAction();
			setScreen(mainGameScreen);
		}
		super.render();
	}

	public void recreateLevel() {this.mainGameScreen.recreate();}

	public void connectSocket() {
		try {
			socket = IO.socket("http://localhost:8000");
			socket.connect();
			JSONObject obj = new JSONObject();
			JSONObject data = new JSONObject();
			obj.put("event", "spawn");
			data.put("x", 100);
			data.put("y", 100);
			data.put("action", "idle");
			obj.put("data", data);
			socket.emit("message", obj);
		} catch(Exception e) {
			System.out.println(e);
		}
	}

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

	public boolean isScreensaverOver() {
		return isScreensaverOver;
	}

	public void setScreensaverOver(boolean screensaverOver) {
		isScreensaverOver = screensaverOver;
	}
}
