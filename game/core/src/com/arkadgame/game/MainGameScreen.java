package com.arkadgame.game;

import com.arkadgame.game.obj.Acid;
import com.arkadgame.game.obj.ActorPinchos;
import com.arkadgame.game.obj.Background;
import com.arkadgame.game.obj.Barrel;
import com.arkadgame.game.obj.CustomActor;
import com.arkadgame.game.obj.Stairs;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.arkadgame.game.obj.Person;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;


public class MainGameScreen extends BaseScreen {
    public MainGameScreen (ArkadGame game, ProcessInput process, Socket socket) {

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
    private Viewport viewport;
    private Person person;
    private Texture personTexture, texturaPinchos, menuTexture;
    private TextureRegion regionPinchos1, regionPinchos2, regionPinchos3, regionStairs1, regionStairs2;
    private Integer speed = 2;
    private float minX = 320;
    private float maxX = 544;
    private int width = 720;
    private int height = 1280;
    private float minY = 240f;
    private ArrayList<CustomActor> pinchos = new ArrayList<>(2000);
    private OrthographicCamera camera;

    @Override
    public void show() {
        Gdx.input.setInputProcessor(process);
        person = new Person(personTexture);
        camera = new OrthographicCamera(width, height);
        camera.position.set(100, person.getY(), 0f);
        camera.update();
        /*viewport = new FitViewport(width, height, camera);
        stage = new Stage(viewport);
        stage.getViewport().apply();*/
        System.out.println(camera.viewportHeight + " " + camera.viewportWidth);
        stage = new Stage();
        stage.getViewport().setCamera(camera);
        // stage.getViewport().setScreenSize(width, height);
        Background background = new Background(menuTexture);
        stage.addActor(background);
        this.create_acid_on(0, 0, 16);
        this.create_platform_on(240, 96, 13);
        this.create_platform_on(48, 240, 14);
        this.create_stairs_on(576, 144, 3);
        this.create_platform_on(192, 384, 14);
        this.create_stairs_on(240, 288, 3);
        this.create_platform_on(0, 530, 15);
        this.create_stairs_on(588, 432, 3);
        stage.addActor(person);
        pinchos.add(person);
        this.create_barrel(400, 620);
        this.create_barrel(400, 820);
        this.create_barrel(400, 950);
        this.create_barrel(400, 1050);
        this.create_barrel(400, 1200);
        this.create_barrel(400, 1350);
        this.create_barrel(400, 1500);
        person.setPinchoss(this.pinchos);
        person.setPosition(260, 160);
    }

    private void create_barrel(int x, int y) {
        Barrel bar = new Barrel(texturaPinchos, x, y);
        bar.setPosition(x, -1000);
        bar.setPinchoss(this.pinchos);
        stage.addActor(bar);
        this.pinchos.add(bar);
    }

    private void create_platform_on(int x, int y, int l) {
        ActorPinchos a = new ActorPinchos(regionPinchos1);
        a.setPosition(x, y);
        stage.addActor(a);
        this.pinchos.add(a);
        for (int i = 1; i < l - 1; i++) {
            a = new ActorPinchos(regionPinchos2);
            a.setPosition(x + i * 48, y);
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
            b.setPosition(x + i * 48, y);
            stage.addActor(b);
            this.pinchos.add(b);
        }
    }

    private void create_stairs_on(int x, int y, int l) {
        Stairs c = new Stairs(regionStairs1);
        c.setPosition(x, y);
        c.setType("BaseStairs");
        stage.addActor(c);
        this.pinchos.add(c);
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
        stage.act();
        float newY = person.getY();
        float newX = person.getX();
        if (newX < minX) {newX = minX;}
        else if (newX > maxX) {newX = maxX;}
        if (newY < minY) {newY = minY;}
        camera.position.lerp(new Vector3(newX, newY, 0f), 0.1f);
        camera.update();
        stage.getBatch().setProjectionMatrix(camera.combined);
        float time = Gdx.graphics.getDeltaTime();
        person.update(this.process, time);
        stage.getBatch().begin();
        for (Actor i: stage.getActors()) {
            i.draw(stage.getBatch(), 0);
        }
        stage.getBatch().end();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void dispose() {
        // dispose something
    }
}