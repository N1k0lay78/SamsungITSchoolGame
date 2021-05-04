package com.arkadgame.game.obj;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;


public class Person extends Actor {

    private Texture person;
    private boolean alive;
    private ArrayList<ActorPinchos> pinchoss;
    private Float gravity = 1000f;
    private Float jumpForce = 250f;
    private float jumpSpeed = 0f;
    private float gravitySpeed = 0f;
    private boolean collision = false;
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

    public void setPinchoss(ArrayList<ActorPinchos> pinchoss) {this.pinchoss = pinchoss;}

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Person(Texture person){
        this.person = person;
        this.alive=true;
        setSize(person.getWidth(), person.getHeight());
    }

    public void act (float delta){
        super.act(delta);
    }

    public void draw (Batch batch, float parentAlpha){
        batch.draw(person, getX(), getY());
    }

    public void move_left(float speed) {
        this.setX(this.getX() - speed);
        this.checkCollisions();
    }

    public void move_right(float speed) {
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
        for (ActorPinchos pinchos: this.pinchoss) {
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