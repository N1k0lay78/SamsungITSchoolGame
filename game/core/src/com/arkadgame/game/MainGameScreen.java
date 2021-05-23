package com.arkadgame.game;

import com.arkadgame.game.obj.Acid;
import com.arkadgame.game.obj.ActorPinchos;
import com.arkadgame.game.obj.Background;
import com.arkadgame.game.obj.Barrel;
import com.arkadgame.game.obj.CustomActor;
import com.arkadgame.game.obj.Stairs;
import com.arkadgame.game.obj.Terminator;
import com.badlogic.gdx.Game;
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
import java.util.Random;

import io.socket.client.IO;
import io.socket.client.Socket;


public class MainGameScreen extends BaseScreen {
    public MainGameScreen(ArkadGame game, ProcessInput process, Socket socket) {
        super(game, process);
        menuTexture = new Texture("menu.png");
        personTexture = new Texture("player_anim.png");
        terminatorTexture = new Texture("terminator.png");
        texturaPinchos = new Texture("tileset_16.png");
        regionPinchos1 = new TextureRegion(texturaPinchos, 0, 0, 16, 16);
        regionPinchos2 = new TextureRegion(texturaPinchos, 16, 0, 16, 16);
        regionPinchos3 = new TextureRegion(texturaPinchos, 32, 0, 16, 16);
        regionStairs2 = new TextureRegion(texturaPinchos, 0, 16, 16, 16);
        regionStairs1 = new TextureRegion(texturaPinchos, 16, 16, 16, 16);
    }

    private Stage stage;
    private Person person;
    private Terminator terminator;
    private Texture personTexture, texturaPinchos, menuTexture, terminatorTexture;
    private TextureRegion regionPinchos1, regionPinchos2, regionPinchos3, regionStairs1, regionStairs2;
    private ArrayList<CustomActor> pinchos;
    private OrthographicCamera camera;
    private Random random = new java.util.Random();
    private float offsetX = 0f;
    private float offsetY = 0f;
    private float offsetTime = 0f;
    private float minX = 500;
    private float maxX = 640;
    private float minY = 240f;
    private float maxY = 1800f;
    private float scale = 1;
    private int speed = 2;
    private int width = 1440;
    private int height = 810;
    private int cPause = 0;
    private boolean pause = false;
    private boolean clear = true;

    @Override
    public void show() {
        Gdx.input.setInputProcessor(process);
        person = new Person(personTexture);
        terminator = new Terminator(terminatorTexture, this, person);
        terminator.setPosition(100, 720);
        camera = new OrthographicCamera(width, height);
        float c_x = person.getX(), c_y = person.getY();
        if (c_x < minX) {
            c_x = minX;
        } else if (c_x > maxX) {
            c_x = maxX;
        }
        if (c_y < minY) {
            c_y = minY;
        } else if (c_x > maxY) {
            c_y = maxY;
        }
        camera.position.set(c_x, c_y, 0f);
        camera.update();
        System.out.println("CreateGame");
        this.recreate();
    }

    public boolean isClear() {
        return clear;
    }

    public void recreate() {
        clear = false;
        this.pinchos = new ArrayList<>(2000);
        stage = new Stage();
        stage.getViewport().setCamera(camera);
        this.scale = stage.getWidth() / height;
        Background background = new Background(menuTexture);
        stage.addActor(background);
        this.minX = (stage.getWidth() / 2);
        this.maxX = (1056 * scale - stage.getWidth() / 2);
        this.minY = (stage.getHeight() / 2);
        if (maxX < minX) {
            maxX *= 2;
        }
        this.create_acid_on(0, 0, 22);
        this.create_platform_on(144, 96, 19);
        this.create_platform_on(48, 288, 17);
        this.create_stairs_on(672, 144, 4);
        this.create_platform_on(240, 480, 17);
        this.create_stairs_on(336, 336, 4);
        this.create_platform_on(48, 674, 17);
        this.create_stairs_on(684, 530, 4);
        stage.addActor(person);
        pinchos.add(person);
        stage.addActor(terminator);
        person.setPinchoss(this.pinchos);
        person.setPosition(260, 160);
    }

    public void create_barrel(float x, float y) {
        Barrel bar = new Barrel(texturaPinchos, person, x, y, terminator);
        bar.setPosition(x, y);
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
        clear = true;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        stage.act();
        if (terminator.getAgrMod() && offsetTime > 0.1) {
            offsetTime = 0;
            this.createOffset(75, 75);
        } else {
            if (offsetTime > 0.1) {
                offsetX = 0;
                offsetY = 0;
            }
        }
        if (!person.isAlive()) {
            // To do something
        }
        float newY = person.getY() * scale + offsetY;
        float newX = person.getX() * scale + offsetX;
        if (newX < minX) {
            newX = minX;
        } else if (newX > maxX) {
            newX = maxX;
        }
        if (newY < minY) {
            newY = minY;
        }
        offsetTime += delta;
        camera.position.lerp(new Vector3(newX, newY, 0f), 0.1f);
        camera.update();
        camera.combined.scl(scale);
        stage.getBatch().setProjectionMatrix(camera.combined);
        person.update(this.process, delta);
        terminator.update(delta);
        stage.getBatch().begin();
        for (Actor i : stage.getActors()) {
            i.draw(stage.getBatch(), 0);
        }
        stage.getBatch().end();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    public void createOffset(int x, int y) {
        offsetTime = 0;
        offsetX = random.nextInt(x);
        if (random.nextInt(1) == 1) {
            offsetX *= -1;
        }
        offsetY = random.nextInt(y);
        if (random.nextInt(1) == 1) {
            offsetY *= -1;
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        clear = true;
    }
}