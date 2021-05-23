package com.arkadgame.game.obj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Text extends CustomActor {
    protected Texture texture;
    protected TextureRegion TextRegion;
    private float regionX;
    private float regionY;
    private float sizeX;
    private float sizeY;
    private float zoom;

    public Text(Texture texture, int x, int y, int sizeX, int sizeY, float zoom) {
        // получаем текстуру
        this.texture = texture;
        // сохраняем позицию текстуры
        this.regionX = x;
        this.regionY = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.zoom = zoom;
        this.setSize(sizeX * zoom, sizeY * zoom);
        this.TextRegion = new TextureRegion(texture, x, y, sizeX, sizeY);
    }

    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(new Color(1,1,1,parentAlpha));
        //System.out.println(getX() + "X" + getY() + ":" + sizeX * zoom + "X" + sizeY * zoom + "test" + zoom);
        batch.draw(this.TextRegion, getX(), getY(), getWidth(), getHeight());
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
    public float   getHeight() { return sizeY * zoom; }
    public boolean getActive() { return false; } // аналогично
}
