package com.arkadgame.game.obj;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Text extends CustomActor {
    protected Texture texture;
    protected TextureRegion region;
    private float regionX;
    private float regionY;
    private float sizeX;
    private float sizeY;
    private float zoom;

    public Text(Texture texture, float x, float y, float sizeX, float sizeY, float zoom) {
        // получаем текстуру
        this.texture = texture;
        this.region = new TextureRegion(texture, x, y, sizeX, sizeY);
        // сохраняем позицию текстуры
        this.regionX = x;
        this.regionY = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.zoom = zoom;
    }

    public void update() {
        this.region = new TextureRegion(texture, regionX, regionY, sizeX, sizeY);
    }

    public void draw(Batch batch, float parentAlpha) {
        //System.out.println(getX() + "X" + getY() + ":" + sizeX * zoom + "X" + sizeY * zoom + "test" + zoom);
        batch.setColor(new Color(1,1,1, parentAlpha));
        batch.draw(region, getX(), getY(), sizeX * zoom, sizeY * zoom);
        batch.setColor(new Color(1,1,1,1));
    }

    // за что отвечает слайдер
    public String getType()          { return type; }
    public void setType(String type) { this.type = type; }
    // гетеры и сетеры
    public void    setSizeX(float sizeX) { this.sizeX = sizeX; }
    public void    setSizeY(float sizeY) { this.sizeY = sizeY; }
    public void    setZoom(float zoom)   { this.zoom = zoom;}
    public void    setRegionPosition(float x, float y) { this.regionX = x; this.regionY = y; }
    public void    setActive(boolean active) { } // текст не может быть активным
    public float   getSizeX()  { return sizeX; }
    public float   getSizeY()  { return sizeY; }
    public float   getZoom()   { return zoom; }
    public float   getWidth()  { return sizeX * zoom; }
    public float   getHeight() { return sizeX * zoom; }
    public boolean getActive() { return false; } // аналогично
}
