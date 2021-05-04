package com.arkadgame.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import javax.xml.stream.FactoryConfigurationError;


public class ProcessInput extends InputAdapter {
    private boolean W;
    private boolean S;
    private boolean A;
    private boolean D;
    private boolean Space;
    private boolean Shift;

    @Override
    public boolean keyDown(int button) {
        if (button == Input.Keys.W) {W = true;}
        if (button == Input.Keys.S) {S = true;}
        if (button == Input.Keys.A) {A = true;}
        if (button == Input.Keys.D) {D = true;}
        if (button == Input.Keys.SPACE) {Space = true;}
        if (button == Input.Keys.SHIFT_LEFT) {Shift = true;}
        return true;
    }

    @Override
    public boolean keyUp (int button) {
        if (button == Input.Keys.W) {W = false;}
        if (button == Input.Keys.S) {S = false;}
        if (button == Input.Keys.A) {A = false;}
        if (button == Input.Keys.D) {D = false;}
        if (button == Input.Keys.SPACE) {Space = false;}
        if (button == Input.Keys.SHIFT_LEFT) {Shift = false;}
        return false;
    }

    public boolean getW() {
        return W;
    }
    public boolean getS() {
        return S;
    }
    public boolean getA() {
        return A;
    }
    public boolean getD() {
        return D;
    }
    public boolean getSpace() {return Space;}
    public boolean getShift() {
        return Shift;
    }
}
