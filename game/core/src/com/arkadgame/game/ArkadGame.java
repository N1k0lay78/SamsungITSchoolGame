package com.arkadgame.game;

import com.badlogic.gdx.Game;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ArkadGame extends Game {
	private Socket socket;

	@Override
	public void create () {
		connectSocket();
		ProcessInput process = new ProcessInput();
		setScreen(new MainGameScreen(this, process));
	}

	public void connectSocket() {
		try {
			socket = IO.socket("http://localhost:8000");
			socket.connect();
		} catch(Exception e){
			System.out.println(e);
		}
	}
}
