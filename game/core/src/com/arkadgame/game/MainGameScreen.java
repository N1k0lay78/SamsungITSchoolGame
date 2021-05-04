package com.arkadgame.game;

import com.arkadgame.game.obj.Acid;
import com.arkadgame.game.obj.ActorPinchos;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.arkadgame.game.obj.Person;

import java.util.ArrayList;


public class MainGameScreen extends BaseScreen {
    public MainGameScreen (ArkadGame game, ProcessInput process) {
        super(game, process);
        this.process = process;
        personTexture = new Texture("player_anim.png");
        texturaPinchos = new Texture("tileset_16.png");
        regionPinchos1 = new TextureRegion(texturaPinchos, 0, 0, 16, 16);
        regionPinchos2 = new TextureRegion(texturaPinchos, 16, 0, 16, 16);
        regionPinchos3 = new TextureRegion(texturaPinchos, 32, 0, 16, 16);
    }

    private ProcessInput process;
    private Stage stage;
    private Person person;
    private Texture personTexture, texturaPinchos;
    private TextureRegion regionPinchos1, regionPinchos2, regionPinchos3;
    private Integer speed = 2;
    private ArrayList<Actor> pinchos = new ArrayList<>(1000);

    @Override
    public void show() {
        Gdx.input.setInputProcessor(process);
        stage = new Stage();
        person = new Person(personTexture);
        ActorPinchos a = new ActorPinchos(regionPinchos1);
        a.setPosition(0,100);
        stage.addActor(a);
        this.pinchos.add(a);
        for (int i = 1; i < 8; i++) {
            a = new ActorPinchos(regionPinchos2);
            a.setPosition(i * 48,100);
            stage.addActor(a);
            this.pinchos.add(a);
        }
        for (int i = 0; i < 16; i++) {
            Acid b = new Acid(texturaPinchos);
            b.setPosition(i * 48,0);
            stage.addActor(b);
            this.pinchos.add(b);
        }
        a = new ActorPinchos(regionPinchos3);
        a.setPosition(384,100);
        stage.addActor(a);
        this.pinchos.add(a);
        stage.addActor(person);
        person.setPinchoss(this.pinchos);
        person.setPosition(20,400);
    }

    @Override
    public void hide() {
        stage.dispose();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        float time = Gdx.graphics.getDeltaTime();
        boolean move = false, l = false, r = false;
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        person.gravity(time);
        stage.act();
        if (this.process.getShift()) {this.speed = 260;} else {this.speed = 140;}
        float local_speed = this.speed * time;
        if (this.process.getW()||this.process.getSpace()) {person.jump(time);}
        // if (this.process.getS()) {person.move_down(local_speed);}
        if (this.process.getA()) {person.move_left(local_speed); move = true; l = true;}
        if (this.process.getD()) {person.move_right(local_speed); move = true; r = true;}
        if (move&&person.getReadyToJump()&&((l&&!r)||(!l&&r))) {person.updateTextureRun(local_speed);} else if (!person.getReadyToJump()){
            person.updateTextureJump(time);
        } else{person.updateTextureStanding(time);}
        stage.draw();
    }

    @Override
    public void dispose() {
        // dispose something
    }
}