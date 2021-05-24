package com.arkadgame.game.obj;

import com.arkadgame.game.ProcessInput;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;


public class Person extends CustomActor {

    private Texture personTexture;
    private ArrayList<CustomActor> pinchoss;
    private TextureRegion personRegion;
    private Terminator terminator;
    private float gravity = 1000f;
    private float jumpForce = 250f;
    private float runAnim = 0f;
    private float climbingAnim = 0f;
    private float standingAnim = 0f;
    private float jumpingAnim = 0f;
    private float jumpSpeed = 0f;
    private float gravitySpeed = 0f;
    private float xSpeed = 0f;
    private float ySpeed = 0f;
    private float dieTime = 0f;
    private int fallingFrame = 0;
    private int runningFrame = 0;
    private int standingFrame = 0;
    private int climbingFrame = 0;
    private int dieingFrame = 0;
    private int sizeX = 48;
    private int sizeY = 72;
    private int speed = 140;
    private boolean W = false;
    private boolean S = false;
    private boolean A = false;
    private boolean D = false;
    private boolean moveDown = false;
    private boolean isLeft = false;
    private boolean alive;
    private boolean back = false;
    private boolean onStairs = false;
    private boolean readyToJump = true;
    private boolean isWin = false;

    public void setW(boolean n) {
        W = n;
    }
    public void setS(boolean n) {
        S = n;
    }
    public void setD(boolean n) {
        D = n;
    }
    public void setA(boolean n) {
        A = n;
    }

    public void setTerminator(Terminator terminator) {
        this.terminator = terminator;
    }

