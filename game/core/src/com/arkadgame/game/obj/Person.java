package com.arkadgame.game.obj;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class Person extends Actor {

    private Texture person;
    private boolean alive;

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Person(Texture person){
        this.person = person;
        this.alive=true;
        setSize(person.getWidth(), person.getHeight());
    }

    public void act (float delta){
        super.act(delta);
    }

    public void draw (Batch batch, float parentAlpha){
        batch.draw(person, getX(), getY());
    }
}