package com.arkadgame.game.obj;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Button extends CustomActor {
    private Texture buttonTexture;
    private TextureRegion buttonRegion;
    private boolean active = false;
    public int sizeX;
    public int sizeY;
    private int _x;
    private int _y;

    public Button(Texture buttonTexture, int sizeX, int sizeY, int x, int y) {
        this.buttonTexture = buttonTexture;
        this._x = x;
        this._y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.setSize(sizeX * 5, sizeY * 5);
        buttonRegion = new TextureRegion(buttonTexture, _x, _y, sizeX, sizeY);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {this.type = type;}

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(buttonRegion, getX(), getY(), sizeX * 5, sizeY * 5);
    }
}
