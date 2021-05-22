package com.arkadgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class SettingsScreen extends BaseScreen {
    // для отрисовки
    private SpriteBatch batch;
    private int width;
    private int height;
    // для анимации

    // режим отладки
    final private boolean test = true;
    // слайдер
    // текстуры
    final private Texture background, buttonTexture;

    public SettingsScreen(ArkadGame game, ProcessInput process) {
        super(game, process);
        buttonTexture = new Texture(Gdx.files.internal("buttons.png"));
        background = new Texture(Gdx.files.internal("menu_2.png"));
    }
}
