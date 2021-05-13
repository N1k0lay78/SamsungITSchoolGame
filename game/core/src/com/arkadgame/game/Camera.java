package com.arkadgame.game;

import com.arkadgame.game.obj.ActorPinchos;
import com.arkadgame.game.obj.CustomActor;
import com.arkadgame.game.obj.Person;

import java.util.ArrayList;

public class Camera {
    private float coordX = 20;
    private float coordY = 400;
    private float deltaX = 0;
    private float deltaY = 0;
    private boolean upd = false;
    private Person person;
    private ArrayList<CustomActor> objects;

    public Camera(Person person) {
        this.person = person;
    }

    public void setCoord(float x, float y) {
        this.deltaX += x - this.coordX;
        this.deltaY += y - this.coordY;
        this.coordX = x;
        this.coordY = y;
    }

    public void setObjects(ArrayList<CustomActor> objects) {
        this.objects = objects;
    }

    public float getCoordX() {
        return coordX;
    }

    public float getCoordY() {
        return coordY;
    }

    public void update() {
        if (Math.abs(deltaY) > 0 || Math.abs(deltaX) > 0) {
            for (CustomActor objc : objects) {
                if (!objc.getType().equalsIgnoreCase("Person")) {
                    objc.setX(objc.getX() - deltaX);
                    objc.setY(objc.getY() - deltaY);
                }
            } if (person.checkCollisions()) {
                for (CustomActor objc : objects) {
                    if (!objc.getType().equalsIgnoreCase("Person")) {
                        objc.setX(objc.getX() + deltaX);
                        objc.setY(objc.getY() + deltaY);
                    }
                }

            }
            deltaY = 0;
            deltaX = 0;
        }
    }
}
