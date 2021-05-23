package com.arkadgame.game;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Online {
    private int port;
    private int host;
    public boolean connected;
    private Socket socket;

    public Online() {
        try {
            socket = IO.socket("http://localhost:8000/");
            socket.connect();
            connected = true;
            JSONObject obj = new JSONObject();
            obj.put("event", "spawn");
            obj.put("x", 100);
            obj.put("y", 100);
            obj.put("frame", 0);
            socket.emit("message", obj);
            System.out.println("ONLINE:SPAWNED");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            connected = false;
            System.out.println("ERR:NO CONNECTION");
        } catch (JSONException e) {
            System.out.println("ERR:BAD DATA TO SPAWN");
        }
    }
}
