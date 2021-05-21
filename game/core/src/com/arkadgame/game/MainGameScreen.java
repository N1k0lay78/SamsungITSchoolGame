package com.arkadgame.game;

import com.arkadgame.game.obj.Acid;
import com.arkadgame.game.obj.ActorPinchos;
import com.arkadgame.game.obj.Background;
import com.arkadgame.game.obj.Barrel;
import com.arkadgame.game.obj.CustomActor;
import com.arkadgame.game.obj.Stairs;
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

import io.socket.client.IO;
import io.socket.client.Socket;


public class MainGameScreen extends BaseScreen {
    public MainGameScreen (ArkadGame game, ProcessInput process, Socket socket) {
        super(game, process);
        menuTexture = new Texture("menu.png");
        personTexture = new Texture("player_anim.png");
        texturaPinchos = new Texture("tileset_16.png");
        regionPinchos1 = new TextureRegion(texturaPinchos, 0, 0, 16, 16);
        regionPinchos2 = new TextureRegion(texturaPinchos, 16, 0, 16, 16);
        regionPinchos3 = new TextureRegion(texturaPinchos, 32, 0, 16, 16);
        regionStairs2 = new TextureRegion(texturaPinchos, 0, 16, 16, 16);
        regionStairs1 = new TextureRegion(texturaPinchos, 16, 16, 16, 16);
    }

    private Stage stage;
    private Person person;
    private Texture personTexture, texturaPinchos, menuTexture;
    private TextureRegion regionPinchos1, regionPinchos2, regionPinchos3, regionStairs1, regionStairs2;
    private int speed = 2;
    private float minX = 500;
    private float maxX = 640;
    private float minY = 240f;
    private float maxY = 1800f;
    private int width = 1280;
    private int height = 720;
    private int cPause = 0;
    private boolean pause = false;
    private ArrayList<CustomActor> pinchos;
    private OrthographicCamera camera;
    private float scale = 1;
    private boolean clear = true;

    @Override
    public void show() {
        Gdx.input.setInputProcessor(process);
        person = new Person(personTexture);
        person.setPosition(100, 900);
        System.out.println("Game");
        camera = new OrthographicCamera(width, height);
        float c_x = person.getX(), c_y = person.getY();
        if (c_x < minX) {c_x = minX;} else if (c_x > maxX) {c_x = maxX;}
        if (c_y < minY) {c_y = minY;} else if (c_x > maxY) {c_y = maxY;}
        camera.position.set(c_x, c_y, 0f);
        camera.update();
        System.out.println(camera.viewportHeight + " " + camera.viewportWidth);
        System.out.println("CreateGame");
        this.recreate();
    }

    public boolean isClear() {return clear;}

    public void recreate() {
        clear = false;
        this.pinchos = new ArrayList<>(2000);
        stage = new Stage();
        stage.getViewport().setCamera(camera);
        this.scale = stage.getWidth() / height;
        System.out.println(scale);
        Background background = new Background(menuTexture);
        stage.addActor(background);
        System.out.println(stage.getWidth() + " " + stage.getHeight());
        this.minX = (stage.getWidth() / 2);
        this.maxX = (1056 * scale - stage.getWidth() / 2);
        this.minY = (stage.getHeight() / 2);
        if (maxX < minX) {maxX *= 2;}
        System.out.println(minX + " " + maxX + " " + minY + " " + scale);
        this.create_acid_on(0, 0, 22);
        this.create_platform_on(144, 96, 19);
        this.create_platform_on(48, 288, 17);
        this.create_stairs_on(672, 144, 4);
        this.create_platform_on(240, 480, 17);
        this.create_stairs_on(336, 336, 4);
        this.create_platform_on(48, 674, 17);
        this.create_stairs_on(684, 528, 4);
        stage.addActor(person);
        pinchos.add(person);
        this.create_barrel(500, 840);
        /*this.create_barrel(400, 1720);
        this.create_barrel(400, 950);
        this.create_barrel(400, 1050);
        this.create_barrel(400, 1200);
        this.create_barrel(400, 1350);
        this.create_barrel(400, 1500); */
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
        clear = true;
    }

    @Override
    public void render(float delta) {
        /*if (this.process.getP()) {
             if (cPause == 2) {cPause = 0; this.pause = false;} else {this.pause = true;}
        } else {cPause++;}*/
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        stage.act();
        float newY = person.getY() * scale;
        float newX = person.getX() * scale;
        if (newX < minX) {
            newX = minX;
        } else if (newX > maxX) {
            newX = maxX;
        }
        if (newY < minY) {
            newY = minY;
        }
        camera.position.lerp(new Vector3(newX, newY, 0f), 0.1f);
        camera.update();
        camera.combined.scl(scale);
        stage.getBatch().setProjectionMatrix(camera.combined);
        float time = Gdx.graphics.getDeltaTime();
        person.update(this.process, time);
        stage.getBatch().begin();
        for (Actor i : stage.getActors()) {
            i.draw(stage.getBatch(), 0);
        }
        stage.getBatch().end();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void dispose() {
        stage.dispose();
        clear = true;
    }
}