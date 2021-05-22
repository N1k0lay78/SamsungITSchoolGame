package com.arkadgame.game.obj;

import com.badlogic.gdx.Gdx;;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision._btMprSimplex_t;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.Random;


public class Barrel extends CustomActor {

    private ArrayList<CustomActor> pinchoss;
    private TextureRegion barrelRegion;
    private Texture barrelTexture;
    private float gravity = 1925;
    private int frame = 1;
    private float angle = 1;
    private float speedRotation = 525f;
    private float time = 0f;
    private float xSpeed = -225f;
    private float ySpeed = 0f;
    private float sx = 0f;
    private float sy = 0f;
    private float chance = 0.2f;
    private int stairsFrame = 0;
    private boolean onStairs = false;
    private float dist = 0;
    private int sizeX = 40;
    private int sizeY = 40;
    private boolean collideStairs = false;
    private Person person;
    private Random random = new java.util.Random();

    public Barrel(Texture barrelTexture, Person person, float sx, float sy) {
        this.person = person;
        this.barrelTexture=barrelTexture;
        this.sx = sx;
        this.sy = sy;
        barrelRegion = new TextureRegion(barrelTexture, 32, 16, 16, 16);
        setSize(sizeX, sizeY);
    }

    public void updateTextureStairs(float dist) {
        this.dist += dist;
        if (this.dist > 20f) {
            this.dist = 0f;
            if (stairsFrame > 1) {
                stairsFrame = 0;
            } else {
                stairsFrame++;
            }
            barrelRegion = new TextureRegion(barrelTexture, 48, 16 * stairsFrame, 16, 16);
            setSize(sizeX, sizeY);
        }
    }

    public void setPinchoss(ArrayList<CustomActor> pinchoss) {this.pinchoss = pinchoss;}

    public void move (){
        time = Gdx.graphics.getDeltaTime();
        if (onStairs) {
            this.move_up(-255 * time);
            updateTextureStairs(time * 255);
        } else {
            barrelRegion = new TextureRegion(barrelTexture, 32, 16, 16, 16);
        }
        if (!this.checkCollisions()&&!onStairs) {
            this.ySpeed -= time * time * gravity / 2;
        } else {
            this.ySpeed = 0f;
        }
        this.move_up(this.ySpeed);
        if (!onStairs) {this.move_right(xSpeed * time);}
        if (this.getY() < -5f) {this.setPosition(sx, sy); this.xSpeed = -225f; speedRotation = 525f; this.ySpeed = 0f;}
    }

    public void move_up(float speed) {
        this.setY(this.getY() + speed);
        if (!onStairs) {if (checkCollisions()) {this.setY(this.getY() - speed); if (this.ySpeed < -5f) {xSpeed *= -1; speedRotation *= -1;} ySpeed = 0f;}}
    }

    public void move_right(float speed) {
        if (!onStairs) {
            this.setX(this.getX() + speed);
            if (checkCollisions()) {this.setX(this.getX() - speed);}
        }
    }

    public String getType() {
        return "Barrel";
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        move();
        if (onStairs) {angle = 0;} else {angle += speedRotation * Gdx.graphics.getDeltaTime();}
        batch.draw(barrelRegion, this.getX(), this.getY(), this.getWidth() / 2f, this.getHeight() / 2f, this.getWidth(), this.getHeight(), 1, 1, angle);
    }

    public boolean checkCollisions(){
        boolean c_s = false, res = false;
        for (CustomActor pinchos: this.pinchoss) {
            boolean x_col = false, y_col = false;
            if ((this.getX()+this.getWidth()>pinchos.getX()||this.getX()>pinchos.getX())&&
                    (pinchos.getX()+pinchos.getWidth()>this.getWidth()+this.getX()||pinchos.getX()+pinchos.getWidth()>this.getX())) {
                x_col = true;
            }
            if ((this.getY()+this.getHeight()>pinchos.getY()||this.getY()>pinchos.getY())&&
                    (pinchos.getY()+pinchos.getHeight()>this.getHeight()+this.getY()||pinchos.getY()+pinchos.getHeight()>this.getY())) {
                y_col = true;
            }
            if(x_col&&y_col) {
                if (pinchos.getType().equalsIgnoreCase("Stairs")) {
                    c_s = true;
                    if (!collideStairs) {
                        collideStairs = true;
                        if (random.nextInt(100) < (int)(chance * 100)) {
                            onStairs = true;
                            this.setX(pinchos.getX() + 4);
                        }
                    }
                }
                if (pinchos.getType().equalsIgnoreCase("Person")) {
                    person.setAlive(false);
                }
                if (!pinchos.getType().equalsIgnoreCase("Stairs")&&pinchos.getType() != "Barrel" && pinchos.getType() != "Person" && pinchos.getType() != "BaseStairs") {
                    res = true;
                }
            }
        }
        if (!c_s) {
            if (collideStairs) {
                collideStairs = false;
                if (onStairs) {
                    onStairs = false;
                    this.xSpeed *= -1;
                    this.speedRotation *= -1;
                }
            }
        }
        return res;
    }
}