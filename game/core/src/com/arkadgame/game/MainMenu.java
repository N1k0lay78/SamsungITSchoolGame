package com.arkadgame.game;

import com.arkadgame.game.obj.Background;
import com.arkadgame.game.obj.Button;
import com.arkadgame.game.obj.CustomActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.ArrayList;

public class MainMenu extends BaseScreen {
    private Stage stage;
    private Button playButton;
    private Texture buttonTexture, menuTexture;
    private ArrayList<CustomActor> pinchos;
    private ArrayList<Button> buttons;
    private int width = 720;
    private int height = 480;
    private String currButton = "None";
    private float scale = 1;
    private boolean clear = true;

    public MainMenu(ArkadGame game, ProcessInput process) {
        super(game, process);
    }

    @Override
    public void show() {
        clear = false;
        stage = new Stage();
        scale = stage.getWidth() / width;
        buttons = new ArrayList<>(100);
        menuTexture = new Texture("menu.png");
        buttonTexture = new Texture("buttons.png");
        playButton = new Button(buttonTexture, 133, 32, 128, 0, 2f * scale,true);
        playButton.setType("PlayButton");
        playButton.setPosition(180, 150);
        buttons.add(playButton);
        stage.addActor(new Background(menuTexture));
        stage.addActor(playButton);
        System.out.println("CreateMenu");
    }

    public boolean isClear() {return clear;}

    public String getCurrButton() {
        return currButton;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        stage.act();
        float time = Gdx.graphics.getDeltaTime();
        boolean leftPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        float firstX = Gdx.input.getX();
        float firstY = stage.getHeight() - Gdx.input.getY();
        checkButtonPress(firstX, firstY, leftPressed);
        stage.getBatch().begin();
        stage.getCamera().combined.scl(scale);
        for (Actor i: stage.getActors()) {
            i.draw(stage.getBatch(), 0);
        }
        stage.getBatch().end();
    }

    private void checkButtonPress(float x, float y, boolean press) {
        currButton = "None";
        for (Button i: buttons) {
            if (i.checkCollision(x, y)) {
                i.setActive(true);
                if (press) {currButton = i.getType();}
            } else {
                i.setActive(false);
            }
        }
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
        show();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        stage.dispose();
        clear = true;
    }

    @Override
    public void dispose() {
        clear = true;
    }

    public void clear() {
        currButton = "None";
    }
}
