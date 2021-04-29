package com.arkadgame.game;

import com.arkadgame.game.obj.ActorPinchos;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.arkadgame.game.obj.Person;


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
    private ActorPinchos pinchos;
    private Person person;
    private Texture one_punch, texturaPinchos;
    private TextureRegion regionPinchos;
    private Integer speed = 2;

    @Override
    public void show() {
        Gdx.input.setInputProcessor(process);
        stage = new Stage();
        person = new Person(one_punch);
        pinchos = new ActorPinchos(regionPinchos);
        stage.addActor(person);
        stage.addActor(pinchos);
        person.setPosition(20,100);
        pinchos.setPosition(500,100);
    }

    @Override
    public void hide() {
        stage.dispose();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        if (this.process.getShift()) {this.speed = 4;} else {this.speed = 2;}
        if (this.process.getW()) {person.setY(person.getY() + speed);}
        if (this.process.getS()) {person.setY(person.getY() - speed);}
        if (this.process.getA()) {person.setX(person.getX() - speed);}
        if (this.process.getD()) {person.setX(person.getX() + speed);}
        comprobarColisiones();
        stage.draw();
    }

    private void comprobarColisiones(){
        if (person.isAlive()&&person.getX()+person.getWidth()>pinchos.getX()){
            System.out.println("Colision");
            person.setAlive(false);
        }
    }

    @Override
    public void dispose() {
        one_punch.dispose();
    }
}