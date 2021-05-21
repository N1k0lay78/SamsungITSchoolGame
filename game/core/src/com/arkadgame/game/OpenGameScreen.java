package com.arkadgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class OpenGameScreen extends BaseScreen {
    // переменные для логотипа и отрисовки
    final private Texture logo;
    final private Texture nameGame;
    // для анимации
    private float alpha;
    final private float deltaSpeed;
    final private float waitTime;
    private float wait;
    // какая картинка
    private int page;
    // режим отладки картиок (будут крутиться по кругу и не выключаться)
    final private boolean test = true;
    // для отрисовки
    final private SpriteBatch batch;
    private int width;
    private int height;
    // пропуск картинки
    private boolean skipped;

    public OpenGameScreen(ArkadGame game, ProcessInput process, float deltaSpeed, float waitTime) {
        super(game, process);
        // начинаем заставку
        game.setScreensaverOver(false);
        // загружаем изображения
        logo = new Texture(Gdx.files.internal("logo_war_2.png"));
        nameGame = new Texture(Gdx.files.internal("cybercong.png"));
        // загружаем изображения
        batch = new SpriteBatch();
        // задаём тайминги
        this.waitTime = waitTime;
        wait = waitTime;
        this.deltaSpeed = deltaSpeed;
        // какую картинку показывать
        this.page = 0;
        // задаём цвет заливки
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        // мы ничего не пропускаем
        skipped = false;
    }

    @Override
    public void render(float delta) {
        // сложо очень сложно
        if (alpha < 1f) {
            if (wait > 0) { // появление изображения
                alpha += deltaSpeed * delta;
            } else { // стерание изображения
                alpha -= 2f * deltaSpeed * delta;
                if (alpha <= 0) { // изображение стёрто
                    page++;
                    wait = waitTime;
                }
            }
        } else { // показываем изображение некоторое время
            wait -= delta;
            if (wait < 0) { // начинаем стирать
                wait = 0f;
                alpha = 0.99f;
            }
        }

        // при нажатии на экран переходим на следующую картинку
        if ((Gdx.input.isButtonPressed(Input.Buttons.LEFT) || Gdx.input.isTouched()) && !skipped) {
            wait = waitTime;
            alpha = 0f;
            page++;
            skipped = true;
        } else if (!(Gdx.input.isButtonPressed(Input.Buttons.LEFT) || Gdx.input.isTouched())) {
            skipped = false;
        }
        // отладка
        if (test) {
            if (page > 1) {
                page = 0;
            }
            // System.out.println(alpha + " " + wait + " " + page);
            System.out.println((float) (width / 2 - 0.3f * height) + " " + (float) (0.2f * height) + " " + (float) (0.6f * height) + " " + (float) (0.6f * height));
        }
        // перекладываем бардюры
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // задаём прозрачность
        batch.setColor(new Color(1,1,1,alpha));
        // отрисовка
        batch.begin();
        if (page == 0) { // первая картинка логотип
            batch.draw(logo, (float) (width / 2 - 0.3f * height), (float) (0.2f * height), (float) (0.6f * height), (float) (0.6f * height));
        } else if (page == 1) { // вторая название игры
            batch.draw(nameGame, (float) (width / 2 - 0.3f * height), (float) (0.45f * height), (float) (0.6f * height), (float) (0.1f * height));
        } else if (page == 2) {
            game.setScreensaverOver(true);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // получаем размер экрана
        this.width = width;
        this.height = height;
        batch.getProjectionMatrix().setToOrtho2D(0, 0, this.width, this.height);
        if (test) {
            System.out.println("Update size" + width + "x" + height);
        }
    }

    public void dispose() {
        logo.dispose();
        nameGame.dispose();
        batch.dispose();
    }
}
