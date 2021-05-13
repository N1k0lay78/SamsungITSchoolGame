package com.arkadgame.game.obj;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class Stairs extends CustomActor {
    private TextureRegion stairsTexture;
    private int sizeX = 48;
    private int sizeY = 48;
    private String type = "Stairs";

    public Stairs(TextureRegion stairsTexture) {
        this.stairsTexture = stairsTexture;
        setSize(sizeX, sizeY + 2);
    }

    public String getType() {
        return this.type;
    }


    public void setType(String newType) {
        this.type = newType;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(stairsTexture, getX(), getY(), sizeX, sizeY);
    }
}