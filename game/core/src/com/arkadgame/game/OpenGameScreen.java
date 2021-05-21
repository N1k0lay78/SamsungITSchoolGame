package com.arkadgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class OpenGameScreen extends BaseScreen {
    // переменная для логотипа и отрисовки
    final private Texture logo;
    final private Texture nameGame;
    private float alpha;
    final private float deltaSpeed;
    final private float waitTime;
    private float wait;
    private int page;
    final private boolean test = true;
    final private SpriteBatch batch;

    public OpenGameScreen(ArkadGame game, ProcessInput process, float deltaSpeed, float waitTime) {
        super(game, process);
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
        // отладка
        if (test) {
            if (page > 1) {
                page = 0;
            }
            System.out.println(alpha + " " + wait + " " + page);
        }
        // перекладываем бардюры
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // задаём прозрачность
        batch.setColor(new Color(1,1,1,alpha));
        // отрисовка
        batch.begin();
        if (page == 0) { // первая картинка логотип
            batch.draw(logo, 100, 100);
        } else { // вторая название игры
            batch.draw(nameGame, 100, 100);
        }
        batch.end();
    }
}
