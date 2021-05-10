package com.arkadgame.game.obj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class Acid extends CustomActor {
    private TextureRegion acidRegion;
    private Texture acidTexture;
    private int frame = 1;
    private float time = 0f;

    public Acid (Texture acidTexture) {
        this.acidTexture=acidTexture;
        acidRegion = new TextureRegion(acidTexture, 16 * frame, 32, 16, 16);
        setSize(48, 10);
    }

    public void anim (){
        time += Gdx.graphics.getDeltaTime();
        if (time > 0.5f) {
            time = 0f;
            frame += 1;
            if (frame > 1) {
                frame = 0;
            }
            acidRegion = new TextureRegion(acidTexture, 16 * frame, 32, 16, 16);
            setSize(48, 48);
        }
    }

    public String getType() {
        return "Acid";
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        anim();
        batch.draw(acidRegion, getX(), getY(), 48, 48);
    }
}