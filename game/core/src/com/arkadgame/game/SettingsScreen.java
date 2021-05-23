package com.arkadgame.game;

import com.arkadgame.game.obj.Button;
import com.arkadgame.game.obj.Slider;
import com.arkadgame.game.obj.Text;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.lang.management.BufferPoolMXBean;


public class SettingsScreen extends BaseScreen {
    // для отрисовки
    private SpriteBatch batch;
    private int width;
    private int height;
    private float offset;
    private float zoom;
    // для анимации
    private float alpha;
    private int animationElement;
    // режим отладки
    final private boolean test = true;
    // интерфейс
    private Text nameText;
    private Text volumeText;
    private Slider volume;
    private Button ready;
    private String currButton = "None";
    // текстуры
    final private Texture background, buttonTexture;

    public SettingsScreen(ArkadGame game, ProcessInput process, float speedAmimation) {
        super(game, process);
        buttonTexture = new Texture(Gdx.files.internal("buttons.png"));
        background = new Texture(Gdx.files.internal("menu_2.png"));
        batch = new SpriteBatch();
    }

    public void show() {
        // создаём интерфейс
        System.out.println("SEttings Open");
        zoom = 1f;
        nameText = new Text(buttonTexture, 0, 128, 187, 32, zoom);
        nameText.setPosition(100, 100);
        volumeText = new Text(buttonTexture, 187, 128, 184, 32, zoom);
        nameText.setPosition(100, 200);
        volume = new Slider(buttonTexture, 0, 0, zoom);
        volume.setType("VolumeSlider");
        ready = new Button(buttonTexture, 126, 32, 327, 128, zoom, false);
        ready.setType("ReadyButton");
        ready.setPosition(100, 300);
    }

    public void render(float delta) {
        // анимация
        // обновление
        // в режиме отладки
        if (test) {
            System.out.println("render");
        }
        // перекладывание бардьров
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // отрисовка
        batch.begin();
        //batch.draw(background, 0, 0, width, height);
        // отрисовка UI
        nameText.draw(batch, 1f);
        volumeText.draw(batch, 1f);
        volume.draw(batch, 1f);
        ready.draw(batch, 1f);
        batch.end();
    }

    public void resize(int width, int height) {
        // получаем размер экрана
        this.width = width;
        this.height = height;
        this.offset = height * 0.1f;
        batch.getProjectionMatrix().setToOrtho2D(0, 0, this.width, this.height);
        show();
    }

    private void checkButtonPress(float x, float y, boolean press) {
        currButton = "None";
        // обновление UI
    }

    @Override
    public void hide() {

    }

    public void clearAction() {
        this.currButton = "None";
    }

    public String getCurrButton() {
        return currButton;
    }
}
