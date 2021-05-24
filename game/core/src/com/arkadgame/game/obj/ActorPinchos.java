package com.arkadgame.game.obj;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class ActorPinchos extends CustomActor {
    private TextureRegion pinchos;
    private int sizeX = 48;
    private int sizeY = 48;

    public ActorPinchos(TextureRegion pinchos) {
        this.pinchos = pinchos;
        setSize(sizeX, sizeY);
    }

    public void act(float delta) {

    }

    public String getType() {
        return "Pinchos";
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(pinchos, getX(), getY(), sizeX, sizeY);
    }
}