package com.arkadgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class OpenGameScreen extends BaseScreen {
    // переменная для логотипа и отрисовки
    private Texture logo;
    private Texture nameGame;
    private SpriteBatch batch;

    public OpenGameScreen(ArkadGame game, ProcessInput process) {
        super(game, process);
        logo = new Texture(Gdx.files.internal("logo_war_2.png"));
        logo = new Texture(Gdx.files.internal("logo_war_2.png"));
        batch = new SpriteBatch();
    }
}
