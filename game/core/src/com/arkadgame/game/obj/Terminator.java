package com.arkadgame.game.obj;

import com.arkadgame.game.MainGameScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision._btMprSupport_t;

import java.util.Random;

public class Terminator extends CustomActor {

    private Texture terminatorTexture;
    private TextureRegion terminatorRegion;
    private MainGameScreen mainGameScreen;
    private Person person;
    private Random random = new java.util.Random();
    private String type = "Terminator";
    private float throwDelayTime = 1.25f;
    private float THROWDELAYTIME = 1.25f;
    private float curTime = 0f;
    private float idleTime = 0f;
    private float throwTime = 0f;
    private float agrTime = 0f;
    private float defeatTime = 0f;
    private int defeatFrame = 0;
    private int lenAgrAnim = 4;
    private int sizeX = 96;
    private int sizeY = 128;
    private int frameThrow = 0;
    private int frameIdle = 0;
    private int frameAgr = 0;
    private int agrAnim = 0;
    private boolean agrMod = false;
    private boolean alive = true;
    private boolean isThrow = true;
    private boolean throwDelay = true;

    public Terminator(Texture terminatorTexture, MainGameScreen mainGameScreen, Person person) {
        this.terminatorTexture = terminatorTexture;
        this.person = person;
        this.mainGameScreen = mainGameScreen;
        this.terminatorRegion = new TextureRegion(terminatorTexture, 0, 0, 24, 32);
        this.setSize(sizeX, sizeY);
        this.setAlive(true);
    }

    public MainGameScreen getMainGameScreen() {
        return mainGameScreen;
    }

    public boolean getAgrMod() {
        return agrMod && (this.agrAnim < lenAgrAnim);
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getFrameThrow() {
        return frameThrow;
    }

    public String getType() {
        return "Terminator";
    }

    public boolean isAlive() {
        return alive;
    }

    private void updateIdleTexture(float time) {
        idleTime += time;
        if (idleTime > 0.6) {
            idleTime = 0f;
            if (frameIdle > 0) {
                frameIdle = 0;
            } else {
                frameIdle++;
            }
            this.terminatorRegion = new TextureRegion(terminatorTexture, frameIdle * 24, 0, 24, 32);
            this.setSize(sizeX, sizeY);
        }
    }

    private void updateAgrTexture(float time) {
        agrTime += time;
        if (agrTime > 0.05f) {
            agrTime = 0f;
            if (frameAgr < 10) {
                frameAgr++;
            } else {
                frameAgr = 0;
                agrAnim++;
            }
            this.terminatorRegion = new TextureRegion(terminatorTexture, 696 + frameAgr * 24, 0, 24, 32);
            this.setSize(sizeX, sizeY);
        }
    }

    public void updateThrowTexture(float time) {
        throwTime += time;
        if (throwTime > 0.01) {
            throwTime = 0;
            if (frameThrow < 25) {
                frameThrow++;
            } else {
                throwDelayTime = THROWDELAYTIME * random.nextFloat() + 0.6f;
                frameThrow = 0;
                isThrow = false;
                throwDelay = true;
            }
            if (frameThrow == 10) {
                mainGameScreen.create_barrel(this.getX() + this.sizeX / 3.25f, this.getY() + this.sizeY / 4f);
            }
            this.terminatorRegion = new TextureRegion(terminatorTexture, 48 + frameThrow * 24, 0, 24, 32);
            this.setSize(sizeX, sizeY);
        }
    }

    public void update(float time) {
        if (isAlive()) {
            boolean can = !agrMod;
            if (!agrMod) {
                if (this.getY() - person.getY() < 800) {
                    agrMod = true;
                    THROWDELAYTIME /= 2;
                }
            } else {
                if (agrAnim < lenAgrAnim) {
                    updateAgrTexture(time);
                } else {
                    can = true;
                }
            }
            if (can) {
                if (throwDelay && curTime > throwDelayTime) {
                    isThrow = true;
                    throwDelay = false;
                    curTime = 0f;
                }
                if (isThrow) {
                    updateThrowTexture(time);
                }
                if (throwDelay) {
                    updateIdleTexture(time);
                    curTime += time;
                }
            }
        } else {
            updateDefeatTexture(time);
        }
    }

    public void updateDefeatTexture(float time) {
        defeatTime += time;
        if (defeatTime > 0.7f) {
            defeatTime = 0f;
            if (defeatFrame < 4) {
                defeatFrame++;
            } else {
                this.remove();
            }
            this.terminatorRegion = new TextureRegion(terminatorTexture, 960 + defeatFrame * 24, 0, 24, 32);
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        batch.draw(terminatorRegion, this.getX(), this.getY(), this.sizeX, this.sizeY);
    }
}
