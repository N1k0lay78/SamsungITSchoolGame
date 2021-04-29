package com.arkadgame.game;

import com.badlogic.gdx.Screen;


public abstract class BaseScreen implements Screen {
    private ProcessInput process;
    private ArkadGame game;

    public BaseScreen(ArkadGame game, ProcessInput process) {
        this.game = game;
        this.process = process;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}