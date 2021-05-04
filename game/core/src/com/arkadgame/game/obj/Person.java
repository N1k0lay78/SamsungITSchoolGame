package com.arkadgame.game.obj;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;


public class Person extends Actor {

    private Texture personTexture;
    private ArrayList<Actor> pinchoss;
    private TextureRegion personRegion;
    private int fallingFrame = 0;
    private int runningFrame = 0;
    private int standingFrame = 0;
    private int climbingFrame = 0;
    private int jumpingFrame = 0;
    private float gravity = 1000f;
    private float jumpForce = 275f;
    private float runAnim = 0f;
    private float standingAnim = 0f;
    private float jumpingAnim = 0f;
    private float jumpSpeed = 0f;
    private float gravitySpeed = 0f;
    private boolean isLeft = false;
    private boolean alive;
    private boolean back = false;
    private boolean collision = false;
    private boolean onStairs = false;
    private boolean readyToJump = true;

    public void gravity(float time) {
        if (!this.collision) {
            gravitySpeed += time * time * gravity / 2;
        } else {
            gravitySpeed = 0f;
            this.jumpSpeed = 0f;
            this.collision = false;
            this.readyToJump = true;
        }
        this.move_up(this.jumpSpeed);
        this.move_down(this.gravitySpeed);
    }

    public boolean getReadyToJump() {return readyToJump;}

    public void setPinchoss(ArrayList<Actor> pinchoss) {this.pinchoss = pinchoss;}

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Person(Texture personTexture){
        this.personTexture = personTexture;
        this.alive=true;
        this.personRegion = new TextureRegion(personTexture, 0, 120, 16, 24);
        setSize(48, 72);
    }

    public void act (float delta){
        super.act(delta);
    }

    public void draw (Batch batch, float parentAlpha){
        batch.draw(personRegion, getX(), getY(), 48, 72);
    }

    private void resetAnim(int a, int b) {
        standingAnim = 0;
        runningFrame = 0;
        climbingFrame = 0;
        switch (a) {
            case 1: runningFrame = b; break;
            case 2: standingFrame = b; break;
            case 3: climbingFrame = b; break;
        }
    }

    public void updateTextureJump(float time) {
            resetAnim(0, 0);
            standingAnim = 1f;
            personRegion = new TextureRegion(personTexture, 16, 144, 16, 24);
            if (isLeft) {personRegion.flip(true, false);}
            setSize(48, 72);
    }

    public void updateTextureStanding(float time) {
        standingAnim += time;
        if (standingAnim > 0.75f && readyToJump && !onStairs) {
            standingAnim = 0f;
            resetAnim(2, standingFrame);
            if (standingFrame > 5) {standingFrame = 1;} else {standingFrame += 1;}
            personRegion = new TextureRegion(personTexture, 16 * standingFrame, 0, 16, 24);
            if (isLeft) {personRegion.flip(true, false);}
            setSize(48, 72);
        }
    }

    public void updateTextureRun(float speed) {
        runAnim += speed;
        if (runAnim > 12f && readyToJump) {
            runAnim = 0f;
            resetAnim(1, runningFrame);
            standingAnim = 1f;
            if (runningFrame < 7 && !back) {runningFrame += 1;} else if (runningFrame > 2) {runningFrame = 1; back = false;} else {back = false; runningFrame += 1;}
            personRegion = new TextureRegion(personTexture, 16 * runningFrame, 96, 16, 24);
            if (isLeft) {personRegion.flip(true, false);}
            setSize(48, 72);
        }
    }

    public void move_left(float speed) {
        isLeft = true;
        this.setX(this.getX() - speed);
        this.checkCollisions();
    }

    public void move_right(float speed) {
        isLeft = false;
        this.setX(this.getX() + speed);
        this.checkCollisions();
    }

    public void move_up(float speed) {
        this.setY(this.getY() + speed);
        this.checkCollisions();
    }

    public void jump(float time) {
        readyToJump = false;
        jumpSpeed = jumpForce * time;
    }

    public void move_down(float speed) {
        this.setY(this.getY() - speed);
        this.checkCollisions();
    }

    private void checkCollisions(){
        for (Actor pinchos: this.pinchoss) {
            boolean x_col = false, y_col = false;
            if ((this.getX()+this.getWidth()>pinchos.getX()||this.getX()>pinchos.getX())&&
                    (pinchos.getX()+pinchos.getWidth()>this.getWidth()+this.getX()||pinchos.getX()+pinchos.getWidth()>this.getX())) {
                x_col = true;
            }
            if ((this.getY()+this.getHeight()>pinchos.getY()||this.getY()>pinchos.getY())&&
                    (pinchos.getY()+pinchos.getHeight()>this.getHeight()+this.getY()||pinchos.getY()+pinchos.getHeight()>this.getY())) {
                y_col = true;
            }
            if (x_col == true&&y_col==true) {
                this.collision = true;
                if (pinchos.getY()+pinchos.getHeight()-this.getY()<this.getHeight()+this.getY()-pinchos.getY()) {
                    this.move_up(pinchos.getY() + pinchos.getHeight() - this.getY());
                } else {this.move_down(this.getHeight() + this.getY() - pinchos.getY());}
            }
        }
    }
}