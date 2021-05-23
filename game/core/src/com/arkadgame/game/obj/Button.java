package com.arkadgame.game.obj;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Button extends CustomActor {
    private Texture buttonTexture;
    private TextureRegion buttonRegion;
    private boolean active = false;
    private boolean vertical = false;
    private float zoom;
    private int sizeX;
    private int sizeY;
    private int _x;
    private int _y;

    public Button(Texture buttonTexture, int sizeX, int sizeY, int x, int y, float zoom, boolean vertical) {
        this.vertical = vertical;
        this.buttonTexture = buttonTexture;
        this.zoom = zoom;
        this._x = x;
        this._y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.setSize(sizeX * zoom, sizeY * zoom);
        buttonRegion = new TextureRegion(buttonTexture, _x, _y, sizeX, sizeY);
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public float getWidth() {
        return sizeX * zoom;
    }

    public boolean checkCollision(float x, float y) {
        if (this.getX() <= x && x <= this.getX() + this.getSizeX() * zoom &&
                this.getY() <= y && y <= this.getY() + this.getSizeY() * zoom) {
            this.setActive(true);
            return true;
        }
        this.setActive(false);
        return false;
    }

    public boolean checkCollision2(boolean pres, float x, float y, boolean pres2, float x2, float y2) {
        if (pres && this.getX() <= x && x <= this.getX() + this.getSizeX() * zoom &&
                this.getY() <= y && y <= this.getY() + this.getSizeY() * zoom ||
                pres2 && this.getX() <= x2 && x2 <= this.getX() + this.getSizeX() * zoom &&
                        this.getY() <= y2 && y2 <= this.getY() + this.getSizeY() * zoom) {
            this.setActive(true);
            return true;
        }
        this.setActive(false);
        return false;
    }

    public void setActive(boolean active) {
        this.active = active;
        changeTexture();
    }

    private void changeTexture() {
        float yB = _y, xB = _x;
        if (this.active) {
            if (vertical) {
                yB += sizeY;
            } else {
                xB += sizeX;
            }
        }
        buttonRegion = new TextureRegion(buttonTexture, (int) xB, (int) yB, sizeX, sizeY);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //System.out.println(getX() + "X" + getY() + ":" + sizeX * zoom + "X" + sizeY * zoom + "test" + zoom);
        batch.setColor(new Color(1, 1, 1, parentAlpha));
        batch.draw(buttonRegion, getX(), getY(), sizeX * zoom, sizeY * zoom);
        batch.setColor(new Color(1, 1, 1, 1));
    }
}
