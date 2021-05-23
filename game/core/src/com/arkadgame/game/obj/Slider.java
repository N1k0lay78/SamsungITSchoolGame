package com.arkadgame.game.obj;

import com.badlogic.gdx.graphics.Texture;
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

    public Slider(Texture buttonTexture, float x, float y, float zoom) {
        this.setPosition(x, y);
        this.buttonTexture = buttonTexture;
        this.zoom = zoom;
        this.buttonTexture = buttonTexture;
        this.filledLine = new TextureRegion(buttonTexture,0, 96, 240, 16);
        this.emptyLine = new TextureRegion(buttonTexture,0, 112, 240, 16);
        this.hand = new TextureRegion(buttonTexture, 432, 64, 16, 16);

        //this.setSize(sizeX * zoom, sizeY * zoom);
        //buttonRegion = new TextureRegion(buttonTexture, _x, _y, sizeX, sizeY);
    }

    public boolean checkCollision(float x, float y) {
        if (x < x&& x < this.getX() + this.getSizeX() * zoom&&
                this.getY() < y&& y < this.getY() + this.getSizeY() * zoom) {
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
        if (active) {
            this.hand = new TextureRegion(buttonTexture, 432, 80, 16, 16);
        } else {
            this.hand = new TextureRegion(buttonTexture, 432, 64, 16, 16);

        }
    }

    public float getSizeX() {return 256 * zoom;}
    public float getSizeY() {return 16 * zoom;}
    public String getType() {
        return type;
    }
    public void setType(String type) {this.type = type;}
}
