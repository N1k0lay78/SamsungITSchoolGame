package com.arkadgame.game.obj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class Background extends CustomActor {
    private TextureRegion backgroundRegion;
    private Texture backgroundTexture;
    private int sizeX = 864;
    private int sizeY = 3456;


    public Background(Texture backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
        backgroundRegion = new TextureRegion(backgroundTexture, 0, 0, 144, 576);
        setSize(sizeX, sizeY);
    }

    public String getType() {
        return "Background";
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(backgroundRegion, 0, -80, sizeX, sizeY);
    }
}