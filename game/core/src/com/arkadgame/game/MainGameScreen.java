package com.arkadgame.game;

import com.arkadgame.game.obj.Acid;
import com.arkadgame.game.obj.ActorPinchos;
import com.arkadgame.game.obj.Background;
import com.arkadgame.game.obj.Barrel;
import com.arkadgame.game.obj.Button;
import com.arkadgame.game.obj.CustomActor;
import com.arkadgame.game.obj.Stairs;
import com.arkadgame.game.obj.Terminator;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.arkadgame.game.obj.Person;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.omg.CORBA.PRIVATE_MEMBER;

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
        buttonTexture = new Texture(Gdx.files.internal("buttons.png"));
        regionPinchos1 = new TextureRegion(texturaPinchos, 0, 0, 16, 16);
        regionPinchos2 = new TextureRegion(texturaPinchos, 16, 0, 16, 16);
        regionPinchos3 = new TextureRegion(texturaPinchos, 32, 0, 16, 16);
        regionStairs2 = new TextureRegion(texturaPinchos, 0, 16, 16, 16);
        regionStairs1 = new TextureRegion(texturaPinchos, 16, 16, 16, 16);
    }

    private Stage stage;
    private SpriteBatch batch;
    private Person person;
    private Terminator terminator;
    private Texture terminatorTexture, personTexture, texturaPinchos, menuTexture, buttonTexture;
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
    private ArrayList<Button> buttons;
    private boolean clear = true;
    private float offset;
    private float zoom;

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
        createButtons();
    }

    public boolean isClear() {
        return clear;
    }

    public void recreate() {
        clear = false;
        this.pinchos = new ArrayList<>(2000);
        batch = new SpriteBatch();
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
        this.checkButtons(delta);
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
        person.update(this.process, delta);
        terminator.update(delta);
        stage.getBatch().begin();
        for (Actor i : stage.getActors()) {
            i.draw(stage.getBatch(), 0);
        }
        stage.getBatch().end();
        batch.begin();
        for (Button butt: buttons) {
            butt.draw(batch, 1f);
        }
        batch.end();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
        this.width = width;
        this.height = height;
        this.offset = height * 0.03f;
        batch.getProjectionMatrix().setToOrtho2D(0, 0, this.width, this.height);
        zoom = height/200f;
        createButtons();
    }

    private void createButtons() {
        buttons = new ArrayList<>(10);
        Button button;
        button = new Button(buttonTexture, 32, 32, 0, 0, zoom, true);
        button.setType("LeftButton");
        button.setPosition(offset, offset);
        buttons.add(button);
        button = new Button(buttonTexture, 32, 32, 64, 0, zoom, true);
        button.setType("RightButton");
        button.setPosition(offset*2 + button.getWidth(), offset);
        buttons.add(button);
        button = new Button(buttonTexture, 32, 32, 0, 32, zoom, true);
        button.setType("UpButton");
        System.out.println(height - offset + " " + height);
        button.setPosition(width - offset - button.getWidth(), offset * 2 + button.getHeight());
        buttons.add(button);
        button = new Button(buttonTexture, 32, 32, 64, 32, zoom, true);
        button.setType("DownButton");
        button.setPosition(width- offset - button.getWidth(), offset);
        buttons.add(button);
    }

    private void checkButtons(float time) {
        person.setA(false);
        person.setD(false);
        person.setS(false);
        person.setW(false);
        for (int i = 0; i < 2; i++) {
            float x = Gdx.input.getX(i);
            float y = height - Gdx.input.getY(i);
            boolean press = Gdx.input.isTouched(i);
            for (Button butt : buttons) {
                if (butt.checkCollision(x, y) && press && butt.getType().equalsIgnoreCase("LeftButton") || process.getA()) {
                    person.setA(true);
                }
                if (butt.checkCollision(x, y) && press && butt.getType().equalsIgnoreCase("RightButton") || process.getD()) {
                    person.setD(true);
                }
                if (butt.checkCollision(x, y) && press && butt.getType().equalsIgnoreCase("UpButton") || process.getW() || process.getSpace()) {
                    person.setW(true);
                }
                if (butt.checkCollision(x, y) && press && butt.getType().equalsIgnoreCase("DownButton") || process.getS() || process.getCtrl()) {
                    person.setS(true);
                }
            }
        }
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