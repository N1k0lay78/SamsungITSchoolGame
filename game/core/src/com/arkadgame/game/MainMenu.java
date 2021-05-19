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
    private ProcessInput process;
    private ArkadGame game;
    private Stage stage;
    private Button playButton;
    private Texture buttonTexture, menuTexture;
    private ArrayList<CustomActor> pinchos;
    private ArrayList<Button> buttons;

    public MainMenu(ArkadGame game, ProcessInput process) {
        super(game, process);
        this.process = process;
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        buttons = new ArrayList<>(10);
        menuTexture = new Texture("menu.png");
        buttonTexture = new Texture("test_for_game.png");
        playButton = new Button(buttonTexture, 49, 12, 0, 0);
        playButton.setType("PlayButton");
        playButton.setPosition(180, 350);
        buttons.add(playButton);
        stage.addActor(new Background(menuTexture));
        stage.addActor(playButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        stage.act();
        float time = Gdx.graphics.getDeltaTime();
        boolean leftPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        if (leftPressed) {
            int firstX = Gdx.input.getX();
            int firstY = 480 - Gdx.input.getY();
            String buttonType = checkButtonPress(firstX, firstY);
            System.out.println(buttonType);
            if (buttonType.equals("PlayButton")) {this.game.setScreenMainGameScreen();}
        }
        // if (!this.process.getEsc()) {this.game.setScreenMainGameScreen(); this.game.recreateLevel();}
        stage.getBatch().begin();
        for (Actor i: stage.getActors()) {
            i.draw(stage.getBatch(), 0);
        }
        stage.getBatch().end();
    }

    private String checkButtonPress(float x, float y) {
        String buttonType = "None";
        for (Button i: buttons) {
            if (i.getX() < x&& x < i.getX() + i.sizeX * 5&&i.getY() < y&& y < i.getY() + i.sizeY * 5) {
                return i.getType();
            }
        }
        return buttonType;
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
