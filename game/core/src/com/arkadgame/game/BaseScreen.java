package com.arkadgame.game;

import com.badlogic.gdx.Screen;


public class BaseScreen implements Screen {
    private ProcessInput process;
    private ArkadGame game;
    private MainMenu mainMenu;
    private MainGameScreen mainGameScreen;
    private boolean a = false;

    public BaseScreen(ArkadGame game, ProcessInput process) {
        this.mainMenu = new MainMenu(process);
        this.game = game;
        this.process = process;
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (this.process.getSpace()&&!a) {
            this.a = true;
            game.setScreen(this.mainMenu);
        } else if (a) {this.a = false; game.setScreen(this.mainGameScreen);}

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