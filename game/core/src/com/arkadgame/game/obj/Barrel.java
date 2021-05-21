package com.arkadgame.game.obj;

import com.badlogic.gdx.Gdx;;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision._btMprSimplex_t;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;


public class Barrel extends CustomActor {

    private ArrayList<CustomActor> pinchoss;
    private TextureRegion barrelRegion;
    private Texture barrelTexture;
    private float gravity = 2000;
    private int frame = 1;
    private float angle = 1;
    private float speedRotation = 525f;
    private float time = 0f;
    private float xSpeed = -225f;
    private float ySpeed = 0f;
    private float sx = 0f;
    private float sy = 0f;
    private float chance = 0.5f;
    private int stairsFrame = 0;
    private boolean onStairs = false;
    private float dist = 0;
    private int sizeX = 40;
    private int sizeY = 40;
    private boolean collideStairs = false;

    public Barrel(Texture barrelTexture, float sx, float sy) {
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
            this.move_up(-xSpeed * time);
            updateTextureStairs(time * xSpeed);
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
                    // Прописать пидарасу по хребтине, ненавижу!
                }
                if (!pinchos.getType().equalsIgnoreCase("Stairs")&&pinchos.getType() != "Barrel" && pinchos.getType() != "Person" && pinchos.getType() != "BaseStairs") {
                    res = true;
                }
            }
        }
        return res;
    }
}