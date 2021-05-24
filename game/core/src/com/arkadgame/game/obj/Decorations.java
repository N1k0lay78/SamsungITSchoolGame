package com.arkadgame.game.obj;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Decorations extends CustomActor {
    private Texture decorationTexture;
    private TextureRegion decorationRegion;
    private int xSize = 0;
    private int ySize = 0;
    private int sizeX = 120;
    private int sizeY = 80;

    public Decorations (Texture decorationTexture, int x, int y, int sizeX, int sizeY) {
        this.xSize = sizeX;
        this.ySize = sizeY;
        this.decorationTexture = decorationTexture;
        this.decorationRegion = new TextureRegion(decorationTexture, x, y, sizeX, sizeY);
        setSize(sizeX, sizeY);
    }

    public void act (float delta){

    }

    public String getType() {
        return "Decoration";
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(decorationRegion, getX(), getY(), sizeX, sizeY);
    }
}