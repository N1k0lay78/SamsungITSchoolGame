package com.arkadgame.game.obj;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class Stairs extends CustomActor {
    private TextureRegion stairsTexture;

    public Stairs (TextureRegion stairsTexture) {
        this.stairsTexture=stairsTexture;
        setSize(48, 50);
    }

    public String getType() {
        return "Stairs";
    }

    public void act (float delta){

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(stairsTexture, getX(), getY(), 48, 48);
    }
}