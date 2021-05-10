package com.arkadgame.game;

import com.arkadgame.game.obj.Acid;
import com.arkadgame.game.obj.ActorPinchos;
import com.arkadgame.game.obj.Background;
import com.arkadgame.game.obj.Barrel;
import com.arkadgame.game.obj.CustomActor;
import com.arkadgame.game.obj.Stairs;
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
        menuTexture = new Texture("menu.png");
        personTexture = new Texture("player_anim.png");
        texturaPinchos = new Texture("tileset_16.png");
        regionPinchos1 = new TextureRegion(texturaPinchos, 0, 0, 16, 16);
        regionPinchos2 = new TextureRegion(texturaPinchos, 16, 0, 16, 16);
        regionPinchos3 = new TextureRegion(texturaPinchos, 32, 0, 16, 16);
        regionStairs2 = new TextureRegion(texturaPinchos, 0, 16, 16, 16);
        regionStairs1 = new TextureRegion(texturaPinchos, 16, 16, 16, 16);
    }

    private ProcessInput process;
    private Stage stage;
    private Person person;
    private Texture personTexture, texturaPinchos, menuTexture;
    private TextureRegion regionPinchos1, regionPinchos2, regionPinchos3, regionStairs1, regionStairs2;
    private Integer speed = 2;
    private ArrayList<CustomActor> pinchos = new ArrayList<>(2000);

    @Override
    public void show() {
        Gdx.input.setInputProcessor(process);
        stage = new Stage();
        Background background = new Background(menuTexture);
        stage.addActor(background);
        this.create_acid_on(0, 0, 16);
        this.create_platform_on(0, 96, 8);
        this.create_platform_on(144, 240, 8);
        this.create_stairs_on(192, 144, 3);
        person = new Person(personTexture);
        stage.addActor(person);
        pinchos.add(person);
        this.create_barrel(400, 300);
        person.setPinchoss(this.pinchos);
        person.setPosition(20,400);
    }

    private void create_barrel(int x, int y) {
        Barrel bar = new Barrel(texturaPinchos);
        bar.setPosition(x, y);
        bar.setPinchoss(this.pinchos);
        stage.addActor(bar);
        this.pinchos.add(bar);
    }

    private void create_platform_on(int x, int y, int l) {
        ActorPinchos a = new ActorPinchos(regionPinchos1);
        a.setPosition(x,y);
        stage.addActor(a);
        this.pinchos.add(a);
        for (int i = 1; i < l - 1; i++) {
            a = new ActorPinchos(regionPinchos2);
            a.setPosition(x + i * 48,y);
            stage.addActor(a);
            this.pinchos.add(a);
        }
        a = new ActorPinchos(regionPinchos3);
        a.setPosition(x + 48 * (l - 1), y);
        stage.addActor(a);
        this.pinchos.add(a);
    }

    private void create_acid_on(int x, int y, int l) {
        for (int i = 0; i < l; i++) {
            Acid b = new Acid(texturaPinchos);
            b.setPosition(x + i * 48,y);
            stage.addActor(b);
            this.pinchos.add(b);
        }
    }

    private void create_stairs_on(int x, int y, int l) {
        Stairs c = new Stairs(regionStairs1);
        c.setPosition(x, y);
        stage.addActor(c);
        for (int i = 1; i < l; i++) {
            c = new Stairs(regionStairs2);
            c.setPosition(x, y + i * 48);
            stage.addActor(c);
            this.pinchos.add(c);
        }
    }

    @Override
    public void hide() {
        stage.dispose();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        float time = Gdx.graphics.getDeltaTime();
        float m_s = 0f;
        boolean move = false, l = false, r = false;
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        person.gravity(time);
        stage.act();
        if (this.process.getShift()) {this.speed = 260;} else {this.speed = 140;}
        float local_speed = this.speed * time;
        if (this.process.getW()||this.process.getSpace()) {m_s = local_speed; if (!person.getOnStairs()) {person.jump(time);} else {person.move_up(local_speed);}}
        if ((this.process.getS()||this.process.getCtrl())&&person.getOnStairs()) {person.move_down(local_speed);}
        if (this.process.getS()||this.process.getCtrl()) {m_s = -local_speed; person.setMoveDown(true);} else {person.setMoveDown(false);}
        if (this.process.getA()) {person.move_left(local_speed); move = true; l = true;}
        if (this.process.getD()) {person.move_right(local_speed); move = true; r = true;}
        if (person.getOnStairs()) {person.updateTextureClimbing(m_s);} else {
        if (move&&person.getReadyToJump()&&(!l||!r)) {person.updateTextureRun(local_speed);} else if (!person.getReadyToJump()){
            person.updateTextureJump(time);
        } else{person.updateTextureStanding(time); }}
        stage.draw();
    }

    @Override
    public void dispose() {
        // dispose something
    }
}