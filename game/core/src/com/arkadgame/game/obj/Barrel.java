package com.arkadgame.game.obj;

import com.badlogic.gdx.Gdx;;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision._btMprSimplex_t;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;

// код прошёл программу коментаризации и готов к прочтению перед сном)
public class Barrel extends CustomActor {
    // хз что, но не отрошь оно без него не рабоатет!
    private ArrayList<CustomActor> pinchoss;
    // для отрисовки
    final private int sizeX = 40;
    final private int sizeY = 40;
    private TextureRegion barrelRegion;
    final private Texture barrelTexture;
    // для анимаций
    // private int frame = 1; оно вроде не нужно
    private float angle = 1;
    private int stairsFrame = 0;
    private float speedRotation = 525f;
    private float dist = 0f;
    // для перемещения
    final private float gravity = 2000;
    private float xSpeed = -225f;
    private float ySpeed = 0f;
    // начальное положение
    private float sx = 0f;
    private float sy = 0f;
    // для спуска по лестнице
    private float chance = 0.5f;
    private boolean onStairs = false;
    private boolean collideStairs = false;

    public Barrel(Texture barrelTexture, float sx, float sy) {
        // задаём скорость
        this.sx = sx;
        this.sy = sy;
        // задаём текстурку
        this.barrelTexture=barrelTexture;
        barrelRegion = new TextureRegion(barrelTexture, 32, 16, 16, 16);
        // здадаём размер
        setSize(sizeX, sizeY);
    }

    public void updateTextureStairs(float dist) {
        // анимация спуска по лестнице
        this.dist += dist;
        if (this.dist > 20f) {
            this.dist = 0f;
            if (stairsFrame > 1) { // их 3!
                stairsFrame = 0;
            } else {
                stairsFrame++;
            }
            barrelRegion = new TextureRegion(barrelTexture, 48, 16 * stairsFrame, 16, 16);
            // setSize(sizeX, sizeY); это вроде не нужно
        }
    }

    public void setPinchoss(ArrayList<CustomActor> pinchoss) {
        this.pinchoss = pinchoss;
    }

    public void move (){
        // получаем дельту
        float time = Gdx.graphics.getDeltaTime();
        if (onStairs) { // если движемся по лестнице
            this.move_up(-xSpeed * time);
            updateTextureStairs(time * xSpeed);
        } else { // если катимся
            barrelRegion = new TextureRegion(barrelTexture, 32, 16, 16, 16);
            // приминяем гравитацию
            if (!this.checkCollisions()) { // если под нами ничего нету кроме лестницы
                this.ySpeed -= time * time * gravity / 2;
            } else { // ну тогда мы на чём-то стоим)
                this.ySpeed = 0f;
            }
            // зачем-то применяем гравитацию
            this.move_up(this.ySpeed);
            // катим бочку на человека) - по фактам
            this.move_right(xSpeed * time);

        }
        // if (!onStairs) {this.move_right(xSpeed * time);} мне кажется модно перенести наверх
        // если бочка улетела за карту
        // мы используем сложную систему для оптимизации использования памяти, это вам не гарбаге калектор
        if (this.getY() < -5f) {this.setPosition(sx, sy); this.xSpeed = -225f; speedRotation = 525f; this.ySpeed = 0f;}
    }

    public void move_up(float speed) {
        // применили скорость
        this.setY(this.getY() + speed);
        if (!onStairs) { // елси мы не скатываемся по лестнице но во что-то упёрлись
            if (checkCollisions()) {
                // не сталкиваемся (отходим назад)
                this.setY(this.getY() - speed);
                // меняем сторону движения
                if (this.ySpeed < -5f) {
                    xSpeed *= -1;
                    speedRotation *= -1;
                }
                // падать больше незачем, мы уже достаточно низко упали)
                ySpeed = 0f;
            }
        }
    }

    public void move_right(float speed) {
        // катимся за человеками!
        this.setX(this.getX() + speed);
        // когда это происходит????
        if (checkCollisions()) {this.setX(this.getX() - speed);}
    }

    public String getType() {
        return "Barrel";
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // катим бочку
        move();
        // делаем бочку кручёной если катится по земле
        if (onStairs) {angle = 0;} else {angle += speedRotation * Gdx.graphics.getDeltaTime();}
        // выглядит страшно, но это всево лишь отрисовка)
        batch.draw(barrelRegion, this.getX(), this.getY(), this.getWidth() / 2f, this.getHeight() / 2f, this.getWidth(), this.getHeight(), 1, 1, angle);
    }

    public boolean checkCollisions() { // проверка колизии
        // магик и булевы нам теперь не нужны)
        for (CustomActor pinchos: this.pinchoss) { // проходимся по бочкам
            // колизии по осям
            boolean x_col = false, y_col = false;
            // сам не понял что это такое, но наверное должо рабоатать
            if ((this.getX()+this.getWidth()>pinchos.getX()||this.getX()>pinchos.getX())&&
                    (pinchos.getX()+pinchos.getWidth()>this.getWidth()+this.getX()||pinchos.getX()+pinchos.getWidth()>this.getX())) {
                x_col = true;
            }
            // ставлю на то, что это колизия по игрику
            if ((this.getY()+this.getHeight()>pinchos.getY()||this.getY()>pinchos.getY())&&
                    (pinchos.getY()+pinchos.getHeight()>this.getHeight()+this.getY()||pinchos.getY()+pinchos.getHeight()>this.getY())) {
                y_col = true;
            }
            // если бочка столкнулась по всем осям, мы столкнулись
            if(x_col&&y_col) {
                // если столкнулись с лестницей, то пофиг
                if (pinchos.getType().equalsIgnoreCase("Stairs")&&Math.abs(this.getX() - pinchos.getX())<5f&&!collideStairs) {
                    // это не моё честно слово, это мне подкинули
                    // - Прописать пидарасу по хребтине, ненавижу!
                    // - приём прописываю
                    collideStairs = true;
                    // по лестнице скатиться или нет?
                    onStairs = true;
                    // сменяем направление движения
                    xSpeed *= -1;
                } else if (!pinchos.getType().equalsIgnoreCase("Stairs")) {
                    collideStairs = false;
                } if (pinchos.getType() == "BaseStairs") {
                    onStairs = false;
                }
                // если мы не столкнулись с этим, то мы столкнулись)
                if (!pinchos.getType().equalsIgnoreCase("Stairs")&&pinchos.getType() != "Barrel" && pinchos.getType() != "Person" && pinchos.getType() != "BaseStairs") {
                    // зачем искать дальше то что можно выкинуть сейчас)
                    return true;
                }
            }
        }
        return false;
    }
}