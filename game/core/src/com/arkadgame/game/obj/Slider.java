package com.arkadgame.game.obj;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Slider extends CustomActor {
    // текстурки
    private Texture buttonTexture;
    private TextureRegion filledLine;
    private TextureRegion emptyLine;
    private TextureRegion hand;
    // кнопка активна
    private boolean active = false;
    private float zoom; // для скейла
    // значение от 0 до 1
    private float value;

    public Slider(Texture buttonTexture, float x, float y, float zoom, float value) {
        this.setPosition(x, y);
        this.value = value;
        this.buttonTexture = buttonTexture;
        this.zoom = zoom;
        this.buttonTexture = buttonTexture;
        this.filledLine = new TextureRegion(buttonTexture,0, 112, (int)(240*value), 16);
        this.emptyLine = new TextureRegion(buttonTexture,0, 96, 240, 16);
        this.hand = new TextureRegion(buttonTexture, 432, 64, 16, 16);

        //this.setSize(sizeX * zoom, sizeY * zoom);
        //buttonRegion = new TextureRegion(buttonTexture, _x, _y, sizeX, sizeY);
    }

    public boolean checkCollision(float x, float y, boolean pressed) {
        if (this.getX() <= x&& x <= this.getX() + this.getWidth()&&
                this.getY() <= y&& y <= this.getY() + this.getHeight()) {
            this.setActive(true);
            if (pressed) {
                value = (x - getX())/ getWidth();
                update();
            }
            return true;
        }
        this.setActive(false);
        return false;
    }

    public void draw(Batch batch, float parentAlpha) {
        //System.out.println(getX() + "X" + getY() + ":" + sizeX * zoom + "X" + sizeY * zoom + "test" + zoom);
        batch.setColor(new Color(1,1,1,parentAlpha));
        batch.draw(emptyLine, getX(), getY(), getWidth(), getHeight());
        batch.draw(filledLine, getX(), getY(), getWidth()*value, getHeight());
        batch.draw(hand, getX()+getWidth()*value-16*zoom/2, getY(), getHeight(), getHeight());
        batch.setColor(new Color(1,1,1,1));
    }

    public void setActive(boolean active) {
        this.active = active;
        changeTexture();
    }

    private void changeTexture() {
        if (active) {
            this.hand = new TextureRegion(buttonTexture, 432, 64, 16, 16);
        } else {
            this.hand = new TextureRegion(buttonTexture, 432, 80, 16, 16);
        }
    }

    private void update() {
        this.filledLine = new TextureRegion(buttonTexture,0, 112, (int)(240*value), 16);
    }

    // гетеры и сетеры
    public void    set(float val) {this.value = val;}
    public void    setType(String type) {this.type = type;}
    public void    setZoom(float zoom)  { this.zoom = zoom;}
    public String  getType()   { return type; }
    public float   getVolume() {return value;}
    public float   getSizeX()  { return 240; }
    public float   getSizeY()  { return 16; }
    public float   getZoom()   { return zoom; }
    public float   getWidth()  { return getSizeX() * getZoom(); }
    public float   getHeight() { return getSizeY() * getZoom(); }
}
