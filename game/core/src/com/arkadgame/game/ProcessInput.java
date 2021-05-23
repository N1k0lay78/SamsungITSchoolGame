package com.arkadgame.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import javax.xml.stream.FactoryConfigurationError;


public class ProcessInput extends InputAdapter {
    private boolean W;
    private boolean S;
    private boolean A;
    private boolean D;
    private boolean P;
    private boolean Space;
    private boolean Shift;
    private boolean Ctrl;
    private boolean Esc;

    @Override
    public boolean keyDown(int button) {
        if (button == Input.Keys.W) {W = true;}
        if (button == Input.Keys.S) {S = true;}
        if (button == Input.Keys.A) {A = true;}
        if (button == Input.Keys.D) {D = true;}
        if (button == Input.Keys.P) {P = true;}
        if (button == Input.Keys.ESCAPE) {Esc = true;}
        if (button == Input.Keys.SPACE) {Space = true;}
        if (button == Input.Keys.SHIFT_LEFT) {Shift = true;}
        if (button == Input.Keys.CONTROL_LEFT) {Ctrl = true;}
        return true;
    }

    @Override
    public boolean keyUp (int button) {
        if (button == Input.Keys.W) {W = false;}
        if (button == Input.Keys.S) {S = false;}
        if (button == Input.Keys.A) {A = false;}
        if (button == Input.Keys.D) {D = false;}
        if (button == Input.Keys.P) {P = false;}
        if (button == Input.Keys.ESCAPE) {Esc = false;}
        if (button == Input.Keys.SPACE) {Space = false;}
        if (button == Input.Keys.SHIFT_LEFT) {Shift = false;}
        if (button == Input.Keys.CONTROL_LEFT) {Ctrl = false;}
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
    public boolean getP() {
        return P;
    }
    public boolean getSpace() {return Space;}
    public boolean getShift() {
        return Shift;
    }
    public boolean getCtrl() {
        return Ctrl;
    }
    public boolean getEsc() {
        return Esc;
    }
}
