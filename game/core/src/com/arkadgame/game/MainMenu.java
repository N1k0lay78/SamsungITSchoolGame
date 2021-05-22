package com.arkadgame.game;

import com.arkadgame.game.obj.Background;
import com.arkadgame.game.obj.Button;
import com.arkadgame.game.obj.CustomActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.ArrayList;

import javax.crypto.spec.PSource;

public class MainMenu extends BaseScreen {
    // для отрисовки
    private SpriteBatch batch;
    private int width;
    private int height;
    // текстуры с кнопками и фоном
    private Texture buttonTexture, menuTexture;
    // для анимации
    private int movingButton;
    private float progress;
    private float speed;
    // режим отладки картиок (будут крутиться по кругу и не выключаться)
    final private boolean test = false;
    // кнопки
    private ArrayList<Button> buttons;
    private Button activeButton;
    private String currButton = "None";

    public MainMenu(ArkadGame game, ProcessInput process, float speedAnimation) {
        super(game, process);
        // загружаем текстурки
        buttonTexture = new Texture(Gdx.files.internal("buttons.png"));
        menuTexture = new Texture(Gdx.files.internal("menu_2.png"));
        // создаём batch
        batch = new SpriteBatch();
        // переменные для анимации
        movingButton = 0;
        progress = 0f;
        speed = speedAnimation;
        // задаём цвет фона
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
    }

    @Override
    public void show() {
        // пересоздаём кнопки при открытии или ресайзе
        float zoom = height / 200;
        System.out.println(zoom);
        buttons = new ArrayList<>(4);
        Button button; // Button(buttonTexture, sizeX, sizeY, x, y, zoom, true);
        button = new Button(buttonTexture, 133, 32, 128, 0, zoom, true);
        button.setType("PlayButton");
        button.setPosition(-button.getWidth(),height - height * 0.1f - 32 * zoom);
        buttons.add(button);
        button = new Button(buttonTexture, 196, 32, 272, 0, zoom, true);
        button.setType("SettingButton");
        button.setPosition(-button.getWidth(),height - height * 0.2f - 2 * 32 * zoom);
        buttons.add(button);
        button = new Button(buttonTexture, 118, 32, 480, 0, zoom, true);
        button.setType("ExitButton");
        button.setPosition(-button.getWidth(),height - height * 0.3f - 3 * 32 * zoom);
        buttons.add(button);
        /*
        button = new Button(buttonTexture, 128, 0, 133, 32, zoom, true);
        button.setType("OnlineButton");
        button.setPosition(height * 0.1f,height - height * 0.4f - 4 * 32 * zoom);
        buttons.add(button);
         */
    }

    @Override
    public void render(float delta) {
        // aнимация
        if (movingButton < buttons.size()) {
            buttons.get(movingButton).setX(buttons.get(movingButton).getX() + delta * speed);
            if (buttons.get(movingButton).getX() >= height * 0.1f) {
                movingButton++;
                if (test) {
                    if (movingButton > buttons.size() - 1) {
                        movingButton = 0;
                    }
                }
                buttons.get(movingButton).setX(-buttons.get(movingButton).getWidth());
            }
        }
        // обновление
        float firstX = Gdx.input.getX();
        float firstY = height - Gdx.input.getY();
        boolean leftPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        checkButtonPress(firstX, firstY, leftPressed);
        // перекладывание бардюров
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (test) {
            if (movingButton > buttons.size() - 1) {
                movingButton = 0;
            }
            System.out.println(progress + " " + (buttons.get(movingButton).getWidth() + height * 0.1f));
        }
        // отрисовка
        batch.begin();
        batch.draw(menuTexture, 0, 0, width, height);
        for (int i=0; i < buttons.size(); i++) {
            if (i < movingButton) {
                buttons.get(i).draw(batch, 1f);
            } else if (i == movingButton) {
                buttons.get(i).draw(batch, (buttons.get(movingButton).getX() + buttons.get(i).getWidth())/(buttons.get(i).getWidth() + height * 0.1f));
            }
        }
        batch.end();
    }

    private void checkButtonPress(float x, float y, boolean press) {
        if (activeButton != null) {
            activeButton.setActive(false);
            activeButton = null;
        }
        currButton = "None";
        for (Button butt: buttons) {
            if (butt.checkCollision(x, y)) {
                // System.out.println("HAVE ACTIVE BUTTON");
                butt.setActive(true);
                activeButton = butt;
                if (press) { currButton = butt.getType(); }
                break;
            }
        }
    }

    public void resize(int width, int height) {
        // получаем размер экрана
        this.width = width;
        this.height = height;
        batch.getProjectionMatrix().setToOrtho2D(0, 0, this.width, this.height);
        show();
    }

    @Override
    public void hide() {

    }

    public String getCurrButton() {
        return currButton;
    }
}
