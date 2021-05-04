package com.arkadgame.game;

import com.arkadgame.game.obj.ActorPinchos;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.arkadgame.game.obj.Person;

import java.util.ArrayList;


public class MainGameScreen extends BaseScreen {
    public MainGameScreen (ArkadGame game, ProcessInput process) {
        super(game, process);
        this.process = process;
        one_punch = new Texture("onepunch.png");
        texturaPinchos = new Texture("pinchos.png");
        regionPinchos = new TextureRegion(texturaPinchos, 0, 64, 128, 64);
    }

    private ProcessInput process;
    private Stage stage;
    private Person person;
    private Texture one_punch, texturaPinchos;
    private TextureRegion regionPinchos;
    private Integer speed = 2;
    private ArrayList<ActorPinchos> pinchos = new ArrayList<>(1000);

    @Override
    public void show() {
        Gdx.input.setInputProcessor(process);
        stage = new Stage();
        person = new Person(one_punch);
        for (int i = 0; i < 4; i++) {
            ActorPinchos a = new ActorPinchos(regionPinchos);
            a.setPosition(i * 200,100);
            stage.addActor(a);
            this.pinchos.add(a);
        }
        stage.addActor(person);
        person.setPinchoss(this.pinchos);
        person.setPosition(20,100);
    }

    @Override
    public void hide() {
        stage.dispose();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        person.gravity(Gdx.graphics.getDeltaTime());
        stage.act();
        if (this.process.getShift()) {this.speed = 260;} else {this.speed = 140;}
        float local_speed = this.speed * Gdx.graphics.getDeltaTime();
        if (this.process.getW()) {person.jump(Gdx.graphics.getDeltaTime());}
        // if (this.process.getS()) {person.move_down(local_speed);}
        if (this.process.getA()) {person.move_left(local_speed);}
        if (this.process.getD()) {person.move_right(local_speed);}
        stage.draw();
    }

    @Override
    public void dispose() {
        one_punch.dispose();
    }
}