    public void update(ProcessInput process, float time) {
        float m_s = 0f;
        boolean move = false, l = false, r = false;
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.gravity(time);
        if (this.isAlive()) {
            if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
                if (process.getShift()) {
                    this.speed = 320;
                } else {
                    this.speed = 260;
                }
            } else {
                this.speed = 260;
            }
            float local_speed = this.speed * time;
            if (W) {
                m_s = local_speed;
                if (!this.getOnStairs()) {
                    this.jump(time);
                } else {
                    this.move_up(local_speed);
                }
            }
            if ((S) && this.getOnStairs()) {
                this.move_down(local_speed);
            }
            if (S) {
                m_s = -local_speed;
                this.setMoveDown(true);
            } else {
                this.setMoveDown(false);
            }
            if (A) {
                this.move_left(local_speed);
                move = true;
                l = true;
            }
            if (D) {
                this.move_right(local_speed);
                move = true;
                r = true;
            }
            if (this.getOnStairs()) {
                this.updateTextureClimbing(m_s / 2);
            } else {
                if (move && this.getReadyToJump() && (!l || !r)) {
                    this.updateTextureRun(local_speed);
                } else if (!this.getReadyToJump()) {
                    this.updateTextureJump(time);
                } else {
                    this.updateTextureStanding(time);
                }
            }
        } else {
            this.updateTextureDie(time);

        }
    }

    public void gravity(float time) {
        if (!onStairs) {
            if (!this.checkCollisions()) {
                this.ySpeed -= time * time * gravity / 2;
            } else {
                gravitySpeed = 0f;
                this.ySpeed = 0f;
            }
            this.move_up(this.ySpeed);
        }
    }

    public void setMoveDown(boolean r) {
        moveDown = r;
    }

    public boolean getOnStairs() {
        return onStairs;
    }

    public boolean getReadyToJump() {
        return readyToJump;
    }

    public void setPinchoss(ArrayList<CustomActor> pinchoss) {
        this.pinchoss = pinchoss;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Person(Texture personTexture) {
        this.personTexture = personTexture;
        this.alive = true;
        this.personRegion = new TextureRegion(personTexture, 0, 120, 16, 24);
        setSize(sizeX, sizeY);
    }

    public void act(float delta) {
        super.act(delta);
    }

    public void draw(Batch batch, float parentAlpha) {
        batch.draw(personRegion, getX(), getY(), sizeX, sizeY);
    }

    private void resetAnim(int a, int b) {
        standingAnim = 0;
        runningFrame = 0;
        climbingFrame = 0;
        switch (a) {
            case 1:
                runningFrame = b;
                break;
            case 2:
                standingFrame = b;
                break;
            case 3:
                climbingFrame = b;
                break;
        }
    }

    public void updateTextureDie(float time) {
        dieTime += time;
        if (dieTime > 0.5f) {
            dieTime = 0;
            if (dieingFrame < 6) {
                dieingFrame++;
            }
        }
        personRegion = new TextureRegion(personTexture, 16 * dieingFrame, 120, 16, 24);
        if (isLeft) {
            personRegion.flip(true, false);
        }
        setSize(sizeX, sizeY);
    }

    public void updateTextureJump(float time) {
        resetAnim(0, 0);
        standingAnim = 1f;
        personRegion = new TextureRegion(personTexture, 16, 144, 16, 24);
        setSize(sizeX, sizeY);
    }

    public void updateTextureStanding(float time) {
        this.standingAnim += time;
        if (this.standingAnim > 0.75f && readyToJump && !onStairs) {
            this.standingAnim = 0f;
            resetAnim(2, standingFrame);
            if (standingFrame > 5) {
                standingFrame = 1;
            } else {
                standingFrame += 1;
            }
            personRegion = new TextureRegion(personTexture, 16 * standingFrame, 0, 16, 24);
            if (isLeft) {
                personRegion.flip(true, false);
            }
            setSize(sizeX, sizeY);
        }
    }

    public void updateTextureClimbing(float speed) {
        float ch = 2f;
        this.climbingAnim += speed;
        if (this.climbingAnim > ch && onStairs) {
            this.climbingAnim = 0f;
            resetAnim(3, climbingFrame);
            if (climbingFrame > 13) {
                climbingFrame = 0;
            } else {
                climbingFrame += 1;
            }
        }
        if (this.climbingAnim < -ch && onStairs) {
            this.climbingAnim = 0f;
            resetAnim(3, climbingFrame);
            if (climbingFrame < 1) {
                climbingFrame = 14;
            } else {
                climbingFrame -= 1;
            }
        }
        if (climbingFrame < 5) {
            personRegion = new TextureRegion(personTexture, 112 + 16 * climbingFrame, 0, 16, 24);
        } else {
            personRegion = new TextureRegion(personTexture, 16 * (climbingFrame - 5), 24, 16, 24);
        }
        setSize(sizeX, sizeY);
    }

    public void updateTextureRun(float speed) {
        runAnim += speed;
        if (runAnim > 12f && readyToJump) {
            runAnim = 0f;
            resetAnim(1, runningFrame);
            standingAnim = 1f;
            if (runningFrame < 7 && !back) {
                runningFrame += 1;
            } else if (runningFrame > 2) {
                runningFrame = 1;
                back = false;
            } else {
                back = false;
                runningFrame += 1;
            }
            personRegion = new TextureRegion(personTexture, 16 * runningFrame, 96, 16, 24);
            if (isLeft) {
                personRegion.flip(true, false);
            }
            setSize(sizeX, sizeY);
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void move_left(float speed) {
        isLeft = true;
        this.setX(this.getX() - speed);
        if (this.checkCollisions()) {
            this.setX(this.getX() + speed);
        }
    }

    public void move_right(float speed) {
        isLeft = false;
        this.setX(this.getX() + speed);
        if (this.checkCollisions()) {
            this.setX(this.getX() - speed);
        }
    }

    public void move_up(float speed) {
        this.setY(this.getY() + speed);
        if (this.checkCollisions()) {
            this.setY(this.getY() - speed);
            this.ySpeed = 0f;
        }
    }

    public void jump(float time) {
        if (this.readyToJump) {
            this.ySpeed = jumpForce * time;
        }
        this.readyToJump = false;
    }

    public void move_down(float speed) {
        this.setY(this.getY() - speed);
        if (this.checkCollisions()) {
            this.setY(this.getY() + speed);
            this.ySpeed = 0f;
        }
    }

    public boolean getIsWin() {
        return isWin;
    }

    public String getType() {
        return "Person";
    }

    public boolean checkCollisions() {
        boolean res = false, onStair = false;
        for (CustomActor pinchos : this.pinchoss) {
            boolean x_col = false, y_col = false;
            if ((this.getX() + this.getWidth() > pinchos.getX() || this.getX() > pinchos.getX()) &&
                    (pinchos.getX() + pinchos.getWidth() > this.getWidth() + this.getX() || pinchos.getX() + pinchos.getWidth() > this.getX())) {
                x_col = true;
            }
            if ((this.getY() + this.getHeight() > pinchos.getY() || this.getY() > pinchos.getY()) &&
                    (pinchos.getY() + pinchos.getHeight() > this.getHeight() + this.getY() || pinchos.getY() + pinchos.getHeight() > this.getY())) {
                y_col = true;
            }
            if (x_col && y_col && !pinchos.getType().equalsIgnoreCase("Person")&&!pinchos.getType().equalsIgnoreCase("BaseStairs")&&
                    !pinchos.getType().equalsIgnoreCase("DestroyBarrel")) {
                if (pinchos.getType().equalsIgnoreCase("Barrel")||pinchos.getType().equalsIgnoreCase("Acid")) {
                    this.setAlive(false);
                    return true;
                }
                if (pinchos.getType().equalsIgnoreCase("Terminator")) {
                    terminator.setAlive(false);
                    isWin = true;
                }
                else if (pinchos.getType().equalsIgnoreCase("Stairs")) {
                    if (pinchos.getY() < this.getY() + 32) {
                        //System.out.println(moveDown + " " + ySpeed + " " + onStair);
                        if (pinchos.getY() < this.getY()) {
                            if ((moveDown || onStairs) && ((this.getX() + this.getWidth() - 18 > pinchos.getX() || this.getX() + 18 > pinchos.getX()) && (pinchos.getX() + pinchos.getWidth() > this.getWidth() + this.getX() - 18 || pinchos.getX() + pinchos.getWidth() > this.getX() + 18))) {
                                onStair = true;
                                setX(pinchos.getX());
                            }
                        } else {
                            if ((ySpeed > 1.5f || onStairs) && ((this.getX() + this.getWidth() - 18 > pinchos.getX() || this.getX() + 18 > pinchos.getX()) &&
                                    (pinchos.getX() + pinchos.getWidth() > this.getWidth() + this.getX() - 18 || pinchos.getX() + pinchos.getWidth() > this.getX() + 18))) {
                                onStair = true;
                                setX(pinchos.getX());
                            }
                        }
                    }
                } else {
                    if ((this.getX() + this.getWidth() - 6 > pinchos.getX() || this.getX() + 6 > pinchos.getX()) &&
                            (pinchos.getX() + pinchos.getWidth() > this.getWidth() + this.getX() - 6 || pinchos.getX() + pinchos.getWidth() > this.getX() + 6) &&
                            this.getY() + this.getHeight() > pinchos.getY() && this.getY() + this.getHeight() < pinchos.getY() + pinchos.getHeight()) {
                        this.ySpeed = 0;
                    } else if ((this.getX() + this.getWidth() - 6 > pinchos.getX() || this.getX() + 6 > pinchos.getX()) &&
                            (pinchos.getX() + pinchos.getWidth() > this.getWidth() + this.getX() - 6 || pinchos.getX() + pinchos.getWidth() > this.getX() + 6)) {
                        this.readyToJump = true;
                    }
                    res = true;
                }
            }
        }
        if (onStair) {
            if (!terminator.getAgrMod()) {
                onStairs = true;
            }
            res = false;
            readyToJump = false;
        } else {
            if (onStairs) {
                this.ySpeed = 1.5f;
            }
            onStairs = false;
        }
        return res;
    }
}