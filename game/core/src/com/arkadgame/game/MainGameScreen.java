package com.arkadgame.game;

import com.arkadgame.game.obj.Acid;
import com.arkadgame.game.obj.ActorPinchos;
import com.arkadgame.game.obj.Background;
import com.arkadgame.game.obj.Barrel;
import com.arkadgame.game.obj.Button;
import com.arkadgame.game.obj.CustomActor;
import com.arkadgame.game.obj.Decorations;
import com.arkadgame.game.obj.Stairs;
import com.arkadgame.game.obj.Terminator;
import com.arkadgame.game.obj.Text;
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
    private ArrayList<Button> buttons;
    private ArrayList<Text> texts;
    private float offsetX = 0f;
    private float offsetY = 0f;
    private float offsetTime = 0f;
    private float minX = 500;
    private float maxX = 640;
    private float minY = 240f;
    private float maxY = 4000f;
    private float scale = 1;
    private float newX = 0;
    private float newY = 0;
    private float offset;
    private float zoom;
    private float cameraY = 0;
    private float cameraX = 0;
    private final int WIDTH = 1920;//!!!!
    private final int HEIGHT = 1080;
    private int speed = 2;
    private int width = 1920; //1440
    private int height = 1080;//810
    private int cPause = 0;
    private int cameraSpeed = -145;
    private boolean showResetButtonBool;
    private boolean showGameOverBool;
    private boolean pause = false;
    private boolean clear = true;
    private boolean agrMusic;
    private boolean BeautifulCamera = false;
    private boolean showWin = false;
    private String currButton = "None";
    private boolean musicWin;


    public void createBeautifulCamera() {
        cameraX = 300;
        cameraY = 192 * 20 - 400;
    }

    public void updateBeautifulCamera(float time) {
        cameraY += cameraSpeed * time;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(process);
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        batch = new SpriteBatch();
        stage = new Stage();
        createButtons();
        createUI();
        createBeautifulCamera();
        BeautifulCamera = false;
        this.recreate();
    }

    public void recreate() {
        person = new Person(personTexture);
        terminator = new Terminator(terminatorTexture, this, person);
        agrMusic = false;
        musicWin = false;
        showResetButtonBool = false;
        showGameOverBool = false;
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
        game.setMusic(1);
        agrMusic = false;
        camera.position.set(c_x, c_y, 0f);
        camera.update();
        System.out.println("CreateGame");
        clear = false;
        this.pinchos = new ArrayList<>(2000);
        stage.getViewport().setCamera(camera);
        this.scale = stage.getWidth() / HEIGHT;
        Background background = new Background(menuTexture);
        stage.addActor(background);
        this.minX = (stage.getWidth() / 2);
        this.maxX = (1056 * scale - stage.getWidth() / 2);
        this.minY = (stage.getHeight() / 2);
        if (maxX < minX) {
            maxX = minX;
        }
        int layer = 0;
        int step = 48;
        int stairs = 8;
        int newStairs = -10;
        int lastStairs = -10;
        int secondLastStairs = -10;
        for (int i = stairs; i > 0; i--) {
            layer = i * 4 + 2;
            if (i % 2 == 0) {
                this.create_platform_on(step, layer * step, 17);
                if (i < stairs - 1) {
                    lastStairs = 17 - random.nextInt(13);
                    this.create_stairs_on(step * lastStairs, (layer + 1) * step, 4);
                    if (random.nextInt(2) == 1) {
                        do {
                            newStairs = 17 - random.nextInt(13);
                        }
                        while (Math.abs(newStairs - lastStairs) < 5 || Math.abs(newStairs - secondLastStairs) < 2);
                        this.create_stairs_on(step * newStairs, (layer + 1) * step, 4);
                    }
                } else if (stairs == i) {
                    Decorations barrels = new Decorations(texturaPinchos, 0, 48, 48, 32);
                    barrels.setPosition(step, (layer + 1) * step);
                    stage.addActor(barrels);
                    terminator.setPosition(3 * step, (layer + 1) * step);
                    pinchos.add(terminator);
                }
                System.out.println(stairs + " " + i);
            } else {
                this.create_platform_on(5 * step, layer * step, 17);
                if (stairs - 1 == i) {
                    this.create_stairs_on(step * 15, (layer + 1) * step, 4);
                } else {
                    lastStairs = 5 + random.nextInt(13);
                    this.create_stairs_on(step * lastStairs, (layer + 1) * step, 4);
                    if (random.nextInt(2) == 1) {
                        do {
                            newStairs = 5 + random.nextInt(13);
                        }
                        while (Math.abs(newStairs - lastStairs) < 5 || Math.abs(newStairs - secondLastStairs) < 2);
                        this.create_stairs_on(step * newStairs, (layer + 1) * step, 4);
                    }
                }
            }
            secondLastStairs = newStairs;
            newStairs = -1;
        }
        //System.out.println("SPAWN");
        person.setPosition(360, 3 * step);
        this.create_acid_on(0, 0, 24);
        this.create_platform_on(0, 2 * step, 17);
        this.create_stairs_on(step * 10, 3 * step, 4);
        person.setPinchoss(this.pinchos);
        stage.addActor(person);
        pinchos.add(person);
        stage.addActor(terminator);
        person.setTerminator(terminator);
    }

    public boolean isClear() {
        return clear;
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
        if (terminator.getAgrMod()) {
            if (!agrMusic) {
                agrMusic = true;
                game.setMusic(2);
            }
            if (offsetTime > 0.1) {
                offsetTime = 0;
                this.createOffset(125, 125);
            }
        }
        if (offsetTime > 0.1) {
            offsetX = 0;
        if (!person.isAlive()) {
            showResetButton();
            hideShowWin();
            showShowGameOver();
            offsetY = 0;
        }
        } else {
            hideResetButton();
            hideShowGameOver();
            if (person.getIsWin()) {
                showShowWin();
            } else {
                hideShowWin();
            }
        }
        if (person.getIsWin()&&!musicWin) {
            musicWin = true;
            agrMusic = false;
            game.setMusic(3);
        }
        person.setAlive(true);
        this.checkButtons(delta);
        if (terminator.getAgrMod()) {
            newX = terminator.getX() * scale + offsetX;
            newY = terminator.getY() * scale + offsetY;
        } else {
            newX = person.getX() * scale + offsetX;
            newY = person.getY() * scale + offsetY;
        }
        if (BeautifulCamera) {
            updateBeautifulCamera(delta);
            newX = cameraX;
            newY = cameraY;
        }
        if (newX < minX) {
            newX = minX;
        } else if (newX > maxX) {
            newX = maxX;
        }
        if (newY < minY) {
            newY = minY;
            if (BeautifulCamera) {
                BeautifulCamera = false;
            }
        }
        offsetTime += delta;
        camera.position.lerp(new Vector3(newX, newY, 0f), 0.1f);
        camera.update();
        camera.combined.scl(scale);
        stage.getBatch().setProjectionMatrix(camera.combined);
        person.update(this.process, delta);
        terminator.update(delta);
        stage.getBatch().begin();
        ArrayList<CustomActor> removeList = new ArrayList<CustomActor>(100);
        for (CustomActor cs : this.pinchos) {
            if (cs.getType().equalsIgnoreCase("Remove")) {
                removeList.add(cs);
            }
        }
        for (CustomActor cs : removeList) {
            this.pinchos.remove(cs);
        }
        for (Actor i : stage.getActors()) {
            i.draw(stage.getBatch(), 0);
        }
        stage.getBatch().end();
        if (!BeautifulCamera) {
            batch.begin();
            for (Button butt : buttons) {
                butt.draw(batch, 1f);
            }
            for (Text text : texts) {
                text.draw(batch, 1f);
                text.update(delta);
            }
            batch.end();
        }
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
        this.width = width;
        this.height = height;
        this.offset = height * 0.03f;
        batch.getProjectionMatrix().setToOrtho2D(0, 0, this.width, this.height);
        zoom = height / 150f;
        createButtons();
        createUI();
    }

    private void createUI() {
        texts = new ArrayList<>(10);
        showGameOverBool = true;
        showWin = true;
        Text text = new Text(buttonTexture, 240, 64, 96, 64, zoom * 0.6f);
        text.setPosition(-1000, (height * 1.2f - text.getHeight()) / 2);
        text.setType("GameOver");
        texts.add(text);
        text = new Text(buttonTexture, 448, 64, 80, 64, zoom * 0.6f);
        text.setPosition(-1000, (height * 1.2f - text.getHeight()) / 2);
        text.setType("Win");
        texts.add(text);
    }

    private void showResetButton() {
        if (!showResetButtonBool) {
            showResetButtonBool = true;
            for (Button but : buttons) {
                if (but.getType().equalsIgnoreCase("ResetButton")) {
                    but.setPosition(offset, height - offsetY - but.getHeight());
                }
            }
        }
    }

    private void hideResetButton() {
        if (showResetButtonBool) {
            showResetButtonBool = false;
            for (Button but : buttons) {
                if (but.getType().equalsIgnoreCase("ResetButton")) {
                    but.setPosition(-1000, height - offsetY - but.getHeight());
                }
            }
        }
    }

    private void showShowWin() {
        if (!showWin) {
            showWin = true;
            for (Text text : texts) {
                if (text.getType().equalsIgnoreCase("Win")) {
                    text.setPosition((width - text.getWidth()) / 2, (height * 1.2f - text.getHeight()) / 2);
                }
            }
        }
    }

    private void hideShowWin() {
        if (showWin) {
            showWin = false;
            for (Text text : texts) {
                if (text.getType().equalsIgnoreCase("Win")) {
                    text.setPosition(-1000, height - offsetY - text.getHeight());
                }
            }
        }
    }

    private void showShowGameOver() {
        if (!showGameOverBool) {
            showGameOverBool = true;
            for (Text text : texts) {
                if (text.getType().equalsIgnoreCase("GameOver")) {
                    text.setPosition((width - text.getWidth()) / 2, (height * 1.2f - text.getHeight()) / 2);
                }
            }
        }
    }

    private void hideShowGameOver() {
        if (showGameOverBool) {
            showGameOverBool = false;
            for (Text text : texts) {
                if (text.getType().equalsIgnoreCase("GameOver")) {
                    text.setPosition(-1000, height - offsetY - text.getHeight());
                }
            }
        }
    }

    private void createButtons() {
        buttons = new ArrayList<>(10);
        Button button;
        button = new Button(buttonTexture, 32, 32, 0, 0, zoom, false);
        button.setType("LeftButton");
        button.setPosition(offset, offset);
        buttons.add(button);
        button = new Button(buttonTexture, 32, 32, 64, 0, zoom, false);
        button.setType("RightButton");
        button.setPosition(offset * 2 + button.getWidth(), offset);
        buttons.add(button);
        button = new Button(buttonTexture, 32, 32, 0, 32, zoom, false);
        button.setType("UpButton");
        System.out.println(height - offset + " " + height);
        button.setPosition(width - offset - button.getWidth(), offset * 2 + button.getHeight());
        buttons.add(button);
        button = new Button(buttonTexture, 32, 32, 64, 32, zoom, false);
        button.setType("DownButton");
        button.setPosition(width - offset - button.getWidth(), offset);
        buttons.add(button);
        button = new Button(buttonTexture, 32, 32, 0, 160, zoom, false);
        button.setType("MenuButton");
        button.setPosition(width - offset - button.getWidth(), height - offset - button.getHeight());
        buttons.add(button);
        showResetButtonBool = true;
        button = new Button(buttonTexture, 32, 32, 64, 64, zoom, false);
        button.setType("ResetButton");
        button.setPosition(-1000, height - offsetY - button.getHeight());
        buttons.add(button);
    }

    private void checkButtons(float time) {
        currButton = "None";
        float x = Gdx.input.getX(), x2 = Gdx.input.getX(1);
        float y = height - Gdx.input.getY(), y2 = height - Gdx.input.getY(1);
        boolean press = Gdx.input.isButtonPressed(0), press2 = Gdx.input.isTouched(1);
        for (Button butt : buttons) {
            if (butt.getType().equalsIgnoreCase("LeftButton")) {
                if (butt.checkCollision2(press, x, y, press2, x2, y2) || process.getA()) {
                    person.setA(true);
                } else {
                    person.setA(false);
                }
            }
            if (butt.getType().equalsIgnoreCase("RightButton")) {
                if (butt.checkCollision2(press, x, y, press2, x2, y2) || process.getD()) {
                    person.setD(true);
                } else {
                    person.setD(false);
                }
            }
            if (butt.getType().equalsIgnoreCase("UpButton")) {
                if (butt.checkCollision2(press, x, y, press2, x2, y2) || process.getW() || process.getSpace()) {
                    person.setW(true);
                } else {
                    person.setW(false);
                }
            }
            if (butt.getType().equalsIgnoreCase("DownButton")) {
                if (butt.checkCollision2(press, x, y, press2, x2, y2) || process.getS() || process.getCtrl()) {
                    person.setS(true);
                } else {
                    person.setS(false);
                }
            }
            if (butt.getType().equalsIgnoreCase("ResetButton")) {
                if (butt.checkCollision2(press, x, y, press2, x2, y2)) {
                    this.recreate();
                }
            }
            if (butt.getType().equalsIgnoreCase("MenuButton")) {
                if (butt.checkCollision(x, y)&&press) {
                    this.currButton = butt.getType();
                }
            }
        }
    }

    public void createOffset(int x, int y) {
        offsetTime = 0;
        offsetX = random.nextInt(x);
        if (random.nextInt(2) == 1) {
            offsetX *= -1;
        }
        offsetY = random.nextInt(y);
        if (random.nextInt(2) == 1) {
            offsetY *= -1;
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        clear = true;
    }

    public String getCurrButton() { return currButton; }
    public void clearCurrButton() { currButton = "None"; }
}