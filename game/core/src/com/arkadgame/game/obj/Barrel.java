package com.arkadgame.game.obj;

import com.badlogic.gdx.Gdx;;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;


public class Barrel extends CustomActor {

    private ArrayList<CustomActor> pinchoss;
    private TextureRegion barrelRegion;
    private Texture barrelTexture;
    private float gravity = 1400;
    private int frame = 1;
    private float angle = 1;
    private float speedRotation = 525f;
    private float time = 0f;
    private float xSpeed = -225f;
    private float ySpeed = 0f;
    private float sx = 0f;
    private float sy = 0f;

    public Barrel(Texture barrelTexture, float sx, float sy) {
        this.barrelTexture=barrelTexture;
        this.sx = sx;
        this.sy = sy;
        barrelRegion = new TextureRegion(barrelTexture, 32, 16, 16, 16);
        setSize(40, 40);
    }

    public void setPinchoss(ArrayList<CustomActor> pinchoss) {this.pinchoss = pinchoss;}

    public void move (){
        time = Gdx.graphics.getDeltaTime();
        if (!this.checkCollisions()) {
            this.ySpeed -= time * time * gravity / 2;
        } else {
            this.ySpeed = 0f;
        }
        this.move_up(this.ySpeed);
        this.move_right(xSpeed * time);
        if (this.getY() < -5f) {this.setPosition(sx, sy); this.xSpeed = -225f; speedRotation = 525f;}
    }

    public void move_up(float speed) {
        this.setY(this.getY() + speed);
        if (checkCollisions()) {this.setY(this.getY() - speed); if (this.ySpeed < -3f) {xSpeed *= -1; speedRotation *= -1;} ySpeed = 0f;}
    }

    public void move_right(float speed) {
        this.setX(this.getX() + speed);
        if (checkCollisions()) {this.setX(this.getX() - speed);}}

    public String getType() {
        return "Barrel";
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        move();
        angle += speedRotation * Gdx.graphics.getDeltaTime();
        batch.draw(barrelRegion, this.getX(), this.getY(), this.getWidth() / 2f, this.getHeight() / 2f, this.getWidth(), this.getHeight(), 1, 1, angle);
    }

    public boolean checkCollisions(){
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
            if (x_col&&y_col&&pinchos.getType() != "Stairs"&&pinchos.getType() != "Barrel"&&pinchos.getType() != "Person" &&pinchos.getType() != "BaseStairs") {
                return true;
            }
        }
        return false;
    }
}